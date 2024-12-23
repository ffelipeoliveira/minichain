package other;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

import core.Encryption;
import core.App;
import core.Blockchain;
import io.Simple;

public class Load {
	private static final String DEFAULT_FOLDER_NAME = "\\minichain\\";
	private static final String DEFAULT_BLOCK_DIRECTORY_NAME = DEFAULT_FOLDER_NAME + "blockchain\\";
	private static final String DEFAULT_KEY_DIRECTORY_NAME = DEFAULT_FOLDER_NAME + "keys\\";

	// Will be acessed
	public String directory, blockDirectory, keyDirectory;
	public Blockchain blockchain;
	public boolean error = false;

	private void loadDefaultDirectory() {
		String pathToDocuments = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		try {
			File directory = new File(pathToDocuments + DEFAULT_FOLDER_NAME);
			if (!directory.exists()) {
				 directory.mkdir();
			}

			this.directory = pathToDocuments + DEFAULT_FOLDER_NAME;

			File keyDirectory = new File(pathToDocuments + DEFAULT_KEY_DIRECTORY_NAME);
			if (!keyDirectory.exists()) {
				 keyDirectory.mkdir();
			}

			this.keyDirectory = pathToDocuments + DEFAULT_KEY_DIRECTORY_NAME;

			File blockDirectory = new File(pathToDocuments + DEFAULT_BLOCK_DIRECTORY_NAME);
			if (!blockDirectory.exists()) {
				blockDirectory.mkdir();
			}

			this.blockDirectory = pathToDocuments + DEFAULT_BLOCK_DIRECTORY_NAME;
		} catch (Exception e) {
			Simple.banner("Loading error");
			System.out.println("[X] " + e.getMessage());
			error = true;
			Simple.pause();
		}
	}

	private boolean loadUserKeypair() {
		try {
			Encryption.readPrivateKey(keyDirectory);
			Encryption.readPublicKey(keyDirectory);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void createUserKeypair() {
		try {
			Encryption.storeKeys(keyDirectory, Encryption.generateKeyPair());
			System.out.println("[!] Key pair created sucessfully on " + keyDirectory);
		} catch (Exception e) {
			Simple.banner("Loading error");
			System.out.println("[X] " + e.getMessage());
			System.out.println(
					"[!] Could not create a key pair on default directory. Please manually type a path to store your keys.");
			keyDirectory = Simple.strInput(100);
			try {
				System.out.println("[! Attempting to create a key pair again...");
				Encryption.storeKeys(keyDirectory, Encryption.generateKeyPair());
				System.out.println("[!] Key pair created sucessfully on " + keyDirectory);
			} catch (Exception ex) {
				System.out.println("[X] " + e.getMessage());
				System.out.println(
						"[X] Could not create a key pair on this directory. Check your drive or filesystem for any errors and try again later.");
				error = true;
				Simple.pause();
			}
		}
	}

	private boolean readBlockChainKey() {
		try {
			Encryption.readBlockchainKey(keyDirectory);
			System.out.println("[!] Blockchain key found!");
			return true;
		} catch (Exception e) {
			System.out.println("[X] Blockchain key not available, attempting to create one.");
			try {
				Encryption.generateBlockchainKey(keyDirectory);
				System.out.println("[!] Blockchain key created!");
				return true;
			} catch (Exception ex) {
				System.out.println("[X]" + ex.getMessage());
				Simple.pause();
				return false;
			}

		}
	}

	private void printLoading(String message) {
		Simple.logo();
		Simple.printWithSpace(message, 43);
		System.out.println("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▀▀▀░░░░░░░░░░░░░░░░░░░░░░░░░░");
		Simple.cooldown();
	}

	public Load() {
		printLoading("0% Loading default directory...");
		loadDefaultDirectory();

		if (error) return;
		printLoading("25% Searching for user keys...");
		if (loadUserKeypair()) {
			printLoading("40% Your keys were found!");
		} else {
			printLoading("40% Keys not found, creating a key pair...");
			createUserKeypair();
		}

		if (error) return;
		printLoading("50% Searching for Blockchain key...");
		readBlockChainKey();

		if (error) return;
		printLoading("75% Loading Blockchain...");
		blockchain = App.loadBlockchain(this.blockDirectory);

		if (error) return;
	}
}
