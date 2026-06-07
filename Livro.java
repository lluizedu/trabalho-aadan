package br.ucb.biblioteca.model;

public class Livro {
    private int id;
    private String titulo;
    private String isbn;
    private Integer anoPublicacao;
    private int quantidadeTotal;
    private int editoraId;
    private int categoriaId;
    private int autorId;
    private String editoraNome;
    private String categoriaNome;
    private String autoresNomes;
    private int quantidadeDisponivel;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    public int getQuantidadeTotal() { return quantidadeTotal; }
    public void setQuantidadeTotal(int quantidadeTotal) { this.quantidadeTotal = quantidadeTotal; }
    public int getEditoraId() { return editoraId; }
    public void setEditoraId(int editoraId) { this.editoraId = editoraId; }
    public int getCategoriaId() { return categoriaId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }
    public int getAutorId() { return autorId; }
    public void setAutorId(int autorId) { this.autorId = autorId; }
    public String getEditoraNome() { return editoraNome; }
    public void setEditoraNome(String editoraNome) { this.editoraNome = editoraNome; }
    public String getCategoriaNome() { return categoriaNome; }
    public void setCategoriaNome(String categoriaNome) { this.categoriaNome = categoriaNome; }
    public String getAutoresNomes() { return autoresNomes; }
    public void setAutoresNomes(String autoresNomes) { this.autoresNomes = autoresNomes; }
    public int getQuantidadeDisponivel() { return quantidadeDisponivel; }
    public void setQuantidadeDisponivel(int quantidadeDisponivel) { this.quantidadeDisponivel = quantidadeDisponivel; }

    @Override
    public String toString() {
        return id + " - " + titulo;
    }
}
