package controle;

import modelo.* ;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jogo {
    private Tabuleiro tabuleiro;
    private Jogador jogador;
    private int dificuldade;
    private boolean modoDebug;
    private int contadorTurnos;
    private boolean jogoEmAndamento;
    private boolean vitoria;
    
    // Controladores específicos
    private CombateController combateController;
    private MapaController mapaController;
    private MovimentoController movimentoController;
    
    public Jogo() {
        this.contadorTurnos = 0;
        this.jogoEmAndamento = false;
        this.vitoria = false;
    }
    
    
//      Inicia um novo jogo com a dificuldade e modo debug especificados
//      @param dificuldade nível de dificuldade (1-Difícil, 2-Médio, 3-Fácil)
//      @param modoDebug se true, todo o mapa fica visível
     
    public void iniciarJogo(int dificuldade, boolean modoDebug) {
        this.dificuldade = dificuldade;
        this.modoDebug = modoDebug;
        this.contadorTurnos = 0;
        this.jogoEmAndamento = true;
        this.vitoria = false;
        
        System.out.println("Iniciando jogo com dificuldade " + dificuldade + " e modo debug: " + modoDebug);
        
        // Cria tabuleiro 10x10
        this.tabuleiro = new Tabuleiro(10, 10);
        
        // Carrega mapa aleatoriamente
        String arquivoMapa = selecionarMapaAleatorio();
        mapaController = new MapaController(tabuleiro);
        mapaController.carregarMapa(arquivoMapa);
        
        // Posiciona o jogador (exemplo: posição 0,0)
        this.jogador = new Jogador(5, dificuldade, 0, 0);
        tabuleiro.getCelula(0, 0).setOcupante(jogador);
        
        // Inicializa controladores
        this.combateController = new CombateController(tabuleiro);
        this.movimentoController = new MovimentoController(tabuleiro);
        
        // Atualiza visibilidade inicial
        atualizarVisibilidade();
        
        System.out.println("Jogo iniciado com " + tabuleiro.getZumbis().size() + " zumbis.");
    }
    
    
//      Seleciona um arquivo de mapa aleatoriamente da pasta de recursos
//      @return caminho para o arquivo de mapa selecionado
    
    private String selecionarMapaAleatorio() {
        File pastaMapas = new File("resources/maps");
        File[] arquivos = pastaMapas.listFiles((dir, nome) -> nome.toLowerCase().endsWith(".txt"));
        
        if (arquivos == null || arquivos.length == 0) {
            System.err.println("Nenhum arquivo de mapa encontrado. Usando mapa padrão.");
            // Se não encontrar mapas, usa um padrão
            return "resources/maps/mapa_padrao.txt";
        }
        
        Random random = new Random();
        int indice = random.nextInt(arquivos.length);
        System.out.println("Mapa selecionado: " + arquivos[indice].getPath());
        return arquivos[indice].getPath();
    }
    
    
//      Atualiza a visibilidade do mapa com base na posição do jogador
     
    public void atualizarVisibilidade() {
    if (modoDebug) {
        // Em modo debug, tudo fica visível
        for (int x = 0; x < tabuleiro.getTamanhoX(); x++) {
            for (int y = 0; y < tabuleiro.getTamanhoY(); y++) {
                tabuleiro.getCelula(x, y).setVisivel(true);
            }
        }
        System.out.println("Modo DEBUG: todo o mapa está visível");
    } else {
        // Modo normal: atualiza com base na linha de visão
        tabuleiro.atualizarVisibilidade(jogador);
    }
}
    
    
//      Processa um turno completo do jogo
//      @param acao tipo de ação ("mover", "curar", "sair")
//      @param posX posição X de destino (para ação "mover")
//      @param posY posição Y de destino (para ação "mover")
//      @return mensagem descrevendo o resultado da ação
     
    public String processarTurno(String acao, int posX, int posY) {
        String mensagem = "";
        
        if (!jogoEmAndamento) {
            return "O jogo não está em andamento.";
        }
        
        System.out.println("Processando turno: " + acao + " para posição (" + posX + "," + posY + ")");
        
        // Processa a ação do jogador
        if (acao.equals("mover")) {
            mensagem = processarMovimento(posX, posY);
        } else if (acao.equals("curar")) {
            mensagem = processarCura();
        } else if (acao.equals("sair")) {
            jogoEmAndamento = false;
            return "Jogo encerrado pelo jogador.";
        }
        
        // Se o jogo ainda estiver em andamento após a ação do jogador
        if (jogoEmAndamento) {
            // Turno dos zumbis - COMENTADO para que os zumbis não se movam
            // mensagem += "\n" + moverZumbis();
            //mensagem += "\nOs zumbis permanecem imóveis.";
            
            // Verifica fim de jogo
            if (verificarFimJogo()) {
                jogoEmAndamento = false;
                mensagem += "\n" + (vitoria ? "Você venceu! Todos os zumbis foram eliminados." 
                                           : "Você perdeu! Sua saúde chegou a zero.");
            }
            
            contadorTurnos++;
            System.out.println("Turno " + contadorTurnos + " completado. Zumbis restantes: " + tabuleiro.getZumbis().size());
        }
        
        return mensagem;
    }
    
   
//      Processa o movimento do jogador
//      @param posX posição X de destino
//      @param posY posição Y de destino
//      @return mensagem descrevendo o resultado do movimento
     
    private String processarMovimento(int posX, int posY) {
        int jogadorX = jogador.getPosicaoX();
        int jogadorY = jogador.getPosicaoY();
        
        System.out.println("Tentando mover jogador de (" + jogadorX + "," + jogadorY + ") para (" + posX + "," + posY + ")");
        
        // Verifica se o movimento é válido (apenas uma posição na horizontal ou vertical)
        if ((Math.abs(posX - jogadorX) + Math.abs(posY - jogadorY) != 1)) {
            return "Movimento inválido. Você só pode mover uma posição na horizontal ou vertical.";
        }
        
        // Verifica se a posição é válida e não contém parede
        if (!tabuleiro.posicaoValida(posX, posY)) {
            if (tabuleiro.getCelula(posX, posY).temParede()) {
                return "Movimento inválido. Há uma parede nessa posição.";
            } else {
                return "Movimento inválido. A posição está fora dos limites do tabuleiro.";
            }
        }
        
        Celula celulaDestino = tabuleiro.getCelula(posX, posY);
        
        // Se a célula destino tiver um zumbi, inicia combate
        if (celulaDestino.estaOcupada() && celulaDestino.getOcupante() instanceof Zumbi) {
            Zumbi zumbi = (Zumbi) celulaDestino.getOcupante();
            System.out.println("Iniciando combate com zumbi tipo " + zumbi.getTipo() + " na posição (" + posX + "," + posY + ")");
            String resultado = combateController.iniciarCombate(jogador, zumbi);
            
            // Após o combate, atualiza a visibilidade
            atualizarVisibilidade();
            
            return resultado;
        }
        
        // Se a célula tiver um baú, abre o baú
        if (celulaDestino.temBau()) {
            Bau bau = celulaDestino.getBau();
            boolean foiAtacado = bau.abrir(jogador);
            
            // Processa itens do baú
            List<Item> itens = bau.pegarItens();
            for (Item item : itens) {
                jogador.pegarItem(item);
            }
            
            // Move jogador para a célula
            movimentoController.moverJogador(jogador, posX, posY);
            atualizarVisibilidade();
            
            StringBuilder mensagem = new StringBuilder("Você abriu um baú");
            if (foiAtacado) {
                mensagem.append(" e foi atacado por um Zumbi Rastejante! (-1 de saúde)");
            }
            
            if(itens.isEmpty()){
                mensagem.append(" vazio!");
            }
            if (!itens.isEmpty()) {
                mensagem.append(" e encontrou ");
                for (int i = 0; i < itens.size(); i++) {
                    if (i > 0) mensagem.append(", ");
                    mensagem.append(itens.get(i).getNome());
                }
            }

            return mensagem.toString();
        }
        
        // Caso seja uma célula vazia
        boolean moveu = movimentoController.moverJogador(jogador, posX, posY);
        if (moveu) {
            atualizarVisibilidade();
            //return "Você se moveu para a posição (" + posX + ", " + posY + ").";
            return null;
        } else {
            return "Não foi possível se mover para essa posição.";
        }
    }
    
   
//      Processa a ação de cura do jogador
//      @return mensagem descrevendo o resultado da cura
     
    private String processarCura() {
        if (jogador.getAtaduras() <= 0) {
            return "Você não possui ataduras para se curar.";
        }
        
        if (jogador.getSaude() <= 0) {
            return "Você não pode se curar, pois já está morto.";
        }
        
        boolean curou = jogador.curar();
        return curou ? "Você utilizou uma atadura e recuperou 1 ponto de saúde." 
                     : "Não foi possível utilizar a atadura.";
    }
    
  
//      Realiza o movimento de todos os zumbis em direção ao jogador
//      @return mensagem descrevendo os movimentos dos zumbis
     
    private String moverZumbis() {
        // Como queremos que os zumbis fiquem parados, simplesmente retornamos uma string vazia
        // Este método está comentado e não será mais chamado
        
        StringBuilder mensagem = new StringBuilder();
        List<Zumbi> zumbis = tabuleiro.getZumbis();
        
        // Faz uma cópia da lista para evitar problemas com a remoção durante iteração
        List<Zumbi> zumbisCopia = new ArrayList<>(zumbis);
        
        for (Zumbi zumbi : zumbisCopia) {
            // Zumbis mortos são removidos
            if (!zumbi.estaVivo()) {
                zumbis.remove(zumbi);
                tabuleiro.getCelula(zumbi.getPosicaoX(), zumbi.getPosicaoY()).removerOcupante();
                System.out.println("Removendo zumbi morto da posição (" + zumbi.getPosicaoX() + "," + zumbi.getPosicaoY() + ")");
                continue;
            }
            
            // Zumbis Gigantes não se movem
            if (zumbi instanceof ZumbiGigante) {
                continue;
            }
            
            int posXAntigo = zumbi.getPosicaoX();
            int posYAntigo = zumbi.getPosicaoY();
            
           
            
            // Verifica se o zumbi chegou na posição do jogador
            if (zumbi.getPosicaoX() == jogador.getPosicaoX() && 
                zumbi.getPosicaoY() == jogador.getPosicaoY()) {
                // Inicia combate com o zumbi atacando primeiro
                mensagem.append("\nUm Zumbi ")
                       .append(zumbi.getTipo())
                       .append(" te alcançou!\n")
                       .append(combateController.processarAtaqueZumbi(zumbi, jogador));
            } else if (posXAntigo != zumbi.getPosicaoX() || posYAntigo != zumbi.getPosicaoY()) {
                // Se o zumbi se moveu, atualiza a mensagem
                if (tabuleiro.getCelula(zumbi.getPosicaoX(), zumbi.getPosicaoY()).isVisivel()) {
                    mensagem.append("\nUm Zumbi ")
                           .append(zumbi.getTipo())
                           .append(" se aproximou.");
                }
            }
        }
        
        return mensagem.toString();
    }
    
    
//      Verifica se o jogo chegou ao fim
//     @return true se o jogo acabou, false caso contrário
     
    public boolean verificarFimJogo() {
        // Jogador perdeu toda a saúde
        if (jogador.getSaude() <= 0) {
            vitoria = false;
            System.out.println("Fim de jogo: o jogador morreu.");
            return true;
        }
        
        // Todos os zumbis foram eliminados
        if (tabuleiro.getZumbis().isEmpty()) {
            vitoria = true;
            System.out.println("Fim de jogo: todos os zumbis foram eliminados!");
            return true;
        }
        
        return false;
    }
    
   
//     Reinicia o jogo com as mesmas configurações
     
    public void reiniciarJogo() {
        iniciarJogo(dificuldade, modoDebug);
    }
    
    
//     Remove um zumbi do jogo
//      @param zumbi o zumbi a ser removido
     
    public void removerZumbi(Zumbi zumbi) {
        if (zumbi == null) return;
        
        // Remove da célula
        tabuleiro.getCelula(zumbi.getPosicaoX(), zumbi.getPosicaoY()).removerOcupante();
        
        // Remove da lista de zumbis
        tabuleiro.getZumbis().remove(zumbi);
        
        System.out.println("Zumbi removido do jogo. Zumbis restantes: " + tabuleiro.getZumbis().size());
        
        // Verifica condição de vitória
        if (tabuleiro.getZumbis().isEmpty()) {
            vitoria = true;
            jogoEmAndamento = false;
            System.out.println("Todos os zumbis foram eliminados! O jogador venceu!");
        }
    }
    
    // Getters
    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }
    
    public Jogador getJogador() {
        return jogador;
    }
    
    public int getDificuldade() {
        return dificuldade;
    }
    
    public boolean isModoDebug() {
        return modoDebug;
    }
    
    public int getContadorTurnos() {
        return contadorTurnos;
    }
    
    public boolean isJogoEmAndamento() {
        return jogoEmAndamento;
    }
    
    public boolean isVitoria() {
        return vitoria;
    }
    
    public CombateController getCombateController() {
        return combateController;
    }
}