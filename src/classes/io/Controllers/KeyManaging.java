package io.Controllers;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import core.Encryption;
import io.Simple;

public class KeyManaging {

    // Case 1
    public static int userViewPublicKeys(String menuPath, String defaultDirectory, boolean viewBanner) {
        if (viewBanner) Simple.banner(menuPath + " > View Key");
        System.out.println("[!] Public keys:");
        int counter = 1;
        for (File file : new File(defaultDirectory).listFiles()) {
            if (!file.isDirectory() && file.getName().length() > 4) {
                String fileName = file.getName();
                int length = fileName.length();
                if (fileName.charAt(length - 1) == 'y' && fileName.charAt(length - 2) == 'e' && fileName.charAt(length - 3) == 'k' && fileName.charAt(length - 4) == '.') {
                    try {
                        PublicKey publicKey = Encryption.readPublicKey(defaultDirectory, fileName);
                        System.out.println("[" + counter + "] " + fileName + ": " + Encryption.calculateKeyHash(publicKey));
                        counter++;
                    } catch (Exception e) {}
                }
            }
        }
        return counter;
    }

    // Case 1
    public static int userViewPrivateKeys(String menuPath, String defaultDirectory, boolean viewBanner) {
        if (viewBanner) Simple.banner(menuPath + " > View Key");
        System.out.println("[!] Private keys:");
        int counter = 1;
        for (File file : new File(defaultDirectory).listFiles()) {
            if (!file.isDirectory() && file.getName().length() > 4) {
                String fileName = file.getName();
                int length = fileName.length();
                if (fileName.charAt(length - 1) == 'y' && fileName.charAt(length - 2) == 'e' && fileName.charAt(length - 3) == 'k' && fileName.charAt(length - 4) == '.') {
                    try {
                        Encryption.readPrivateKey(defaultDirectory, fileName);
                        System.out.println("[" + counter + "] " + fileName);
                        counter++;
                    } catch (Exception e) {}
                }
            }
        }
        return counter;
    }

    // Case 2
	public static void userCreateKeyPair(String menuPath, String defaultFolder) {
        menuPath += " > Create Key Pair";
        Simple.banner(menuPath);
        try {
            if (Simple.userConfirmation("This can override existing Key Pairs, proceed?")) {
                KeyPair keyPair = Encryption.generateKeyPair();
                userStoreKeyPair(defaultFolder, "[!] Type the file name of your new key pair (this can override existing Key Pairs, be careful)", keyPair);
                System.out.println("[!] Your private and public keys has beeen stored under " + defaultFolder);
            }
            else {
                Simple.banner(menuPath + " (aborted)");
                System.out.println("[!] Operation aborted");
            }
        } catch (IOException e) {
            System.out.println("[X] Invalid path or file not found");
        } catch (Exception e) {
            Simple.banner(menuPath + " (error)");
            System.out.println("[X] " + e.getMessage());
        }
    }

    // Case 3
	public static void userDeletePublicKey(String menuPath, String defaultDirectory) {
        menuPath += " > Delete Public Key";
        File file = userSelectPublicKeyFile(menuPath, defaultDirectory, true);
        if (file.delete()) { 
            Simple.banner(menuPath);
            System.out.println("[!] Deleted the file " + file.getName());
            Simple.pause();
            return;
        }
    }

    // Case 4
    public static void userDeletePrivateKey(String menuPath, String defaultDirectory) {
        menuPath += " > Delete Private Key";
        Simple.banner(menuPath);
        Simple.userConfirmation("This action CANNOT BE REVERSED and will make you lose access to any tokens you might have");
        File file = userSelectPrivateKeyFile(menuPath, defaultDirectory, true);
        if (file.delete()) { 
            Simple.banner(menuPath);
            System.out.println("[!] Deleted the file " + file.getName());
            Simple.pause();
            return;
        }
    }

    // Can read a public key file
    public static File userSelectPublicKeyFile(String menuPath, String defaultDirectory, boolean viewBanner) {
        menuPath += " > Select Key";
        if (viewBanner) Simple.banner(menuPath);
        int maxIndex = userViewPublicKeys(menuPath, defaultDirectory, false);
        System.out.println("[!] Type the index of a public key (0 to cancel)");
        int index = Simple.intInput(0, maxIndex);
        if (index == 0) {
            Simple.banner(menuPath + " (aborted)");
            System.out.println("[X] Selection aborted.");
            Simple.pause();
            return null;
        }
        int counter = 1;
        for (File file : new File(defaultDirectory).listFiles()) {
            if (!file.isDirectory() && file.getName().length() > 4) {
                String fileName = file.getName();
                int length = fileName.length();
                if (fileName.charAt(length - 1) == 'y' && fileName.charAt(length - 2) == 'e' && fileName.charAt(length - 3) == 'k' && fileName.charAt(length - 4) == '.') {
                    try {
                        Encryption.readPublicKey(defaultDirectory, fileName);
                        if (counter == index) {
                            return file;
                        }
                        counter++;
                    } catch (Exception e) {}                    
                }
            }
        }
        return null;
    }

