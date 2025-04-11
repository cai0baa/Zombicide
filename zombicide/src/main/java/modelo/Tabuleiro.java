package modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {
    private Celula[][] mapa;
    private int tamanhoX;
    private int tamanhoY;
    private List<Zumbi> zumbis;
    
    public Tabuleiro(int tamanhoX, int tamanhoY) {
        this.tamanhoX = tamanhoX;
        this.tamanhoY = tamanhoY;
        this.mapa = new Celula[tamanhoX][tamanhoY];
        this.zumbis = new ArrayList<>();
        
        // Inicializa todas as células como vazias
        for (int x = 0; x < tamanhoX; x++) {
            for (int y = 0; y < tamanhoY; y++) {
                mapa[x][y] = new Celula(false);
            }
        }
    }
    
    public void inicializarMapa(String arquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            int y = 0;
            
            while ((linha = br.readLine()) != null && y < tamanhoY) {
                String[] elementos = linha.split(" ");
                for (int x = 0; x < Math.min(elementos.length, tamanhoX); x++) {
                    processarElemento(elementos[x], x, y);
                }
                y++;
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar o mapa: " + e.getMessage());
        }
    }
    
    private void processarElemento(String elemento, int x, int y) {
        // Limpa a célula atual
        mapa[x][y] = new Celula(false);
        
        switch (elemento) {
            case "P":  // Parede
                mapa[x][y] = new Celula(true);
                break;
            case "Z":  // Zumbi Comum
                ZumbiComum zumbiComum = new ZumbiComum(2, x, y);
                mapa[x][y].setOcupante(zumbiComum);
                zumbis.add(zumbiComum);
                break;
            case "ZR": // Zumbi Rastejante
                ZumbiRastejante zumbiRastejante = new ZumbiRastejante(1, x, y);
                mapa[x][y].setOcupante(zumbiRastejante);
                zumbis.add(zumbiRastejante);
                break;
            case "ZC": // Zumbi Corredor
                ZumbiCorredor zumbiCorredor = new ZumbiCorredor(2, x, y);
                mapa[x][y].setOcupante(zumbiCorredor);
                zumbis.add(zumbiCorredor);
                break;
            case "ZG": // Zumbi Gigante
                ZumbiGigante zumbiGigante = new ZumbiGigante(3, x, y);
                mapa[x][y].setOcupante(zumbiGigante);
                zumbis.add(zumbiGigante);
                break;
            case "B":  // Baú
                mapa[x][y].setBau(new Bau());
                break;
            // Outros casos podem ser adicionados conforme necessário
        }
    }
    
    // Na classe Tabuleiro
public boolean posicaoValida(int x, int y) {
    // Verifica se está dentro dos limites do tabuleiro
    if (x < 0 || x >= tamanhoX || y < 0 || y >= tamanhoY) {
        return false;
    }
    
    // Verifica se não tem parede
    if (mapa[x][y].temParede()) {
        return false;
    }
    
    return true;
}
    
public void atualizarVisibilidade(Jogador jogador) {
    // Reinicia a visibilidade
    for (int x = 0; x < tamanhoX; x++) {
        for (int y = 0; y < tamanhoY; y++) {
            mapa[x][y].setVisivel(false);
        }
    }
    
    int jogadorX = jogador.getPosicaoX();
    int jogadorY = jogador.getPosicaoY();
    
    // Marca a posição do jogador como visível e visitada
    mapa[jogadorX][jogadorY].setVisivel(true);
    mapa[jogadorX][jogadorY].setVisitada(true);
    
    // Atualiza visibilidade para a direita
    for (int x = jogadorX + 1; x < tamanhoX; x++) {
        mapa[x][jogadorY].setVisivel(true);
        // Para a visibilidade ao encontrar parede, baú ou zumbi (exceto Rastejante)
        if (mapa[x][jogadorY].temParede() || 
            mapa[x][jogadorY].temBau() || 
            (mapa[x][jogadorY].estaOcupada() && 
             mapa[x][jogadorY].getOcupante() instanceof Zumbi && 
             !(mapa[x][jogadorY].getOcupante() instanceof ZumbiRastejante))) {
            break;
        }
    }
    
    // Atualiza visibilidade para a esquerda
    for (int x = jogadorX - 1; x >= 0; x--) {
        mapa[x][jogadorY].setVisivel(true);
        // Para a visibilidade ao encontrar parede, baú ou zumbi (exceto Rastejante)
        if (mapa[x][jogadorY].temParede() || 
            mapa[x][jogadorY].temBau() || 
            (mapa[x][jogadorY].estaOcupada() && 
             mapa[x][jogadorY].getOcupante() instanceof Zumbi && 
             !(mapa[x][jogadorY].getOcupante() instanceof ZumbiRastejante))) {
            break;
        }
    }
    
    // Atualiza visibilidade para cima
    for (int y = jogadorY - 1; y >= 0; y--) {
        mapa[jogadorX][y].setVisivel(true);
        // Para a visibilidade ao encontrar parede, baú ou zumbi (exceto Rastejante)
        if (mapa[jogadorX][y].temParede() || 
            mapa[jogadorX][y].temBau() || 
            (mapa[jogadorX][y].estaOcupada() && 
             mapa[jogadorX][y].getOcupante() instanceof Zumbi && 
             !(mapa[jogadorX][y].getOcupante() instanceof ZumbiRastejante))) {
            break;
        }
    }
    
    // Atualiza visibilidade para baixo
    for (int y = jogadorY + 1; y < tamanhoY; y++) {
        mapa[jogadorX][y].setVisivel(true);
        // Para a visibilidade ao encontrar parede, baú ou zumbi (exceto Rastejante)
        if (mapa[jogadorX][y].temParede() || 
            mapa[jogadorX][y].temBau() || 
            (mapa[jogadorX][y].estaOcupada() && 
             mapa[jogadorX][y].getOcupante() instanceof Zumbi && 
             !(mapa[jogadorX][y].getOcupante() instanceof ZumbiRastejante))) {
            break;
        }
    }
}
    public boolean moverPersonagem(Personagem p, int novoX, int novoY) {
        if (!posicaoValida(novoX, novoY) || mapa[novoX][novoY].estaOcupada()) {
            return false;
        }
        
        // Remove personagem da posição atual
        mapa[p.getPosicaoX()][p.getPosicaoY()].removerOcupante();
        
        // Atualiza posição do personagem
        p.setPosicaoX(novoX);
        p.setPosicaoY(novoY);
        
        // Coloca personagem na nova posição
        mapa[novoX][novoY].setOcupante(p);
        
        return true;
    }
    
    // Getters e métodos adicionais
    public Celula getCelula(int x, int y) {
        if (x >= 0 && x < tamanhoX && y >= 0 && y < tamanhoY) {
            return mapa[x][y];
        }
        return null;
    }
    
    public List<Zumbi> getZumbis() {
        return zumbis;
    }
    
    public List<Zumbi> getZumbisVisiveis(Jogador jogador) {
        List<Zumbi> zumbisVisiveis = new ArrayList<>();
        for (Zumbi zumbi : zumbis) {
            int x = zumbi.getPosicaoX();
            int y = zumbi.getPosicaoY();
            if (mapa[x][y].isVisivel() && !(zumbi instanceof ZumbiRastejante)) {
                zumbisVisiveis.add(zumbi);
            }
        }
        return zumbisVisiveis;
    }
    
    public int getTamanhoX() {
        return tamanhoX;
    }
    
    public int getTamanhoY() {
        return tamanhoY;
    }
}