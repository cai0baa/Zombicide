package interfaceGrafica;

import controle.Jogo;

import javax.swing.* ;
import java.awt.* ;

public class TelaPrincipal extends JFrame {
    private Jogo jogo;
    private TelaInicial telaInicial;
    
    private JPanel painelPrincipal;
    private TabuleiroPanel painelTabuleiro;
    private StatusPanel painelStatus;
    private AcoesPanel painelAcoes;
    
    public TelaPrincipal(Jogo jogo, TelaInicial telaInicial) {
        super("Zumbicídio");
        this.jogo = jogo;
        this.telaInicial = telaInicial;
        
        // Configura a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        inicializarComponentes();
    }
    

//       Inicializa os componentes da interface gráfica
     
    private void inicializarComponentes() {
        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        
        // Cria os painéis principais
        painelTabuleiro = new TabuleiroPanel(jogo);
        painelStatus = new StatusPanel(jogo);
        painelAcoes = new AcoesPanel(this, jogo);
        
        // Adiciona os painéis ao painel principal
        painelPrincipal.add(painelTabuleiro, BorderLayout.CENTER);
        painelPrincipal.add(painelStatus, BorderLayout.EAST);
        painelPrincipal.add(painelAcoes, BorderLayout.SOUTH);
        
        // Adiciona o painel principal à janela
        setContentPane(painelPrincipal);
        
        // Configura os eventos
        painelTabuleiro.addCelulaClickListener(new TabuleiroPanel.CelulaClickListener() {
            @Override
            public void onCelulaClick(int x, int y) {
                processarClickCelula(x, y);
            }
        });
    }
    
    
//       Processa o clique em uma célula do tabuleiro
//       @param x coordenada X da célula
//       @param y coordenada Y da célula
    
    private void processarClickCelula(int x, int y) {
        // Verifica se o jogo está em andamento
        if (!jogo.isJogoEmAndamento()) {
            return;
        }
        
        // Verifica se é uma ação de movimento
        if (painelAcoes.getAcaoSelecionada().equals("mover")) {
            String resultado = jogo.processarTurno("mover", x, y);
            if(resultado == null){
                // Atualiza interface após a ação
                atualizarInterface();

                // Verifica se o jogo acabou
                verificarFimJogo();
            }
            else {
                mostrarMensagem(resultado);
                // Atualiza interface após a ação
                atualizarInterface();

                // Verifica se o jogo acabou
                verificarFimJogo();
            }
        }
    }
    
    
//       Processa uma ação do jogador
//       @param acao tipo de ação ("mover", "curar", "sair")
   
    public void processarAcao(String acao) {
        // Verifica se o jogo está em andamento
        if (!jogo.isJogoEmAndamento()) {
            return;
        }
        
        String resultado = "";
        
        if (acao.equals("curar")) {
            resultado = jogo.processarTurno("curar", 0, 0);
            mostrarMensagem(resultado);

            // Verifica se o jogo acabou
            verificarFimJogo();
        } else if (acao.equals("sair")) {
            int opcao = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente sair do jogo?", 
                "Confirmar Saída", 
                JOptionPane.YES_NO_OPTION);

            // Verifica se o jogo acabou
            verificarFimJogo();
            
            if (opcao == JOptionPane.YES_OPTION) {
                resultado = jogo.processarTurno("sair", 0, 0);
                //mostrarOpcoesFinais();
                this.setVisible(false);
                telaInicial.setVisible(true);
            }
        }
        
        // Atualiza interface após a ação
        atualizarInterface();

    }
    
    
//       Atualiza a interface após uma ação
  
    private void atualizarInterface() {
        painelTabuleiro.repaint();
        painelStatus.atualizar();
        painelAcoes.atualizar();
    }
    
    
//       Verifica se o jogo chegou ao fim e mostra a mensagem apropriada
   
    private void verificarFimJogo() {
        if (!jogo.isJogoEmAndamento()) {
            String mensagem = jogo.isVitoria() ? 
                "Parabéns! Você eliminou todos os zumbis e venceu o jogo!" : 
                "Game Over! Você foi derrotado pelos zumbis.";
            
            JOptionPane.showMessageDialog(this, mensagem);
            mostrarOpcoesFinais();
        }
    }
    
  
//       Mostra as opções de reiniciar ou iniciar novo jogo
  
    private void mostrarOpcoesFinais() {
        Object[] opcoes = {"Reiniciar Jogo", "Novo Jogo"};
        int escolha = JOptionPane.showOptionDialog(this, 
            "O que deseja fazer agora?", 
            "Fim de Jogo", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            opcoes, 
            opcoes[0]);
        
        if (escolha == 0) {
            // Reiniciar jogo
            jogo.reiniciarJogo();
            atualizarInterface();
        } else {
            // Novo jogo (voltar à tela inicial)
            this.setVisible(false);
            telaInicial.setVisible(true);
        }
    }
    
   
//       Mostra uma mensagem na interface
//       @param mensagem texto da mensagem
   
    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }
    
 
//       Atualiza o jogo
//       @param jogo novo objeto Jogo
    
    public void atualizarJogo(Jogo jogo) {
        this.jogo = jogo;
        painelTabuleiro.setJogo(jogo);
        painelStatus.setJogo(jogo);
        painelAcoes.setJogo(jogo);
        atualizarInterface();
    }
}