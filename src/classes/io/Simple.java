package classes.io;

import java.util.Scanner;

import classes.core.App;

public class Simple {
	public static void clear() { //Will execute different ways of cleaning terminal for better visibility
		try {	
			new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		}catch(Exception E) {
			System.out.println("\033[H\033[2J");
		}
	    
    }

    public static void banner() {
        clear();
        System.out.println("""
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█▀▄▀█░█░█▄░█░█░█▀▀░█░█░▄▀█░█░█▄░█░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█░▀░█░█░█░▀█░█░█▄▄░█▀█░█▀█░█░█░▀█░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
                """);
    }

    
    public static void menuBanner() {
        clear();
        System.out.println("""
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█▀▄▀█░█░█▄░█░█░█▀▀░█░█░▄▀█░█░█▄░█░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█░▀░█░█░█░▀█░█░█▄▄░█▀█░█▀█░█░█░▀█░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░""");
    }

    public static String strInput(int maxChar) { // Will take a maximum of chars and return user input
        Scanner sc = new Scanner(System.in);
        boolean loop = true;
        String str = "";
        while(loop) {
            System.out.print("=> ");
            str = sc.nextLine();
            System.out.println();
            if(str.length() > maxChar) System.err.println("[!] Need to have less than " + maxChar + " characters.");
            else return str;
        }
        return str;
    }

    public static int intInput(int min, int max) { // Will take a min and max int value and return user input
        Scanner sc = new Scanner(System.in);
        do {
            try {
                System.out.print("=> ");
                int input = sc.nextInt();
                System.out.println();
                if ((input < min) || (input > max)) System.err.println("[!] Need to be between " + min + " and " + max + ".");
                else return input;
            } catch (Exception e) {
                System.out.println("[!] Need to be a number");
            }
            sc.nextLine();
        } while (true);
    }

    public static int intInput() { // Will take a min and max int value and return user input
        Scanner sc = new Scanner(System.in);
        do {
            try {
                return sc.nextInt();
            } catch (Exception e) {
                System.out.println("[!] Need to be a number");
            }
            sc.nextLine();
        } while (true);
    }

    public static boolean userConfirmation(String message) { //Will ask for confirmation from user
        System.out.println(message +" (Y/N)");
        String input = strInput(10);
        try {
            return (input.toLowerCase().charAt(0) == 'y');
        } catch (IndexOutOfBoundsException e) {
            return true;
        } catch (Exception exception) {
            System.out.println("[X]: " + exception.getMessage());
            return false;
        } 
    }
    

    public static void pause() { // Will ask user to press ENTER
        Scanner sc = new Scanner(System.in);
        System.out.println("\n" + App.getRandomFact());
        System.out.println("[-] Press ENTER to continue...");
        sc.nextLine();
        clear();
    }
}
