package modelo;

public class Revolver extends Item {
    private int municao;
    
    public Revolver() {
        super("Revólver", "Causa dano crítico automático, mas gasta munição. Não funciona contra Zumbis Corredores.");
        this.municao = 1; // Cada revólver vem com uma munição
    }
    
    public Revolver(int municao) {
        super("Revólver", "Causa dano crítico automático, mas gasta munição. Não funciona contra Zumbis Corredores.");
        this.municao = municao;
    }
    
    @Override
    public void aplicar(Jogador jogador) {
        if (jogador.possuiRevolver()) {
            // Se já possui revólver, apenas adiciona munição
            jogador.adicionarMunicao(municao);
        } else {
            // Senão, equipa o revólver e adiciona munição
            jogador.setPossuiRevolver(true);
            jogador.adicionarMunicao(municao);
        }
    }
    
    public int getMunicao() {
        return municao;
    }
}