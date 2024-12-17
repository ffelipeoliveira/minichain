package io;

import core.App;
import core.Block;
import core.Blockchain;
import core.Transaction;

public class Print {
	private static final int MAX_BLOCKS_PER_ROW = 4; // Optimal for small terminals
    private static final int MAX_STR_LENGTH = 23; // A block has 27 in length, including the two borders and two spaces at beginning and end
    private static final int MAX_INT_LENGTH = 10; // The label that come before numbers are 13 characters long

	private static void printSpace(int number, int maxIntLength) { //The default number of available space for an number to be printed is 10 since they need an extra label (id, hash, nonce which is not implemented yet, and the previous hash)
        for (int i = 0; i < (maxIntLength - App.getNumeralQtt(number)); i++) {
            System.out.print(" ");
        }
        if(number >= 0) System.out.print(" "); //Negative numbers take up a extra character -
        System.out.print(" ║   ");
    }

    private static void printSpace(long number, int maxIntLength) { //The default number of available space for an number to be printed is 10 since they need an extra label (id, hash, nonce which is not implemented yet, and the previous hash)
        for (int i = 0; i < (maxIntLength - App.getNumeralQtt(number)); i++) {
            System.out.print(" ");
        }
        if(number >= 0) System.out.print(" "); //Negative numbers take up a extra character -
        System.out.print(" ║   ");
    }


    private static void printSpace(String str, int maxStrLength) { //The default number of available space for an information to be printed is 23 characters, since they don't need an extra label
        for (int i = 0; i < (maxStrLength - str.length()); i++) {
            System.out.print(" ");
        }
        System.out.print(" ║   ");
    }

	public static void PrintFullBlock(Block block) { // Basic print of a full block information
        System.out.println("Showing full block information\n╔════════════════════════════════════════════════════════════════════════╗ ");
        System.out.print(  "║ BLOCK NUMBER ID       : " + block.getId());
        printSpace(block.getId(), 45);
        System.out.println();
        System.out.print(  "║ THIS BLOCK'S HASH     :  " + block.getHash());
        printSpace(block.getHash(), 45);
        System.out.println();
        //System.out.println("║ TIMESTAMP             :  " + block.getTimestamp());
        //printSpace(block.getTimestamp(), 45);
        //System.out.println();
        if(block.getPrevHash() != null) {
            System.out.print(  "║ PREVIOUS BLOCK'S HASH  : " + block.getPrevHash());
            printSpace(block.getPrevHash(), 45);
        }
        System.out.println("║░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░║");
        for (Transaction data : block.getData()) {
            System.out.print("║ " + data.toString().substring(0, Math.min(data.toString().length(), 70)));
            printSpace(data.toString(), 70);
            System.out.println();
        }
        System.out.println("╚════════════════════════════════════════════════════════════════════════╝ ");
    }

	public static void PrintBlockchain(Blockchain blockChain) { //Will print an Array of blocks horizontally line by line
        int blockQtt = blockChain.length();
        int rows = 1; //Default amount of rows
        if(blockQtt > MAX_BLOCKS_PER_ROW) {
            rows = (blockQtt + MAX_BLOCKS_PER_ROW - 1) / MAX_BLOCKS_PER_ROW; // This division will round up to the nearest int, since a full row is needed to print even if it's just only one block
        }
        Block[] row; // This array will be used to store the blocks in each row
        Block currentBlock = blockChain.getHead();

        System.out.println("Showing full BlockChain: ");
        for(int i = 0; i < rows; i++) {
            int maxDataLength = 1;
            row = new Block[MAX_BLOCKS_PER_ROW]; // To erase the previous blocks a new object is created, the previous object, now untracked, will be discarded by the JVM
            for(int j = 0; (j < MAX_BLOCKS_PER_ROW && currentBlock != null); j++) {
                row[j] = currentBlock;
                if(currentBlock.getData().length > maxDataLength) maxDataLength = currentBlock.getData().length;
                currentBlock = currentBlock.getPrevious();
                System.out.print("╔═════════════════════════╗   ");
            }
            System.out.println();
    
            for(int j = 0; (j < MAX_BLOCKS_PER_ROW && row[j] != null); j++) {
                System.out.print("║ BLOCK NUM : " + row[j].getId());
                printSpace(row[j].getId(), MAX_INT_LENGTH);
            }
            System.out.println();
    
            for(int j = 0; (j < MAX_BLOCKS_PER_ROW && row[j] != null); j++) {
                System.out.print("║ THIS HASH : " + row[j].getHash().substring(0, Math.min(row[j].getHash().length(), 8)) + "...");
                printSpace(row[j].getHash(), MAX_INT_LENGTH - 3);
            }
            System.out.println();
    
            for(int j = 0; (j < MAX_BLOCKS_PER_ROW && row[j] != null); j++) {
                if(row[j].getPrevHash() != null) {
                    System.out.print("║ PREV HASH : " + row[j].getPrevHash().substring(0, Math.min(row[j].getHash().length(), 8)) + "...");
                    printSpace(row[j].getPrevHash(), MAX_INT_LENGTH - 3);
                }
                else System.out.print("║                         ║   ");
            }
            System.out.println();

            for(int j = 0; (j < MAX_BLOCKS_PER_ROW && row[j] != null); j++) {
                System.out.print("║ TIMESTAMP : " + row[j].getTimestamp() + "  ║   ");
            }
            System.out.println();

            for(int j = 0; (j < MAX_BLOCKS_PER_ROW && row[j] != null); j++) {
                System.out.print("║░░░░░░░░░░░░░░░░░░░░░░░░░║");
                if (row[j].getPrevious() != null) {
                    if(App.validateLink(row[j], row[j].getPrevious())) System.out.print("===");
                    else System.out.print("=X=");
                };
            }
            System.out.println();

            for(int j = 0; j < maxDataLength; j++) { // J is the index of the data
                for (int k = 0; (k < MAX_BLOCKS_PER_ROW && row[k] != null); k++) { // K is the index of the block
                    if(row[k].getData().length > j) {
                        int strLength = row[k].getData()[j].toString().length();
                        if(strLength >= MAX_STR_LENGTH)  System.out.print("║ " + row[k].getData()[j].toString().substring(0, Math.min(strLength, MAX_STR_LENGTH - 3)) + "... ║   "); // This will check if the data will overflow long and if yes it will print a substring made from the first few characters allowed
                        else {
                            System.out.print("║ " + row[k].getData()[j]);
                            printSpace(row[k].getData()[j].toString(), MAX_STR_LENGTH);
                        }
                    }
                    else {
                        System.out.print("║                         ║   ");
                    }
                }
                System.out.println();
            }

            for(int j = 0; (j < MAX_BLOCKS_PER_ROW && row[j] != null); j++) {
                System.out.print("╚═════════════════════════╝   ");
            }
            System.out.println();
        }
	}
}
