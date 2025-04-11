package util;

public class Constants {
    // Constantes para o jogo
    public static final int TAMANHO_TABULEIRO = 10;
    public static final int SAUDE_INICIAL_JOGADOR = 5;
    
    // Constantes para os zumbis
    public static final int SAUDE_ZUMBI_RASTEJANTE = 1;
    public static final int SAUDE_ZUMBI_COMUM = 2;
    public static final int SAUDE_ZUMBI_CORREDOR = 2;
    public static final int SAUDE_ZUMBI_GIGANTE = 3;
    
    public static final int DANO_ZUMBI_NORMAL = 1;
    public static final int DANO_ZUMBI_GIGANTE = 2;
    
    // Constantes para dificuldade
    public static final int PERCEPCAO_FACIL = 3;
    public static final int PERCEPCAO_MEDIO = 2;
    public static final int PERCEPCAO_DIFICIL = 1;
    
    // Constantes para combate
    public static final int VALOR_GOLPE_CRITICO = 6; // Valor do dado para golpe crítico (mãos)
    public static final int DANO_GOLPE_NORMAL = 1;
    public static final int DANO_GOLPE_CRITICO = 2;
    
    // Constantes para os arquivos
    public static final String PASTA_MAPAS = "resources/maps/";
    
    // Constantes para os caracteres do mapa
    public static final char CHAR_VAZIO = 'V';
    public static final char CHAR_PAREDE = 'P';
    public static final char CHAR_ZUMBI_COMUM = 'Z';
    public static final char CHAR_ZUMBI_RASTEJANTE = 'R';
    public static final char CHAR_ZUMBI_CORREDOR = 'C';
    public static final char CHAR_ZUMBI_GIGANTE = 'G';
    public static final char CHAR_BAU = 'B';
}