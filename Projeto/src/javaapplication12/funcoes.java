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

    private static boolean Declaracoes_de_subprogramas() {
        Declaracoes_de_subprogramas_2();
        return true;
    }

}