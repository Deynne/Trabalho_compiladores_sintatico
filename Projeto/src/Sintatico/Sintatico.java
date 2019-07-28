package Sintatico;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import Excessao.Erro_Sintatico_Exception;

/**
 * Para a compreensão deste arquivo recomenda-se seguir as especificações localizadas em https://drive.google.com/file/d/1REI8cnx7Kktlgh_fw-LiJcwGZq7V-Atc/view?usp=sharing
 * @author Junior Ribeiro
 */
public class Sintatico {
    /** Array contendo todos os elementos lidos pelo lexico */
    private static ArrayList<String[]> Codigo = new ArrayList<String[]>();
    /** Marcador do ultimo elemento lido */
    private static int contador = 0;
    /** Armazena a linha atualmente analisada do léxico */
    private static String[] str;
    /** Flag para tratar as diferentes respostas do comando_composto */
    private static boolean proxima_execucao_eh_teste = false,
                           /** Flag para tratar as diferentes respostas do comando */
                           eh_o_segundo_comando = false,
                           pode_ser_vazio = false;

    public static void LeArquivo(String str) throws FileNotFoundException {
        Scanner codigo = new Scanner(new File(str));

        String linha;
        String[] line;

        while (codigo.hasNextLine()) {
            linha = codigo.nextLine();
            line = linha.split("#");
            Codigo.add(line);
            System.out.println(Arrays.toString(line));;

        }
        codigo.close();
        //System.out.println(Codigo);

    }
    
    /**
     * Esta função será a primeira a ser executada de uma lista recursiva para
     * análise sintátiva de um programa
     *
     * @return True se o programa estiver correto, false caso contrário
     * @throws Excessao.Erro_Sintatico_Exception
     */
    public static boolean Programa() throws Erro_Sintatico_Exception {
        // Pega o elemento do array
        str = Codigo.get(contador++);
        // O programa inicia com "Program id"
        if (str[0].equals("program")) {
            str = Codigo.get(contador++); // Pega o elemento
            // id pode ser qualquer tipo de identificador
            if (str[1].equals("Identificador")) {
                str = Codigo.get(contador++); // Pega o elemento
                // Depois do identificador tem que haver um ;
                if (str[0].equals(";")) {
                    // Se tudo isso estiver certo inicia-se a declaração de variaveis. No pascal todas são feitas
                    // juntas
                    Declaracoes_de_variaveis();
                    // Se houver, define os subprogramas
                    Declaracoes_de_subprogramas();
                    // Por fim se inicia a programação dos códigos
                    Comando_composto();
                    // O programa termina com .
                    str = Codigo.get(contador++);
                    if (str[0].equals(".")) {
                        return true;
                    }
                    else {
                        throw new Erro_Sintatico_Exception("Esperado . ao fim do arquivo");
                    }
                }
                else {
                    throw new Erro_Sintatico_Exception("Esperado ; mas recebido ".concat(str[0]),str[2]);
                }
                
            }
            else {
                throw new Erro_Sintatico_Exception("Esperado um identificador mas recebido ".concat(str[0].concat(" um ".concat(str[1]))),str[2]);
            }
        }
        else {
            throw new Erro_Sintatico_Exception("Esperado program mas recebido ".concat(str[0]),str[2]);
        }
    }

    /**
     * Aqui é o trecho onde há definições de variáveis
     *
     * @return retorna true se der tudo certo, false caso contrário
     */
    private static boolean Declaracoes_de_variaveis() throws Erro_Sintatico_Exception {
        // Pega o elemento
        str = Codigo.get(contador++);
        // Se var for definido, haverá ao menos uma variável, caso não seja, não haverão variáveis
        if (str[0].equals("var")) {
            // Podem haver mais de uma variável
            Lista_declaracoes_variaveis();
            return true;
        }
        contador--; // Casp não exista var não há variáveis
        return false; // representa o retorno vazio
    }

