package io.Controllers;

import java.io.IOException;
import java.security.PublicKey;

import core.App;
import core.Blockchain;
import core.Block;

import io.Simple;

import other.Log;

public class Mining {

    // Case 1
    // Controller for userMineBlock
    public static void userMine(String menuPath, String defaultKeyDirectory, String defaultBlockDirectory,
            Blockchain blockchain, Log log) {
        menuPath = menuPath + " > Mine new Block";
        Simple.banner(menuPath);
        try {
            App.validateBlockchain(blockchain);
            log.start();
            boolean sucessful = userMineBlock(menuPath, defaultKeyDirectory, defaultBlockDirectory, blockchain);
            if (sucessful)
                log.addToLog("Mined block", log.finish());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        Simple.pause();
    }

    // Controller for mining a block
    public static boolean userMineBlock(String menuPath, String defaultKeyDirectory, String defaultBlockDirectory,
            Blockchain blockChain) {
        menuPath = menuPath + " > Mining Block";
        PublicKey minerPublicKey = null;

        try {
            Simple.banner(menuPath);
            minerPublicKey = KeyManaging.userSelectPublicKey(menuPath, defaultKeyDirectory, true,
                    "Select your Public Key");
            if (minerPublicKey == null) {
                return false;
            }

            Simple.banner(menuPath);
            if (!Simple
                    .userConfirmation("ARE YOU SURE YOU WANT TO PROCEED? (THIS ACTION MIGHT TAKE SEVERAL MINUTES)")) {
                return false;
            }

            Block block = App.mineBlock(menuPath, defaultKeyDirectory, blockChain, minerPublicKey);

            Simple.banner(menuPath);
            System.out.println("[!] Block sucessfully mined! Nonce was " + block.getNonce()
                    + ", your reward will be included in the next Block.");

            App.saveBlock(defaultBlockDirectory, block);
            System.out.println("[!] Block has been saved in " + defaultBlockDirectory);

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
