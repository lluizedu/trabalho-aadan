package br.ucb.biblioteca.ui;

import br.ucb.biblioteca.model.Emprestimo;
import br.ucb.biblioteca.model.Livro;
import br.ucb.biblioteca.model.Usuario;
import br.ucb.biblioteca.service.EmprestimoService;
import br.ucb.biblioteca.service.LivroService;
import br.ucb.biblioteca.service.UsuarioService;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
import java.time.LocalDate;

public class EmprestimoPanel extends JPanel {
    private final EmprestimoService service = new EmprestimoService();
    private final LivroService livroService = new LivroService();
    private final UsuarioService usuarioService = new UsuarioService();
    private final JComboBox<Livro> livroBox = new JComboBox<Livro>();
    private final JComboBox<Usuario> usuarioBox = new JComboBox<Usuario>();
    private final JTextField dataEmprestimoField = new JTextField(String.valueOf(LocalDate.now()));
    private final JTextField dataPrevistaField = new JTextField(String.valueOf(LocalDate.now().plusDays(7)));
    private final DefaultTableModel tableModel = UiHelper.model(new String[] {"ID", "Livro", "Usuario", "Emprestimo", "Prevista", "Devolucao"});
    private final JTable table = new JTable(tableModel);

    public EmprestimoPanel() {
        setLayout(new BorderLayout(8, 8));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(formPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.SOUTH);
        carregarCombos();
        carregarTabela();
    }

    private JPanel formPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 6, 6));
        panel.add(new JLabel("Livro"));
        panel.add(livroBox);
        panel.add(new JLabel("Usuario"));
        panel.add(usuarioBox);
        panel.add(new JLabel("Data emprestimo (AAAA-MM-DD)"));
        panel.add(dataEmprestimoField);
        panel.add(new JLabel("Devolucao prevista (AAAA-MM-DD)"));
        panel.add(dataPrevistaField);
        return panel;
    }

    private JPanel buttonPanel() {
        JPanel panel = new JPanel();
        JButton registrar = new JButton("Registrar emprestimo");
        JButton devolver = new JButton("Registrar devolucao");
        JButton excluir = new JButton("Excluir");
        JButton atualizar = new JButton("Atualizar");
        registrar.addActionListener(e -> registrar());
        devolver.addActionListener(e -> devolver());
        excluir.addActionListener(e -> excluir());
        atualizar.addActionListener(e -> {
            carregarCombos();
            carregarTabela();
        });
        panel.add(registrar);
        panel.add(devolver);
        panel.add(excluir);
        panel.add(atualizar);
        return panel;
    }

    private void carregarCombos() {
        try {
            livroBox.removeAllItems();
            usuarioBox.removeAllItems();
            for (Livro livro : livroService.listar()) {
                livroBox.addItem(livro);
            }
            for (Usuario usuario : usuarioService.listar()) {
                if (usuario.isAtivo()) {
                    usuarioBox.addItem(usuario);
                }
            }
        } catch (Exception e) {
            UiHelper.erro(this, e);
        }
    }

    private void carregarTabela() {
        try {
            tableModel.setRowCount(0);
            for (Emprestimo e : service.listar()) {
                tableModel.addRow(new Object[] {
                        e.getId(),
                        e.getLivroTitulo(),
                        e.getUsuarioNome(),
                        e.getDataEmprestimo(),
                        e.getDataPrevistaDevolucao(),
                        e.getDataDevolucao() == null ? "" : e.getDataDevolucao()
                });
            }
        } catch (Exception e) {
            UiHelper.erro(this, e);
        }
    }

    private void registrar() {
        try {
            Livro livro = (Livro) livroBox.getSelectedItem();
            Usuario usuario = (Usuario) usuarioBox.getSelectedItem();
            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setLivroId(livro.getId());
            emprestimo.setUsuarioId(usuario.getId());
            emprestimo.setDataEmprestimo(LocalDate.parse(dataEmprestimoField.getText().trim()));
            emprestimo.setDataPrevistaDevolucao(LocalDate.parse(dataPrevistaField.getText().trim()));
            service.registrar(emprestimo);
            carregarCombos();
            carregarTabela();
        } catch (Exception e) {
            UiHelper.erro(this, e);
        }
    }

    private void devolver() {
        try {
            int id = UiHelper.selectedId(table);
            if (id == 0) {
                JOptionPane.showMessageDialog(this, "Selecione um emprestimo.");
                return;
            }
            service.devolver(id);
            carregarCombos();
            carregarTabela();
        } catch (Exception e) {
            UiHelper.erro(this, e);
        }
    }

    private void excluir() {
        try {
            int id = UiHelper.selectedId(table);
            if (id == 0) {
                JOptionPane.showMessageDialog(this, "Selecione um emprestimo.");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Excluir emprestimo selecionado?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                service.excluir(id);
                carregarTabela();
            }
        } catch (Exception e) {
            UiHelper.erro(this, e);
        }
    }
}