    /**
     * Aqui é vê a lista de variaveis
     *
     * @return
     */
    private static boolean Lista_declaracoes_variaveis() throws Erro_Sintatico_Exception {
        // adiciona o identificador Isto vai por ao menos um identificador/variável
        // Neste ponto é certo que haverá ao menos uma variável
        Lista_de_identificadores();
        // pega elemento
        str = Codigo.get(contador++);
        // Verifica se depois de por os identificadores todos tem um :
        if (str[0].equals(":")) {
            // Verifica se o tipo foi definido
            Tipo();
            // pega elemento
            str = Codigo.get(contador++);
            // Verifica se a linha fio encerrada com ;
            if (str[0].equals(";")) {
                // Inicia a declaração, se houver, de novas variáveis, provavelmente de outro tipo
                // O código neste ponto não verifica se serão tipos diferentes
                Lista_declaracoes_variaveis_2();
                return true;
            }
            else {
                throw new Erro_Sintatico_Exception("Esperado ; mas recebido ".concat(str[0]),str[2]);
            }
        }
        else {
            throw new Erro_Sintatico_Exception("Esperado : mas recebido ".concat(str[0]),str[2]);
        }
    }

    /**
     * Aqui vê a lista de variaveis. Criado pra evitar loop infinito.
     *
     * @return
     */
    private static boolean Lista_declaracoes_variaveis_2() throws Erro_Sintatico_Exception {
        pode_ser_vazio = true;
        // Pega a lista de identificadores
        if(Lista_de_identificadores()) {
            pode_ser_vazio = false;
            // pega elemento
            str = Codigo.get(contador++);
            // Verifica se após os identificadores todos tem um :
            if (str[0].equals(":")) {
                // Verifica o tipo
                Tipo();
                // Pega elemnto
                str = Codigo.get(contador++);
                // Verifica se a linha foi encerrada com ;
                if (str[0].equals(";")) {

                    // Inicia a declaração, se houver, de novas variáveis, provavelmente de outro tipo
                    // O código neste ponto não verifica se serão tipos diferentes
                    Lista_declaracoes_variaveis_2();
                    return true;
                }
                else {
                    throw new Erro_Sintatico_Exception("Esperado ; mas recebido ".concat(str[0]),str[2]);
                }
            }
            else {
                throw new Erro_Sintatico_Exception("Esperado : mas recebido ".concat(str[0]),str[2]);
            }
        }
        else {
            pode_ser_vazio = false;
            // Pode não haver mais variáveis. Nesse caso reverte a leitura
            return false; // retorna false para indicar que esta retornando vazio
        }
    }

    /**
     * Aqui ve a lista de indetificadores
     *
     * @return
     */
    private static boolean Lista_de_identificadores() throws Erro_Sintatico_Exception {
        // Pega o próximo
        str = Codigo.get(contador++);
        // Vê se ele é identificador
        if (str[1].equals("Identificador")) {
            // Verifica se haverão mais identificadores e coloca eles
            Lista_de_identificadores_2();
            return true;
        }
        else if(pode_ser_vazio){
            contador--;
            return false;//
        }
        else {
            throw new Erro_Sintatico_Exception("Esperado um identificador mas recebido ".concat(str[0].concat(" um ".concat(str[1]))),str[2]);
        }
    }

    /**
     * Aqui ve a lista de indetificadores. Criado para evitar loop infinito.
     *
     * @return
     */
    private static boolean Lista_de_identificadores_2() throws Erro_Sintatico_Exception {
        str = Codigo.get(contador++); //pega elemento
        // Verifica se haverão mais identificadores, eles são separados por "," então se houver ","
        // Haverá mais
        if (str[0].equals(",")) {
            // Pega o elemento
            str = Codigo.get(contador++);
            // Verifica se eesse novo elemento é identificador
            if (str[1].equals("Identificador")) {
                // Verifica se há mais variáveis.
                // PS: Neste código há a possibilidade de stackoverflow com excesso de variáveis
                Lista_de_identificadores_2();
                return true;
            }
            else {
                throw new Erro_Sintatico_Exception("Esperado um identificador mas recebido ".concat(str[0].concat(" um ".concat(str[1]))),str[2]);
            }
        } // Caso não hava virgula só tem uma variável, volta o contador para reler o elemento
        contador--;
        return false; // o false representa que voltou vazio
        
    }

