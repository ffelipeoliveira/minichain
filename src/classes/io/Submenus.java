package io;

import java.sql.Blob;

import core.App;
import core.Block;
import core.Blockchain;

import other.Log;
import other.Calculator;

import io.Controllers.*;

public class Submenus {
    double calculatorResult = 0; // Displayed when using calculator

    // Case 1:
    protected boolean transactionSubmenu(String menuPath, String defaultDirectory, Blockchain blockchain, Log log) {
        menuPath += " > Transactions";
        Simple.banner(menuPath);
        System.out.print("""
                [1] Transfer
                [2] See tokens
                [3] Go back
                """);
        switch (Simple.intInput(1, 3)) {
            case 1:
                Simple.banner(menuPath + " > Transfer");
                log.start();
                boolean sucessful = Transferring.userAddTransaction(menuPath, defaultDirectory, blockchain);
                if (sucessful)
                    log.addToLog("Added Transaction", log.finish());
                if (sucessful)
                    System.out.println(
                            "[!] Your Transaction has been added to the transactions layer and Can be added in the next block after being validated");
                Simple.pause();
                return true;
            case 2:
                Simple.banner("Menu > Transactions > See tokens");
                System.out.println("[!] Paste the hash of a public key (Can be viewed in the Blockchain)");
                String pk = Simple.strInput(30);
                log.start();
                System.out
                        .println("[!] Found " + App.searchForTokens("Menu > Transactions > See tokens", blockchain, pk)
                                + " tokens for this public key.");
                log.addToLog("Searched tokens", log.finish());
                Simple.pause();
                return true;
            default:
                return false;
        }
    }

    // Case 2:
    protected boolean miningSubmenu(String menuPath, String defaultKeyDirectory, String defaultBlockDirectory,
            Blockchain blockchain, Log log) {
        menuPath += " > Mining";
        Simple.banner(menuPath);
        System.out.print("""
                [1] Mine a Block
                [2] Go back
                """); // More options soon
        System.out.println("[!] Current difficulty: " + blockchain.difficulty());
        System.out.println("[!] Current reward:" + blockchain.reward());
        switch (Simple.intInput(1, 2)) {
            case 1:
                Mining.userMine(menuPath, defaultKeyDirectory, defaultBlockDirectory, blockchain, log);
                return true;
            default:
                return false;
        }
    }

