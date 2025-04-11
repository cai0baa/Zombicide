package controle;

import modelo.* ;

public class MovimentoController {
    private Tabuleiro tabuleiro;
    
    public MovimentoController(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    
   
//      Move o jogador para uma nova posição
//      @param jogador o jogador a ser movido
//      @param novoX coordenada X de destino
//      @param novoY coordenada Y de destino
//      @return true se o movimento foi bem-sucedido, false caso contrário
    
    public boolean moverJogador(Jogador jogador, int novoX, int novoY) {
        // Verifica se o movimento é válido (apenas uma célula na horizontal ou vertical)
        int deltaX = Math.abs(novoX - jogador.getPosicaoX());
        int deltaY = Math.abs(novoY - jogador.getPosicaoY());
        
        if ((deltaX + deltaY != 1) || !tabuleiro.posicaoValida(novoX, novoY)) {
            return false;
        }
        
        // Verifica se a célula de destino está ocupada
        if (tabuleiro.getCelula(novoX, novoY).estaOcupada()) {
            return false;
        }
        
        // Remove o jogador da posição atual
        tabuleiro.getCelula(jogador.getPosicaoX(), jogador.getPosicaoY()).removerOcupante();
        
        // Atualiza a posição do jogador
        jogador.setPosicaoX(novoX);
        jogador.setPosicaoY(novoY);
        
        // Coloca o jogador na nova posição
        tabuleiro.getCelula(novoX, novoY).setOcupante(jogador);
        
        return true;
    }
    
  
//       Move um zumbi em direção ao jogador
//       @param zumbi o zumbi a ser movido
//       @param jogador o jogador alvo
//       @return true se o movimento foi bem-sucedido, false caso contrário
     
    public boolean moverZumbi(Zumbi zumbi, Jogador jogador) {
        // Implementação diferente para cada tipo de zumbi
        if (zumbi instanceof ZumbiGigante) {
            // Zumbi Gigante não se move
            return false;
        }
        
        int velocidade = (zumbi instanceof ZumbiCorredor) ? 2 : 1;
        boolean moveuPeloMenosUmaVez = false;
        
        for (int i = 0; i < velocidade; i++) {
            // Se já alcançou o jogador, não precisa mover mais
            if (zumbi.getPosicaoX() == jogador.getPosicaoX() && 
                zumbi.getPosicaoY() == jogador.getPosicaoY()) {
                break;
            }
            
            int deltaX = jogador.getPosicaoX() - zumbi.getPosicaoX();
            int deltaY = jogador.getPosicaoY() - zumbi.getPosicaoY();
            
            // Determina a direção do movimento (prioriza a maior diferença)
            int novoX = zumbi.getPosicaoX();
            int novoY = zumbi.getPosicaoY();
            
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                // Move horizontalmente
                novoX += (deltaX > 0) ? 1 : -1;
            } else {
                // Move verticalmente
                novoY += (deltaY > 0) ? 1 : -1;
            }
            
            // Verifica se o movimento é válido
            if (tabuleiro.posicaoValida(novoX, novoY) && 
                !tabuleiro.getCelula(novoX, novoY).estaOcupada()) {
                
                // Remove o zumbi da posição atual
                tabuleiro.getCelula(zumbi.getPosicaoX(), zumbi.getPosicaoY()).removerOcupante();
                
                // Atualiza a posição do zumbi
                zumbi.setPosicaoX(novoX);
                zumbi.setPosicaoY(novoY);
                
                // Coloca o zumbi na nova posição
                tabuleiro.getCelula(novoX, novoY).setOcupante(zumbi);
                
                moveuPeloMenosUmaVez = true;
            } else {
                // Se não conseguiu mover na direção preferencial, tenta a outra direção
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    // Tenta mover verticalmente já que horizontalmente falhou
                    novoX = zumbi.getPosicaoX();
                    novoY = zumbi.getPosicaoY() + ((deltaY > 0) ? 1 : -1);
                } else {
                    // Tenta mover horizontalmente já que verticalmente falhou
                    novoX = zumbi.getPosicaoX() + ((deltaX > 0) ? 1 : -1);
                    novoY = zumbi.getPosicaoY();
                }
                
                // Verifica novamente se o movimento é válido
                if (tabuleiro.posicaoValida(novoX, novoY) && 
                    !tabuleiro.getCelula(novoX, novoY).estaOcupada()) {
                    
                    // Remove o zumbi da posição atual
                    tabuleiro.getCelula(zumbi.getPosicaoX(), zumbi.getPosicaoY()).removerOcupante();
                    
                    // Atualiza a posição do zumbi
                    zumbi.setPosicaoX(novoX);
                    zumbi.setPosicaoY(novoY);
                    
                    // Coloca o zumbi na nova posição
                    tabuleiro.getCelula(novoX, novoY).setOcupante(zumbi);
                    
                    moveuPeloMenosUmaVez = true;
                } else {
                    // Se não conseguiu mover em nenhuma direção, para a iteração
                    break;
                }
            }
        }
        
        return moveuPeloMenosUmaVez;
    }
    
    
//       Verifica se um personagem pode mover para uma determinada posição
//       @param personagem o personagem a ser verificado
//       @param posX coordenada X de destino
//       @param posY coordenada Y de destino
//       @return true se o movimento é possível, false caso contrário
     
    public boolean podeMover(Personagem personagem, int posX, int posY) {
        // Verifica se a posição é válida
        if (!tabuleiro.posicaoValida(posX, posY)) {
            return false;
        }
        
        // Verifica se a célula está ocupada
        if (tabuleiro.getCelula(posX, posY).estaOcupada()) {
            return false;
        }
        
        // Se for o jogador, verifica se o movimento é adjacente (não diagonal)
        if (personagem instanceof Jogador) {
            int deltaX = Math.abs(posX - personagem.getPosicaoX());
            int deltaY = Math.abs(posY - personagem.getPosicaoY());
            return (deltaX + deltaY == 1);
        }
        
        return true;
    }
    
    
//       Verifica se existem posições adjacentes livres para um personagem
//       @param personagem o personagem a ser verificado
//       @return true se existem posições adjacentes livres, false caso contrário
     
    public boolean existemPosicoesAdjacentes(Personagem personagem) {
        int x = personagem.getPosicaoX();
        int y = personagem.getPosicaoY();
        
        // Verifica as quatro direções (norte, sul, leste, oeste)
        return podeMover(personagem, x+1, y) || podeMover(personagem, x-1, y) ||
               podeMover(personagem, x, y+1) || podeMover(personagem, x, y-1);
    }
}