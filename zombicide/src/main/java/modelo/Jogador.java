package modelo;

import java.util.ArrayList;
import java.util.List;

public class Jogador extends Personagem {
    private int percepcao;
    private boolean possuiTaco;
    private boolean possuiRevolver;
    private int municao;
    private int ataduras;
    
    public Jogador(int saude, int percepcao, int posicaoX, int posicaoY) {
        super(saude, posicaoX, posicaoY);
        this.percepcao = percepcao;
        this.possuiTaco = false;
        this.possuiRevolver = false;
        this.municao = 0;
        this.ataduras = 0;
    }
    
    public int atacar(Zumbi zumbi, String tipoAtaque) {
        int dano = 0;
        
        // Verifica o tipo de ataque
        if (tipoAtaque.equals("revolver")) {
            if (possuiRevolver && municao > 0) {
                municao--;
                
                // O revólver não funciona contra Zumbis Corredores
                if (zumbi instanceof ZumbiCorredor) {
                    return 0;
                }
                
                // Golpe crítico automático com revólver
                dano = 2;
            }
        } else if (tipoAtaque.equals("taco")) {
            if (possuiTaco) {
                dano = 1;
            }
        } else {
            // Mãos nuas - precisa de um golpe crítico para o Zumbi Comum
            // e não funciona contra o Zumbi Gigante
            if (zumbi instanceof ZumbiGigante) {
                return 0;
            }
            
            // Simula rolagem de dado D6
            int resultado = (int) (Math.random() *  6) + 1;
            
            if (resultado == 6) {
                // Golpe crítico
                dano = 2;
            } else {
                dano = 1;
            }
        }
        
        zumbi.receberDano(dano);
        return dano;
    }
    
    public boolean esquivar() {
        // Simula rolagem de dado D3
        int resultado = (int) (Math.random()  * 3) + 1;
        
        // Esquiva bem-sucedida se o resultado for <= percepção
        return resultado <= percepcao;
    }
    
    public boolean curar() {
        if (ataduras > 0) {
            ataduras--;
            saude++;
            return true;
        }
        return false;
    }
    
    public void pegarItem(Item item) {
        item.aplicar(this);
    }
    
    @Override
    public void executarAcao() {
        // Implementação vazia, já que o jogador é controlado pelo usuário
    }
    
    // Getters e Setters
    public int getPercepcao() {
        return percepcao;
    }
    
    public void setPercepcao(int percepcao) {
        this.percepcao = percepcao;
    }
    
    public boolean possuiTaco() {
        return possuiTaco;
    }
    
    public void setPossuiTaco(boolean possuiTaco) {
        this.possuiTaco = possuiTaco;
    }
    
    public boolean possuiRevolver() {
        return possuiRevolver;
    }
    
    public void setPossuiRevolver(boolean possuiRevolver) {
        this.possuiRevolver = possuiRevolver;
    }
    
    public int getMunicao() {
        return municao;
    }
    
    public void adicionarMunicao(int quantidade) {
        this.municao += quantidade;
    }
    
    public int getAtaduras() {
        return ataduras;
    }
    
    public void adicionarAtadura() {
        this.ataduras++;
    }
    
    public List<String> getOpcoesAtaque() {
        List<String> opcoes = new ArrayList<>();
        
        // Sempre pode atacar com as mãos
        opcoes.add("maos");
        
        // Adiciona opção de taco se possuir
        if (possuiTaco) {
            opcoes.add("taco");
        }
        
        // Adiciona opção de revólver se possuir e tiver munição
        if (possuiRevolver && municao > 0) {
            opcoes.add("revolver");
        }
        
        return opcoes;
    }
}