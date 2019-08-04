/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semantico;

import Excessao.Erro_Compilacao_Exception;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author deynne
 */
public class Semantico {
    private  ArrayList<Escopo> Lista_de_escopos = new ArrayList<Escopo>();
    private int posicao = 0,posicao_paralelo = 0, posicao_serie = 0;
    private  Stack<String> Pct = new Stack<String>();
    public static boolean controle_posicao_serie = false;

    public void marcaTipo(String string) {
        ArrayList<Variavel> l = Lista_de_escopos.get(posicao).lista_de_variaveis;
        
        for(int i = l.size()-1; i >=0;i--) {
            if(l.get(i).getTipo().equals("indefinido")) {
                l.get(i).setTipo(string);
            }
            else {
                return;
            }
        }
            
       
    }

    public String avalia_expressao() throws Erro_Compilacao_Exception {
        String [] val = new String[3];
        int contador = 0;
        while(Pct.size() != 0) {
            val[contador++] = Pct.pop();
            if(val[contador-1].equals(")")) { 
                contador--;
                Pct.push(avalia_expressao());
            }
            else if(val[contador-1].equals("("))
                return val[0];
            if(contador == 3) {
                contador = 0;
                Pct.push(verifica_operacao(val[2], val[1], val[0]));
            }
            else if(contador == 2 && (val[contador-1].equals("Sinal") || val[contador-1].equals("Negacao")))
                Pct.push(verifica_operacao(val[1],val[0]));
                
        }
        return val[0];
    }
    
    private String verifica_operacao(String val1, String operador, String val2) throws Erro_Compilacao_Exception {
        if(operador.equals("Atribuicao") && val1.equals("Numero real") && !val2.equals("Booleano") && !val2.equals("procedure") && !val2.equals("program"))
            return val1;
        else if(operador.equals("Atribuicao") && val1.equals("Booleano") && val2.equals("Booleano"))
            return val1;
        else if (operador.equals("Relacional") && (!val1.equals("procedure") || !val1.equals("program")) && (!val2.equals("procedure") || !val2.equals("program")))
            return "Booleano";
        else if ((operador.equals("Multiplicativo") || operador.equals("Aditivo")) && !val1.equals("Booleano") && !val2.equals("Booleano"))
                return "Numero real";
        else if(operador.equals("And") && ((val1.equals("Booleano") && !val2.equals("Booleano"))))
            return  val2;
        else if(operador.equals("And") && ((val2.equals("Booleano") && !val1.equals("Booleano"))))
            return  val1;
        else if(operador.equals("Or") && ((val1.equals("Booleano") && !val2.equals("Booleano"))))
            return  val2;
        else if(operador.equals("Or") && ((val2.equals("Booleano") && !val1.equals("Booleano"))))
            return  val1;
        else if ((operador.equals("And") || operador.equals("Or")) && ((val1.equals("Booleano") && val2.equals("Booleano"))))
            return val1;
        else
            throw new Erro_Compilacao_Exception("Operação inválida entre tipos", Sintatico.Sintatico.getStr()[2]);
        
    }
    
    private String verifica_operacao(String operador, String val) throws Erro_Compilacao_Exception {
        if(operador.equals("Sinal") && !val.equals("Booleano"))
            return val;
        else if (operador.equals("Negacao") && val.equals("Booleano"))
            return val;
        else
            throw new Erro_Compilacao_Exception("Operação inválida entre tipos", Sintatico.Sintatico.getStr()[2]);
        
    }

    private class Escopo {
        private ArrayList<Variavel> lista_de_variaveis;
        private final Escopo pai;

        private ArrayList<Variavel> getLista_de_variaveis() {
            return lista_de_variaveis;
        }

        private Escopo getPai() {
            return pai;
        }

        private Escopo() {
            this.lista_de_variaveis = new ArrayList<>();
            this.pai = null;
        }
        private Escopo(Escopo pai) {
            this.lista_de_variaveis = new ArrayList<>();
            this.pai = pai;
        }
        
        private void Insere_Variável(String identificador, String tipo) throws Erro_Compilacao_Exception {
            if(!lista_de_variaveis.isEmpty()) {
                Variavel v;

                for(int i = 0; i < lista_de_variaveis.size();i++) {
                    v = lista_de_variaveis.get(i);
                    if(v.getIdentificador().equals(identificador)) {
                        throw new Erro_Compilacao_Exception("Declaração repetida da variável ".concat(identificador),Sintatico.Sintatico.getStr()[2]);
                    }

                }
            }
            lista_de_variaveis.add(new Variavel(identificador,tipo));
        }
        
