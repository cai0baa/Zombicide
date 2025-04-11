package interfaceGrafica;

import controle.Jogo;
import modelo.Jogador;

import javax.swing.* ;
import java.awt.* ;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AcoesPanel extends JPanel {
    private TelaPrincipal telaPrincipal;
    private Jogo jogo;
    
    private JRadioButton radioMover;
    private JButton botaoCurar;
    private JButton botaoSair;
    private ButtonGroup grupoAcoes;
    
    public AcoesPanel(TelaPrincipal telaPrincipal, Jogo jogo) {
        this.telaPrincipal = telaPrincipal;
        this.jogo = jogo;
        
        // Configura o painel
        setPreferredSize(new Dimension(800, 100));
        setBorder(BorderFactory.createTitledBorder("Ações"));
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        inicializarComponentes();
        configurarEventos();
        atualizar();
    }
    
    
//       Inicializa os componentes da interface gráfica do mapa
    
    private void inicializarComponentes() {
        // Grupo de botões para seleção de ação
        grupoAcoes = new ButtonGroup();
        
        // Opção de mover
        radioMover = new JRadioButton("Mover");
        radioMover.setSelected(true);
        grupoAcoes.add(radioMover);
        
        // Botão de cura
        botaoCurar = new JButton("Curar");
        
        // Botão de sair
        botaoSair = new JButton("Sair");
        
        // Adiciona os componentes ao painel
        //add(radioMover);
        add(botaoCurar);
        add(botaoSair);
    }
    
   
//       Configura os eventos dos componentes
    
    private void configurarEventos() {
        // Evento do botão Curar
        botaoCurar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                telaPrincipal.processarAcao("curar");
            }
        });
        
        // Evento do botão Sair
        botaoSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                telaPrincipal.processarAcao("sair");
            }
        });
    }
    
   
//       Atualiza o estado dos componentes
   
    public void atualizar() {
        if (jogo == null || jogo.getJogador() == null) return;
        
        Jogador jogador = jogo.getJogador();
        
        // Atualiza o botão de cura
        botaoCurar.setEnabled(jogador.getAtaduras() > 0);
    }
    
    
//       Retorna a ação selecionada pelo jogador
//       @return tipo de ação ("mover", "curar", "sair")
     
    public String getAcaoSelecionada() {
        if (radioMover.isSelected()) {
            return "mover";
        }
        return "";
    }
    
    
//       Define o jogo
//       @param jogo novo objeto Jogo
    
    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
        atualizar();
    }
}