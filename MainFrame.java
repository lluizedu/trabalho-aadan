package br.ucb.biblioteca.ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Gestao de Biblioteca - CRUD");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Livros", new LivroPanel());
        tabs.addTab("Usuarios", new UsuarioPanel());
        tabs.addTab("Emprestimos", new EmprestimoPanel());
        tabs.addTab("Relatorio", new ReportPanel());

        add(tabs, BorderLayout.CENTER);
    }
}