    // Case 3:
    protected boolean viewingSubmenu(String menuPath, Blockchain blockchain, Log log) {
        menuPath += " > Viewing";
        Simple.banner(menuPath);
        System.out.print("""
                [1] View Blockchain
                [2] View a specific block
                [3] View transactions
                [4] See tokens
                [5] Go back
                """);
        switch (Simple.intInput(1, 5)) {
            case 1:
                Simple.banner(menuPath + " > View Blockchain");
                try {
                    App.validateBlockchain(blockchain);
                    log.start();
                    Print.PrintBlockchain(blockchain);
                    log.addToLog("Printed Blockchain", log.finish());

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    if (Simple.userConfirmation("Continue anyways? ")) {
                        log.start();
                        Print.PrintBlockchain(blockchain);
                        log.addToLog("printed Blockchain", log.finish());

                    } else {
                        Simple.banner(menuPath + " (aborted)");
                        System.out.println("[!] Operation aborted.");
                    }
                }
                Simple.pause();
                return true;
            case 2:
                Simple.banner(menuPath);
                try {
                    App.validateBlockchain(blockchain);
                    Viewing.userPrintBlock(menuPath, blockchain);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                Simple.pause();
                return true;
            case 3:
                Viewing.userPrintTransactions(menuPath, blockchain);
                Simple.pause();
                return true;
            case 4:
                Simple.banner("Menu > Viewing > View Tokens");
                System.out.println("[!] Paste the hash of a public key (Can be viewed on the Blockchain)");
                String pk = Simple.strInput(30);
                log.start();
                System.out.println("[!] Found " + App.searchForTokens("Menu > Viewing > View Tokens", blockchain, pk)
                        + " tokens for this public key.");
                log.addToLog("Searched tokens", log.finish());
                Simple.pause();
                return true;
            default:
                return false;
        }
    }

    // Case 4:
    protected boolean logSubmenu(String menuPath, Log log) {
        menuPath += " > Log";
        Simple.banner(menuPath);
        System.out.print("""
                [1] View log
                [2] Clear log
                [3] Go back
                """);
        switch (Simple.intInput(1, 3)) {
            case 1:
                Simple.banner(menuPath + " > View Log");
                log.printLog();
                Simple.pause();
                return true;
            case 2:
                if (Simple.userConfirmation("Do you really want to clear you log?")) {
                    log = new Log();
                    Simple.banner(menuPath + " > Clear Log");
                    System.out.println("[!] Cleared log sucessfully");
                } else {
                    Simple.banner(menuPath + " > Clear Log (aborted)");
                    System.out.println("[!] Operation aborted");
                }
                Simple.pause();
                return true;
            default:
                return false;
        }
    }

    // Case 5
    protected boolean calculatorSubmenu(String menuPath) {
        menuPath += "> Calculator";
        Calculator calculator = new Calculator();
        Simple.banner(menuPath + " > Result: " + calculatorResult);
        System.out.print("""
                [1] Calculate
                [2] Reset
                [3] Go back
                """);
        switch (Simple.intInput(1, 3)) {
            case 1:
                Simple.banner(menuPath);
                calculatorResult = calculator.calcUsingPreviousResult("Menu", calculatorResult);
                Simple.pause();
                return true;
            case 2:
                calculatorResult = 0;
                Simple.banner(menuPath);
                System.out.println("[!] Resetted");
                return true;
            default:
                return false;
        }
    }

    // Case 6
    protected boolean testingSubmenu(String menuPath, String defaultKeyDirectory, String defaultBlockDirectory, Blockchain blockchain, Log log) {
        menuPath += " > Testing";
        Simple.banner(menuPath);
        System.out.print("""
                [1] Reset Blockchain
                [2] Modify Block
                [3] Recalculate Block's Hash
                [4] Go back
                """);
        switch (Simple.intInput(1, 4)) {
            case 1:
                Testing.deleteBlockchainFiles(defaultBlockDirectory);
                blockchain = new Blockchain(defaultBlockDirectory);
                Simple.banner(menuPath);
                log.start();
                System.out.println("[!] Resetted sucesfully. Restart to take effect.");
                log.addToLog("Resetted", log.finish());
                Simple.pause();
                return true;
            case 2:
                Simple.banner(menuPath + " > Modify Block");
                Block modifiedBlock = Testing.userModifyBlock(menuPath, defaultKeyDirectory, blockchain);
                if (modifiedBlock != null) {
                    Simple.banner(menuPath + " > Modify Block");
                    System.out.println("[!] Block modified sucessfully!");
                    log.addToLog("Modified Block", log.finish());
                }
                Simple.pause();
                return true;
            case 3:
                Testing.userReMineHash("Menu > Testing", blockchain);
                Simple.pause();
                return true;
            default:
                return false;
        }
    }

    // Case 7
    protected boolean keySubmenu(String menuPath, String defaultDirectory, Log log) {
        menuPath += " > Keys";
        Simple.banner(menuPath);
        System.out.print("""
                [1] View  All Keys
                [2] Create Key Pair
                [3] Delete a Public Key
                [4] Delete a Private key
                [5] Go back
                """);
        switch (Simple.intInput(1, 5)) {
            case 1:
                KeyManaging.userViewPublicKeys(menuPath, defaultDirectory, true);
                System.out.println();
                KeyManaging.userViewPrivateKeys(menuPath, defaultDirectory, false);
                Simple.pause();
                return true;
            case 2:
                KeyManaging.userCreateKeyPair(menuPath, defaultDirectory);
                Simple.pause();
                return true;
            case 3:
                KeyManaging.userDeletePublicKey(menuPath, defaultDirectory);
                return true;
            case 4:
                KeyManaging.userDeletePrivateKey(menuPath, defaultDirectory);
                return true;
            default:
                return false;
        }
    }
}
