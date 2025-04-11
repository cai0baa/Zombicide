package modelo;

public class ZumbiGigante extends Zumbi {
    private boolean podeSerAtacadoSemArma;
    
    public ZumbiGigante(int saude, int posicaoX, int posicaoY) {
        super(saude, 2, posicaoX, posicaoY); // Causa 2 pontos de dano por ataque
        this.podeSerAtacadoSemArma = false;
    }
    
//    
    
    @Override
    public String getTipo() {
        return "Gigante";
    }
    
    public boolean podeSerAtacadoSemArma() {
        return podeSerAtacadoSemArma;
    }
}