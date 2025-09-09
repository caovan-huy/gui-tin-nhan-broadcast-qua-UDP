package udp_demo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            int port = 9876; // Cổng để server lắng nghe
            socket = new DatagramSocket(port);
            System.out.println("✅ Server đang chạy, chờ Client gửi tin nhắn...");

            byte[] buffer = new byte[1024];

            while (true) {
                // --- Nhận dữ liệu từ Client ---
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet); // chờ gói tin đến

                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("📩 Nhận từ Client: " + message);

                // Nếu Client gửi "exit" thì dừng Server
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("⛔ Client yêu cầu thoát, Server dừng...");
                    break;
                }

                // --- Trả lời lại Client ---
                String reply = "Server đã nhận: " + message;
                byte[] sendData = reply.getBytes();
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                DatagramPacket replyPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                socket.send(replyPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
