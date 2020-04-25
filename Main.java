package recognition;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final Scanner sc = new Scanner(System.in);

        menu(sc);
//        menu(new Scanner("1\n"));
//        menu(new Scanner("2\n_X_\n_X_\n_X_\n_X_\n_X_\n"));
//        menu(new Scanner("2\nXXX\n__X\nXXX\nX__\nXXX\n"));
//        menu(new Scanner("2\nXXX\n__X\nXXX\n__X\nXXX\n"));
//        menu(new Scanner("2\nX_X\nX_X\nXXX\n__X\n__X\n"));
//        menu(new Scanner("2\nXXX\nX__\nXXX\n__X\nXXX\n"));
//        menu(new Scanner("2\nXXX\nX__\nXXX\nX_X\nXXX\n"));
//        menu(new Scanner("2\nXXX\n__X\n__X\n__X\n__X\n"));
//        menu(new Scanner("2\nXXX\nX_X\nXXX\nX_X\nXXX\n"));
//        menu(new Scanner("2\nXXX\nX_X\nXXX\n__X\nXXX\n"));
//        menu(new Scanner("2\nXXX\nX_X\nX_X\nX_X\nXXX\n"));
//
//        menu(new Scanner("2\nXX_\n__X\n_XX\nX__\nXXX\n"));
//        menu(new Scanner("2\nXX_\n__X\n_X_\n__X\nXXX\n"));
    }

    public static void menu(Scanner sc) throws IOException, ClassNotFoundException{
        System.out.println("1. Learn the network");
        System.out.println("2. Guess a number");
        System.out.print("Your choice: ");
        String choice = sc.nextLine();
        Network nn = new Network();

        switch (choice) {
            case "1":
                nn.train();
                break;

            case "2":
                nn.guess(sc);
                break;
        }
    }
}

