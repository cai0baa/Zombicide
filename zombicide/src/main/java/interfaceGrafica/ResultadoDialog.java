package interfaceGrafica;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.* ;
import java.awt.* ;

public class ResultadoDialog extends JDialog {
    private boolean vitoria;
    private JButton botaoReiniciar;
    private JButton botaoNovoJogo;
    private ResultadoListener listener;
    
  
//       Interface para ouvir os eventos do diálogo de resultado
    
    public interface ResultadoListener {
        void onReiniciarJogo();
        void onNovoJogo();
    }
    
    public ResultadoDialog(JFrame pai, boolean vitoria) {
        super(pai, "Fim de Jogo", true);
        
        this.vitoria = vitoria;
        
        // Configura o diálogo
        setSize(400, 200);
        setLocationRelativeTo(pai);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        inicializarComponentes();
        configurarEventos();
    }
    
   
//       Inicializa os componentes da interface gráfica
     
    private void inicializarComponentes() {
        // Painel principal
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        
        // Mensagem de resultado
        String mensagem = vitoria ? 
            "Parabéns! Você eliminou todos os zumbis e venceu o jogo!" : 
            "Game Over! Você foi derrotado pelos zumbis.";
        
        JLabel labelResultado = new JLabel(mensagem);
        labelResultado.setHorizontalAlignment(SwingConstants.CENTER);
        labelResultado.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Painel de botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        botaoReiniciar = new JButton("Reiniciar Jogo");
        botaoNovoJogo = new JButton("Novo Jogo");
        
        painelBotoes.add(botaoReiniciar);
        painelBotoes.add(botaoNovoJogo);
        
        // Adiciona os componentes ao painel principal
        painelPrincipal.add(labelResultado, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
        
        // Adiciona o painel principal ao diálogo
        setContentPane(painelPrincipal);
    }
    
   
//       Configura os eventos dos componentes
    
    private void configurarEventos() {
        // Evento do botão Reiniciar Jogo
        botaoReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.onReiniciarJogo();
                }
                dispose();
            }
        });
        
        // Evento do botão Novo Jogo
        botaoNovoJogo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.onNovoJogo();
                }
                dispose();
            }
        });
    }
    
  
//       Define o ouvinte para os eventos do diálogo
//       @param listener ouvinte a ser definido
    
    public void setResultadoListener(ResultadoListener listener) {
        this.listener = listener;
    }
}