package br.ucb.biblioteca.ui;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UiHelper {
    private UiHelper() {
    }

    public static void erro(java.awt.Component parent, Exception e) {
        String msg = e.getMessage() == null ? e.toString() : e.getMessage();
        JOptionPane.showMessageDialog(parent, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static int selectedId(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            return 0;
        }
        int modelRow = table.convertRowIndexToModel(row);
        Object value = table.getModel().getValueAt(modelRow, 0);
        return Integer.parseInt(String.valueOf(value));
    }

    public static DefaultTableModel model(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
