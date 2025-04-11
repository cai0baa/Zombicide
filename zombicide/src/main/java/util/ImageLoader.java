package util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


// * Classe utilitária para carregar e gerenciar as imagens do jogo.
// * Implementa o padrão Singleton para garantir que as imagens sejam carregadas apenas uma vez.
 
public class ImageLoader {
    // Instância única da classe (Singleton)
    private static ImageLoader instance;
    
    // Mapa que armazena todas as imagens carregadas
    private Map<String, Image> imagens;
    
    // Caminho base para a pasta de imagens
    private static final String IMAGENS_PATH = "resources/images/";
    
    // Chaves para acesso às imagens
    public static final String JOGADOR = "jogador";
    public static final String ZUMBI_COMUM = "zumbi_comum";
    public static final String ZUMBI_RASTEJANTE = "zumbi_rastejante";
    public static final String ZUMBI_CORREDOR = "zumbi_corredor";
    public static final String ZUMBI_GIGANTE = "zumbi_gigante";
    public static final String PAREDE = "parede";
    public static final String BAU = "bau";
    
    
//     * Construtor privado que carrega todas as imagens do jogo.
//     * É chamado apenas uma vez (padrão Singleton).
     
    private ImageLoader() {
        imagens = new HashMap<>();
        
        System.out.println("Inicializando ImageLoader...");
        verificarPastaImagens();
        
        // Carrega as imagens dos personagens
        carregarImagem(JOGADOR, "jogador.png");
        carregarImagem(ZUMBI_COMUM, "zumbi_comum.png");
        carregarImagem(ZUMBI_RASTEJANTE, "zumbi_rastejante.png");
        carregarImagem(ZUMBI_CORREDOR, "zumbi_corredor.png");
        carregarImagem(ZUMBI_GIGANTE, "zumbi_gigante.png");
        
        // Carrega imagem da parede
        carregarImagem(PAREDE, "parede.png");
        
        // Carrega imagem do baú
        carregarImagem(BAU, "bau.png");
        
        System.out.println("ImageLoader inicializado com " + imagens.size() + " imagens.");
    }
    
    
//     * Retorna a instância única desta classe (Singleton).
//     * @return a instância de ImageLoader
     
    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }
    
    
//     * Verifica se a pasta de imagens existe e lista seus arquivos.
     
    private void verificarPastaImagens() {
        File pasta = new File(IMAGENS_PATH);
        System.out.println("Verificando pasta de imagens: " + pasta.getAbsolutePath());
        
        if (!pasta.exists()) {
            System.err.println("ERRO: Pasta de imagens não encontrada: " + pasta.getAbsolutePath());
            System.err.println("Diretório de trabalho atual: " + System.getProperty("user.dir"));
            
            // Tenta criar o diretório
            boolean criado = pasta.mkdirs();
            if (criado) {
                System.out.println("Pasta de imagens criada: " + pasta.getAbsolutePath());
            } else {
                System.err.println("Não foi possível criar a pasta de imagens.");
            }
            return;
        }
        
        if (!pasta.isDirectory()) {
            System.err.println("ERRO: O caminho não é uma pasta: " + pasta.getAbsolutePath());
            return;
        }
        
        System.out.println("Pasta de imagens encontrada. Listando arquivos:");
        File[] arquivos = pasta.listFiles();
        if (arquivos != null) {
            for (File arquivo : arquivos) {
                System.out.println(" - " + arquivo.getName());
            }
        } else {
            System.out.println("Nenhum arquivo encontrado na pasta.");
        }
    }
    
    
