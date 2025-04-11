package interfaceGrafica;

import controle.CombateController;
import modelo.Jogador;
import modelo.Zumbi;

import javax.swing.* ;
import java.awt.* ;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CombateDialog extends JDialog {
    private CombateController combateController;
    private Jogador jogador;
    private Zumbi zumbi;
    
    private JLabel labelStatus;
    private JPanel painelOpcoes;
    private JPanel painelBotoes;
    private ButtonGroup grupoOpcoes;
    private JButton botaoAtacar;
    private JButton botaoFugir;
    
    public CombateDialog(JFrame pai, CombateController combateController, Jogador jogador, Zumbi zumbi) {
        super(pai, "Combate", true);
        
        this.combateController = combateController;
        this.jogador = jogador;
        this.zumbi = zumbi;
        
        // Configura o diálogo
        setSize(400, 300);
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
        
        // Label de status
        labelStatus = new JLabel("Combate contra Zumbi " + zumbi.getTipo() + 
                                 " (Saúde: " + zumbi.getSaude() + ")");
        labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
        labelStatus.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Painel de opções de ataque
        painelOpcoes = new JPanel();
        painelOpcoes.setLayout(new BoxLayout(painelOpcoes, BoxLayout.Y_AXIS));
        painelOpcoes.setBorder(BorderFactory.createTitledBorder("Escolha como atacar:"));
        
        grupoOpcoes = new ButtonGroup();
        List<String> opcoesAtaque = jogador.getOpcoesAtaque();
        
        for (String opcao : opcoesAtaque) {
            JRadioButton radio = new JRadioButton(formatarOpcaoAtaque(opcao));
            grupoOpcoes.add(radio);
            painelOpcoes.add(radio);
            
            // Seleciona a primeira opção por padrão
            if (grupoOpcoes.getSelection() == null) {
                radio.setSelected(true);
            }
        }
        
        // Painel de botões
        painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        botaoAtacar = new JButton("Atacar");
        botaoFugir = new JButton("Fugir");
        
        painelBotoes.add(botaoAtacar);
        painelBotoes.add(botaoFugir);
        
        // Adiciona os componentes ao painel principal
        painelPrincipal.add(labelStatus, BorderLayout.NORTH);
        painelPrincipal.add(painelOpcoes, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
        
        // Adiciona o painel principal ao diálogo
        setContentPane(painelPrincipal);
    }
    
    
//       Formata o nome da opção de ataque para exibição
//       @param opcao tipo de ataque ("maos", "taco", "revolver")
//       @return texto formatado
     
    private String formatarOpcaoAtaque(String opcao) {
        switch (opcao) {
            case "maos":
                return "Atacar com as mãos";
            case "taco":
                return "Atacar com o taco de beisebol";
            case "revolver":
                return "Atacar com o revólver (" + jogador.getMunicao() + " munições)";
            default:
                return opcao;
        }
    }
    
    
//       Configura os eventos dos componentes
   
    private void configurarEventos() {
        // Evento do botão Atacar
        botaoAtacar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atacar();
            }
        });
        
        // Evento do botão Fugir
        botaoFugir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    
//       Processa o ataque do jogador
     
    private void atacar() {
        // Obtém a opção de ataque selecionada
        String tipoAtaque = "maos";
        
        for (java.util.Enumeration<AbstractButton> buttons = grupoOpcoes.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            
            if (button.isSelected()) {
                String texto = button.getText();
                if (texto.contains("mãos")) {
                    tipoAtaque = "maos";
                } else if (texto.contains("taco")) {
                    tipoAtaque = "taco";
                } else if (texto.contains("revólver")) {
                    tipoAtaque = "revolver";
                }
                break;
            }
        }
        
        // Processa o ataque
        String resultado = combateController.processarAtaqueJogador(jogador, zumbi, tipoAtaque);
        
        // Se o zumbi morreu, encerra o combate
        if (!zumbi.estaVivo()) {
            JOptionPane.showMessageDialog(this, resultado, "Resultado do Ataque", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }
        
        // Mostra o resultado do ataque
        JOptionPane.showMessageDialog(this, resultado, "Resultado do Ataque", JOptionPane.INFORMATION_MESSAGE);
        
        // Zumbi contra-ataca
        String resultadoZumbi = combateController.processarAtaqueZumbi(zumbi, jogador);
        JOptionPane.showMessageDialog(this, resultadoZumbi, "Contra-ataque do Zumbi", JOptionPane.INFORMATION_MESSAGE);
        
        // Se o jogador morreu, encerra o combate
        if (jogador.getSaude() <= 0) {
            dispose();
            return;
        }
        
        // Atualiza o status
        labelStatus.setText("Combate contra Zumbi " + zumbi.getTipo() + 
                           " (Saúde: " + zumbi.getSaude() + ")");
        
        // Atualiza as opções de ataque
        atualizarOpcoesAtaque();
    }
    
  
//       Atualiza as opções de ataque disponíveis
    
    private void atualizarOpcoesAtaque() {
        // Limpa o painel de opções
        painelOpcoes.removeAll();
        grupoOpcoes = new ButtonGroup();
        
        // Adiciona as opções atualizadas
        List<String> opcoesAtaque = jogador.getOpcoesAtaque();
        
        for (String opcao : opcoesAtaque) {
            JRadioButton radio = new JRadioButton(formatarOpcaoAtaque(opcao));
            grupoOpcoes.add(radio);
            painelOpcoes.add(radio);
            
            // Seleciona a primeira opção por padrão
            if (grupoOpcoes.getSelection() == null) {
                radio.setSelected(true);
            }
        }
        
        // Redesenha o painel
        painelOpcoes.revalidate();
        painelOpcoes.repaint();
    }
}