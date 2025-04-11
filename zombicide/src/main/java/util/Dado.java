package util;

import java.util.Random;

public class Dado {
    private Random random;
    
    public Dado() {
        this.random = new Random();
    }
    
 
//     * Simula a rolagem de um dado de 6 lados
//     * @return valor entre 1 e 6
     
    public int rolarD6() {
        return random.nextInt(6) + 1;
    }
    
    
//     * Simula a rolagem de um dado de 3 lados
//     * @return valor entre 1 e 3
     
    public int rolarD3() {
        return random.nextInt(3) + 1;
    }
    
    
//     * Simula a rolagem de um dado com número personalizado de lados
//     * @param lados número de lados do dado
//     * @return valor entre 1 e o número de lados
     
    public int rolar(int lados) {
        if (lados < 1) {
            throw new IllegalArgumentException("Número de lados deve ser maior que zero");
        }
        return random.nextInt(lados) + 1;
    }
}