    /**
     * Aqui vê o tipo
     *
     * @return
     */
    private static boolean Tipo() throws Erro_Sintatico_Exception {
        str = Codigo.get(contador++); // Pega elemento
        // Verifica se é um tipo aceito
        if (str[0].equals("integer") || str[0].equals("real") || str[0].equals("boolean")) {
            return true;
        }
        else {
            throw new Erro_Sintatico_Exception("Esperado integer, real ou boolean mas recebido ".concat(str[0]),str[2]);
        }
    }
    
    /**
     * Aqui vê a declaração de subprogramas
     *
     * @return
     */ //OBS: PODE SER QUE POSSA SER REMODIVO, TESTAR APÓS FINALIZAÇÃO
    private static boolean Declaracoes_de_subprogramas() throws Erro_Sintatico_Exception {
        Declaracoes_de_subprogramas_2();
        return true;
    }

    /**
     * Aqui vê a declaração de subprogramas, com permição do vazio.
     *
     * @return
     */
    private static boolean Declaracoes_de_subprogramas_2() throws Erro_Sintatico_Exception {
        // Caso retorne vazio não há subprogramas
        if (Declaracao_de_subprograma()) {
            
            str = Codigo.get(contador++); // pega elemento
            // Caso inicie-se a declaração de um subprograma
            // Verifica se o subprograma foi encerrado com ;
            if (str[0].equals(";")) {
                // Verifica se há outros subprogramas além do ja declarado.
                // Este trecho do código pode dar stackoverflow de acordo com o número
                // de subprogramas
                Declaracoes_de_subprogramas_2();
                return true;
            }
            else {
                throw new Erro_Sintatico_Exception("Esperado ; mas recebido ".concat(str[0]),str[2]);
            }
        }
        return false; // Retorna false para indicar retorno vazio
    }

    private static boolean Declaracao_de_subprograma() throws Erro_Sintatico_Exception {
        // pega elemento
        str = Codigo.get(contador++);
        // Verifica se é  inicio de uma declaração de subprograma, se não for
        // retorna vazio
        if (str[0].equals("procedure")) {
            // Se for
            str = Codigo.get(contador++); // pega elemento
            // verifica se é um identificador
            if (str[2].equals("Identificador")) {
                // Pega a lista de argumentos do subrograma
                Argumentos();
                // pega elemento
                str = Codigo.get(contador++);
                // Verifica se a lista de argumento é encerrada com ;
                if (str[0].equals(";")) {
                    // Inicia a declaração de variáveis, subprogramas e comandos compostos deste subprograma
                    // Este trecho de código pode dar stackoverflow de acordo com o nível de profundidade dos
                    // subsubsub-...subprogramas
                    Declaracoes_de_variaveis();
                    Declaracoes_de_subprogramas();
                    Comando_composto();
                    return true;
                }
                else {
                    throw new Erro_Sintatico_Exception("Esperado ; mas recebido ".concat(str[0]),str[2]);
                }

            } else {
                throw new Erro_Sintatico_Exception("Esperado integer, real ou boolean mas recebido ".concat(str[0]),str[2]);
            }
        }
        contador--; // Caso não seja reverte a leitura
        return false; //Neste caso o false representa que o retorno é vazio
    }

    /**
     * Pega a lista de parametros de um subprograma
     * @return
     * @throws Erro_Sintatico_Exception
     */
    private static boolean Argumentos() throws Erro_Sintatico_Exception {
        // Pega elemento
        str = Codigo.get(contador++);
        // Verifica se abre parenteses
        if (str[0].equals("(")) {
            // Inicia a lista de parâmetros
            lista_De_Parametros();
            // Pega próximo elemento
            str = Codigo.get(contador++);
            // Verifica se fecha o parenteses
            if (str[0].equals(")")) {  // (LISTA DE PARAMETROS)
                // Tudo certo
                return true;
            } else { // NÃO FECHOU PARENTESES)
                throw new Erro_Sintatico_Exception("Esperado ) mas recebido ".concat(str[0]),str[2]);
            }
        }
        contador--; // Caso não abra parenteses pode ser um subprograma sem parametros, nesse caso reverte a leitura
        return false; // retorna false para indicar vazio
    }

