package classes.io;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;

import classes.core.App;
import classes.core.BlockChain;
import classes.core.Encryption;
import classes.other.Log;

public class Menu extends Submenus{
    // Main menu #########################################
    public boolean menu(BlockChain blockChain, Log log) {
        Simple.menuBanner();
        System.out.print("""
                        [Menu                   ] ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
                        [1] Transferring
                        [2] Mining
                        [3] Viewing
                        [4] Logs
                        [5] Testing
                        [6] Keys
                        [7] Exit
                        """);
        switch (Simple.intInput(1, 7)) {
            case 1:
                while(transactionSubmenu(blockChain, log));
                return true;
            case 2:
                Simple.banner();
                try {
                    App.validateBlockChain(blockChain);
                    log.start();
                    boolean sucessful = userMineBlock(blockChain);
                    if (sucessful) log.addToLog("Mined block", log.finish());
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                Simple.pause();
                return true;
            case 3:
                while(viewingSubmenu(blockChain, log));
                return true; 
            case 4:
                while(logSubmenu(log));
                return true; 
            case 5:
                if (Simple.userConfirmation("[?] Any actions on this menu can invalidate your BlockChain, proceed anyways?")) {
                    while(testingSubmenu(blockChain, log));
                    return true;
                } else {
                    Simple.banner();
                    System.out.println("[!] Operation aborted");
                }
                return true;
            case 6:
                Simple.banner();
                try {
                    if (Simple.userConfirmation("[?] This can override existing Key Pairs, proceed?")) {
                        KeyPair keyPair = Encryption.generateKeyPair();
                        userStoreKeyPair("[!] Type the file name of your new key pair (this can override existing Key Pairs, be careful)", keyPair);
                        System.out.println("[!] Your private and public keys has beeen stored under ./keys/");
                    }
                    else {
                        Simple.banner();
                        System.out.println("[!] Operation aborted");
                    }
                } catch (IOException e) {
                    System.out.println("[X] Invalid path or file not found");
                } catch (Exception e) {
                    Simple.banner();
                    System.out.println("[X] " + e.getMessage());
                }
                Simple.pause();
                return true;
            default:
                if (Simple.userConfirmation("[?] Do you really want to exit?")) {
                    Simple.clear();
                    for (File file : new java.io.File("./keys/").listFiles())
                        if (!file.isDirectory()) file.delete();
                    System.out.println("[!] Exitting...");
                    return false;
                }
                return true;
        }
    }
}
