package Semantico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Junior Ribeiro
 */
public class Semantico {

    public static ArrayList<String[]> Codigo = new ArrayList<String[]>();

    public static void main(String[] args) throws InterruptedException, IOException {
        ArrayList<String[]> caracteres;
        caracteres = LeArquivo();
    }

    private static ArrayList<String[]> LeArquivo() throws FileNotFoundException {
        Scanner codigo = new Scanner(new File("C:\\Users\\Junior Ribeiro\\Google Drive\\Universidade\\UFPB\\2019.1\\Compiladores\\Projeto Semantico\\Trabalho_compiladores_sintatico\\tabela_lexica.txt"));

        String linha;
        String[] line;

        while (codigo.hasNextLine()) {
            linha = codigo.nextLine();
            line = linha.split("#");
            Codigo.add(line);
            System.out.println(Arrays.toString(line));

        }
        System.out.println(Codigo);
        return Codigo;

    }

    public static int contador = 0;

    /**
     * Esta função será a primeira a ser executada de uma lista recursiva para
     * análise sintátiva de um programa
     *
     * @return True se o programa estiver correto, false caso contrário
     */
    public static boolean Programa() {
        // String[] s = "nanana#identificador#1".split("#");
        // codigo.add(s);
        // Pega o primeiro elemento do array
        String[] str = Codigo.get(contador++);
        // O programa inicia com "Program id"
        if (str[0].equals("program")) {
            str = Codigo.get(contador++);
            // id pode ser qualquer tipo de identificador
            if (str[1].equals("Identificador")) {
                if (str[0].equals(";")) {
                    Declaracoes_de_variaveis();
                    Declaracoes_de_subprogramas();
                    Comando_composto();
                }
                if (str.equals(".")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Aqui é o trecho onde há definições de variáveis
     *
     * @return retorna true se der tudo certo, false caso contrário
     */
    private static boolean Declaracoes_de_variaveis() {
        String[] str = Codigo.get(contador++);

        if (str[0].equals("var")) {
            Lista_declaracoes_variaveis();
        }
        return true;
    }

    private static boolean Declaracoes_de_subprogramas() {
        Declaracoes_de_subprogramas_2();
        return true;
    }

    private static boolean Comando_composto() {
        String[] str = Codigo.get(contador++);

        if (str[0].equals("begin")) {
            str = Codigo.get(contador++);
            Comandos_opcionais();
            if (str.equals("end")) {
                return true;
            }
        }
        return false;
    }

    private static boolean Comandos_opcionais() {
        Lista_de_Comandos();
        return true;
    }

    private static boolean Lista_de_Comandos() {
        return true;
    }

    private static boolean Comando() {
        String[] str = Codigo.get(contador++);
        if (Variavel()) {
            if (str[0].equals("?=")) {
                if (Expressao()) {
                    return true;
                }
            }

        } else if (ativacaoDeProcedimento()) {
            return true;
        } else if (comandoComposto()) {
            return true;
        } else if (str[0].equals("if")) {
            if (Expressao()) {
                if (str[0].equals("then")) {
                    if (Comando()) {
                        if (parteElse()) {
                            return true;
                        }
                    } else {
                        return false;
                    }

                }

            }

            //nenhuma das possibilidades de um comando
            return false;
        }

    

    

    private static boolean ativacaoDeProcedimento() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static boolean comandoComposto() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static boolean Variavel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static boolean Expressao() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static boolean parteElse() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

}
