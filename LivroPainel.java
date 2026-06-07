package br.ucb.biblioteca.ui;

import br.ucb.biblioteca.dao.LookupDAO;
import br.ucb.biblioteca.model.Item;
import br.ucb.biblioteca.model.Livro;
import br.ucb.biblioteca.service.LivroService;

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
import java.sql.SQLException;
import java.util.List;

public class LivroPanel extends JPanel {
    private final LivroService service = new LivroService();
    private final LookupDAO lookupDAO = new LookupDAO();
    private final JTextField idField = new JTextField();
    private final JTextField tituloField = new JTextField();
    private final JTextField isbnField = new JTextField();
    private final JTextField anoField = new JTextField();
    private final JTextField quantidadeField = new JTextField("1");
    private final JComboBox<Item> editoraBox = new JComboBox<Item>();
    private final JComboBox<Item> categoriaBox = new JComboBox<Item>();
    private final JComboBox<Item> autorBox = new JComboBox<Item>();
    private final DefaultTableModel tableModel = UiHelper.model(new String[] {"ID", "Titulo", "ISBN", "Ano", "Qtd", "Disp.", "Autor(es)", "Editora", "Categoria"});
    private final JTable table = new JTable(tableModel);

    public LivroPanel() {
        setLayout(new BorderLayout(8, 8));
        idField.setEditable(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(formPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.SOUTH);
        carregarCombos();
        carregarTabela();
    }

    private JPanel formPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 6, 6, 6));
        panel.add(new JLabel("ID"));
        panel.add(idField);
        panel.add(new JLabel("Titulo"));
        panel.add(tituloField);
        panel.add(new JLabel("ISBN"));
        panel.add(isbnField);
        panel.add(new JLabel("Ano"));
        panel.add(anoField);
        panel.add(new JLabel("Quantidade"));
        panel.add(quantidadeField);
        panel.add(new JLabel("Editora"));
        panel.add(editoraBox);
        panel.add(new JLabel("Categoria"));
        panel.add(categoriaBox);
        panel.add(new JLabel("Autor"));
        panel.add(autorBox);
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

    private void carregarCombos() {
        try {
            editoraBox.removeAllItems();
            categoriaBox.removeAllItems();
            for (Item item : lookupDAO.listarEditoras()) {
                editoraBox.addItem(item);
            }
            for (Item item : lookupDAO.listarCategorias()) {
                categoriaBox.addItem(item);
            }
            for (Item item : lookupDAO.listarAutores()) {
                autorBox.addItem(item);
            }
        } catch (SQLException e) {
            UiHelper.erro(this, e);
        }
    }

    private void carregarTabela() {
        try {
            tableModel.setRowCount(0);
            List<Livro> livros = service.listar();
            for (Livro livro : livros) {
                tableModel.addRow(new Object[] {
                        livro.getId(), livro.getTitulo(), livro.getIsbn(), livro.getAnoPublicacao(),
                        livro.getQuantidadeTotal(), livro.getQuantidadeDisponivel(), livro.getAutoresNomes(), livro.getEditoraNome(), livro.getCategoriaNome()
                });
            }
        } catch (Exception e) {
            UiHelper.erro(this, e);
        }
    }

    private void salvar() {
        try {
            Livro livro = new Livro();
            livro.setId(idField.getText().trim().isEmpty() ? 0 : Integer.parseInt(idField.getText()));
            livro.setTitulo(tituloField.getText().trim());
            livro.setIsbn(isbnField.getText().trim());
            livro.setAnoPublicacao(anoField.getText().trim().isEmpty() ? null : Integer.parseInt(anoField.getText().trim()));
            livro.setQuantidadeTotal(Integer.parseInt(quantidadeField.getText().trim()));
            livro.setEditoraId(((Item) editoraBox.getSelectedItem()).getId());
            livro.setCategoriaId(((Item) categoriaBox.getSelectedItem()).getId());
            livro.setAutorId(((Item) autorBox.getSelectedItem()).getId());
            service.salvar(livro);
            limpar();
            carregarTabela();
        } catch (Exception e) {
            UiHelper.erro(this, e);
        }
    }

    private void carregarSelecionado() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um livro.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        idField.setText(String.valueOf(tableModel.getValueAt(modelRow, 0)));
        tituloField.setText(String.valueOf(tableModel.getValueAt(modelRow, 1)));
        isbnField.setText(String.valueOf(tableModel.getValueAt(modelRow, 2)));
        Object ano = tableModel.getValueAt(modelRow, 3);
        anoField.setText(ano == null ? "" : String.valueOf(ano));
        quantidadeField.setText(String.valueOf(tableModel.getValueAt(modelRow, 4)));
        selecionarComboPorPrefixo(autorBox, String.valueOf(tableModel.getValueAt(modelRow, 6)));
        selecionarCombo(editoraBox, String.valueOf(tableModel.getValueAt(modelRow, 7)));
        selecionarCombo(categoriaBox, String.valueOf(tableModel.getValueAt(modelRow, 8)));
    }

    private void excluir() {
        try {
            int id = UiHelper.selectedId(table);
            if (id == 0) {
                JOptionPane.showMessageDialog(this, "Selecione um livro.");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Excluir livro selecionado?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                service.excluir(id);
                carregarTabela();
                limpar();
            }
        } catch (Exception e) {
            UiHelper.erro(this, e);
        }
    }

    private void limpar() {
        idField.setText("");
        tituloField.setText("");
        isbnField.setText("");
        anoField.setText("");
        quantidadeField.setText("1");
        if (editoraBox.getItemCount() > 0) editoraBox.setSelectedIndex(0);
        if (categoriaBox.getItemCount() > 0) categoriaBox.setSelectedIndex(0);
        if (autorBox.getItemCount() > 0) autorBox.setSelectedIndex(0);
    }

    private void selecionarCombo(JComboBox<Item> combo, String nome) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getNome().equals(nome)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selecionarComboPorPrefixo(JComboBox<Item> combo, String nomes) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (nomes != null && nomes.startsWith(combo.getItemAt(i).getNome())) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }
}
