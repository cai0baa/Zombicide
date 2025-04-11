package interfaceGrafica;

import controle.Jogo;
import modelo.* ;
import util.ImageLoader;

import javax.swing.* ;
import java.awt.* ;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TabuleiroPanel extends JPanel {
    private Jogo jogo;
    private List<CelulaClickListener> celulaClickListeners;
    
    // Constantes para o desenho
    private static final int TAMANHO_CELULA = 50;
    private static final Color COR_FUNDO = Color.WHITE;
    private static final Color COR_GRADE = Color.BLACK;
    private static final Color COR_CELULA_INVISIVEL = Color.LIGHT_GRAY;
    
   
//       Interface para eventos de clique em células
    
    public interface CelulaClickListener {
        void onCelulaClick(int x, int y);
    }
    
    public TabuleiroPanel(Jogo jogo) {
        this.jogo = jogo;
        this.celulaClickListeners = new ArrayList<>();
        
        // Assegura que o ImageLoader seja inicializado
        ImageLoader.getInstance();
        
        // Configura o painel
        setPreferredSize(new Dimension(500, 500));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // Adiciona evento de clique
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / TAMANHO_CELULA;
                int y = e.getY() / TAMANHO_CELULA;
                
                // Garante que as coordenadas estão dentro dos limites
                if (x >= 0 && x < 10 && y >= 0 && y < 10) {
                    for (CelulaClickListener listener : celulaClickListeners) {
                        listener.onCelulaClick(x, y);
                    }
                }
            }
        });
    }
    
    
//       Adiciona um ouvinte para eventos de clique em células
//       @param listener ouvinte a ser adicionado
    
    public void addCelulaClickListener(CelulaClickListener listener) {
        celulaClickListeners.add(listener);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Obtém o tabuleiro do jogo
        Tabuleiro tabuleiro = jogo.getTabuleiro();
        if (tabuleiro == null) return;
        
        // Desenha todas as células
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Celula celula = tabuleiro.getCelula(x, y);
                desenharCelula(g, celula, x, y);
            }
        }
    }
    
   
//       Desenha uma célula do tabuleiro
//       @param g contexto gráfico
//       @param celula célula a ser desenhada
//       @param x coordenada X
//       @param y coordenada Y
   
    private void desenharCelula(Graphics g, Celula celula, int x, int y) {
    int pixelX = x  * TAMANHO_CELULA;
    int pixelY = y  * TAMANHO_CELULA;
    
    // Se a célula não for visível e não estiver em modo debug, desenha como invisível
    if (!celula.isVisivel() && !jogo.isModoDebug()) {
        g.setColor(COR_CELULA_INVISIVEL);
        g.fillRect(pixelX, pixelY, TAMANHO_CELULA, TAMANHO_CELULA);
        g.setColor(COR_GRADE);
        g.drawRect(pixelX, pixelY, TAMANHO_CELULA, TAMANHO_CELULA);
        return;
    }
    
    // Desenha o fundo da célula
    g.setColor(COR_FUNDO);
    g.fillRect(pixelX, pixelY, TAMANHO_CELULA, TAMANHO_CELULA);
    
    // Para depuração: desenha números de coordenadas
    g.setColor(Color.GRAY);
    g.drawString(x + "," + y, pixelX + 2, pixelY + 10);
    
    // Desenha o conteúdo da célula
    if (celula.temParede()) {
        // Desenha a parede com imagem
        Image imagem = ImageLoader.getInstance().getImagem(ImageLoader.PAREDE);
        if (imagem != null) {
            g.drawImage(imagem, pixelX, pixelY, TAMANHO_CELULA, TAMANHO_CELULA, null);
        } else {
            // Fallback: desenha um retângulo cinza
            g.setColor(Color.DARK_GRAY);
            g.fillRect(pixelX + 1, pixelY + 1, TAMANHO_CELULA - 2, TAMANHO_CELULA - 2);
            g.setColor(Color.BLACK);
            g.drawString("P", pixelX + 20, pixelY + 30);
        }
    } else if (celula.estaOcupada()) {
        Personagem ocupante = celula.getOcupante();
        
        if (ocupante instanceof Jogador) {
            // Desenha o jogador com imagem
            Image imagem = ImageLoader.getInstance().getImagem(ImageLoader.JOGADOR);
            if (imagem != null) {
                g.drawImage(imagem, pixelX, pixelY, TAMANHO_CELULA, TAMANHO_CELULA, null);
            } else {
                // Fallback: desenha um círculo azul
                g.setColor(Color.BLUE);
                g.fillOval(pixelX + 10, pixelY + 10, TAMANHO_CELULA - 20, TAMANHO_CELULA - 20);
                g.setColor(Color.WHITE);
                g.drawString("J", pixelX + 20, pixelY + 30);
            }
        } else if (ocupante instanceof Zumbi) {
            // Escolhe a imagem correta baseada no tipo de zumbi
            String imagemChave = ImageLoader.ZUMBI_COMUM;
            String tipo = "Z";
            Color corFallback = Color.GREEN;
            
            if (ocupante instanceof ZumbiRastejante) {
                imagemChave = ImageLoader.ZUMBI_RASTEJANTE;
                tipo = "R";
                corFallback = Color.YELLOW;
            } else if (ocupante instanceof ZumbiCorredor) {
                imagemChave = ImageLoader.ZUMBI_CORREDOR;
                tipo = "C";
                corFallback = Color.ORANGE;
            } else if (ocupante instanceof ZumbiGigante) {
                imagemChave = ImageLoader.ZUMBI_GIGANTE;
                tipo = "G";
                corFallback = Color.RED;
            }
            
            // Desenha o zumbi com imagem
            Image imagem = ImageLoader.getInstance().getImagem(imagemChave);
            if (imagem != null) {
                g.drawImage(imagem, pixelX, pixelY, TAMANHO_CELULA, TAMANHO_CELULA, null);
            } else {
                // Fallback: desenha um retângulo colorido
                g.setColor(corFallback);
                g.fillRect(pixelX + 10, pixelY + 10, TAMANHO_CELULA - 20, TAMANHO_CELULA - 20);
                g.setColor(Color.BLACK);
                g.drawString(tipo, pixelX + 20, pixelY + 30);
            }
        }
    } else if (celula.temBau()) {
        // Desenha o baú com imagem
        Image imagem = ImageLoader.getInstance().getImagem(ImageLoader.BAU);
        if (imagem != null) {
            g.drawImage(imagem, pixelX, pixelY, TAMANHO_CELULA, TAMANHO_CELULA, null);
        } else {
            // Fallback: desenha um retângulo marrom
            g.setColor(new Color(139, 69, 19)); // Marrom
            g.fillRect(pixelX + 10, pixelY + 15, TAMANHO_CELULA - 20, TAMANHO_CELULA - 30);
            g.setColor(Color.BLACK);
            g.drawString("B", pixelX + 20, pixelY + 30);
        }
    }
    
    // Desenha a grade
    g.setColor(COR_GRADE);
    g.drawRect(pixelX, pixelY, TAMANHO_CELULA, TAMANHO_CELULA);
}
    
   
//       Define o jogo
//       @param jogo novo objeto Jogo
    
    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
        repaint();
    }
}