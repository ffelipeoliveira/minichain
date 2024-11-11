package classes.core;

import java.io.IOException;
import java.security.PublicKey;

import classes.io.Simple;

public class BlockChain {
	private static final String DEFAULT_GENESIS_TEXT = "The Times 03/Jan/2009 Chancellor on brink of second bailout for banks"; // The famous phrase from Satoshi Nakamoto
	private static final int DEFAULT_REWARD = 50; //Tokens

	private Block head;
	private Transaction transactionHead;

	public BlockChain() { // Can create a Genesis Block on instance using default text
		Transaction[] genesisData = new Transaction[1];
		genesisData[0] = new Transaction(DEFAULT_GENESIS_TEXT);
		head = new Block(null, 0, 0, genesisData);
	}

	//Basic BlockChain Functions ########################################

	// Can set a block as the head, reset transactions and set the reward for the miner on the next block
	public void addBlock(Block block, PublicKey minerPublicKey) { 
		this.head = block;
		try {
			PublicKey blockchainKey = Encryption.readBlokChainKey();
			this.transactionHead = new Transaction(null, blockchainKey, minerPublicKey, reward(), null);
		} catch (IOException e) {
			System.out.println("[X] BlockChain's public key not found.");
			Simple.pause();
		} catch (Exception e) {
			Simple.banner();
			System.out.println("[X]" + e.getMessage());
		}
	}

	// Every Blockchain will have at least a block by default
	public int length() {
		int counter = 1; 
		Block aux = this.getHead();
		while (aux.getPrevious() != null) {
			aux = aux.getPrevious(); 
			counter++;
		}
		return counter;
	}

	public int transactionsLength() {
		int counter = 0;
		if(this.transactionHead != null) {
			counter = 1;
			Transaction aux = this.transactionHead;
			while (aux.getPrevious() != null) {
				aux = aux.getPrevious(); 
				counter++;
			}
		}
		return counter;
	}

	// Can return the number of zeroes necessary to place a block
	public int difficulty() { 
		int difficulty = 0;
		int target = 2;
		while (target < this.length()) {
			target*=target;
			difficulty++;
		}
		return difficulty;
	}

	// Can return the number of zeroes necessary for a block to be valid on a certain place
	public int difficulty(int length) { 
		int difficulty = 0;
		int target = 2;
		while (target < this.length()) {
			target*=target;
			difficulty++;
		}
		return difficulty;
	}

	// Can return a number of tokens as reward for mining a block
	public float reward() {
		int reward = DEFAULT_REWARD;
		int target = 2;
		while (target < this.length()) {
			target*=target;
			reward/=2;
		}
		return reward;
	}

	public void addTransaction(PublicKey senderPublicKey, PublicKey receiverPublicKey, float tokenAmount, String signature) {
		Transaction newTransaction = new Transaction(this.transactionHead, senderPublicKey, receiverPublicKey, tokenAmount, signature);
		this.transactionHead = newTransaction;
	}

	public void addTransaction(Transaction newTransaction) {
		this.transactionHead = newTransaction;
	}



	public Block getHead() {
		return this.head;
	}

	public Transaction getTransactionHead() {
		return this.transactionHead;
	}
}
