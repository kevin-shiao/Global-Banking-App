package ui;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            new BankApp();
        } catch (FileNotFoundException e) {
            System.out.println("File not Found: Unable to Run Program");
        }
    }
}
