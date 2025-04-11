package controle;

import modelo.* ;
import util.Dado;

public class CombateController {
    private Dado dado;
    private Tabuleiro tabuleiro;
    
    public CombateController(Tabuleiro tabuleiro) {
        this.dado = new Dado();
        this.tabuleiro = tabuleiro;
    }
    
    
     //Inicia um combate entre o jogador e um zumbi
     // @param jogador o jogador
     //@param zumbi o zumbi a ser combatido
     //@return mensagem descrevendo o resultado do combate
     
    public String iniciarCombate(Jogador jogador, Zumbi zumbi) {
        StringBuilder resultado = new StringBuilder();
        resultado.append("Iniciando combate contra Zumbi ").append(zumbi.getTipo()).append("!\n");
        
        // Obtém a lista de opções de ataque do jogador
        java.util.List<String> opcoesAtaque = jogador.getOpcoesAtaque();
        
        // Para simplificar, usamos a primeira opção disponível
        String tipoAtaque = opcoesAtaque.get(0);
        if (jogador.possuiTaco()) {
            tipoAtaque = "taco";
        } else if (jogador.possuiRevolver() && jogador.getMunicao() > 0 && !(zumbi instanceof ZumbiCorredor)) {
            tipoAtaque = "revolver";
        }
        
        // Jogador ataca primeiro
        resultado.append(processarAtaqueJogador(jogador, zumbi, tipoAtaque));
        
        // Se o zumbi sobreviveu, ele contra-ataca
        if (zumbi.estaVivo()) {
            resultado.append("\n").append(processarAtaqueZumbi(zumbi, jogador));
        }
        
        return resultado.toString();
    }
    
    
     //Processa o ataque do jogador contra um zumbi
     //@param jogador o jogador atacante
     // @param zumbi o zumbi alvo
     //@param tipoAtaque o tipo de ataque ("maos", "taco", "revolver")
     //@return mensagem descrevendo o resultado do ataque
     
    public String processarAtaqueJogador(Jogador jogador, Zumbi zumbi, String tipoAtaque) {
        StringBuilder resultado = new StringBuilder();
        
        System.out.println("Processando ataque do jogador com " + tipoAtaque + " contra zumbi " + zumbi.getTipo() + 
                           " (saúde inicial: " + zumbi.getSaude() + ")");
        
        // Verifica restrições especiais
        if (tipoAtaque.equals("revolver") && zumbi instanceof ZumbiCorredor) {
            return "O revólver não funciona contra Zumbis Corredores! Escolha outra arma.";
        }
        
        if (tipoAtaque.equals("maos") && zumbi instanceof ZumbiGigante) {
            return "Não é possível atacar um Zumbi Gigante com as mãos nuas! Use uma arma.";
        }
        
        // Realiza o ataque
        int dano = jogador.atacar(zumbi, tipoAtaque);
        
        System.out.println("Jogador causou " + dano + " de dano. Saúde do zumbi após o ataque: " + zumbi.getSaude());
        
        if (tipoAtaque.equals("revolver")) {
            resultado.append("Você atirou com o revólver e causou um golpe crítico! ");
            resultado.append("(-").append(dano).append(" pontos de vida do zumbi)\n");
            resultado.append("Munição restante: ").append(jogador.getMunicao());
        } else if (tipoAtaque.equals("taco")) {
            resultado.append("Você golpeou com o taco de beisebol. ");
            resultado.append("(-").append(dano).append(" pontos de vida do zumbi)");
        } else {
            if (dano == 2) {
                resultado.append("Golpe crítico! Você acertou a cabeça do zumbi. ");
                resultado.append("(-").append(dano).append(" pontos de vida do zumbi)");
            } else {
                resultado.append("Você acertou o zumbi com um golpe normal. ");
                resultado.append("(-").append(dano).append(" pontos de vida do zumbi)");
            }
        }
        
        // Verifica se o zumbi morreu
        if (!zumbi.estaVivo()) {
            resultado.append("\nVocê eliminou o Zumbi ").append(zumbi.getTipo()).append("!");
            
            // Remove o zumbi do jogo
            removerZumbi(zumbi);
        }
        
        return resultado.toString();
    }
    
   
     //Remove um zumbi do jogo
     //@param zumbi o zumbi a ser removido
   
    private void removerZumbi(Zumbi zumbi) {
        if (zumbi == null || !(zumbi.getSaude() <= 0)) return;
        
        System.out.println("Removendo zumbi " + zumbi.getTipo() + " da posição (" + 
                          zumbi.getPosicaoX() + "," + zumbi.getPosicaoY() + ")");
        
        // Remove o zumbi da célula
        Celula celula = tabuleiro.getCelula(zumbi.getPosicaoX(), zumbi.getPosicaoY());
        if (celula != null) {
            celula.removerOcupante();
        }
        
        // Remove da lista de zumbis do tabuleiro
        tabuleiro.getZumbis().remove(zumbi);
        
        System.out.println("Zumbi removido com sucesso. Zumbis restantes: " + tabuleiro.getZumbis().size());
    }
    
    
     //Processa o ataque de um zumbi contra o jogador
     //@param zumbi o zumbi atacante
     //@param jogador o jogador alvo
     //@return mensagem descrevendo o resultado do ataque
     
    public String processarAtaqueZumbi(Zumbi zumbi, Jogador jogador) {
        StringBuilder resultado = new StringBuilder();
        
        resultado.append("O Zumbi ").append(zumbi.getTipo()).append(" tenta te atacar...\n");
        
        boolean acertou = zumbi.atacar(jogador);
        
        if (acertou) {
            resultado.append("O zumbi te acertou! (-").append(zumbi.getDano()).append(" pontos de vida)");
            System.out.println("Zumbi acertou o jogador. Saúde do jogador: " + jogador.getSaude());
        } else {
            resultado.append("Você conseguiu esquivar do ataque!");
            System.out.println("Jogador esquivou do ataque do zumbi.");
        }
        
        return resultado.toString();
    }
    
   
//      Verifica se o jogador pode fugir do combate para uma determinada posição
//      @param jogador o jogador
//      @param posX posição X de destino
//      @param posY posição Y de destino
//      @return true se pode fugir, false caso contrário
     
    public boolean podeFugir(Jogador jogador, int posX, int posY) {
        // Verifica se a posição é adjacente
        if (Math.abs(posX - jogador.getPosicaoX()) + Math.abs(posY - jogador.getPosicaoY()) != 1) {
            return false;
        }
        
        // Verifica se a posição é válida e não está ocupada
        return tabuleiro.posicaoValida(posX, posY) && !tabuleiro.getCelula(posX, posY).estaOcupada();
    }
}