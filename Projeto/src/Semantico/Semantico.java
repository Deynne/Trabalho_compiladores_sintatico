package Semantico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import static javaapplication12.Funcao.codigo;
import static javaapplication12.Funcao.contador;

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

    /**
     * Aqui é vê a lista de variaveis
     *
     * @return
     */
    private static boolean Lista_declaracoes_variaveis() {
        Lista_de_identificadores();
        String[] str = codigo.get(contador++);
        if (str[0].equals(":")) {
            Tipo();
            str = codigo.get(contador++);
            if (str[0].equals(";")) {
                Lista_declarações_variáveis_2();
                return true;
            }
        }
        return false;
    }

    /**
     * Aqui vê a lista de variaveis. Criado pra evitar loop infinito.
     *
     * @return
     */
    private static boolean Lista_declarações_variáveis_2() {
        Lista_de_identificadores();
        String[] str = codigo.get(contador++);
        if (str[0].equals(":")) {
            Tipo();
            str = codigo.get(contador++);
            if (str[0].equals(";")) {
                Lista_declarações_variáveis_2();
                return true;
            }
        }
        return true;
    }

    /**
     * Aqui ve a lista de indetificadores
     *
     * @return
     */
    private static boolean Lista_de_identificadores() {
        String[] str = codigo.get(contador++);
        if (str[1].equals("Identificador")) {
            Lista_de_identificadores_2();
            return true;
        }
        return false;
    }

    /**
     * Aqui ve a lista de indetificadores. Criado para evitar loop infinito.
     *
     * @return
     */
    private static boolean Lista_de_identificadores_2() {
        String[] str = codigo.get(contador++);

        if (str[0].equals(",")) {
            str = codigo.get(contador++);
            if (str[1].equals("Identificador")) {
                Lista_de_identificadores_2();
                return true;
            }
        }
        return true;
    }

    /**
     * Aqui vê a declaração de subprogramas
     *
     * @return
     */
    private static boolean Declaracoes_de_subprogramas() {
        if (Declaracoes_de_subprogramas_2()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Aqui vê a declaração de subprogramas, com permição do vazio.
     *
     * @return
     */
    private static boolean Declaracoes_de_subprogramas_2() {
        String[] str = Codigo.get(contador++);
        if (Declaracoes_de_subprogramas()) {
            if (str[0].equals(";")) {
                return Declaracoes_de_subprogramas_2();
            }
        }
        return true;
    }

    private boolean declaracao_de_Subprograma() {

        String[] str = Codigo.get(contador++);
        if (str[0].equals(":")) {
            if (str[2].equals("Identificador")) {
                if (Argumentos()) {
                    if (str[0].equals(";")) {
                        if (Declaracoes_de_variaveis()) {
                            if (Declaracoes_de_subprogramas());
                        }
                        if (comandoComposto()) {
                            return true;
                        }
                    } else {
                        return false;
                    }

                } else {
                    return false;
                }
            }
        }

        return false;
    }

    private boolean Argumentos() {

        String[] str = Codigo.get(contador++);
        if (str[0].equals("(")) {
            if (lista_De_Parametros()) {
                if (str[0].equals(")")) {  // (LISTA DE PARAMETROS)
                    return true;
                } else { // NÃO FECHOU PARENTESES)
                    return false;
                }
            } else { //SEM ARGUMENTOS
                return false;
            }
        }
        return true;
    }

    private boolean lista_De_Parametros() {
        String[] str = Codigo.get(contador++);
        if (Lista_de_identificadores()) {
            if (str[0].equals(":")) {
                if (lista_De_Parametros_2()) {
                    return true;
                }
            }
        } else {
            return false;
        }

        return true;
    }

    private boolean lista_De_Parametros_2() {
        String[] str = Codigo.get(contador++);
        if (str[0].equals(";")) {
            if (Lista_de_identificadores()) {
                if (str[0].equals(":")) {
                    if (Tipo()) {
                        return lista_De_Parametros_2();
                    } else {
                        return false;
                    }
                } else {
                    return false;

                }
            } else {
                return false;
            }

        } else {
            return true;
        }
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
        String[] str = Codigo.get(contador++);

        if (Comando()) {
            if (Lista_de_Comandos2()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;

        }
    }

    private static boolean Lista_de_Comandos2() {
        String[] str = Codigo.get(contador++);
        if (str[0].equals(";")) {
            if (Comando()) {
                return Lista_de_Comandos2();
            } else {
                return false;
            }
        } else {
            return true;
        }

    }

    private static boolean Comando() {
        String[] str = Codigo.get(contador++);
        if (Variavel()) {
            if (str[0].equals("?=")) {
                if (Expressao()) {
                    return true;
                } else {
                    return false;
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

                } else {
                    return false;
                }

            } else {
                return false;
            }
        } else if (str[0].equals("while")) {
            if (Expressao()) {
                if (str[0].equals("do")) {
                    if (Comando()) {
                        return true;
                    } else {
                        return false;
                    }

                } else {
                    return false;
                }

            } else {
                return false;
            }
        }

        // Nenhuma das possibildiades do comando
        return false;

    }

    private static boolean parteElse() {
        String[] str = codigo.get(contador++);
        if (str[0].equals("else")) {
            if (Comando()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private static boolean Variavel() {
        String[] str = codigo.get(contador++);

        if (str[1].equals("Identificador")) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean ativacaoDeProcedimento() {

        String[] str = codigo.get(contador++);

        if (str[1].equals("Identificador")) {
            if (str[0].equals("(")) {
                if (listaEspressoes()) {
                    if (str[0].equals(")")) {
                        return true;

                        // Procedimento de tipo id ( lista de expressão)
                    } else {
                        return false;
                    }

                } else {
                    return false;

                }

            } else {
                return true;
                // Procedimento de tipo id
            }
        } else {
            System.out.println("Ativação Invalida na linha" + str[1]);
            return false;

            // Ativaçã de procedimento inválido.
        }

    }

    private static boolean comandoComposto() {

        String[] str = codigo.get(contador++);

        if (str[0].equals("Begin")) {
            if (Comandos_opcionais()) {
                if (str[0].equals("End")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            //System.err.println("Comando composto invalido (Falta 'begin') na linha " + getCurrentTokenPosition());
            return false;
        }

    }

    private static boolean listaEspressoes() {

        String[] str = codigo.get(contador++);

        if (Expressao()) {
            if (listaExpressoes2()) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }

    }

    private static boolean listaExpressoes2() {

        String[] str = codigo.get(contador++);

        if (str[0].equals(",")) {
            if (Expressao()) {
                return listaExpressoes2();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private static boolean Expressao() {
        if (Expressao_Simples()) {
            if (OperadorRelacional()) {
                if (Expressao_Simples()) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private static boolean Expressao_Simples() {
        if (Termo()) {
            Expressao_Simples_2();

        } else if (Sinal()) {
            if (Termo()) {
                Expressao_Simples_2();
            }
        } else {
            return false;
        }
        //  termo expressaoSimples OU sinal termo expressaoSimples
        return true;
    }

    private static boolean Expressao_Simples_2() {
        if (OperadorAditivo()) {
            if (Termo()) {
                if (Expressao_Simples_2());
                return true;
            } else {
                return false;
            }
        }
        //(operadorAditivo termo/)*
        return true;
    }

    private static boolean Termo() {
        if (Fator()) {
            if (Termo_2()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean Termo_2() {
        if (OperadorMultiplicativo()) {
            if (Fator()) {
                Termo_2();// operadorMultiplicativo fator Termo_2
                return true;
            } else {
                return false;
            }
        }
        return true;

    }

    private static boolean Fator() {
        String[] str = Codigo.get(contador++);
        if (str[1].equals("identificador")) {
            if (str[0].equals("(")) {
                if (listaEspressoes()) {
                    if (str[0].equals(")")) {//  id(listaDeExpressoes)
                        return true;
                    } else {
                        return false;
                    }

                } else {
                    return false;
                }
            } //  id
            return true;
        } else if (str[1].equals("Numero Inteiro")) {
            return true;
        } else if (str[1].equals("Numero Real")) {
            return true;
        } else if (str[0].equals("(true")) {
            return true;
        } else if (str[0].equals("false")) {
            return true;
        } else if (str[0].equals("(")) {
            if (Expressao()) {
                if (str[0].equals(")")) {    //(expressao)
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else if (str[0].equals("not")) {
            if (Fator()) {   //not fator
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * Aqui vê o tipo
     *
     * @return
     */
    private static boolean Tipo() {
        String[] str = codigo.get(contador++);

        if (str[0].equals("integer") || str[0].equals("real") || str[0].equals("boolean") || str[0].equals("char")) {
            return true;
        }
        return false;
    }

    private static boolean OperadorRelacional() {
        String[] str = codigo.get(contador++);

        if (str[0].equals("=") || str[0].equals("<") || str[0].equals(">") || str[0].equals("<=") || str[0].equals(">=") || str[0].equals("<>")) {
            return true;
        }

        return false;
    }

    private static boolean Sinal() {
        String[] str = codigo.get(contador++);

        if (str[0].equals("+") || str[0].equals("-")) {
            return true;
        }

        return false;
    }

    private static boolean OperadorAditivo() {
        String[] str = codigo.get(contador++);

        if (str[0].equals("+") || str[0].equals("-") || str[0].equals("or")) {
            return true;
        }

        return false;
    }

    private static boolean OperadorMultiplicativo() {
        String[] str = codigo.get(contador++);

        if (str[0].equals("*") || str[0].equals("/") || str[0].equals("and")) {
            return true;
        }

        return false;
    }
}
