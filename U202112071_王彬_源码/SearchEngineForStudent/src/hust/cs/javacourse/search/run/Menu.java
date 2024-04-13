package hust.cs.javacourse.search.run;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Menu {
    public static void main(String[] args){
        System.out.println("--------------- Menu --------------");
        System.out.println("| 1. Build index of texts         |");
        System.out.println("| 2. Query word(s) [AND/OR]       |");
        System.out.println("|---------------------------------|");
        Scanner in = new Scanner(System.in);
        if (in.hasNextLine()) {
            if (in.nextInt() == 1) {
                //TestBuildIndex.main(new String[] "" );

            }
            if (in.nextInt()==2) {
                //TestSearchIndex.main();
            }
        }


    }

}
