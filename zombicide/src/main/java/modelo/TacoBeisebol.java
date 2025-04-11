package modelo;

public class TacoBeisebol extends Item {
    
    public TacoBeisebol() {
        super("Taco de Beisebol", "Substitui o ataque com as mãos nuas, facilitando o combate.");
    }
    
    @Override
    public void aplicar(Jogador jogador) {
        jogador.setPossuiTaco(true);
    }
}