package com.example.udpbroadcast;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;

public class SenderPanel extends JPanel {
    private JTextField txtHistory; // combo-like (simple)
    private JComboBox<String> comboHistory;
    private JTextField txtQuickMsg;
    private JTextField txtPort;
    private JCheckBox chkEpoch, chkSeq;
    private JButton btnAuto, btnSend;
    private JButton btnChooseFile, btnSendFile;
    private JTextField txtPacketSize, txtDelayMs;
    private JLabel lblSelectedFile;
    private JTextArea txtLog;

    private Timer autoTimer;
    private byte[] selectedFileBytes;
    private String selectedFileName;

    public SenderPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(10,10,10,10));

        // Top: history dropdown (simple)
        JPanel topHistory = new JPanel(new BorderLayout(6,6));
        comboHistory = new JComboBox<>(new String[] { "xin chaof", "hello", "test message" });
        JButton btnStop = new JButton("Dừng");
        topHistory.add(comboHistory, BorderLayout.CENTER);
        topHistory.add(btnStop, BorderLayout.EAST);
        add(topHistory, BorderLayout.NORTH);

        // Center: form panel
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));

        // Quick message
        JPanel p1 = new JPanel(new BorderLayout(6,6));
        p1.setBorder(BorderFactory.createTitledBorder("Tin nhắn nhanh"));
        txtQuickMsg = new JTextField();
        p1.add(txtQuickMsg, BorderLayout.CENTER);
        center.add(p1);

        // Controls row (port, epoch, seq, auto, send)
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        p2.add(new JLabel("Chu kỳ (ms):"));
        txtPort = new JTextField("50000",6); // reused as port display? keep separate below
        // We will show port field later; here show interval
        JTextField txtCycle = new JTextField("1000",6);
        p2.add(txtCycle);
        chkEpoch = new JCheckBox("Gắn epoch");
        chkSeq = new JCheckBox("Gắn số thứ tự");
        p2.add(chkEpoch);
        p2.add(chkSeq);
        btnAuto = new JButton("Tự động");
        btnSend = new JButton("Gửi");
        p2.add(btnAuto);
        p2.add(btnSend);
        center.add(p2);

        // File send panel
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8,8));
        filePanel.setBorder(BorderFactory.createTitledBorder("Gửi tệp (radio/audio)"));
        lblSelectedFile = new JLabel("Chưa chọn tệp");
        btnChooseFile = new JButton("Chọn tệp...");
        filePanel.add(new JLabel("Tệp:"));
        filePanel.add(lblSelectedFile);
        filePanel.add(btnChooseFile);
        filePanel.add(new JLabel("Kích thước gói (bytes):"));
        txtPacketSize = new JTextField("1200",6);
        filePanel.add(txtPacketSize);
        filePanel.add(new JLabel("Độ trễ mỗi gói (ms):"));
        txtDelayMs = new JTextField("2",4);
        filePanel.add(txtDelayMs);
        btnSendFile = new JButton("Gửi tệp");
        filePanel.add(btnSendFile);
        center.add(filePanel);

        // Port + simple send once fields (bottom of center)
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT,8,8));
        p3.add(new JLabel("Cổng (port):"));
        JTextField txtPort2 = new JTextField("50000",6);
        p3.add(txtPort2);
        center.add(p3);

        add(center, BorderLayout.CENTER);

        // Bottom: log area
        txtLog = new JTextArea(8,20);
        txtLog.setEditable(false);
        add(new JScrollPane(txtLog), BorderLayout.SOUTH);

        // Actions
        btnSend.addActionListener(e -> {
            String msg = txtQuickMsg.getText();
            if (msg == null || msg.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nhập tin nhắn để gửi.");
                return;
            }
            int port = Integer.parseInt(txtPort2.getText().trim());
            sendMessage(msg, port);
        });

        btnAuto.addActionListener(e -> {
            String s = txtCycle.getText().trim();
            try {
                int interval = Integer.parseInt(s);
                startAutoSend(interval, txtQuickMsg, txtPort2);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Chu kỳ phải là số (ms).");
            }
        });

        btnChooseFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int r = chooser.showOpenDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                selectedFileName = f.getName();
                try {
                    selectedFileBytes = Files.readAllBytes(f.toPath());
                    lblSelectedFile.setText(selectedFileName + " (" + selectedFileBytes.length + " bytes)");
                    log("Đã chọn tệp: " + selectedFileName);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Không thể đọc tệp: " + ex.getMessage());
                }
            }
        });

        btnSendFile.addActionListener(e -> {
            if (selectedFileBytes == null) {
                JOptionPane.showMessageDialog(this, "Chưa chọn tệp.");
                return;
            }
            try {
                int packetSize = Integer.parseInt(txtPacketSize.getText().trim());
                int delay = Integer.parseInt(txtDelayMs.getText().trim());
                int port = Integer.parseInt(txtPort2.getText().trim());
                sendFileAsPackets(selectedFileName, selectedFileBytes, packetSize, delay, port);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Packet size và delay phải là số.");
            }
        });

        btnStop.addActionListener(e -> stopAutoSend());
    }

    private void startAutoSend(int intervalMs, JTextField msgField, JTextField txtPortField) {
        stopAutoSend();
        autoTimer = new Timer();
        autoTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String msg = msgField.getText();
                int port = Integer.parseInt(txtPortField.getText().trim());
                SwingUtilities.invokeLater(() -> sendMessage(msg, port));
            }
        }, 0, intervalMs);
        log("Auto sending started every " + intervalMs + " ms");
    }

    private void stopAutoSend() {
        if (autoTimer != null) {
            autoTimer.cancel();
            autoTimer = null;
            log("Auto sending stopped");
        }
    }

    private void sendMessage(String message, int port) {
        try {
            String payload = "MSG;" + (chkEpoch.isSelected() ? System.currentTimeMillis() + ";" : "") +
                    (chkSeq.isSelected() ? java.util.UUID.randomUUID().toString() + ";" : "") +
                    message;
            UDPUtils.sendTextAsBroadcast(payload, port);
            String time = HistoryManager.now();
            String csvLine = String.format("%s,LOCALHOST,%d,%s", time, port, escapeCsv(message));
            HistoryManager.appendSenderCsv(csvLine);
            log("Sent message -> " + message);
        } catch (Exception ex) {
            log("Lỗi gửi tin: " + ex.getMessage());
        }
    }

    // Gửi file: trước tiên gửi header: FILEHDR;filename;totalPackets
    // Sau đó mỗi gói dạng: FILEPKT;filename;index;base64data
    private void sendFileAsPackets(String filename, byte[] bytes, int packetSize, int delayMs, int port) {
        new Thread(() -> {
            try {
                // Base64 split: we will split raw bytes into chunks and Base64-encode each chunk for safe text transfer
                int total = (bytes.length + packetSize -1) / packetSize;
                // Send header
                String header = String.format("FILEHDR;%s;%d;%d", filename, bytes.length, total);
                UDPUtils.sendTextAsBroadcast(header, port);
                Thread.sleep(20);

                for (int i = 0; i < total; i++) {
                    int start = i * packetSize;
                    int end = Math.min(bytes.length, start + packetSize);
                    byte[] part = java.util.Arrays.copyOfRange(bytes, start, end);
                    String b64 = Base64.getEncoder().encodeToString(part);
                    String pkt = String.format("FILEPKT;%s;%d;%s", filename, i, b64);
                    UDPUtils.sendTextAsBroadcast(pkt, port);
                    log(String.format("Đã gửi gói %d/%d (%d bytes)", i+1, total, part.length));
                    Thread.sleep(Math.max(0, delayMs));
                }
                log("Hoàn tất gửi tệp: " + filename);
                HistoryManager.appendSenderCsv(String.format("%s,LOCALHOST,%d,%s", HistoryManager.now(), port, escapeCsv("SENT FILE: " + filename)));
            } catch (Exception ex) {
                log("Lỗi gửi tệp: " + ex.getMessage());
            }
        }).start();
    }

    private void log(String s) {
        SwingUtilities.invokeLater(() -> {
            txtLog.append("[" + HistoryManager.now() + "] " + s + "\n");
        });
    }

    private static String escapeCsv(String s) {
        if (s == null) return "";
        s = s.replace("\"", "\"\"");
        if (s.contains(",") || s.contains("\"")) return "\"" + s + "\"";
        return s;
    }
}