    /**
     * Pega um conjunto de parâmetros de um subprograma
     * @return
     * @throws Erro_Sintatico_Exception
     */
    private static boolean lista_De_Parametros() throws Erro_Sintatico_Exception {
        // Pega a lista de identificadores dos parametros de um determinado tipo
        Lista_de_identificadores();
        // pega elemento
        str = Codigo.get(contador++);
        // Verifica se após a lista há uma separação por :
        if (str[0].equals(":")) {
            // Pega o tipo da lista de parametros
            Tipo();
            // Verifica se há mais de um conjunto de parâmetros
            Lista_De_Parametros_2();
            return true;
        }
        else {
            throw new Erro_Sintatico_Exception("Esperado : mas recebido ".concat(str[0]),str[2]);
        }
    }

    /**
     * Pega um novo conjunto de parâmetros de um subprograma
     * @return
     * @throws Erro_Sintatico_Exception
     */
    private static boolean Lista_De_Parametros_2() throws Erro_Sintatico_Exception {
        // pega elemento
        str = Codigo.get(contador++);
        // Verifica se a lista de parâmetros é separada  da anterior por ;
        if (str[0].equals(";")) {
            // Pega o conjunto de identificadores
            Lista_de_identificadores();
            // pega elemento
            str = Codigo.get(contador++);
            // verifica se o conjunto é separado do tipo com :
            if (str[0].equals(":")) {
                // Pega o tipo do parametro
                Tipo();
                // Verifica se há mais parâmetros
                // Pode causar stackoverflow de acordo com a quantidade de listas de parâmetros diferentes
                // Neste ponto não verifica se há diversos grupos de um mesmo tipo
                Lista_De_Parametros_2();
            } else {
                throw new Erro_Sintatico_Exception("Esperado : mas recebido ".concat(str[0]),str[2]);
            }
        } 
        contador--; // Caso não existam mais conjutos de parametros, reverte a leitura
        return false; // retorna false para indicar que foi vazio
    }

    /**
     * Inicia os comandos do programa ou subprograma
     * @return
     * @throws Erro_Sintatico_Exception
     */
    private static boolean Comando_composto() throws Erro_Sintatico_Exception {
        str = Codigo.get(contador++); // pega elemento
        // Verifica se começou o begin
        if (str[0].equals("begin")) {
            // Avisa que na próxima chamada va
            proxima_execucao_eh_teste = true;
            // Se entrou aqui, independente da execução, o proximo comando é o primeiro do novo bloco
            eh_o_segundo_comando = false;
            // Vê se há comandos opicionais
            Comandos_opcionais();
            // pega elemento
            str = Codigo.get(contador++);
            // Verifica se encerrou os comandos com end
            if (str[0].equals("end")) {
                return true;
            }
            else {
                throw new Erro_Sintatico_Exception("Esperado end mas recebido ".concat(str[0]),str[2]);
            }
        }
        /* No caso do comando composto, ele deve ser chamado ao menos uma vez, mas pode ser chamado novamente
        após a primeira chamada. Neste caso, não necessáriamente vai ser um comando composto, vai ser só uma 
        verificação. Neste caso não faz sentido dar um erro. Deve-se apenas ignorar falar que não é comando composto
        e voltar.
        */
        else if(proxima_execucao_eh_teste) {
            //eh_o_segundo_comando = false;
            // Não é comando composto
            contador--; // Reverte a leitura
            return false; // Nesta situação ele só indica que não é comando composto
        }
        else {
            throw new Erro_Sintatico_Exception("Esperado begin mas recebido ".concat(str[0]),str[2]);
        }
    }
    /**
     * Pega a lista de comandos opicionais
     * @return
     * @throws Erro_Sintatico_Exception
     */
    private static boolean Comandos_opcionais() throws Erro_Sintatico_Exception {
        if(Lista_de_Comandos())  // Pega lista de comandos
            return true;
        return false; // Retorna falso para indicar que esta retornando vazio
    }

    /**
     * Pega lista de comandos
     * @return
     * @throws Erro_Sintatico_Exception
     */
    private static boolean Lista_de_Comandos() throws Erro_Sintatico_Exception {
        // Verifica a lista de comandos
        // Se nenhum dos comandos da lista de comandos for aceito, isso quer dizer que na verdade
        // comando composto é vazio
        pode_ser_vazio = true;
        if(Comando()) {
            pode_ser_vazio = false;
            // No caso de entrar, há um comando, então finaliza o comando e verifica se há outro
            Lista_de_Comandos_2();
            return true;
        }
        return false; // false aqui indica que comando_opcional é vazio, pois o elemento não corresponde a nenhum comando
    }

