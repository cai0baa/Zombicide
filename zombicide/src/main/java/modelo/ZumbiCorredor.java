package modelo;

public class ZumbiCorredor extends Zumbi {
    private int velocidade;
    
   
//       Cria um novo Zumbi Corredor com a saúde e posição especificadas
//       @param saude pontos de vida iniciais
//       @param posicaoX coordenada X inicial
//       @param posicaoY coordenada Y inicial
    
    public ZumbiCorredor(int saude, int posicaoX, int posicaoY) {
        super(saude, 1, posicaoX, posicaoY);
        this.velocidade = 2; // Zumbi Corredor move-se 2 posições por turno
    }
    
  
    @Override
    public String getTipo() {
        return "Corredor";
    }
    
  
//       Retorna a velocidade de movimento do zumbi
//       @return quantidade de posições que pode mover por turno
     
    public int getVelocidade() {
        return velocidade;
    }
}