        private void Insere_Variável(String identificador) throws Erro_Compilacao_Exception {
            if(!lista_de_variaveis.isEmpty()) {
                Variavel v;

                for(int i = 0; i < lista_de_variaveis.size();i++) {
                    v = lista_de_variaveis.get(i);
                    if(v.getIdentificador().equals(identificador)) {
                        throw new Erro_Compilacao_Exception("Declaração repetida da variável ".concat(identificador),Sintatico.Sintatico.getStr()[2]);
                    }

                }
            }
            lista_de_variaveis.add(new Variavel(identificador));
        }



        private String[] Procura_elemento(String identificador) throws Erro_Compilacao_Exception {
            
            if(!lista_de_variaveis.isEmpty()) {
                Variavel v;

                for(int i = 0; i < lista_de_variaveis.size();i++) {
                    v = lista_de_variaveis.get(i);
                    if(v.getIdentificador().equals(identificador)) {
                        return new String[]{v.getIdentificador(),v.getTipo()};
                    }

                }
            }
            if(pai == null) {
                throw new Erro_Compilacao_Exception("Variável ".concat(identificador).concat(" não foi declarada."),Sintatico.Sintatico.getStr()[2]);
            }
            return pai.Procura_elemento(identificador);
        }
    }
    
    private class Variavel {
        private String identificador, tipo;

        private Variavel(String identificador) {
            this.identificador = identificador;
            this.tipo = "indefinido";
        }

        private String getIdentificador() {
            return identificador;
        }

        private void setIdentificador(String identificador) {
            this.identificador = identificador;
        }

        private String getTipo() {
            return tipo;
        }

        private void setTipo(String tipo) {
            this.tipo = tipo;
        }

        private Variavel(String identificador, String tipo) {
            this.identificador = identificador;
            this.tipo = tipo;
        }
        
    }
    
    public   boolean Lista_de_variaveis_vazia () {
        if(Lista_de_escopos.isEmpty()) {
            return true;
        }
        else {
            boolean tem_variavel = false;
            for(int i = 0; i < Lista_de_escopos.size();i++) {
                if(!Lista_de_escopos.get(i).lista_de_variaveis.isEmpty()) {
                    tem_variavel = true;
                }
            }
            return tem_variavel;
        }
    }
    
    public  void Insere_Variável(String identificador,String tipo, int i) throws Erro_Compilacao_Exception {
        Lista_de_escopos.get(i).Insere_Variável(identificador, tipo);
    }
    
    public  void Insere_Variável(String identificador, int i) throws Erro_Compilacao_Exception {
        Lista_de_escopos.get(i).Insere_Variável(identificador);
    }
    
    public  void Cria_Escopo() {
        
            Escopo e =  new Escopo();
            Lista_de_escopos.add(e);
    }
    
    public  void Cria_Escopo(Escopo pai) {
            Escopo e =  new Escopo(pai);
            Lista_de_escopos.add(e);
    }
    
    public  String[] Procura_elemento(String identificador, int i) throws Erro_Compilacao_Exception {
        return Lista_de_escopos.get(i).Procura_elemento(identificador);
    }
    
    public boolean ehFuncao(String identificador) throws Erro_Compilacao_Exception {
        String[] s = Procura_elemento(identificador, posicao);
        if(s[1].equals("procedure")) {
            return true;
        }
        throw new Erro_Compilacao_Exception("Chamada de função invalida", Sintatico.Sintatico.getStr()[2]);
        
    }
    
    public void push_operador(String operador) {
        Pct.push(operador);
    }
    
    public void push_tipo(String identificador) throws Erro_Compilacao_Exception {
        String [] str = Procura_elemento(identificador, posicao);
        Pct.push(str[1]);
    }
    
    public void push_valor_bruto(String tipo) throws Erro_Compilacao_Exception {
        Pct.push(tipo);
    }
    
    public String pop() {
        return Pct.pop();
    }
    
    public void incrementa_posicao() {
        posicao++;
    }
    
    public int get_posicao() {
        return posicao;
    }
    
    public void limpa_Pct() {
        Pct.clear();
    }
    
    public boolean verificacao_case(String identificador) throws Erro_Compilacao_Exception {
        String[] s = Procura_elemento(identificador, posicao);
        if(!s[1].equals("Numero Inteiro"))
            throw new Erro_Compilacao_Exception("Variável do case deve ser integer", Sintatico.Sintatico.getStr()[2]);
        return true;
    }
    public void incrementa_posicao_paralelo() {
        posicao_paralelo++;
    }
    
    public int get_posicao_paralelo() {
        return posicao_paralelo;
    }
    
    public void incrementa_posicao_serie() {
        posicao_serie++;
    }
    
    public int get_posicao_serie() {
        return posicao_serie;
    }
    
    public Escopo get_escopo(int i) {
        return Lista_de_escopos.get(i);
    }
}

