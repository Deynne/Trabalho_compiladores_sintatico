package javaapplication12;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Junior Ribeiro
 */
public class Semantico {

    private Array<String> LeArquivo() throws FileNotFoundException {
        Scanner codigo = new Scanner(new File("C:\\Users\\Junior Ribeiro\\Google Drive\\Universidade\\UFPB\\2019.1\\Compiladores\tabela_lexica.txt"));

        String linha;
        Array<String> Codigo = new ArrayList<>();
        
        while (codigo.hasNextLine()) {
            linha = codigo.nextLine();

            Codigo = linha.split("#");
        }

        return null;

    }

}
