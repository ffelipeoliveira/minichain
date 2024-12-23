package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.Base64;

import io.Simple;

public class App {

	// Will take two number, the min and max values, and return a random number
	public static int getRandomNumber(int min, int max) {
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
		return "[+] Fun fact: " + facts[getRandomNumber(0, facts.length)];
	}

	// Will take a number and return the quantity of numerals on it
	public static int getNumeralQtt(int number) {
		if (!(number == 0)) {
			return (int) Math.floor(Math.log10(Math.abs(number))) + 1;
		}
		return 1;
	}

	// Will compare the Blockchain's length with itself's lenght and validate it
	public void unFork(Blockchain thisBlockchain, Blockchain blockchain) {
		if (blockchain.length() > thisBlockchain.length()) {
			try {
				App.validateBlockchain(blockchain);
				thisBlockchain = blockchain;
			} catch (Exception e) {
				System.out.println("Invalid Blockchain");
				Simple.pause();
			}
		}
	}

	// Will take a number and return the quantity of numerals on it
	public static int getNumeralQtt(long number) {
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

	// Blockchain Block methods #############################################

	// Save block
	public static void saveBlock(String blockDirectory, Block block) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(
					blockDirectory + "\\block" + block.getId() + ".ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(block);
			objectOutputStream.close();
		} catch (IOException e) {
			System.out.println("[X] " + e.getMessage());
		}
	}

