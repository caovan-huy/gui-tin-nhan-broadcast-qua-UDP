package com.example.udpbroadcast;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryManager {
    private static final Path BASE = Paths.get(System.getProperty("user.home"), ".udpbroadcast");
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    static {
        try {
            if (!Files.exists(BASE)) Files.createDirectories(BASE);
            Path rec = BASE.resolve("receiver");
            if (!Files.exists(rec)) Files.createDirectories(rec);
            Path snd = BASE.resolve("sender");
            if (!Files.exists(snd)) Files.createDirectories(snd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void appendSenderCsv(String line) {
        appendCsv("sender/sender_history.csv", line);
    }

    public static synchronized void appendReceiverCsv(String line) {
        appendCsv("receiver/receiver_history.csv", line);
    }

    private static synchronized void appendCsv(String relativePath, String line) {
        Path p = BASE.resolve(relativePath);
        try {
            if (!Files.exists(p.getParent())) Files.createDirectories(p.getParent());
            boolean exists = Files.exists(p);
            try (BufferedWriter bw = Files.newBufferedWriter(p, java.nio.charset.StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (!exists) {
                    bw.write("Time,RemoteIP,Port,Message");
                    bw.newLine();
                }
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Path getReceiverCsv() {
        return BASE.resolve("receiver/receiver_history.csv");
    }

    public static Path getSenderCsv() {
        return BASE.resolve("sender/sender_history.csv");
    }

    public static Path getReceiverFilesDir() {
        Path p = BASE.resolve("receiver/files");
        try { if (!Files.exists(p)) Files.createDirectories(p); } catch (IOException e) { e.printStackTrace(); }
        return p;
    }

    public static void saveFileBytes(String filename, byte[] bytes) throws IOException {
        Path dir = getReceiverFilesDir();
        Path file = dir.resolve(filename);
        Files.write(file, bytes);
    }

    public static String now() {
        return LocalDateTime.now().format(FMT);
    }
}
