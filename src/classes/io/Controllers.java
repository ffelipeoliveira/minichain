package classes.io;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import classes.core.App;
import classes.core.Block;
import classes.core.BlockChain;
import classes.core.Encryption;
import classes.core.Transaction;

public class Controllers {
 // Can read a public key
    protected static PublicKey userReadPublicKey(String message) throws IOException, Exception {
        System.out.println(message + " (d to read default name | location: ./keys/ | c to cancel)");
        String path = Simple.strInput(100);
        if (path.equals("c")) throw new Exception("Operation aborted.");
        if (path.equals("d")) {
            PublicKey key = Encryption.readPublicKey();
            System.out.println("[!] Public key loaded sucessfully!");
            return key;
        } 
        else{
            PublicKey key = Encryption.readPublicKey(path);
            System.out.println("[!] Public key loaded sucessfully!");
            return key;
        }
    }

    // Can read your private key
    protected static PrivateKey userReadPrivateKey(String message) throws IOException, Exception {
        System.out.println(message + " (d to read default name | location: ./keys/ | c to cancel)");
        String path = Simple.strInput(100);
        if (path.equals("c")) throw new Exception("Operation aborted.");
        if (path.equals("d")) {
            PrivateKey key = Encryption.readPrivateKey();
            System.out.println("[!] Private key loaded sucessfully!");
            return key;
        } 
        else{
            PrivateKey key = Encryption.readPrivateKey(path);
            System.out.println("[!] Private key loaded sucessfully!");
            return key;
        }
    }

    // Can store your public key
    protected static void userStoreKeyPair(String message, KeyPair keyPair) throws IOException, Exception {
        System.out.println(message + " (d to store with default name | location: ./keys/ | c to cancel)");
        String path = Simple.strInput(100);
        if (path.equals("c")) throw new Exception("Operation aborted.");
        if (path.equals("d")) {
            Encryption.storeKeys(keyPair);
            System.out.println("[!] Key pair stored sucessfully!");
        } 
        else{
            Encryption.storeKeys(keyPair, path);
            System.out.println("[!] Key pair sucessfully!");
        }
    }

    // Can create a new transaction
    protected static Transaction userCreateTransaction(BlockChain blockChain)  {
        PublicKey senderPublicKey = null;
        PublicKey receiverPublicKey = null;
        PrivateKey senderPrivateKey = null;

        Simple.banner();
        while (senderPublicKey == null) {
            try {
                senderPublicKey = userReadPublicKey("Type the file name of your public key file.");
            } catch (IOException e) {
                System.out.println("[X] Invalid path or file not found");
            } catch (Exception e) {
                Simple.banner();
                System.out.println("[X] " + e.getMessage());
                return null;
            }
        }

        Simple.banner();
        while (receiverPublicKey == null) {
            try {
                receiverPublicKey = userReadPublicKey("Type the file name the receiver's public key.");
            } catch (IOException e) {
                System.out.println("[X] Invalid path or file not found");
            } catch (Exception e) {
                Simple.banner();
                System.out.println("[X] " + e.getMessage());
                return null;
            }
        }

        Simple.banner();
        System.out.println("[!] Type the amount of tokens to transfer: (0 to cancel)");
        long amount = Simple.intInput();
        if(amount == 0) {
            System.out.println("[X] Operation aborted.");
            return null;
        }

        
        Simple.banner();
        System.out.println("[$] " + amount  + " from you to " + receiverPublicKey.hashCode());
        if(!Simple.userConfirmation("[!] ARE YOU SURE THIS IS CORRECT? (THIS ACTION CANNOT BE REVERSED)")) {
            Simple.banner();
            System.out.println("[X] Operation aborted.");
            return null;
        }

        Simple.banner();
        while (senderPrivateKey == null) {
            try {
                senderPrivateKey = userReadPrivateKey("Type the path to the your private key.");
            } catch (IOException e) {
                System.out.println("[X] Invalid path or file not found");
            } catch (Exception e) {
                Simple.banner();
                System.out.println("[X] " + e.getMessage());
                return null;
            }
        }

        try {
            String data = ("$[(" + amount + ") " + senderPublicKey.hashCode() + "]=>[" + receiverPublicKey.hashCode() + "]");
            String signature = Encryption.signTransaction(data, senderPrivateKey);
            return new Transaction(blockChain.getTransactionHead() ,senderPublicKey, receiverPublicKey, amount, signature);
        } catch (Exception e) {
            Simple.banner();
            System.out.println("[X] " + e.getMessage());
            return null;
        }
    }

