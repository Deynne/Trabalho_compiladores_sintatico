package Sintatico;

import Excessao.Erro_Sintatico_Exception;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Junior Ribeiro
 */

public class Main {
    public static void main(String[] args) {
        System.out.println(args);
        try {
            Sintatico.LeArquivo(args[0]);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        boolean b = true;
        try {
            b = Sintatico.Programa();
        } catch (Erro_Sintatico_Exception ex) {
            ex.printStackTrace();
            //System.out.println(ex.getMessage());
        }
        
        if(b) {
                System.out.println("Ok");
            }
            else {
                System.out.println("não ok");
            }
        System.out.println("Cheogu aqui?");
    }
}