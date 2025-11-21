package com.example.udpbroadcast;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Base64;

/**
 * ReceiverPanel: hiển thị toolbar màu, bảng log, lắng nghe UDP, reassemble file
 */
public class ReceiverPanel extends JPanel {
    private final JTextField txtPort;
    private final DefaultTableModel model;
    private final JTable table;
    private volatile boolean listening = false;
    private Thread listenThread;

    // For file reassembly
    private static class FileAssembly {
        int expectedPackets = Integer.MAX_VALUE;
        int totalBytes = -1;
        Map<Integer, byte[]> parts = new ConcurrentHashMap<>();
        long firstReceived = System.currentTimeMillis();
    }
    private final Map<String, FileAssembly> assemblies = new ConcurrentHashMap<>();

    // Use same datetime format as HistoryManager.now() -> "uuuu-MM-dd HH:mm:ss"
    private static final DateTimeFormatter LOG_FMT = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    public ReceiverPanel() {
        setLayout(new BorderLayout(8,8));
        setBorder(new EmptyBorder(8,8,8,8));

        // toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        toolbar.setBackground(Color.WHITE);

        toolbar.add(new JLabel("Cổng:"));
        txtPort = new JTextField("50000", 6);
        toolbar.add(txtPort);

        JButton btnStart = styledButton("Bắt đầu lắng nghe", new Color(76,175,80));
        JButton btnClear = styledButton("Xóa", new Color(244,67,54));
        JButton btnExport = styledButton("Xuất CSV", new Color(255,152,0));
        JButton btnFolder = styledButton("Thư mục lưu...", new Color(123,31,162));
        JButton btnPlayWav = styledButton("Phát WAV", new Color(63,81,181));

        toolbar.add(btnStart);
        toolbar.add(btnClear);
        toolbar.add(btnExport);
        toolbar.add(btnFolder);
        toolbar.add(btnPlayWav);

        add(toolbar, BorderLayout.NORTH);

        // table
        model = new DefaultTableModel(new String[]{"Thời gian","IP nguồn","Cổng","Nội dung"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(26);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // actions
        btnStart.addActionListener(e -> toggleListening(btnStart));
        btnClear.addActionListener(e -> model.setRowCount(0));
        btnExport.addActionListener(e -> PathExporter.exportTableToCSV(table));
        btnFolder.addActionListener(e -> PathExporter.openReceiverFilesFolder());
        btnPlayWav.addActionListener(e -> playSelectedWav());

        // cleanup stale assemblies / stale log rows every 60s
        new java.util.Timer(true).scheduleAtFixedRate(new java.util.TimerTask() {
            @Override public void run() { cleanupStaleAssemblies(); }
        }, 60_000, 60_000);
    }

    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBorder(BorderFactory.createEmptyBorder(6,12,6,12));
        return b;
    }

    private void toggleListening(JButton btn) {
        if (!listening) {
            try {
                int port = Integer.parseInt(txtPort.getText().trim());
                listening = true;
                btn.setText("Dừng");
                listenThread = new Thread(() -> listenLoop(port), "udp-recv-thread");
                listenThread.start();
                appendLog("Bắt đầu lắng nghe cổng " + port);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Port không hợp lệ: " + ex.getMessage());
            }
        } else {
            listening = false;
            if (listenThread != null) listenThread.interrupt();
            btn.setText("Bắt đầu lắng nghe");
            appendLog("Dừng lắng nghe.");
        }
    }

    private void listenLoop(int port) {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            socket.setSoTimeout(1000);
            byte[] buf = new byte[65535];
            while (listening) {
                try {
                    DatagramPacket pkt = new DatagramPacket(buf, buf.length);
                    socket.receive(pkt);
                    int len = pkt.getLength();
                    byte[] data = Arrays.copyOf(pkt.getData(), len);
                    String text = new String(data, java.nio.charset.StandardCharsets.UTF_8);
                    String ip = pkt.getAddress().getHostAddress();
                    int remotePort = pkt.getPort();
                    handleIncomingText(text, ip, remotePort);
                } catch (java.net.SocketTimeoutException ste) {
                    // continue to check listening flag
                } catch (Exception ex) {
                    appendLog("Lỗi nhận: " + ex.getMessage());
                }
            }
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Receiver error: " + e.getMessage()));
        }
    }

