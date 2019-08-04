package Main;

import Excessao.Erro_Compilacao_Exception;
import Sintatico.Sintatico;
import java.io.FileNotFoundException;
import java.util.Arrays;
/**
 *
 * @author Junior Ribeiro
 */

public class Main {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        try {
            Sintatico.LeArquivo(args[0]);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        boolean b = true;
        try {
            b = Sintatico.Programa();
        } catch (Erro_Compilacao_Exception ex) {
            ex.printStackTrace();
            //System.out.println(ex.getMessage());
        }
        System.out.println("Programa Correto.");
    }
}