package io.Controllers;

import java.io.IOException;
import java.security.PublicKey;

import core.App;
import core.Blockchain;

import io.Simple;

import other.Log;

public class Mining {

    // Case 1
    // Controller for mining a Block
    public static void userMine(String menuPath, String defaultDirectory, Blockchain blockchain, Log log) {
        menuPath = menuPath + " > Mine new Block";
        Simple.banner(menuPath);
        try {
            App.validateBlockchain(blockchain);
            log.start();
            boolean sucessful = userMineBlock("Menu > Mining", defaultDirectory, blockchain);
            if (sucessful) log.addToLog("Mined block", log.finish());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        Simple.pause();
    }

    public static boolean userMineBlock(String menuPath, String defaultDirectory, Blockchain blockChain) {
        menuPath = menuPath + " > Mining Block";
        PublicKey minerPublicKey = null;

        try {
            Simple.banner(menuPath);
            minerPublicKey = KeyManaging.userSelectPublicKey(menuPath, defaultDirectory, true, "Select your Public Key");
            if (minerPublicKey == null) {
                return false;
            }
            if(!Simple.userConfirmation("ARE YOU SURE YOU WANT TO PROCEED? (THIS ACTION MIGHT TAKE SEVERAL MINUTES)")) {
                return false;
            }
            double nonce = App.mineBlock(menuPath, blockChain, minerPublicKey);
            Simple.banner(menuPath);
            System.out.println("[!] Block sucessfully mined! Nonce was " + nonce + ", your reward will be included in the next Block.");
            return true;
        } catch (IOException e) {
            System.out.println("[X] Invalid path or file not found");
            return false;
        } catch (Exception e) {
            Simple.banner(menuPath + "(error)");
            System.out.println("[X] " + e.getMessage());
            return false;
        }
    }
}
