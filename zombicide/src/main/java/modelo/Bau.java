package modelo;

import java.util.ArrayList;
import java.util.List;

public class Bau {
    private List<Item> itens;
    private ZumbiRastejante zumbiEscondido;
    private boolean temZumbi;
    private boolean aberto;
    
    public Bau() {
        this.itens = new ArrayList<>();
        this.zumbiEscondido = null;
        this.temZumbi = false;
        this.aberto = false;
    }
    
    public Bau(boolean temZumbi) {
        this.itens = new ArrayList<>();
        this.temZumbi = temZumbi;
        if (temZumbi) {
            this.zumbiEscondido = new ZumbiRastejante(1, 0, 0);
            this.zumbiEscondido.esconder();
        } else {
            this.zumbiEscondido = null;
        }
        this.aberto = false;
    }
    
    public void adicionarItem(Item item) {
        itens.add(item);
    }
    
    public boolean abrir(Jogador jogador) {
        this.aberto = true;
        if (temZumbi) {
            // Testa a percepção do jogador
            int resultado = (int) (Math.random() *  3) + 1;
            boolean esquivou = resultado <= jogador.getPercepcao();
            
            if (!esquivou) {
                jogador.receberDano(1);
            }
            
            // Revela o zumbi
            zumbiEscondido.revelar();
            return !esquivou; // Retorna true se o jogador foi atacado
        }
        return false;
    }
    
    public List<Item> pegarItens() {
        List<Item> itensColetados = new ArrayList<>(itens);
        itens.clear();
        return itensColetados;
    }
    
    public boolean contemZumbi() {
        return temZumbi;
    }
    
    public boolean isAberto() {
        return aberto;
    }
    
    public ZumbiRastejante getZumbiEscondido() {
        return zumbiEscondido;
    }
    
    public void setZumbiEscondido(ZumbiRastejante zumbi) {
        this.zumbiEscondido = zumbi;
        this.temZumbi = (zumbi != null);
        if (zumbi != null) {
            zumbi.esconder();
        }
    }
    
    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }
    
    public List<Item> getItens() {
        return itens;
    }
}