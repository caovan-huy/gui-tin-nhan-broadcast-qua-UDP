package UDP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class UDPClientChat extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton, stopButton;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private final int SERVER_PORT = 12345;
    private boolean running;

    public UDPClientChat() {
        setTitle("UDP Client Chat");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        sendButton = new JButton("Gửi");
        stopButton = new JButton("Stop Client");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(stopButton, BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
        stopButton.addActionListener(e -> stopClient());

        startClient();
    }

    private void startClient() {
        try {
            socket = new DatagramSocket();
            String ip = JOptionPane.showInputDialog(this, "Nhập IP Server:", "127.0.0.1");
            serverAddress = InetAddress.getByName(ip);
            running = true;

            chatArea.append("Đã kết nối với server\n");

            Thread listenThread = new Thread(() -> {
                while (running) {
                    try {
                        byte[] buffer = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);

                        String msg = new String(packet.getData(), 0, packet.getLength());
                        chatArea.append("Server: " + msg + "\n");
                    } catch (Exception e) {
                        if (running) {
                            chatArea.append("Lỗi: " + e.getMessage() + "\n");
                        }
                        break; // khi socket đóng thì thoát luôn
                    }
                }
            });
            listenThread.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối: " + e.getMessage());
        }
    }

    private void sendMessage() {
        try {
            String msg = inputField.getText().trim();
            if (msg.isEmpty()) return;

            byte[] buffer = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, SERVER_PORT);
            socket.send(packet);

            chatArea.append("Client: " + msg + "\n");
            inputField.setText("");
        } catch (Exception e) {
            chatArea.append("Lỗi khi gửi tin: " + e.getMessage() + "\n");
        }
    }

    private void stopClient() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        chatArea.append("Client đã dừng.\n");
        sendButton.setEnabled(false);
        inputField.setEditable(false);
        stopButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UDPClientChat().setVisible(true);
        });
    }
}