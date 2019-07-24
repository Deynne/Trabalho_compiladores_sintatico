package Excessao;

/**
 *
 * @author Junior Ribeiro
 */
public class Erro_Sintatico_Exception extends Exception {
    private static final long serialVersionUID = 454642654;

    public Erro_Sintatico_Exception(String mensagem, String linha) {
        super("Erro: ".concat(mensagem.concat(" na linha ".concat(linha))));
    }
    public Erro_Sintatico_Exception(String mensagem) {
        super("Erro: ".concat(mensagem));
    }

}
