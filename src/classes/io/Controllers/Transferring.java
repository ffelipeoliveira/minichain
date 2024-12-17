package io.Controllers;

import java.security.PrivateKey;
import java.security.PublicKey;

import core.Blockchain;
import core.Encryption;
import core.Transaction;

import io.Simple;

public class Transferring {

    // Case 1:
    // Can create a new transaction
    public static Transaction userCreateTransaction(String menuPath, String defaultDirectory, Blockchain blockChain) throws Exception {
        menuPath += " > Creating Transaction";
        PublicKey senderPublicKey = null;
        PublicKey receiverPublicKey = null;
        PrivateKey senderPrivateKey = null;

        Simple.banner(menuPath + " (1/5)");
        senderPublicKey = KeyManaging.userSelectPublicKey(menuPath, defaultDirectory, false, "Select the sender's public key");
        if (senderPublicKey == null) {
            throw new Exception("Operation aborted");
        }

        Simple.banner(menuPath + " [2/5]");
        receiverPublicKey = KeyManaging.userSelectPublicKey(menuPath, defaultDirectory, false, "Select the receiver's public key");
        if (receiverPublicKey == null) {
            throw new Exception("Operation aborted");
        }

        Simple.banner(menuPath+ " [3/5]");
        System.out.println("[!] Type the amount of tokens to transfer: (0 to cancel)");
        long amount = Simple.intInput();
        if(amount == 0) {
            throw new Exception("Operation aborted");
        }

        
        Simple.banner(menuPath+ " [4/5]");
        System.out.println("[$] " + amount  + " from you to " + receiverPublicKey.hashCode());
        if(!Simple.userConfirmation("ARE YOU SURE THIS IS CORRECT? (THIS ACTION CANNOT BE REVERSED)")) {
            throw new Exception("Operation aborted");
        }

        Simple.banner(menuPath+ " [5/5]");
        senderPrivateKey = KeyManaging.userSelectPrivateKey(menuPath, defaultDirectory, false, "Select the sender's private key");
        if (senderPrivateKey == null) {
            throw new Exception("Operation aborted");
        }

        String data = ("(" + (amount - amount/100) + ") " + senderPublicKey.hashCode() + "=>" + receiverPublicKey.hashCode());
        String signature = Encryption.signTransaction(data, senderPrivateKey);
        return new Transaction(blockChain.getTransactionHead() ,senderPublicKey, receiverPublicKey, amount, signature, false);
    }

    // Can manage the BlockChain's addTransaction()
    public static boolean userAddTransaction(String menuPath, String defaultDirectory, Blockchain blockChain) { 
        try {
            blockChain.addTransaction(userCreateTransaction(menuPath, defaultDirectory, blockChain));
            return true;
        } catch (Exception e) {
            Simple.banner(menuPath);
            System.out.println("[X] " + e.getMessage());
            return false;
        }
    }
}