	// Load block
	public static Block loadBlock(String blockDirectory, String filename) {
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(
					new FileInputStream(blockDirectory + "\\" + filename));
			Block restoredBlock = (Block) objectInputStream.readObject();
			objectInputStream.close();
			return restoredBlock;
		} catch (Exception e) {
			System.out.println("[X] " + e.getMessage());
		}
		return null;
	}

	// Load Blockchain
	public static Blockchain loadBlockchain(String blockDirectory) {
		Blockchain blockchain = new Blockchain(blockDirectory);
		File[] files;

		// If there's no file it'll throw a exception
		try {
			files =  new File(blockDirectory).listFiles();
		} catch (Exception e) {
			return blockchain;
		}
		
		for (File file : files) {
			if (!file.isDirectory() && file.getName().length() > 4) {
				String fileName = file.getName();
				int length = fileName.length();
				if (fileName.charAt(length - 1) == 'r' && fileName.charAt(length - 2) == 'e'
						&& fileName.charAt(length - 3) == 's' && fileName.charAt(length - 4) == '.') {
					Block block = loadBlock(blockDirectory, fileName);
					if (block != null) {
						blockchain.addLoadedBlock(block);
					}
				}
			}
		}
		return blockchain;
	}

	// Will try to mine a block
	public static Block mineBlock(String menuPath, String keyDirectory, Blockchain blockchain, PublicKey publicKey) throws Exception {
		double nonce = 0;
		int id = blockchain.length();
		Transaction[] data = mergeTransactions(menuPath, keyDirectory, blockchain, blockchain.getTransactionHead(), publicKey);

		Block block = new Block(blockchain.getHead(), id, nonce, data);
		while (!validateHash(blockchain, block)) {
			nonce++;
			block = new Block(blockchain.getHead(), id, nonce, data);
		}

		blockchain.addBlock(menuPath, keyDirectory, block, publicKey);
		return block;
	}

	// Will try to mine an already existing block
	public static double mineBlock(Blockchain blockchain, Block block) throws Exception {
		double nonce = 0;
		System.out.println("[-] Nonce: " + nonce);
		Block newBlock = new Block(block.getPrevious(), block.getId(), nonce, block.getData());
		while (!validateHash(blockchain, block)) {
			System.out.println("[-] Nonce: " + nonce);
			nonce++;
			newBlock = new Block(block.getPrevious(), block.getId(), nonce, block.getData());
		}

		Block aux = blockchain.getHead();
		while (aux.getPrevious().getId() != block.getId())
			aux = aux.getPrevious();
		aux.setPrevious(newBlock);
		return nonce;
	}

	// Will return a specified block
	public static Block searchBlock(int id, Blockchain blockchain) throws Exception {
		Block current = blockchain.getHead();
		for (int i = 0; (i < blockchain.length() && current != null); i++) {
			if (current.getId() == id)
				return current;
			current = current.getPrevious();
		}
		throw new Exception("Block not found!");
	}

	// Will check wether or not the hash obey the difficulty rule
	public static boolean validateHash(Blockchain blockchain, Block block) {
		char[] c = block.getHash().toCharArray();
		for (int i = 0; i < blockchain.difficulty(); i++) {
			if (c[i] != '0')
				return false;
		}
		return true;
	}

	// Will check if the previous block Hash is valid
	public static boolean validateLink(Block block, Block previousBlock) {
		try {
			if (block.getPrevHash().equals(calculateBlockHash(previousBlock)))
				return true;
		} catch (Exception e) {
			System.err.println("[X] A error occourred: " + e.getMessage());
		}
		return false;
	}

	public static void validateBlockchain(Blockchain blockchain) throws Exception { // Throws exception if blockchain
																					// isn't valid
		Block aux = blockchain.getHead();
		while (aux.getPrevious() != null) {
			if (!validateLink(aux, aux.getPrevious())) {
				throw new Exception("[X] Invalid Blockchain. Hash " + calculateBlockHash(aux.getPrevious())
						+ " from block " + aux.getPrevious().getId() + " doesn't correspond to the expected hash "
						+ aux.getPrevHash() + ". Please Reset your Blockchain.");
			}
			aux = aux.getPrevious();
		}
	}

	// Blockchain Transaction methods #############################################

	// Will search through all Blockchain looking for transactions from a specific
	// public key
	public static float searchForTokens(String menuPath, String directory, Blockchain blockchain, PublicKey senderPublicKey) {
		Block aux = blockchain.getHead();
		float tokens = 0;

		try {
			PublicKey blockchainKey = Encryption.readBlockchainKey(directory);
			if (senderPublicKey == blockchainKey)
				return 21000000;
		} catch (IOException e) {
			System.out.println("[X] Blockchain's public key not found.");
		} catch (Exception e) {
			Simple.banner(menuPath + " Search for Tokens (error)");
			System.out.println("[X]" + e.getMessage());
		}

		while (aux.getPrevious() != null) {
			for (Transaction data : aux.getData()) {
				try {
					isTransaction(data);

					if (data.getSenderPublicKey().equals(senderPublicKey))
						tokens = tokens - data.getTokenAmount();
					if (data.getReceiverPublicKey().equals(senderPublicKey))
						tokens = tokens + data.getTokenAmount();
				} catch (Exception e) {
				}
			}
			aux = aux.getPrevious();
		}

		return tokens;
	}

	// Overloading for better user experience
	// Instead of loading a public key for searching, you can simply type the
	// hashcode
	public static float searchForTokens(String menuPath, String directory, Blockchain blockchain, String hashOfSenderPK) {
		Block aux = blockchain.getHead();
		float tokens = 0;

		try {
			PublicKey blockchainKey = Encryption.readBlockchainKey(directory);
			if (hashOfSenderPK.equals(Encryption.calculateKeyHash(blockchainKey)))
				return 21000000;
		} catch (IOException e) {
			System.out.println("[X] Blockchain's public key not found.");
		} catch (Exception e) {
			Simple.banner(menuPath + " > Search for tokens");
			System.out.println("[X]" + e.getMessage());
		}

		while (aux.getPrevious() != null) {
			for (Transaction data : aux.getData()) {
				try {
					isTransaction(data);
					if (Encryption.calculateKeyHash(data.getSenderPublicKey()).equals(hashOfSenderPK))
						tokens = tokens - data.getTokenAmount();
					if (Encryption.calculateKeyHash(data.getReceiverPublicKey()).equals(hashOfSenderPK))
						tokens = tokens + data.getTokenAmount();
				} catch (Exception e) {
				}
			}
			aux = aux.getPrevious();
		}

		return tokens;
	}

	// Will check the transaction format and return wether or not the data can be
	// safely parsed
	private static boolean isTransaction(Transaction transaction) throws Exception {
		if (transaction == null)
			return false;
		if (transaction.getTokenAmount() < 0)
			return false;
		if (transaction.getSenderPublicKey() == null)
			return false;
		if (transaction.getReceiverPublicKey() == null)
			return false;

		return true;
	}

	// Will check if the transaction can be done
	public static boolean validateTransaction(String menuPath, String directory, Blockchain blockchain, Transaction transaction)
			throws Exception {
		if (isTransaction(transaction)) {
			if (transaction.getSenderPublicKey() == transaction.getReceiverPublicKey())
				throw new Exception("Cannot transfer to the same public key");
			try {
				PublicKey blockchainKey = Encryption.readBlockchainKey(directory);
				if (transaction.getSenderPublicKey() != blockchainKey) {
					if (!Encryption.verifySignature(transaction.toString(), transaction.getSignature(),
							transaction.getSenderPublicKey()))
						throw new Exception("Invalid Signature");
				}
				if (transaction.getTokenAmount() > searchForTokens(menuPath + " > Validate Transaction", directory, blockchain,
						transaction.getSenderPublicKey()))
					throw new Exception("Insuficient balance");
			} catch (IOException e) {
				Simple.banner(menuPath + " > Validate Transaction (error)");
				System.out.println("[X] Blockchain's public key not found.");
			} catch (Exception e) {
				Simple.banner(menuPath + " > Validate Transaction (error)");
				System.out.println("[X]" + e.getMessage());
			}
			return true;
		}
		return false;
	}

	// Will add fees to transactions

	private static Transaction addFee(Transaction transaction, PublicKey minerPublicKey) {
		return new Transaction(transaction, transaction.getSenderPublicKey(), minerPublicKey,
				(transaction.getTokenAmount() / 100), null, true);
	}

	// Will merge the transactions into a single Array
	public static Transaction[] mergeTransactions(String menuPath, String keyDirectory, Blockchain blockchain, Transaction head,
			PublicKey minerPublicKey) throws Exception {
		Transaction aux = head;
		int validLength = 0;

		if (head != null) {
			if (validateTransaction(menuPath, keyDirectory, blockchain, aux))
				validLength++;
			while (aux.getPrevious() != null) {
				aux = aux.getPrevious();
				if (validateTransaction(menuPath, keyDirectory, blockchain, aux))
					validLength++;
			}
		}

		Transaction[] data = new Transaction[validLength * 2];
		aux = head;
		int counter = 0;
		if (head != null) {
			if (validateTransaction(menuPath, keyDirectory,  blockchain, aux)) {
				data[counter] = aux;
				data[counter + 1] = addFee(aux, minerPublicKey);
				counter++;
			}
			while (aux.getPrevious() != null) {
				aux = aux.getPrevious();
				counter++;
				if (validateTransaction(menuPath, keyDirectory, blockchain, aux)) {
					data[counter] = aux;
					data[counter + 1] = addFee(aux, minerPublicKey);
					counter++;
				}

			}
		}
		return data;
	}
}
