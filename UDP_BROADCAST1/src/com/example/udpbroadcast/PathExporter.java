package com.example.udpbroadcast;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class PathExporter {
    // Export JTable content to CSV (Save dialog)
    public static void exportTableToCSV(JTable table) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Lưu file CSV");
        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".csv")) {
                TableModel model = table.getModel();
                for (int i = 0; i < model.getColumnCount(); i++) {
                    fw.write(model.getColumnName(i) + (i < model.getColumnCount() - 1 ? "," : ""));
                }
                fw.write("\n");
                for (int r = 0; r < model.getRowCount(); r++) {
                    for (int c = 0; c < model.getColumnCount(); c++) {
                        Object o = model.getValueAt(r, c);
                        String cell = o == null ? "" : o.toString().replaceAll("\"", "\"\"");
                        if (cell.contains(",") || cell.contains("\"")) cell = "\"" + cell + "\"";
                        fw.write(cell + (c < model.getColumnCount() - 1 ? "," : ""));
                    }
                    fw.write("\n");
                }
                JOptionPane.showMessageDialog(null, "Xuất CSV thành công.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Lỗi xuất CSV: " + ex.getMessage());
            }
        }
    }

    // Open folder that stores receiver files
    public static void openReceiverFilesFolder() {
        try {
            Path p = HistoryManager.getReceiverFilesDir();
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(p.toFile());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không thể mở thư mục: " + e.getMessage());
        }
    }
}
