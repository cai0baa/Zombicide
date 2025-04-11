package interfaceGrafica;

import controle.Jogo;
import modelo.Jogador;

import javax.swing.* ;
import java.awt.* ;

public class StatusPanel extends JPanel {
    private Jogo jogo;
    
    private JLabel labelTitulo;
    private JLabel labelSaude;
    private JLabel labelPercepcao;
    private JLabel labelArmas;
    private JLabel labelItens;
    private JLabel labelTurno;
    
    public StatusPanel(Jogo jogo) {
        this.jogo = jogo;
        
        // Configura o painel
        setPreferredSize(new Dimension(200, 500));
        setBorder(BorderFactory.createTitledBorder("Status do Jogador"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        inicializarComponentes();
        atualizar();
    }
    
    
//       Inicializa os componentes da interface gráfica
     
    private void inicializarComponentes() {
        // Título
        labelTitulo = new JLabel("Informações do Jogador");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        labelTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Status
        labelSaude = new JLabel("Saúde: 0");
        labelPercepcao = new JLabel("Percepção: 0");
        labelArmas = new JLabel("Armas: Nenhuma");
        labelItens = new JLabel("Itens: Nenhum");
        labelTurno = new JLabel("Turno: 0");
        
        // Adiciona os componentes ao painel
        add(Box.createVerticalStrut(10));
        add(labelTitulo);
        add(Box.createVerticalStrut(20));
        add(labelSaude);
        add(Box.createVerticalStrut(10));
        add(labelPercepcao);
        add(Box.createVerticalStrut(10));
        add(labelArmas);
        add(Box.createVerticalStrut(10));
        add(labelItens);
        add(Box.createVerticalStrut(20));
        add(labelTurno);
        add(Box.createVerticalGlue());
    }
    
    
//       Atualiza as informações do painel
    
    public void atualizar() {
        if (jogo == null || jogo.getJogador() == null) return;
        
        Jogador jogador = jogo.getJogador();
        
        // Atualiza as informações
        labelSaude.setText("Saúde: " + jogador.getSaude());
        labelPercepcao.setText("Percepção: " + jogador.getPercepcao());
        
        // Armas
        StringBuilder armas = new StringBuilder("Armas: ");
        if (jogador.possuiTaco()) {
            armas.append("Taco de Beisebol");
        }
        if (jogador.possuiRevolver()) {
            if (jogador.possuiTaco()) armas.append(", ");
            armas.append("Revólver (").append(jogador.getMunicao()).append(" munições)");
        }
        if (jogador.possuiTaco() && jogador.possuiRevolver()) {
            armas.delete(7,armas.length());
            armas.append("Taco de Beisebol");
        }
        if (!jogador.possuiTaco() && !jogador.possuiRevolver()) {
            armas.append("Mãos nuas");
        }
        labelArmas.setText(armas.toString());
        
        // Itens
        String itens = "Itens: ";
        if (jogador.getAtaduras() > 0) {
            itens += jogador.getAtaduras() + " Atadura(s)";
        } else {
            itens += "Nenhum";
        }
        labelItens.setText(itens);
        
        // Turno
        labelTurno.setText("Turno: " + jogo.getContadorTurnos());
        
        // Repinta o painel
        repaint();
    }
    
  
//       Define o jogo
//       @param jogo novo objeto Jogo
    
    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
        atualizar();
    }
}