    private void handleIncomingText(String text, String ip, int port) {
        // Add to table + CSV
        String time = HistoryManager.now(); // format: uuuu-MM-dd HH:mm:ss
        String display = text.length() > 200 ? text.substring(0,200) + "..." : text;
        SwingUtilities.invokeLater(() -> model.addRow(new Object[]{time, ip, port, display}));
        HistoryManager.appendReceiverCsv(String.format("%s,%s,%d,%s", time, ip, port, escapeCsv(display)));

        // Protocol handling
        if (text.startsWith("MSG;")) {
            // normal message -> nothing extra
            return;
        } else if (text.startsWith("FILEHDR;")) {
            // FILEHDR;filename;totalBytes;totalPackets
            String[] parts = text.split(";", 4);
            if (parts.length >= 4) {
                String filename = parts[1];
                int totalBytes = Integer.parseInt(parts[2]);
                int totalPackets = Integer.parseInt(parts[3]);
                FileAssembly fa = new FileAssembly();
                fa.expectedPackets = totalPackets;
                fa.totalBytes = totalBytes;
                assemblies.put(filename, fa);
                appendLog("Nhận header file: " + filename + " packets=" + totalPackets + " bytes=" + totalBytes);
            }
        } else if (text.startsWith("FILEPKT;")) {
            // FILEPKT;filename;index;base64data
            String[] parts = text.split(";", 4);
            if (parts.length >= 4) {
                String filename = parts[1];
                int idx = Integer.parseInt(parts[2]);
                String b64 = parts[3];
                byte[] chunk = Base64.getDecoder().decode(b64);
                FileAssembly fa = assemblies.computeIfAbsent(filename, k -> new FileAssembly());
                fa.parts.put(idx, chunk);
                appendLog(String.format("Nhận gói file %s [%d] (%d bytes)", filename, idx, chunk.length));
                if (fa.expectedPackets != Integer.MAX_VALUE && fa.parts.size() >= fa.expectedPackets) {
                    assembleAndSaveFile(filename, fa);
                }
            }
        } else {
            // unknown format, ignore
        }
    }

    private void assembleAndSaveFile(String filename, FileAssembly fa) {
        try {
            int totalParts = fa.expectedPackets;
            ByteArrayOutputStream baos = new ByteArrayOutputStream(fa.totalBytes > 0 ? fa.totalBytes : 0);
            for (int i = 0; i < totalParts; i++) {
                byte[] part = fa.parts.get(i);
                if (part == null) {
                    appendLog("Thiếu gói " + i + " của " + filename + " — không thể ghép.");
                    return;
                }
                baos.write(part);
            }
            byte[] full = baos.toByteArray();
            HistoryManager.saveFileBytes(filename, full);
            Path saved = HistoryManager.getReceiverFilesDir().resolve(filename);
            appendLog("Hoàn tất ghép file: " + filename + " (" + full.length + " bytes). Lưu ở: " + saved.toString());
            HistoryManager.appendReceiverCsv(String.format("%s,%s,%s,%s", HistoryManager.now(), "FILE", "-", "RECEIVED FILE: " + filename));
            assemblies.remove(filename);
            // auto play wav if wav
            if (filename.toLowerCase().endsWith(".wav")) {
                try { Utils.playWavFile(saved.toFile()); } catch (Exception ex) { appendLog("Không thể phát WAV tự động: " + ex.getMessage()); }
            }
        } catch (Exception ex) {
            appendLog("Lỗi ghép file: " + ex.getMessage());
        }
    }

    private void appendLog(String s) {
        SwingUtilities.invokeLater(() -> model.addRow(new Object[]{HistoryManager.now(), "LOCAL", "-", s}));
    }

    private void playSelectedWav() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn 1 dòng để phát WAV.");
            return;
        }
        String content = String.valueOf(model.getValueAt(row, 3));
        Optional<String> found = findFilenameInContent(content);
        if (!found.isPresent()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tên file trong nội dung đã chọn.");
            return;
        }
        Path p = HistoryManager.getReceiverFilesDir().resolve(found.get());
        if (!p.toFile().exists()) {
            JOptionPane.showMessageDialog(this, "File không tồn tại: " + p.toString());
            return;
        }
        Utils.playWavFile(p.toFile());
    }

    private Optional<String> findFilenameInContent(String content) {
        // crude search for filename token that ends with common extension
        for (String tok : content.split("\\s+")) {
            String cleaned = tok.replaceAll("[(),;\\[\\]]", "").trim();
            if (cleaned.toLowerCase().endsWith(".wav") || cleaned.toLowerCase().endsWith(".mp3") || cleaned.toLowerCase().endsWith(".txt")) {
                return Optional.of(cleaned);
            }
        }
        return Optional.empty();
    }

    /**
     * Remove table rows older than 5 minutes based on the timestamp format from HistoryManager.now()
     */
    private void cleanupStaleAssemblies() {
        long cutoff = System.currentTimeMillis() - (5L * 60 * 1000); // 5 minutes
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            try {
                String timeStr = String.valueOf(model.getValueAt(i, 0));
                LocalDateTime ldt = LocalDateTime.parse(timeStr, LOG_FMT);
                long epoch = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                if (epoch < cutoff) model.removeRow(i);
            } catch (Exception ex) {
                // If parse fails -> ignore that row
            }
        }
    }

    private static String escapeCsv(String s) {
        if (s == null) return "";
        s = s.replace("\"", "\"\"");
        if (s.contains(",") || s.contains("\"")) return "\"" + s + "\"";
        return s;
    }
}
