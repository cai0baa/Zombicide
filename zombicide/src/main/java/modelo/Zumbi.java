package modelo;

public abstract class Zumbi extends Personagem {
    protected int dano;
    
    public Zumbi(int saude, int dano, int posicaoX, int posicaoY) {
        super(saude, posicaoX, posicaoY);
        this.dano = dano;
    }
    
    public boolean atacar(Jogador jogador) {
        if (!jogador.esquivar()) {
            jogador.receberDano(dano);
            return true;
        }
        return false;
    }
    
    
    public abstract String getTipo();
    
    @Override
    public void executarAcao() {
        // A ação será definida pelo controlador do jogo
    }
    
    public int getDano() {
        return dano;
    }
}