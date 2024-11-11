package classes.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.Base64;

import classes.io.Simple;

public class App {

    // Miscellaneous methods ##############################################

	public static int getRandomNumber(int min, int max) { // Will take two number, the min and max values, and return a random number
		return (int) ((Math.random() * (max - min)) + min);
	}

	public static String getRandomFact() {
		String[] facts = {
			"The first commercial bitcoin transaction was for pizza. 2 of them costed 10,000 BTC",
			"There are more than 20,000 cryptocurrencies in existence",
			"Some cryptocurrencies have more uses than as a coin",
			"CryptoKitties is one of the first blockchain games ",
			"The creator (or creators) of bitcoin remains anonymous",
			"Some countries ban cryptocurrencies",
			"One man wants to excavate a landfill to get his digital wallet back containing 7,500 BTC",
			"The largest single Bitcoin transaction ever recorded was worth $1.1 billion",
			"Dogecoin Started as a Joke"
		};
		return "[+] Fun fact: " + facts[getRandomNumber(0,facts.length)];
	}

    public static int getNumeralQtt(int number) { // Will take a number and return the quantity of numerals on it
        if (!(number == 0)) {
            return (int) Math.floor(Math.log10(Math.abs(number))) + 1;
        }
        return 1;
    }

	public static String calculateBlockHash(Block block) throws Exception {
		var md = MessageDigest.getInstance("SHA-256");
		md.update(block.toString().getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(md.digest());
	}

	// BlockChain Block methods #############################################

	// Will try to mine a block
	public static double mineBlock(BlockChain blockChain, PublicKey publicKey) throws Exception{
		double nonce = 0;
		int id = blockChain.length();
		Transaction[] data = mergeTransactions(blockChain, blockChain.getTransactionHead());

		Block block = new Block(blockChain.getHead(), id , nonce, data);
		while (!validateHash(blockChain, block)) {
			nonce++;
			block = new Block(blockChain.getHead(), id , nonce, data);
		}

		blockChain.addBlock(block, publicKey);
		return nonce;
	}

	// Will try to mine an already existing block
	public static double mineBlock(BlockChain blockChain, Block block) throws Exception{
		double nonce = 0;
		System.out.println("[-] Nonce: " + nonce);
		Block newBlock = new Block(block.getPrevious(), block.getId() , nonce, block.getData());
		while (!validateHash(blockChain, block)) {
			System.out.println("[-] Nonce: " + nonce);
			nonce++;
			newBlock = new Block(block.getPrevious(), block.getId() , nonce, block.getData());
		}

		Block aux = blockChain.getHead();
		while (aux.getPrevious().getId() != block.getId()) aux = aux.getPrevious();
		aux.setPrevious(newBlock);
		return nonce;
	}

	// Will return a specified block
	public static Block searchBlock(int id, BlockChain blockChain) throws Exception{ 
		Block current = blockChain.getHead();
		for (int i = 0; (i < blockChain.length() && current != null); i++) {
			if (current.getId() == id) return current;
			current = current.getPrevious();
		}
		throw new Exception("Block not found!");
	}

	// Will check wether or not the hash obey the difficulty rule
	public static boolean validateHash(BlockChain blockChain, Block block) {
		char[] c = block.getHash().toCharArray();
		for(int i = 0; i < blockChain.difficulty(); i++) {
			if (c[i] != '0') return false;
		} 
		return true;
	}

	// Will check if the previous block Hash is valid
	public static boolean validateLink(Block block, Block previousBlock) { 
		try {
			if(block.getPrevHash().equals(calculateBlockHash(previousBlock))) return true; 
		} catch (Exception e) {
			System.err.println("[X] A error occourred: " + e.getMessage());
		}
		return false;
	}

	public static void validateBlockChain(BlockChain blockChain) throws Exception{ // Throws exception if blockChain isn't valid
		Block aux = blockChain.getHead();
		while (aux.getPrevious() != null) { 
			if (!validateLink(aux, aux.getPrevious())) {
				throw new Exception("[X] Invalid BlockChain. Hash " + calculateBlockHash(aux.getPrevious()) + " from block " + aux.getPrevious().getId() + " doesn't correspond to the expected hash " + aux.getPrevHash() + ". Please Reset your BlockChain.");
			}
			aux = aux.getPrevious();
		}
	}

	// BlockChain Transaction methods #############################################

	// Will search through all BlockChain looking for transactions from a specific public key
	public static float searchForTokens(BlockChain blockChain, PublicKey senderPublicKey) {
		Block aux = blockChain.getHead();
		float tokens = 0;

		try {
			PublicKey blockchainKey = Encryption.readBlokChainKey();
			if (senderPublicKey == blockchainKey) return 21000000;
		} catch (IOException e) {
			System.out.println("[X] BlockChain's public key not found.");
		} catch (Exception e) {
			Simple.banner();
			System.out.println("[X]" + e.getMessage());
		}
		
		while (aux.getPrevious() != null) { 
			for (Transaction data : aux.getData()) {
				try {
					isTransaction(data);
			
					if (data.getSenderPublicKey().equals(senderPublicKey)) tokens = tokens - data.getTokenAmount(); 
					if (data.getReceiverPublicKey().equals(senderPublicKey)) tokens = tokens + data.getTokenAmount(); 
				} catch (Exception e) {
				}
			}
			aux = aux.getPrevious();
		}

		return tokens;
	}

	// Overloading for better user experience
	// Instead of loading a public key for searching, you can simply type the hashcode
	public static float searchForTokens(BlockChain blockChain, String hashOfSenderPK) {
		Block aux = blockChain.getHead();
		float tokens = 0;

		try {
			PublicKey blockchainKey = Encryption.readBlokChainKey();
			if (hashOfSenderPK == "" + blockchainKey.hashCode()) return 21000000;
		} catch (IOException e) {
			System.out.println("[X] BlockChain's public key not found.");
		} catch (Exception e) {
			Simple.banner();
			System.out.println("[X]" + e.getMessage());
		}
		
		while (aux.getPrevious() != null) { 
			for (Transaction data : aux.getData()) {
				try {
					isTransaction(data);
					if (("" + data.getSenderPublicKey().hashCode()).equals(hashOfSenderPK)) tokens = tokens - data.getTokenAmount(); 
					if (("" + data.getReceiverPublicKey().hashCode()).equals(hashOfSenderPK)) tokens = tokens + data.getTokenAmount(); 
				} catch (Exception e) {
				}
			}
			aux = aux.getPrevious();
		}

		return tokens;
	}

	// Will check the transaction format and return wether or not the data can be safely parsed
	private static boolean isTransaction(Transaction transaction) throws Exception {
		if(transaction == null) return false;
		if(transaction.getTokenAmount() < 0) return false;
		if(transaction.getSenderPublicKey() == null) return false;
		if(transaction.getReceiverPublicKey() == null) return false;

		return true;
	}

	// Will check if the transaction can be done
	public static boolean validateTransaction(BlockChain blockChain, Transaction transaction) throws Exception { 
		if (isTransaction(transaction))
		{
			if(transaction.getSenderPublicKey() == transaction.getReceiverPublicKey()) throw new Exception("Cannot transfer to the same public key");
			try {
				PublicKey blockchainKey = Encryption.readBlokChainKey();
				if (transaction.getSenderPublicKey() != blockchainKey) {
					if(!Encryption.verifySignature(transaction.toString(), transaction.getSignature(), transaction.getSenderPublicKey())) throw new Exception("Invalid Signature");
				}
				if (transaction.getTokenAmount() > searchForTokens(blockChain, transaction.getSenderPublicKey())) throw new Exception("Insuficient balance");
			} catch (IOException e) {
				System.out.println("[X] BlockChain's public key not found.");
			} catch (Exception e) {
				Simple.banner();
				System.out.println("[X]" + e.getMessage());
			}
			return true;
		}
		return false;
	} 

	// Will merge the transactions into a single Array
	public static Transaction[] mergeTransactions(BlockChain blockChain, Transaction head) throws Exception{
		Transaction aux = head;
		int validLength = 0;

		if(head != null) {
			if(validateTransaction(blockChain, aux)) validLength++;
			while (aux.getPrevious() != null) {
				aux = aux.getPrevious(); 
				if(validateTransaction(blockChain, aux)) validLength++;
			}
		}

		Transaction[] data = new Transaction[validLength];
		aux = head;
		int counter = 0;
		if(head != null) {
			if(validateTransaction(blockChain, aux)) data[counter] = aux;
			while (aux.getPrevious() != null) {
				aux = aux.getPrevious();
				counter++;
				if(validateTransaction(blockChain, aux)) data[counter] = aux;
			}
		}
		return data;
	}
}
