package modelo;

public class ZumbiComum extends Zumbi {
    
    public ZumbiComum(int saude, int posicaoX, int posicaoY) {
        super(saude, 1, posicaoX, posicaoY);
    }
      
    
    @Override
    public String getTipo() {
        return "Comum";
    }
}