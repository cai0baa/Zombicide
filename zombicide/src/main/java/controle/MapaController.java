package controle;

import modelo.* ;
import util.FileLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapaController {
    private Tabuleiro tabuleiro;
    private FileLoader fileLoader;
    private Random random;
    
    public MapaController(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        this.fileLoader = new FileLoader();
        this.random = new Random();
    }
    
    
//      Carrega um mapa a partir de um arquivo
//      @param caminhoArquivo caminho para o arquivo do mapa
//      @return true se o mapa foi carregado com sucesso, false caso contrário
     
    public boolean carregarMapa(String caminhoArquivo) {
        try {
            List<String> linhas = fileLoader.carregarArquivo(caminhoArquivo);
            processarLinhasMapa(linhas);
            distribuirBaus();
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao carregar o mapa: " + e.getMessage());
            return false;
        }
    }
    
   
//      Processa as linhas do arquivo de mapa e cria os elementos
//      @param linhas linhas do arquivo de mapa
    
    private void processarLinhasMapa(List<String> linhas) {
        int altura = Math.min(linhas.size(), tabuleiro.getTamanhoY());
        
        for (int y = 0; y < altura; y++) {
            String[] elementos = linhas.get(y).split(" ");
            int largura = Math.min(elementos.length, tabuleiro.getTamanhoX());
            
            for (int x = 0; x < largura; x++) {
                String elemento = elementos[x].trim();
                processarElemento(elemento, x, y);
            }
        }
    }
    
   
//      Processa um elemento do mapa e cria o objeto correspondente
//      @param elemento código do elemento (P, Z, ZR, ZC, ZG, B, V)
//      @param x coordenada X
//      @param y coordenada Y
     
    private void processarElemento(String elemento, int x, int y) {
    elemento = elemento.trim(); // Remove espaços extras
    
    // Obtém a célula atual
    Celula celula = tabuleiro.getCelula(x, y);
    
    switch (elemento) {
        case "P":  // Parede
            celula.setTemParede(true);
            System.out.println("Criando parede na posição (" + x + "," + y + ")");
            break;
        case "Z":  // Zumbi Comum
            celula.setTemParede(false);
            ZumbiComum zumbiComum = new ZumbiComum(2, x, y);
            celula.setOcupante(zumbiComum);
            tabuleiro.getZumbis().add(zumbiComum);
            System.out.println("Criando Zumbi Comum na posição (" + x + "," + y + ")");
            break;
        case "R": // Zumbi Rastejante (versão simplificada)
            ZumbiRastejante zumbiRastejante = new ZumbiRastejante(1, x, y);
            celula.setOcupante(zumbiRastejante);
            tabuleiro.getZumbis().add(zumbiRastejante);
            System.out.println("Criando Zumbi Rastejante na posição (" + x + "," + y + ")");
            break;
        case "C": // Zumbi Corredor (versão simplificada)
            ZumbiCorredor zumbiCorredor = new ZumbiCorredor(2, x, y);
            celula.setOcupante(zumbiCorredor);
            tabuleiro.getZumbis().add(zumbiCorredor);
            System.out.println("Criando Zumbi Corredor na posição (" + x + "," + y + ")");
            break;
        case "G": // Zumbi Gigante (versão simplificada)
            ZumbiGigante zumbiGigante = new ZumbiGigante(3, x, y);
            celula.setOcupante(zumbiGigante);
            tabuleiro.getZumbis().add(zumbiGigante);
            System.out.println("Criando Zumbi Gigante na posição (" + x + "," + y + ")");
            break;
        case "B":  // Baú
            celula.setBau(new Bau());
            System.out.println("Criando baú na posição (" + x + "," + y + ")");
            break;
        case "V":  // Espaço vazio
            // Célula vazia já foi criada
            System.out.println("Célula vazia na posição (" + x + "," + y + ")");
            break;
        default:   // Não reconhecido
            System.out.println("ATENÇÃO: Elemento não reconhecido: '" + elemento + "' na posição (" + x + "," + y + ")");
            break;
    }
    
    // Substitui a célula no tabuleiro
    tabuleiro.getCelula(x, y).setVisivel(celula.isVisivel());
    tabuleiro.getCelula(x, y).setVisitada(celula.isVisitada());
    tabuleiro.getCelula(x, y).setOcupante(celula.getOcupante());
    tabuleiro.getCelula(x, y).setItem(celula.getItem());
    tabuleiro.getCelula(x, y).setBau(celula.getBau());
}
    
    
//       Distribui os conteúdos dos baús pelo mapa
    
    private void distribuirBaus() {
        List<Bau> baus = encontrarBaus();
        
        if (baus.size() < 4) {
            System.err.println("Aviso: Menos de 4 baús encontrados no mapa.");
            return;
        }
        
        // Embaralha a lista de baús
        for (int i = 0; i < baus.size(); i++) {
            int j = random.nextInt(baus.size());
            Bau temp = baus.get(i);
            baus.set(i, baus.get(j));
            baus.set(j, temp);
        }
        
        // Configura os 4 primeiros baús conforme especificação
        // 1. Baú com atadura
        if (baus.size() > 0) {
            baus.get(0).adicionarItem(new Atadura());
        }
        
        // 2. Baú com taco de beisebol
        if (baus.size() > 1) {
            baus.get(1).adicionarItem(new TacoBeisebol());
        }
        
        // 3 e 4. Baús com revólver e zumbi rastejante
        for (int i = 2; i < Math.min(4, baus.size()); i++) {
            baus.get(i).adicionarItem(new Revolver());
            
            // Adiciona um zumbi rastejante escondido
            ZumbiRastejante zumbi = new ZumbiRastejante(1, 0, 0); // Posição temporária
            baus.get(i).setZumbiEscondido(zumbi);
            tabuleiro.getZumbis().add(zumbi);
        }
    }
    
    
//       Encontra todos os baús no mapa
//       @return lista de baús encontrados
     
    private List<Bau> encontrarBaus() {
        List<Bau> baus = new ArrayList<>();
        
        for (int x = 0; x < tabuleiro.getTamanhoX(); x++) {
            for (int y = 0; y < tabuleiro.getTamanhoY(); y++) {
                Celula celula = tabuleiro.getCelula(x, y);
                if (celula.temBau()) {
                    baus.add(celula.getBau());
                }
            }
        }
        
        return baus;
    }
}