package modelo;

public class ZumbiRastejante extends Zumbi {
    private boolean estaEmBau;
    
    public ZumbiRastejante(int saude, int posicaoX, int posicaoY) {
        super(saude, 1, posicaoX, posicaoY);
        this.estaEmBau = false;
    }
    
    public void esconder() {
        this.estaEmBau = true;
    }
    
    public void revelar() {
        this.estaEmBau = false;
    }
    
    public boolean estaEmBau() {
        return estaEmBau;
    }
    
//    
    @Override
    public String getTipo() {
        return "Rastejante";
    }
}