    // Can manage the BlockChain's addTransaction()
    protected static boolean userAddTransaction(BlockChain blockChain) { 
        
        try {
            blockChain.addTransaction(userCreateTransaction(blockChain));
            return true;
        } catch (Exception e) {
            Simple.banner();
            System.out.println("[X] " + e.getMessage());
            return false;
        }
    }

    // Can manage the App's mineBlock()
    protected static boolean userMineBlock(BlockChain blockChain) {
        PublicKey minerPublicKey = null;

        while (minerPublicKey == null) {
            try {
                minerPublicKey = userReadPublicKey("Type the path to your public key file.");
                if(!Simple.userConfirmation("[!] ARE YOU SURE YOU WANT TO PROCEED? (THIS ACTION MIGHT TAKE SEVERAL MINUTES)")) {
                    System.out.println("[!] Operation aborted.");
                    return false;
                }
                double nonce = App.mineBlock(blockChain, minerPublicKey);
                Simple.banner();
                System.out.println("[!] Block sucessfully mined! Nonce was " + nonce + ", your reward will be included in the next Block.");
                return true;
            } catch (IOException e) {
                System.out.println("[X] Invalid path or file not found");
                return false;
            } catch (Exception e) {
                Simple.banner();
                System.out.println("[X] " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    // Can manage the App's searchBlock()
    protected static void userPrintBlock(BlockChain blockChain) {
        Print.BlockChain(blockChain);
        System.out.println("[1/1] Select a block by it's number to see it's full content: ");
        try {
            Block foundBlock = App.searchBlock(Simple.intInput(), blockChain);
            Simple.banner();
            Print.FullBlock(foundBlock);
        } catch (Exception e) {
            Simple.banner();
            System.out.println("[X] " + e.getMessage());
        }
    }

    // Can manage the Block's recalculateHash()
    protected static void userReMineHash(BlockChain blockChain) {
        Print.BlockChain(blockChain);
        System.out.println("[1/1] Select a block by it's number to recalculate it's hashes");
        try {
            Block foundBlock = App.searchBlock(Simple.intInput(), blockChain);
            Simple.banner();
            Print.FullBlock(foundBlock);
            if(!Simple.userConfirmation("[!] ARE YOU SURE YOU WANT TO PROCEED? (THIS ACTION MIGHT TAKE SEVERAL MINUTES)")) {
                System.out.println("[!] Operation aborted.");
                return;
            }
            double nonce = App.mineBlock(blockChain, foundBlock);
            System.out.println("[!] Block sucessfully mined! Nonce was " + nonce + ".");
        } catch (Exception e) {
            Simple.banner();
            System.out.println("[X] " + e.getMessage());
        }
    }

    // Can manage the Block's setData()
    protected static Block userModifyBlock(BlockChain blockChain) {
        Print.BlockChain(blockChain);
        System.out.println("[1/1] Select a block by it's number to modify (this operation might invalidate your BlockChain)");
        try {
            Block foundBlock = App.searchBlock(Simple.intInput(), blockChain);
            Simple.banner();
            Print.FullBlock(foundBlock);
            Transaction[] transactions;
            Simple.banner();
            System.out.println("[2/2] How much data (strings) Can you store in this Block?");
            int transactionsAmount = Simple.intInput(1, 5);
            transactions = new Transaction[transactionsAmount];
            for (int i = 0; i < transactionsAmount; i++) {
                transactions[i] = userCreateTransaction(blockChain);
            }
            Block newBlock = new Block(foundBlock.getPrevious(), foundBlock.getId() , foundBlock.getNonce(), foundBlock.getData());
            Block aux = blockChain.getHead();
            while (aux.getPrevious().getId() != foundBlock.getId()) aux = aux.getPrevious();
            aux.setPrevious(newBlock);
            System.out.println("[!] Data modified sucessfully");
            return foundBlock;
        } catch (Exception e) {
            Simple.banner();
            System.out.println("[X] " + e.getMessage());
            return null;
        }
    }
}
