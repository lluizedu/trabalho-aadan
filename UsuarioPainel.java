package br.ucb.biblioteca.ui;

import br.ucb.biblioteca.model.Usuario;
import br.ucb.biblioteca.service.UsuarioService;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class UsuarioPanel extends JPanel {
    private final UsuarioService service = new UsuarioService();
    private final JTextField idField = new JTextField();
    private final JTextField nomeField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField telefoneField = new JTextField();
    private final JCheckBox ativoBox = new JCheckBox("Ativo", true);
    private final DefaultTableModel tableModel = UiHelper.model(new String[] {"ID", "Nome", "Email", "Telefone", "Ativo"});
    private final JTable table = new JTable(tableModel);

    public UsuarioPanel() {
        setLayout(new BorderLayout(8, 8));
        idField.setEditable(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(formPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.SOUTH);
        carregarTabela();
    }

    private JPanel formPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 6, 6));
        panel.add(new JLabel("ID"));
        panel.add(idField);
        panel.add(new JLabel("Nome"));
        panel.add(nomeField);
        panel.add(new JLabel("Email"));
        panel.add(emailField);
        panel.add(new JLabel("Telefone"));
        panel.add(telefoneField);
        panel.add(new JLabel("Status"));
        panel.add(ativoBox);
        return panel;
    }

    private JPanel buttonPanel() {
        JPanel panel = new JPanel();
        JButton novo = new JButton("Novo");
        JButton salvar = new JButton("Salvar");
        JButton editar = new JButton("Carregar selecionado");
        JButton excluir = new JButton("Excluir");
        JButton atualizar = new JButton("Atualizar lista");
        novo.addActionListener(e -> limpar());
        salvar.addActionListener(e -> salvar());
        editar.addActionListener(e -> carregarSelecionado());
        excluir.addActionListener(e -> excluir());
        atualizar.addActionListener(e -> carregarTabela());
        panel.add(novo);
        panel.add(salvar);
        panel.add(editar);
        panel.add(excluir);
        panel.add(atualizar);
        return panel;
    }

    private void carregarTabela() {
        try {
            tableModel.setRowCount(0);
            for (Usuario usuario : service.listar()) {
                tableModel.addRow(new Object[] {usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getTelefone(), usuario.isAtivo()});
            }
        } catch (Exception e) {
            UiHelper.erro(this, e);
        }
    }

    private void salvar() {
        try {
            Usuario usuario = new Usuario();
            usuario.setId(idField.getText().trim().isEmpty() ? 0 : Integer.parseInt(idField.getText()));
            usuario.setNome(nomeField.getText().trim());
            usuario.setEmail(emailField.getText().trim());
            usuario.setTelefone(telefoneField.getText().trim());
            usuario.setAtivo(ativoBox.isSelected());
            service.salvar(usuario);
            limpar();
            carregarTabela();
        } catch (Exception e) {
            UiHelper.erro(this, e);
        }
    }

    private void carregarSelecionado() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um usuario.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        idField.setText(String.valueOf(tableModel.getValueAt(modelRow, 0)));
        nomeField.setText(String.valueOf(tableModel.getValueAt(modelRow, 1)));
        emailField.setText(String.valueOf(tableModel.getValueAt(modelRow, 2)));
        telefoneField.setText(String.valueOf(tableModel.getValueAt(modelRow, 3)));
        ativoBox.setSelected(Boolean.parseBoolean(String.valueOf(tableModel.getValueAt(modelRow, 4))));
    }

    private void excluir() {
        try {
            int id = UiHelper.selectedId(table);
            if (id == 0) {
                JOptionPane.showMessageDialog(this, "Selecione um usuario.");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Excluir usuario selecionado?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                service.excluir(id);
                limpar();
                carregarTabela();
            }
        } catch (Exception e) {
            UiHelper.erro(this, e);
        }
    }

    private void limpar() {
        idField.setText("");
        nomeField.setText("");
        emailField.setText("");
        telefoneField.setText("");
        ativoBox.setSelected(true);
    }
}