    /**
     * Encerra um comando e verifica se há outros comandos. Vale lembrar que o ultimo comando, em pascal, não tem ;
     * @return
     * @throws Erro_Sintatico_Exception
     */
    private static boolean Lista_de_Comandos_2() throws Erro_Sintatico_Exception {
        // Pega o elemento
        str = Codigo.get(contador++);
        // Verifica se após o comando é um ;
        if (str[0].equals(";")) {
            // Se entrou aqui, não é um comando final ja que o ultimo comando não tem ;
            // Neste caso pega o próximo comando
            Comando();
            // Verifica se haverão outros comandos
            // Dependendo da quantidade de comandos pode ocorrer stackoverflow
            Lista_de_Comandos_2();
            return true;
        }
        contador--; // Caso não tenha ; ele esta retornando vazio então desfaz a leitura
        return false; // Retorna false para indicar vazio
    }

    /**
     * Executa algum comando
     */
    private static boolean Comando() throws Erro_Sintatico_Exception {
        // Pega elemnto
        str = Codigo.get(contador++);
        // Se for um if ele é aceito
        if(str[0].equals("if")) {
            // Flag ligada para avisar que comando opicional não tem como ser vazio
            eh_o_segundo_comando = true;
            Expressao(); // Verifica a expressão
            // Pega elemento
            str = Codigo.get(contador++);
            // verifica se é then
            if(str[0].equals("then")) {
                Comando(); // Pega o próximo comando
                Parte_else(); // Verifica se tem else
                return true;
            }
            // Caso não tenha then é um erro
            else {
                throw  new Erro_Sintatico_Exception("Esperado then mas recebido ".concat(str[0]),str[2]);
            }
        }
        // Se não for um if pode ser um while
        else if(str[0].equals("while")) {
            // Flag ligada para avisar que comando opicional não tem como ser vazio
            eh_o_segundo_comando = true;
            Expressao();// Verifica a expressão
            // Pega elemento
            str = Codigo.get(contador++);
            // verifica se é do
            if(str[0].equals("do")) {
                Comando(); // PEga o próximo comando
                return true;
            }
            // Caso não seja do é um erro
            else {
                throw  new Erro_Sintatico_Exception("Esperado do mas recebido ".concat(str[0]),str[2]);
            }
        }
        else if (str[0].equals("case")) {
            str = Codigo.get(contador++);
            if(str[1].equals("Identificador")) {
                str = Codigo.get(contador++);
                if(str[0].equals("of")) {
                   Lista_de_casos();
                   str = Codigo.get(contador++);
                    if(str[0].equals("end")) {
                        return true;
                    }
                    else {
                        throw  new Erro_Sintatico_Exception("Esperado end mas recebido ".concat(str[0]),str[2]);
                    }
                   
                }
                else {
                    throw  new Erro_Sintatico_Exception("Esperado of mas recebido ".concat(str[0]),str[2]);
                }
            }
            else {
                throw new Erro_Sintatico_Exception("Esperado um identificador mas recebido ".concat(str[0].concat(" um ".concat(str[1]))),str[2]);
            }
        }
        // Já que nenhuma das constantes é correta, a leitura precisa ser desfeita
        contador--;
        // Tenta ver se é uma variável
        if (Variavel()) {
            // Flag ligada para avisar que comando opicional não tem como ser vazio
            eh_o_segundo_comando = true;
            // pega elemento
            str = Codigo.get(contador++);
            // tem de ser uma atribuição
            if(str[0].equals(":=")) {
                Expressao(); // Pega expressão
                return true;
            }
            else {
                throw  new Erro_Sintatico_Exception("Esperado := mas recebido ".concat(str[0]),str[2]);
            }
            
            
        }
        // Verifica se é a ativação de um procedimento
        else if (Ativacao_de_procedimento()) {
            // Flag ligada para avisar que comando opicional não tem como ser vazio
            eh_o_segundo_comando = true;
            return true;
        }
        else if (Lista_de_casos()) {
            eh_o_segundo_comando = true;
            return true;
        }
        // Verifica se é um novo comando composto
        else if(Comando_composto()) {
            return true;
        }
        /* Caso não exista nenhum comando até o momento, isso quer dizer que o begin 
        */
        else if (eh_o_segundo_comando && !pode_ser_vazio) {
            throw new Erro_Sintatico_Exception("Houve um erro na leitura do comando",Codigo.get(contador)[2]);
        }
        else {
            return false; // Isso indicará para o comando opcional que ele é vazio
        }
    }

