package modelo;

public abstract class Personagem {
    protected int saude;
    protected int posicaoX;
    protected int posicaoY;
    
    public Personagem(int saude, int posicaoX, int posicaoY) {
        this.saude = saude;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
    }
    
    public boolean estaVivo() {
        return saude > 0;
    }
    
    public void receberDano(int dano) {
    this.saude -= dano;
    if (this.saude < 0) this.saude = 0;
    System.out.println("Personagem recebeu " + dano + " de dano. Saúde atual: " + this.saude);
}
    
    // Getters e Setters
    public int getSaude() {
        return saude;
    }
    
    public void setSaude(int saude) {
        this.saude = saude;
    }
    
    public int getPosicaoX() {
        return posicaoX;
    }
    
    public void setPosicaoX(int posicaoX) {
        this.posicaoX = posicaoX;
    }
    
    public int getPosicaoY() {
        return posicaoY;
    }
    
    public void setPosicaoY(int posicaoY) {
        this.posicaoY = posicaoY;
    }
    
    // Método abstrato que deve ser implementado pelas subclasses
    public abstract void executarAcao();
}