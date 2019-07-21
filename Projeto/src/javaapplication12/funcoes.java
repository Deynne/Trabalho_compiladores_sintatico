package javaapplication12;

import java.util.ArrayList;

public class Funcao {

    //TEMPORARIO
    public static ArrayList<String[]> codigo = new ArrayList<String[]>();
    public static int contador = 0;

    /**
     * Esta função será a primeira a ser executada de uma lista recursiva para análise
     * sintátiva de um programa
     * @return True se o programa estiver correto, false caso contrário
     */
    public static boolean Programa() {
        // String[] s = "nanana#identificador#1".split("#");
        // codigo.add(s);
        // Pega o primeiro elemento do array
        String[] str = codigo.get(contador++);
        // O programa inicia com "Program id"
        if(str[0].equals("program")) {
            str = codigo.get(contador++);
            // id pode ser qualquer tipo de identificador
            if(str[1].equals("Identificador")) {
                if(str[0].equals(";")) {
                    Declaracoes_de_variaveis();
                    Declaracoes_de_subprogramas();
                    Comando_composto();
                }
                    if(str.equals(".")) {
                        return true;
                    }
            }
        }
        return false;
    }

    /**
     * Aqui é o trecho onde há definições de variáveis
     * @return retorna true se der tudo certo, false caso contrário
     */
    private static boolean Declaracoes_de_variaveis() {
        String[] str = codigo.get(contador++);
        
        if(str[0].equals("var")) {
            Lista_declaracoes_variaveis();
        }
        return true;
    }
    /**
     * Aqui é vê a lista de variaveis
     * @return
     */
    private static boolean Lista_declaracoes_variaveis() {
        Lista_de_identificadores();
        String[] str = codigo.get(contador++);
        if(str[0].equals(":")) {
            Tipo();
            str = codigo.get(contador++);
            if(str[0].equals(";")) {
                Lista_declarações_variáveis_2();
                return true;
            }
        }
        return false;
    }

    /**
     * Aqui vê a lista de variaveis. Criado pra evitar loop infinito.
     * @return
     */
    private static boolean Lista_declarações_variáveis_2() {
        Lista_de_identificadores();
        String[] str = codigo.get(contador++);
        if(str[0].equals(":")) {
            Tipo();
            str = codigo.get(contador++);
            if(str[0].equals(";")) {
                Lista_declarações_variáveis_2();
                return true;
            }
        }
        return true;
    }

    /**
     * Aqui ve a lista de indetificadores
     * @return
     */
    private static boolean Lista_de_identificadores() {
        String[] str = codigo.get(contador++);
        if(str[1].equals("Identificador")) {
            Lista_de_identificadores_2();
            return true;
        }
        return false;
    }

    /**
     * Aqui ve a lista de indetificadores. Criado para evitar loop infinito.
     * @return
     */
    private static boolean Lista_de_identificadores_2() {
        String[] str = codigo.get(contador++);
        
        if(str[0].equals(",")) {
            str = codigo.get(contador++);
            if(str[1].equals("Identificador")) {
                Lista_de_identificadores_2();
                return true;
            }
        }
        return true;
    }

    /**
     * Aqui vê o tipo
     * @return
     */
    private static boolean Tipo() {
        String[] str = codigo.get(contador++);

        if(str[0].equals("integer") || str[0].equals("real") || str[0].equals("boolean")) {
            return true;
        }
        return false;
    }

    /**
     * Aqui vê a declarações de funções
     * @return
     */
    private static boolean Declaracoes_de_subprogramas() {
        Declaracoes_de_subprogramas_2();
        return true;
    }

}