//     * Carrega uma imagem a partir do arquivo e armazena no mapa de imagens.
//     * @param chave identificador da imagem
//     * @param nomeArquivo nome do arquivo na pasta de imagens
     
    private void carregarImagem(String chave, String nomeArquivo) {
        try {
            System.out.println("Tentando carregar imagem: " + IMAGENS_PATH + nomeArquivo);
            
            // Verifica se o arquivo existe
            File arquivo = new File(IMAGENS_PATH + nomeArquivo);
            if (!arquivo.exists()) {
                System.err.println("ERRO: Arquivo não encontrado: " + arquivo.getAbsolutePath());
                throw new FileNotFoundException("Arquivo de imagem não encontrado: " + nomeArquivo);
            }
            
            ImageIcon icon = new ImageIcon(IMAGENS_PATH + nomeArquivo);
            
            // Verifica se a imagem foi carregada corretamente
            if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
                System.err.println("ERRO: Falha ao carregar imagem: " + nomeArquivo + " (dimensões inválidas)");
                throw new IOException("Falha ao carregar imagem: " + nomeArquivo);
            }
            
            imagens.put(chave, icon.getImage());
            System.out.println("Imagem carregada com sucesso: " + nomeArquivo + 
                              " (" + icon.getIconWidth() + "x" + icon.getIconHeight() + ")");
        } catch (Exception e) {
            System.err.println("ERRO ao carregar imagem " + nomeArquivo + ": " + e.getMessage());
            e.printStackTrace();
            
            // Cria uma imagem vazia como fallback
            BufferedImage imagemVazia = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
            
            // Desenha um X vermelho na imagem vazia para indicar erro
            Graphics2D g = imagemVazia.createGraphics();
            g.setColor(Color.RED);
            g.drawLine(0, 0, 49, 49);
            g.drawLine(0, 49, 49, 0);
            g.dispose();
            
            imagens.put(chave, imagemVazia);
        }
    }
    
    
//     * Retorna uma imagem pelo seu identificador.
//     * @param chave identificador da imagem
//     * @return a imagem correspondente ou null se não existir
     
    public Image getImagem(String chave) {
        return imagens.get(chave);
    }
    
    
//     * Verifica se uma imagem específica foi carregada.
//     * @param chave identificador da imagem
//     * @return true se a imagem existe, false caso contrário
     
    public boolean imagemExiste(String chave) {
        return imagens.containsKey(chave) && imagens.get(chave) != null;
    }
    
    
//     * Redimensiona uma imagem para o tamanho especificado.
//     * @param chave identificador da imagem
//     * @param largura nova largura
//     * @param altura nova altura
     
    public Image getImagemRedimensionada(String chave, int largura, int altura) {
        Image original = getImagem(chave);
        if (original == null) {
            return null;
        }
        return original.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
    }
    
  
//     * Carrega uma imagem específica sob demanda.
//     * Útil para carregar imagens adicionais que não são carregadas no construtor.
//     * @param chave identificador da imagem
//     * @param nomeArquivo nome do arquivo na pasta de imagens
//     * @return true se a imagem foi carregada com sucesso, false caso contrário
     
    public boolean carregarImagemSobDemanda(String chave, String nomeArquivo) {
        try {
            carregarImagem(chave, nomeArquivo);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem sob demanda: " + e.getMessage());
            return false;
        }
    }
    
    
//     * Limpa todas as imagens carregadas.
//     * Útil para liberar memória quando o jogo é encerrado.
     
    public void limparImagens() {
        imagens.clear();
        System.out.println("Imagens limpas da memória.");
    }
    
    
//     * Recarrega todas as imagens.
//     * Útil se as imagens forem alteradas durante a execução do jogo.
     
    public void recarregarImagens() {
        limparImagens();
        
        // Recarrega as imagens
        carregarImagem(JOGADOR, "jogador.png");
        carregarImagem(ZUMBI_COMUM, "zumbi_comum.png");
        carregarImagem(ZUMBI_RASTEJANTE, "zumbi_rastejante.png");
        carregarImagem(ZUMBI_CORREDOR, "zumbi_corredor.png");
        carregarImagem(ZUMBI_GIGANTE, "zumbi_gigante.png");
        carregarImagem(PAREDE, "parede.png");
        carregarImagem(BAU, "bau.png");
        
        System.out.println("Imagens recarregadas.");
    }
}