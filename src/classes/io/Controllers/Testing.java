package io.Controllers;

import core.App;
import core.Blockchain;
import core.Transaction;
import core.Block;

import io.Simple;
import io.Print;

public class Testing {

    // Case 1 is defined within switch's testing submenu

    // Case 2
    // Can manage the Block's setData()
    public static Block userModifyBlock(String menuPath, String defaultFolder, Blockchain blockChain) {
        Print.PrintBlockchain(blockChain);
        System.out.println("[!] Select a block by it's number to modify (this operation might invalidate your BlockChain)");
        try {
            Block foundBlock = App.searchBlock(Simple.intInput(), blockChain);
            Simple.banner(menuPath + "> Modify Block [1/2]");
            Print.PrintFullBlock(foundBlock);
            Transaction[] transactions;
            Simple.banner(menuPath + "> Modify Block [1/2]");
            System.out.println("[!] How much data (strings) Can you store in this Block?");
            int transactionsAmount = Simple.intInput(1, 5);
            transactions = new Transaction[transactionsAmount];
            for (int i = 0; i < transactionsAmount; i++) {
                transactions[i] = Transferring.userCreateTransaction(menuPath + "> Modify Block", defaultFolder, blockChain);
            }
            Block newBlock = new Block(foundBlock.getPrevious(), foundBlock.getId() , foundBlock.getNonce(), foundBlock.getData());
            Block aux = blockChain.getHead();
            while (aux.getPrevious().getId() != foundBlock.getId()) aux = aux.getPrevious();
            aux.setPrevious(newBlock);
            System.out.println("[!] Data modified sucessfully");
            return foundBlock;
        } catch (Exception e) {
            Simple.banner(menuPath + "> Modify Block (error)");
            System.out.println("[X] " + e.getMessage());
            return null;
        }
    }

    // Case 3
    // Can manage the Block's recalculateHash()
    public static void userReMineHash(String menuPath, Blockchain blockChain) {
        Print.PrintBlockchain(blockChain);
        System.out.println("[1/1] Select a block by it's number to recalculate it's hashes");
        try {
            Block foundBlock = App.searchBlock(Simple.intInput(), blockChain);
            Simple.banner(menuPath + "> Re-mine block");
            Print.PrintFullBlock(foundBlock);
            if(!Simple.userConfirmation("ARE YOU SURE YOU WANT TO PROCEED? (THIS ACTION MIGHT TAKE SEVERAL MINUTES)")) {
                System.out.println("[!] Operation aborted.");
                return;
            }
            double nonce = App.mineBlock(blockChain, foundBlock);
            System.out.println("[!] Block sucessfully mined! Nonce was " + nonce + ".");
        } catch (Exception e) {
            Simple.banner(menuPath + "> Mining (error)");
            System.out.println("[X] " + e.getMessage());
        }
    }

}
