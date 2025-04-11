package modelo;

public abstract class Item {
    protected String nome;
    protected String descricao;
    
    public Item(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
    
    public abstract void aplicar(Jogador jogador);
    
    public String getNome() {
        return nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
}