	// Can read a public key
    public static PublicKey userSelectPublicKey(String menuPath, String defaultDirectory, boolean viewBanner, String message) {
        menuPath += " > Select Key";
        if (viewBanner) Simple.banner(menuPath);
        int maxIndex = userViewPublicKeys(menuPath, defaultDirectory, false);
        System.out.println("[!] " + message + " (0 to cancel)");
        int index = Simple.intInput(0, maxIndex);
        if (index == 0) {
            Simple.banner(menuPath + " (aborted)");
            System.out.println("[X] Selection aborted.");
            Simple.pause();
            return null;
        }
        int counter = 1;
        for (File file : new File(defaultDirectory).listFiles()) {
            if (!file.isDirectory() && file.getName().length() > 4) {
                String fileName = file.getName();
                int length = fileName.length();
                if (fileName.charAt(length - 1) == 'y' && fileName.charAt(length - 2) == 'e' && fileName.charAt(length - 3) == 'k' && fileName.charAt(length - 4) == '.') {
                    try {
                        PublicKey publicKey = Encryption.readPublicKey(defaultDirectory, fileName);
                        Encryption.calculateKeyHash(publicKey);
                        if (counter == index) {
                            return publicKey;
                        }
                        counter++;
                    } catch (Exception e) {}                    
                }
            }
        }
        return null;
    }

    // Can read a private key
    public static File userSelectPrivateKeyFile(String menuPath, String defaultDirectory, boolean viewBanner) {
        if (viewBanner) Simple.banner(menuPath + " > Select Key");
        int maxIndex = userViewPrivateKeys(menuPath, defaultDirectory, false);
        System.out.println("[!] Type the index of a private key (0 to cancel)");
        int index = Simple.intInput(0, maxIndex);
        if (index == 0) {
            Simple.banner(menuPath + " (aborted)");
            System.out.println("[X] Selection aborted.");
            Simple.pause();
            return null;
        }
        int counter = 1;
        for (File file : new File(defaultDirectory).listFiles()) {
            if (!file.isDirectory() && file.getName().length() > 4) {
                String fileName = file.getName();
                int length = fileName.length();
                if (fileName.charAt(length - 1) == 'y' && fileName.charAt(length - 2) == 'e' && fileName.charAt(length - 3) == 'k' && fileName.charAt(length - 4) == '.') {
                    try {
                        Encryption.readPrivateKey(defaultDirectory, fileName);
                        if (counter == index) {
                            return file;
                        }
                        counter++;
                    } catch (Exception e) {}                    
                }
            }
        }
        return null;
    }

    // Can read a private key
    public static PrivateKey userSelectPrivateKey(String menuPath, String defaultDirectory, boolean viewBanner, String message) {
        if (viewBanner) Simple.banner(menuPath + " > Select Key");
        int maxIndex = userViewPrivateKeys(menuPath, defaultDirectory, false);
        System.out.println("[!] " + message + " (0 to cancel)");
        int index = Simple.intInput(0, maxIndex);
        if (index == 0) {
            Simple.banner(menuPath + " (aborted)");
            System.out.println("[X] Selection aborted.");
            Simple.pause();
            return null;
        }
        int counter = 1;
        for (File file : new File(defaultDirectory).listFiles()) {
            if (!file.isDirectory() && file.getName().length() > 4) {
                String fileName = file.getName();
                int length = fileName.length();
                if (fileName.charAt(length - 1) == 'y' && fileName.charAt(length - 2) == 'e' && fileName.charAt(length - 3) == 'k' && fileName.charAt(length - 4) == '.') {
                    try {
                        PrivateKey privateKey = Encryption.readPrivateKey(defaultDirectory, fileName);
                        if (counter == index) {
                            return privateKey;
                        }
                        counter++;
                    } catch (Exception e) {}                    
                }
            }
        }
        return null;
    }

    // Can store your public key
    protected static void userStoreKeyPair(String defaultFolder, String message, KeyPair keyPair) throws IOException, Exception {
        System.out.println(message + " (r to replace your own key | c to cancel)");
        String fileName = Simple.strInput(100);
        if (fileName.equals("c")) throw new Exception("Operation aborted.");
        if (fileName.equals("r") && Simple.userConfirmation("THIS WILL REPLACE YOUR PUBLIC AND PRIVATE KEYS, PROCEED ANYWAYS?")) {
            Encryption.storeKeys(defaultFolder, keyPair);
            System.out.println("[!] Key pair stored sucessfully!");
        } 
        else{
            Encryption.storeKeys(defaultFolder, keyPair, fileName);
            System.out.println("[!] Key pair sucessfully!");
        }
    }
}
