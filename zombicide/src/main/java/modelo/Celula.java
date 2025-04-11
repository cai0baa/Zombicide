package modelo;


//   Representa uma célula individual no tabuleiro do jogo.
//   Cada célula pode conter um personagem, item, baú ou parede.

public class Celula {
    private boolean temParede;
    private Personagem ocupante;
    private Item item;
    private Bau bau;
    private boolean visivel;
    private boolean visitada;
    
    
//       Cria uma nova célula com os valores especificados.
//       @param temParede indica se a célula contém uma parede
     
    public Celula(boolean temParede) {
        this.temParede = temParede;
        this.ocupante = null;
        this.item = null;
        this.bau = null;
        this.visivel = false;
        this.visitada = false;
    }
    
   
//       Verifica se a célula está ocupada por um personagem.
//       @return true se houver um personagem na célula, false caso contrário
     
    public boolean estaOcupada() {
        return ocupante != null;
    }
    
   
//       Verifica se a célula contém um item.
//       @return true se houver um item na célula, false caso contrário
    
    public boolean temItem() {
        return item != null;
    }
    
//    
//       Verifica se a célula contém um baú.
//       @return true se houver um baú na célula, false caso contrário
   
    public boolean temBau() {
        return bau != null;
    }
    
   
//       Verifica se a célula contém uma parede.
//       @return true se houver uma parede na célula, false caso contrário
     
    public boolean temParede() {
        return temParede;
    }
    
  
//       Retorna o personagem que ocupa a célula.
//       @return o personagem ocupante ou null se não houver ocupante
 
    public Personagem getOcupante() {
        return ocupante;
    }
    
    
//       Define o personagem que ocupa a célula.
//       @param ocupante o personagem a ser definido como ocupante
     
    public void setOcupante(Personagem ocupante) {
        this.ocupante = ocupante;
    }
  
//       Remove o personagem ocupante da célula.
    
    public void removerOcupante() {
        this.ocupante = null;
    }
    
    
//       Retorna o item presente na célula.
//       @return o item presente ou null se não houver item
//     
    public Item getItem() {
        return item;
    }
    
    
//       Define o item presente na célula.
//       @param item o item a ser colocado na célula
  
    public void setItem(Item item) {
        this.item = item;
    }
    
    
//       Remove e retorna o item presente na célula.
//       @return o item que foi removido ou null se não havia item
    
    public Item pegarItem() {
        Item itemTemporario = this.item;
        this.item = null;
        return itemTemporario;
    }
    
   
//       Retorna o baú presente na célula.
//       @return o baú presente ou null se não houver baú
    
    public Bau getBau() {
        return bau;
    }
    
    
//       Define o baú presente na célula.
//       @param bau o baú a ser colocado na célula
//    
    public void setBau(Bau bau) {
        this.bau = bau;
    }
    
 
//       Verifica se a célula está visível para o jogador.
//       @return true se a célula estiver visível, false caso contrário
     
    public boolean isVisivel() {
        return visivel;
    }
    
  
//       Define se a célula está visível para o jogador.
//       @param visivel true para tornar a célula visível, false para torná-la invisível
    
    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }
    
//    
//       Verifica se a célula já foi visitada pelo jogador.
//       @return true se a célula já foi visitada, false caso contrário
   
    public boolean isVisitada() {
        return visitada;
    }
    
    
//       Define se a célula já foi visitada pelo jogador.
//       @param visitada true para marcar a célula como visitada, false caso contrário
   
    public void setVisitada(boolean visitada) {
        this.visitada = visitada;
    }
    
    
//       Define se a célula contém uma parede.
//       @param temParede true para definir que a célula tem parede, false caso contrário
    
    public void setTemParede(boolean temParede) {
        this.temParede = temParede;
    }
    
   
//       Limpa completamente a célula, removendo todos os elementos.
    
    public void limpar() {
        this.temParede = false;
        this.ocupante = null;
        this.item = null;
        this.bau = null;
        // Mantém o estado de visibilidade e visitada
    }
    
    
//       Retorna uma representação em string da célula para depuração.
//       @return uma string que representa o estado da célula
    
    @Override
    public String toString() {
        if (temParede) return "P";
        if (ocupante != null) {
            if (ocupante instanceof Jogador) return "J";
            if (ocupante instanceof ZumbiRastejante) return "R";
            if (ocupante instanceof ZumbiCorredor) return "C";
            if (ocupante instanceof ZumbiGigante) return "G";
            return "Z"; // Zumbi comum
        }
        if (temBau()) return "B";
        if (temItem()) return "I";
        return "V"; // Vazia
    }
}