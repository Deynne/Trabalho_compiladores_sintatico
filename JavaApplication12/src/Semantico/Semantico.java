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

}
