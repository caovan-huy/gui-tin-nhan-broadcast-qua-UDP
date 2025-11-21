package com.example.udpbroadcast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPUtils {
    // Gửi 1 gói UDP tới address (broadcast nếu address là 255.255.255.255)
    public static void sendPacket(byte[] data, String address, int port) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        try {
            DatagramPacket pkt = new DatagramPacket(data, data.length, InetAddress.getByName(address), port);
            socket.send(pkt);
        } finally {
            socket.close();
        }
    }

    // Gửi gói text (UTF-8) tới broadcast address
    public static void sendTextAsBroadcast(String text, int port) throws Exception {
        byte[] b = text.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        sendPacket(b, "255.255.255.255", port);
    }
}
