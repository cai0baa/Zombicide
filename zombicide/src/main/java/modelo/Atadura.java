package modelo;

public class Atadura extends Item {
    
    public Atadura() {
        super("Atadura", "Recupera 1 ponto de saúde quando utilizada.");
    }
    
    @Override
    public void aplicar(Jogador jogador) {
        jogador.adicionarAtadura();
    }
}