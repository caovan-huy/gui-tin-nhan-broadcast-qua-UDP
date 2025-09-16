package UDP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class UDPServerChat extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton, stopButton;
    private DatagramSocket socket;
    private InetAddress clientAddress;
    private int clientPort;
    private final int SERVER_PORT = 12345;
    private boolean running;

    public UDPServerChat() {
        setTitle("UDP Server Chat");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        sendButton = new JButton("Gửi");
        stopButton = new JButton("Stop Server");

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
        stopButton.addActionListener(e -> stopServer());

        startServer();
    }

    private void startServer() {
        try {
            socket = new DatagramSocket(SERVER_PORT);
            running = true;
            chatArea.append("");

            Thread listenThread = new Thread(() -> {
                while (running) {
                    try {
                        byte[] buffer = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);

                        clientAddress = packet.getAddress();
                        clientPort = packet.getPort();

                        String msg = new String(packet.getData(), 0, packet.getLength());
                        chatArea.append("Client: " + msg + "\n");
                    } catch (Exception e) {
                        if (running) {
                            chatArea.append("Lỗi: " + e.getMessage() + "\n");
                        }
                        break; // dừng vòng lặp khi socket đóng
                    }
                }
            });
            listenThread.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể khởi động server: " + e.getMessage());
        }
    }

    private void sendMessage() {
        try {
            if (clientAddress == null) {
                chatArea.append("Chưa có client nào kết nối!\n");
                return;
            }
            String msg = inputField.getText().trim();
            if (msg.isEmpty()) return;

            byte[] buffer = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
            socket.send(packet);

            chatArea.append("Server: " + msg + "\n");
            inputField.setText("");
        } catch (Exception e) {
            chatArea.append("Lỗi khi gửi tin: " + e.getMessage() + "\n");
        }
    }

    private void stopServer() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        chatArea.append("Server đã dừng.\n");
        sendButton.setEnabled(false);
        inputField.setEditable(false);
        stopButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UDPServerChat().setVisible(true);
        });
    }
}