    private static boolean Lista_de_casos() throws Erro_Sintatico_Exception {
        str = Codigo.get(contador++);
        if(str[1].equals("Numero Inteiro")) {
            str = Codigo.get(contador++);
            if(str[0].equals(":")) {
                Comando();
                Lista_de_casos_2();
                return true;
            }
            else {
                throw  new Erro_Sintatico_Exception("Esperado : mas recebido ".concat(str[0]),str[2]);
            }
        }
        else {//if(pode_ser_vazio) {
            contador--;
            return false;
        }
//        else {
//            throw new Erro_Sintatico_Exception("Esperado um identificador mas recebido ".concat(str[0].concat(" um ".concat(str[1]))),str[2]);
//        }
        
    }
    
    private static boolean Lista_de_casos_2() throws Erro_Sintatico_Exception {
        str = Codigo.get(contador++);
        if(str[0].equals(";")) {
            Alternativa_lista_de_casos_2();
            return true;
        }
        contador--;
        return false;
        
    }
    
    private static boolean Alternativa_lista_de_casos_2() throws Erro_Sintatico_Exception {
        str = Codigo.get(contador++);
        if(str[1].equals("Numero Inteiro")) {
            str = Codigo.get(contador++);
            if(str[0].equals(":")) {
                Comando();
                Lista_de_casos_2();
                return true;
            }
            else {
                throw  new Erro_Sintatico_Exception("Esperado : mas recebido ".concat(str[0]),str[2]);
            }
        }
        contador--;
        if (Parte_else()){
            return true;
        }
        else {
            throw  new Erro_Sintatico_Exception("Ultima instrução não pode conter ;",str[2]);
        }
    
    }
    
    /**
     * verifica se há else
     * @return
     * @throws Erro_Sintatico_Exception
     */
    private static boolean Parte_else() throws Erro_Sintatico_Exception {
        // pega elemento
        str = Codigo.get(contador++);
        if (str[0].equals("else")) {
            Comando();
            return true;// Executa o novo comando
        }
        contador--; // Se não for um else pode ser vazio, reverte a leitura
        return false; // retorna false para representar vazio
    }

    /**
     * verifica se é variavel
     */
    private static boolean Variavel() {
        str = Codigo.get(contador++); // pega elemento
        // verifica se é identificador
        if (str[1].equals("Identificador")) {
            return true;
        }
        else {
            contador--;
            return false; // Isso indicará para o comando opcional que ele é vazio
            // PS: Variavel não retorna vazio, mas comando trata em caso de um erro
            // Não se pode dar throw aqui por que isso causará problemas em comando opcional
        }
    }

    /**
     * Verifica se esta ocorrendo uma ativação de procedimento
     */
    private static boolean Ativacao_de_procedimento() throws Erro_Sintatico_Exception {
        // pega elemento
        str = Codigo.get(contador++);
        // Verifica se é identificador
        if (str[1].equals("Identificador")) {
            Alternativas_id();
            return true;
        } else {
            contador--;
            return false; // Isso indicará para o comando opcional que ele é vazio
            // PS: Variavel não retorna vazio, mas comando trata em caso de um erro
            // Não se pode dar throw aqui por que isso causará problemas em comando opcional
        }

    }

    /**
     * verifica se id assume alguma alternativa
     */
    private static boolean Alternativas_id() throws Erro_Sintatico_Exception {
        // pega elemento
        str = Codigo.get(contador++);
        if(str[0].equals("(")) {
            Lista_de_expressões();
            str = Codigo.get(contador++);
            if(str[0].equals(")")) {
                return true;
            }
            else {
                throw  new Erro_Sintatico_Exception("Esperado ) mas recebido ".concat(str[0]),str[2]);
            }
        }
        contador--;
        return false;
    }

