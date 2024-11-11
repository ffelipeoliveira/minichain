package classes.io;

import classes.core.App;
import classes.core.Block;
import classes.core.BlockChain;
import classes.core.Transaction;
import classes.other.Log;

public class Submenus extends Controllers{
	protected boolean transactionSubmenu(BlockChain blockChain, Log log) {
        Simple.menuBanner();
        System.out.print("""
                        [Menu > Transactions    ] ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
                        [1] Transfer
                        [2] See tokens
                        [3] Go back
                        """);
        switch (Simple.intInput(1, 3)) {
            case 1:
                Simple.banner();
                log.start();
                boolean sucessful = userAddTransaction(blockChain);
                if (sucessful) log.addToLog("Added Transaction", log.finish());
                if (sucessful) System.out.println("[!] Your Transaction has been added to the transactions layer and Can be added in the next block after being validated");
                Simple.pause();
                return true;
            case 2:
                Simple.banner();
                System.out.println("[!] Type the hash of a public key (Can be viewed in the BlockChain)");
                String pk = Simple.strInput(12);
                log.start();
                System.out.println("[!] Found " + App.searchForTokens(blockChain, pk) + " tokens for this public key.");
                log.addToLog("Searched tokens", log.finish());
                Simple.pause();
                return true;
            default:
                return false;
        }
    }

    protected boolean viewingSubmenu(BlockChain blockChain, Log log) {
        Simple.menuBanner();
        System.out.print("""
                        [Menu > Viewing         ] ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
                        [1] View BlockChain
                        [2] View a specific block
                        [3] View transactions
                        [4] See tokens
                        [5] Go back
                        """);
        switch (Simple.intInput(1, 5)) {
            case 1:
                Simple.banner();
                try {
                    App.validateBlockChain(blockChain);
                    log.start();
                    Print.BlockChain(blockChain);
                    log.addToLog("Printed BlockChain", log.finish());
                    
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    if(Simple.userConfirmation("[?] Continue anyways? ")) {
                        log.start();
                        Print.BlockChain(blockChain);
                        log.addToLog("printed BlockChain", log.finish());
                        
                    }
                    else {
                        Simple.banner();
                        System.out.println("[!] Operation aborted.");
                    }
                }
                Simple.pause();
                return true;
            case 2:
                Simple.banner();
                try {
                    App.validateBlockChain(blockChain);
                    userPrintBlock(blockChain);
                    
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }       
                Simple.pause();
                return true;
            case 3:
                int counter = 0;
                Simple.banner();
                System.out.println("Showing all transactions: ");
                if(blockChain.getTransactionHead() != null) {
                    counter = 1;
                    Transaction aux = blockChain.getTransactionHead();
                    System.out.println("[" + counter + "] " + aux);
                    while (aux.getPrevious() != null) {
                        aux = aux.getPrevious(); 
                        System.out.println("[" + counter + "] " + aux);
                    }
                }
                Simple.pause();
                return true;
            case 4:
                Simple.banner();
                System.out.println("[!] Type the hash of a public key (Can be viewed on the BlockChain)");
                String pk = Simple.strInput(12);
                log.start();
                System.out.println("[!] Found " + App.searchForTokens(blockChain, pk) + " tokens for this public key.");
                log.addToLog("Searched tokens", log.finish());
                Simple.pause();
                return true;
            default:
                return false;
        }
    }

    protected boolean logSubmenu(Log log) {
        Simple.menuBanner();
        System.out.print("""
                        [Menu > Log             ] ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
                        [1] View log
                        [2] Clear log
                        [3] Go back
                        """);
        switch (Simple.intInput(1, 3)) {
            case 1:
                Simple.banner();
                log.printLog();
                Simple.pause();
                return true;
            case 2:
                if(Simple.userConfirmation("[?] Do you really want to clear you log?")) {
                    log = new Log();
                    Simple.banner();
                    System.out.println("[!] Cleared log sucessfully");
                }
                else {
                    Simple.banner();
                    System.out.println("[!] Operation aborted");
                }
                Simple.pause();
                return true;
            default:
                return false;
        }
    }

    protected boolean testingSubmenu(BlockChain blockChain, Log log) {
        Simple.menuBanner();
        System.out.print("""
                        [Menu > Testing         ] ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
                        [1] Reset BlockChain
                        [2] Modify Block
                        [3] Recalculate Block's Hash
                        [4] Go back
                        """);
        switch (Simple.intInput(1, 4)) {
            case 1:
                blockChain = new BlockChain();
                log.start();
                System.out.println("[!] Resetted sucesfully.");
                log.addToLog("Resetted", log.finish());
                Simple.pause();
                return true;
            case 2:
                Simple.banner();
                Block modifiedBlock = userModifyBlock(blockChain);
                if (modifiedBlock != null) {
                    Simple.banner();
                    System.out.println("[!] Block modified sucessfully!");
                    log.addToLog("Modified Block", log.finish());
                }
                Simple.pause();
                return true;
            case 3:
                userReMineHash(blockChain);
                Simple.pause();
                return true;
            default:
                return false;
        }
    }
}
