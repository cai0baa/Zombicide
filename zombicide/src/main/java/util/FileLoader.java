package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileLoader {
    
    
//     * Carrega um arquivo de texto e retorna suas linhas como uma lista de strings
//     * @param caminhoArquivo caminho para o arquivo
//     * @return lista de linhas do arquivo
//     * @throws IOException se ocorrer um erro ao ler o arquivo
     
    public List<String> carregarArquivo(String caminhoArquivo) throws IOException {
        List<String> linhas = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linhas.add(linha);
            }
        }
        
        return linhas;
    }
    
    
//     * Carrega um arquivo de mapa e retorna uma matriz de caracteres
//     * @param caminhoArquivo caminho para o arquivo
//     * @return matriz de caracteres representando o mapa
//     * @throws IOException se ocorrer um erro ao ler o arquivo
     
    public char[][] carregarMapa(String caminhoArquivo) throws IOException {
        List<String> linhas = carregarArquivo(caminhoArquivo);
        
        // Determina as dimensões do mapa
        int altura = linhas.size();
        int largura = 0;
        
        for (String linha : linhas) {
            largura = Math.max(largura, linha.length());
        }
        
        // Cria a matriz para o mapa
        char[][] mapa = new char[altura][largura];
        
        // Preenche a matriz com os caracteres do arquivo
        for (int y = 0; y < altura; y++) {
            String linha = linhas.get(y);
            for (int x = 0; x < linha.length(); x++) {
                mapa[y][x] = linha.charAt(x);
            }
            
            // Preenche o restante da linha com espaços em branco
            for (int x = linha.length(); x < largura; x++) {
                mapa[y][x] = ' ';
            }
        }
        
        return mapa;
    }
}