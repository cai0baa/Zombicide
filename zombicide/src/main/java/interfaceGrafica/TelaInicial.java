package interfaceGrafica;

import controle.Jogo;

import javax.swing.* ;
import java.awt.* ;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaInicial extends JFrame {
    private JPanel painelPrincipal;
    private JComboBox<String> comboBoxDificuldade;
    private JButton botaoJogar;
    private JButton botaoDebug;
    private JButton botaoSair;
    
    private Jogo jogo;
    private TelaPrincipal telaPrincipal;
    
    public TelaInicial() {
        super("Zumbicídio - Tela Inicial");
        
        // Inicializa o controlador do jogo
        jogo = new Jogo();
        
        // Configura a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        inicializarComponentes();
        configurarEventos();
    }
    
  
//       Inicializa os componentes da interface gráfica
     
    private void inicializarComponentes() {
        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        
        // Painel de título
        JPanel painelTitulo = new JPanel();
        JLabel labelTitulo = new JLabel("ZUMBICÍDIO");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 36));
        painelTitulo.add(labelTitulo);
        
        // Painel de seleção de dificuldade
        JPanel painelDificuldade = new JPanel();
        JLabel labelDificuldade = new JLabel("Selecione a dificuldade:");
        comboBoxDificuldade = new JComboBox<>(new String[]{"Fácil", "Médio", "Difícil"});
        
        painelDificuldade.add(labelDificuldade);
        painelDificuldade.add(comboBoxDificuldade);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(3, 1, 10, 10));
        
        botaoJogar = new JButton("Jogar");
        botaoDebug = new JButton("DEBUG");
        botaoSair = new JButton("Sair");
        
        painelBotoes.add(botaoJogar);
        painelBotoes.add(botaoDebug);
        painelBotoes.add(botaoSair);
        
        // Adiciona os painéis ao painel principal
        painelPrincipal.add(painelTitulo, BorderLayout.NORTH);
        painelPrincipal.add(painelDificuldade, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
        
        // Adiciona o painel principal à janela
        setContentPane(painelPrincipal);
    }
    
   
//       Configura os eventos dos componentes
     
    private void configurarEventos() {
        // Evento do botão Jogar
        botaoJogar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJogo(false);
            }
        });
        
        // Evento do botão DEBUG
        botaoDebug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJogo(true);
            }
        });
        
        // Evento do botão Sair
        botaoSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    
//  
//       Inicia o jogo com as configurações selecionadas
//       @param modoDebug true para modo DEBUG, false para modo normal
    
    private void iniciarJogo(boolean modoDebug) {
        // Obtém a dificuldade selecionada (3-Fácil, 2-Médio, 1-Difícil)
        String dificuldadeSelecionada = (String) comboBoxDificuldade.getSelectedItem();
        int nivelDificuldade = 3; // Padrão: Fácil
        
        if (dificuldadeSelecionada.equals("Médio")) {
            nivelDificuldade = 2;
        } else if (dificuldadeSelecionada.equals("Difícil")) {
            nivelDificuldade = 1;
        }
        
        // Inicia o jogo
        jogo.iniciarJogo(nivelDificuldade, modoDebug);
        
        // Abre a tela principal do jogo
        if (telaPrincipal == null) {
            telaPrincipal = new TelaPrincipal(jogo, this);
        } else {
            telaPrincipal.atualizarJogo(jogo);
        }
        
        telaPrincipal.setVisible(true);
        this.setVisible(false);
    }
    
    
//       Método principal que inicia a aplicação

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaInicial().setVisible(true);
            }
        });
    }
}