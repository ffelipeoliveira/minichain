package io.Controllers;

import core.Transaction;
import core.App;
import core.Blockchain;
import core.Block;

import io.Simple;
import io.Print;

public class Viewing {

    // Case 1 is a direct reference to the Print.printBlockchain() method

    // Case 2
    // Can manage the App's searchBlock()
    public static void userPrintBlock(String menuPath, Blockchain blockChain) {
        Print.PrintBlockchain(blockChain);
        System.out.println("[1/1] Select a block by it's number to see it's full content: ");
        try {
            Block foundBlock = App.searchBlock(Simple.intInput(), blockChain);
            Simple.banner(menuPath + "> Print Block");
            Print.PrintFullBlock(foundBlock);
        } catch (Exception e) {
            Simple.banner(menuPath + "> Print Block");
            System.out.println("[X] " + e.getMessage());
        }
    }

    // Case 3
    // Can print all transaction in a list format
    public static void userPrintTransactions(String menuPath, Blockchain blockchain) {
        Simple.banner(menuPath + " > View all transactions");
        int counter = 0;
        System.out.println("Showing all transactions: ");
        if (blockchain.getTransactionHead() != null) {
            counter = 1;
            Transaction aux = blockchain.getTransactionHead();
            System.out.println("[" + counter + "] " + aux);
            while (aux.getPrevious() != null) {
                aux = aux.getPrevious();
                System.out.println("[" + counter + "] " + aux);
            }
        }
    }

    // Case 4 is a direct reference to App.serchForTokens() method
}
