package br.ucb.biblioteca.model;

public class Item {
    private final int id;
    private final String nome;

    public Item(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }

    @Override
    public String toString() {
        return nome;
    }
}
