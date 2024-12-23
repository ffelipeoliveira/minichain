package io;

import core.Blockchain;
import other.Log;

public class Menu extends Submenus {
    // Main menu #########################################
    public boolean menu(String defaultDirectory, String keyDirectory, String blockDirectory,
            Blockchain blockchain, Log log) {
        String menuPath = "Menu";
        Simple.banner(menuPath);
        System.out.print("""
                [1] Transferring
                [2] Mining
                [3] Viewing
                [4] Logs
                [5] Calculator
                [6] Testing
                [7] Keys
                [8] Exit
                """);
        switch (Simple.intInput(1, 8)) {
            case 1:
                while (transactionSubmenu(menuPath, keyDirectory, blockchain, log))
                    ;
                return true;
            case 2:
                while (miningSubmenu(menuPath, keyDirectory, blockDirectory,
                        blockchain, log))
                    ;
                return true;
            case 3:
                while (viewingSubmenu(menuPath, blockchain, log))
                    ;
                return true;
            case 4:
                while (logSubmenu(menuPath, log))
                    ;
                return true;
            case 5:
                while (calculatorSubmenu(menuPath))
                    ;
                return true;
            case 6:
                while (testingSubmenu(menuPath, keyDirectory, blockDirectory, blockchain, log))
                    ;
                return true;
            case 7:
                while (keySubmenu(menuPath, keyDirectory, log))
                    ;
                return true;
            default:
                Simple.banner(menuPath);
                if (Simple.userConfirmation("Do you really want to exit?")) {
                    Simple.clear();
                    System.out.println("[!] Exitting...");
                    return false;
                }
                return true;
        }
    }
}
