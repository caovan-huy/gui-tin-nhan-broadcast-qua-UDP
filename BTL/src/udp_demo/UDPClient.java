package udp_demo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClient {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new DatagramSocket(); // không cần port cố định
            InetAddress serverAddress = InetAddress.getByName("localhost"); // IP của server
            int serverPort = 9876;

            while (true) {
                // --- Nhập dữ liệu từ bàn phím ---
                System.out.print("Nhập tin nhắn gửi Server: ");
                String message = scanner.nextLine();

                // --- Gửi dữ liệu ---
                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                socket.send(sendPacket);

                // Nếu nhập "exit" thì dừng Client
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("⛔ Đóng Client...");
                    break;
                }

                // --- Nhận phản hồi từ Server ---
                byte[] buffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivePacket);

                String reply = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("💬 Server trả lời: " + reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            scanner.close();
        }
    }
}
