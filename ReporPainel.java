package br.ucb.biblioteca.ui;

import br.ucb.biblioteca.service.EmprestimoService;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

public class ReportPanel extends JPanel {
    private final EmprestimoService service = new EmprestimoService();
    private final DefaultTableModel tableModel = UiHelper.model(new String[] {"ID", "Livro", "Usuario", "Emprestimo", "Prevista", "Situacao"});
    private final JTable table = new JTable(tableModel);

    public ReportPanel() {
        setLayout(new BorderLayout(8, 8));
        JButton atualizar = new JButton("Atualizar relatorio de emprestimos em aberto");
        atualizar.addActionListener(e -> carregar());
        add(atualizar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        carregar();
    }

    private void carregar() {
        try {
            tableModel.setRowCount(0);
            for (String[] linha : service.listarAbertosRelatorio()) {
                tableModel.addRow(linha);
            }
        } catch (Exception e) {
            UiHelper.erro(this, e);
        }
    }
}