    /**
     * Lista de expressões
     */
    private static boolean Lista_de_expressões() throws Erro_Sintatico_Exception {
        Expressao();
        Lista_de_expressões_2();
        return true;
    }

    private static boolean Lista_de_expressões_2() throws Erro_Sintatico_Exception {

        str = Codigo.get(contador++);
        if (str[0].equals(",")) {
            Expressao();
            Lista_de_expressões_2();
            return true;
        } else {
            contador--;
            return false;
        }
    }

    private static boolean Expressao() throws Erro_Sintatico_Exception {
        Expressao_Simples();
        Alternativas_expressao();
        return true;
    }

    private static boolean Alternativas_expressao() throws Erro_Sintatico_Exception {
        Operador_relacional();
        Expressao_Simples();
        return true;
    }

    private static boolean Expressao_Simples() throws Erro_Sintatico_Exception {
        
        
        if (Termo()) {
            Expressao_Simples_2();
            return true;
        }
        pode_ser_vazio = true;
        Sinal();
        pode_ser_vazio = false;
        Termo();
        Expressao_Simples_2();
        return true;

    }

    private static boolean Expressao_Simples_2() throws Erro_Sintatico_Exception {
        if (Operador_aditivo()) {
            Termo();
            Expressao_Simples_2();
            return true;
        }
        //(operadorAditivo termo/)*
        return false;
    }

    private static boolean Termo() throws Erro_Sintatico_Exception {
        if(Fator()) {
            Termo_2();
            return true;
        }
        return false;
    }

    private static boolean Termo_2() throws Erro_Sintatico_Exception {
        if(Operador_multiplicativo()) {
            Fator();
            Termo_2();
            return true;
        }

        return false; // retorna false para indicar que é vazio
    }

    private static boolean Fator() throws Erro_Sintatico_Exception {
        str = Codigo.get(contador++);
        if (str[1].equals("Identificador")) {
            Alternativas_id();
            return true;
        }
        else if (str[1].equals("Numero Inteiro")) {
            return true;
        }
        else if (str[1].equals("Numero Real")) {
            return true;
        }
        else if (str[0].equals("true")) {
            return true;
        }
        else if (str[0].equals("false")) {
            return true;
        }            
        else if (str[0].equals("not")) {
            Fator();
            return true;
        }
        else if (str[0].equals("(")) {
            Expressao();
            str = Codigo.get(contador++);
            if(str[0].equals(")")) {
                return true;
            }
            else {
                throw  new Erro_Sintatico_Exception("Esperado ) mas recebido ".concat(str[0]),str[2]);
            }
        
        } else {
            contador--;
            return false;//throw  new Erro_Sintatico_Exception("Esperado integer, real, boolean, (, not, ou Identificador mas recebido ".concat(str[0].concat(" um ".concat(str[1]))),str[2]);
        }

    }

    private static boolean Operador_relacional() throws Erro_Sintatico_Exception {
        str = Codigo.get(contador++);
        if (str[0].equals("=") || str[0].equals("<") || str[0].equals(">") || str[0].equals("<=") || str[0].equals(">=") || str[0].equals("<>")) {
            return true;
        }
        else {
            contador--;
            return false; //throw  new Erro_Sintatico_Exception("Esperado =, <, >, <=, >= ou <> mas recebido ".concat(str[0]),str[2]);
        }
    }

    private static boolean Sinal() throws Erro_Sintatico_Exception {
        str = Codigo.get(contador++);
        if (str[0].equals("+") || str[0].equals("-")) {
            return true;
        }
        else if(pode_ser_vazio) {
            contador--;
            return false;
        }
        else {
            throw  new Erro_Sintatico_Exception("Esperado + ou - mas recebido ".concat(str[0]),str[2]);
        }
    }

    private static boolean Operador_aditivo() throws Erro_Sintatico_Exception {
        str = Codigo.get(contador++);
        if (str[0].equals("+") || str[0].equals("-") || str[0].equals("or")) {
            return true;
        }
        else {
            contador--;
            return false;
        }
    }

    private static boolean Operador_multiplicativo() throws Erro_Sintatico_Exception {
        str = Codigo.get(contador++);
        if (str[0].equals("*") || str[0].equals("/") || str[0].equals("and")) {
            return true;
        }
        else {
            contador--;
            return false;
        }
    }
}
