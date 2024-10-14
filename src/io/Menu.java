package io;

import core.BlockChain;
import core.Block;
import other.Log;

public class Menu {
    private static void userAddBlock(BlockChain blockChain) { // Will manage the BlockChain's userAddBlock function
    int id = 0;
    boolean manualID = false;
    if (!(Simple.userConfirmation("[1/2] Want to automatically assign a number id to this block?"))) {
        Simple.banner();;
        System.out.println("[1/2] Type the number id");
        id = Simple.intInput();
        manualID = true;
    }
    System.out.println("[2/2] How much data (strings) will you store in this Block?");
    int dataAmount = Simple.intInput(1, 5);
    String[] data = new String[dataAmount];
    for (int i = 0; i < dataAmount; i++) {
        Simple.banner();;
        System.out.println("[" + (i + 1) + "/" + dataAmount + "] Type your data: ");
        data[i] = Simple.strInput(70);
    }
    if (manualID) blockChain.addBlock(id,data);
    else blockChain.addBlock(data);
}

private static void userPrintBlock(BlockChain blockChain) { // Will manage the BlockChain's searchBlock function
    Print.BlockChain(blockChain);
    System.out.println("[1/1] Select a block by it's number to see it's full content: ");
    Block foundBlock = blockChain.searchBlock(Simple.intInput());
    if (foundBlock == null) {
        Simple.banner();;
        System.out.println("[!] Block not Found");
    }
    else {
        Simple.banner();;
        Print.FullBlock(foundBlock);
    }
}
private static Block userRecalculateHash(BlockChain blockChain) {
    Print.BlockChain(blockChain);
    System.out.println("[1/1] Select a block by it's number to recalculate it's hashes");
    Block foundBlock = blockChain.searchBlock(Simple.intInput());
    if (foundBlock == null) {
        Simple.banner();;
        System.out.println("[!] Block not Found");
        return null;
    }
    else {
        Simple.banner();
        foundBlock.recalculateHash();
        return foundBlock;
    }
}

private static Block userModifyBlock(BlockChain blockChain) {
    Print.BlockChain(blockChain);
    System.out.println("[1/1] Select a block by it's number to modify (this operation might invalidate your BlockChain)");
    Block foundBlock = blockChain.searchBlock(Simple.intInput());
    if (foundBlock == null) {
        Simple.banner();;
        System.out.println("[!] Block not Found");
        return null;
    }
    else {
        Simple.banner();;
        System.out.println("[2/2] How much data (strings) will you store in this Block?");
        int dataAmount = Simple.intInput(1, 5);
        String[] data = new String[dataAmount];
        for (int i = 0; i < dataAmount; i++) {
            Simple.banner();;
            System.out.println("[" + (i + 1) + "/" + dataAmount + "] Type your data: ");
            data[i] = Simple.strInput(70);
        }
        foundBlock.setData(data);
        return foundBlock;
    }
}

public static boolean menu(BlockChain blockChain, Log log) { // Basic menu for operations using switch statement
    Simple.banner();;
    System.out.print("""
                    [!] Select an Option: 
                    [1] Reset your BlockChain
                    [2] Mine a new Block
                    [3] View your BlockChain
                    [4] View an especific Block
                    [5] View log/operation history
                    [6] Clear log/operation history
                    [7] Modify a block
                    [8] Recalculate a block's hash
                    [9] Exit
                    """);
    switch (Simple.intInput(1, 9)) {
        case 1:
            Simple.banner();;
            if(Simple.userConfirmation("[?] Do you really want to reset?")) {
                blockChain.reset();
                log.addToLog("Reseted BlockChain", 0, blockChain.getHead().getHash());
                Simple.banner();;
                System.out.println("[!] BlockChain reseted sucessfully!");
            }
            else {
                Simple.banner();;
                System.out.println("[!] Resetting aborted.");
            }
            Simple.pause();
            return true;
    
        case 2:
            Simple.banner();;
            try {
                blockChain.validateBlockChain();
                userAddBlock(blockChain);
                Simple.banner();;
                System.out.println("[!] Block addded sucessfully!");
                System.out.println("[-] Fun fact: It takes an average of 2.4 million years for an common computer to mine a real BitCoin block, due than an amount of 0's that the hash is required to have in order to be valid. The only way to find the the valid hash is to randomly modify the nonce value and recalculate the hash until it becomes valid. The amount of 0's is called difficulty. ");
                log.addToLog("Added", blockChain.getHead().getId(), blockChain.getHead().getHash());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            Simple.pause();
            return true;
        
        case 3:
            Simple.banner();;
            try {
                blockChain.validateBlockChain();
                Print.BlockChain(blockChain);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                if(Simple.userConfirmation("[?] Continue anyways? ")) {
                    Print.BlockChain(blockChain);
                }
                else {
                    Simple.banner();;
                    System.out.println("[!] Operation aborted.");
                }
            }
            Simple.pause();
            return true;
        
        case 4:
            Simple.banner();;
            try {
                blockChain.validateBlockChain();
                userPrintBlock(blockChain);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }       
            Simple.pause();
            return true;

        case 5:
            Simple.banner();;
            log.printLog();
            Simple.pause();
            return true;
        
        case 6:
            Simple.banner();;
            if(Simple.userConfirmation("[?] Do you really want to clear you log?")) {
                log = new Log();
                Simple.banner();;
                System.out.println("[!] Cleared log sucessfully");
            }
            else {
                Simple.banner();;
                System.out.println("[!] Operation aborted.");
            }
            Simple.pause();
            return true;
        
        case 7:
            Simple.banner();;
            if (Simple.userConfirmation("[?] This action might potentially invalidate your BlockChain, continue anyways?")) {
                Simple.banner();;
                Block modifiedBlock = userModifyBlock(blockChain);
                if (modifiedBlock != null) {
                    Simple.banner();;
                    System.out.println("[!] Block modified sucessfully!");
                    log.addToLog("Modified", modifiedBlock.getId(), modifiedBlock.getHash());
                }
            }
            else {
                Simple.banner();;
                System.out.println("[!] Modification aborted.");
            }
            Simple.pause();
            return true;
        
        case 8:
            Simple.banner();
            Block block = userRecalculateHash(blockChain);
            if (block != null) {
                Simple.banner();;
                System.out.println("[!] Block hash recalculated sucessfully!");
                System.out.println("[-] Fun fact: It takes an average of 2.4 million years for an common computer to mine a real BitCoin block, due than an amount of 0's that the hash is required to have in order to be valid. The only way to find the the valid hash is to randomly modify the nonce value and recalculate the hash until it becomes valid. The amount of 0's is called difficulty. ");
                log.addToLog("Recalculed", block.getId(), block.getHash());
            }
            Simple.pause();
            return true;
        
        default:
            if (Simple.userConfirmation("[?] Do you really want to exit?")) {
                Simple.clear();
                System.out.println("[!] Exitting...");
                return false;
            }
            return true;
    }
}
}
