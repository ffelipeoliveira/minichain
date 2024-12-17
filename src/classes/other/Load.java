package other;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.filechooser.FileSystemView;

import core.Encryption;
import io.Simple;

public class Load {
	public final String DEFAULT_DIRECTORY;
	public boolean error = false;

	private static String loadDefaultDirectory() {
		String pathToDocuments = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		try {
			File directory = new File(pathToDocuments + "/minichain_keys/");
   			if (!directory.exists()){
        		directory.mkdir();
    		}
			FileOutputStream fos = new FileOutputStream(pathToDocuments + "/minichain_keys/" + "store_your_keys_here.txt");
			fos.close();
			return(pathToDocuments + "/minichain_keys/");
		} catch (Exception e) {
			Simple.banner("Loading error");
			System.out.println("[X] " + e.getMessage());
			System.out.println("[!] Could not load default directory. Please manually type a path to store your keys.");
			return Simple.strInput(100);
		}
	}

	private static boolean loadUserKeypair(String defaultDirectory) {
		try {
			Encryption.readPrivateKey(defaultDirectory);
			Encryption.readPublicKey(defaultDirectory);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static String createUserKeypair(String defaultDirectory) {
		try {
			Encryption.storeKeys(defaultDirectory, Encryption.generateKeyPair());
			System.out.println("[!] Key pair created sucessfully on " + defaultDirectory);
			return defaultDirectory;
		} catch (Exception e) {
			Simple.banner("Loading error");
			System.out.println("[X] " + e.getMessage());
			System.out.println("[!] Could not create a key pair on default directory. Please manually type a path to store your keys.");
			defaultDirectory = Simple.strInput(100);
			try {
				System.out.println("[! Attempting to create a key pair again...");
				Encryption.storeKeys(defaultDirectory, Encryption.generateKeyPair());
				System.out.println("[!] Key pair created sucessfully on " + defaultDirectory);
				return defaultDirectory;
			} catch (Exception ex) {
				System.out.println("[X] " + e.getMessage());
				System.out.println("[X] Could not create a key pair on this directory. Check your drive or filesystem for any errors and try again later.");
				Simple.pause();
				return null;
			}
		}
	}

	private static boolean readBlockChainKey() {
		try {
			if(Encryption.hashCode(Encryption.readBlockchainKey().toString()).equals("MRaAMtC9rImuyGgFzLUhj7Je2nYKFbp/f4D7szdysL0=")) {
				System.out.println("[!] Blockchain key found!");
				return true;
			}
			else {
				System.out.println("[X] Blockchain key adultered, download it from the GitHub repository.");
				Simple.pause();
				return false;
			}
		} catch (Exception e) {
			System.out.println("[X] " + e.getMessage());
			System.out.println("[X] Blockchain key not available, download it from the GitHub repository.");
			Simple.pause();
			return false;
		}
	}

	public Load() {
		Simple.logo();
		Simple.printWithSpace("0% Loading default directory", 43);
		System.out.println("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▀▀▀░░░░░░░░░░░░░░░░░░░░░░░░░░");
		String defaultDirectory = loadDefaultDirectory();

		Simple.cooldown();

		Simple.logo();
		Simple.printWithSpace("25% Searching for user keys.", 43);
		System.out.println("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▀▀▀░░░░░░░░░░░░░░░░░░░░░░░░░░");
		if (loadUserKeypair(defaultDirectory)) {
			System.out.println("[!] Your keys were found!");
			DEFAULT_DIRECTORY = defaultDirectory;
		}
		else {
			Simple.printWithSpace("[!] Keys not found, creating a key pair...", 43);
			System.out.println("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▀▀▀░░░░░░░░░░░░░░░░░░░░░░░░░░");
			DEFAULT_DIRECTORY = createUserKeypair(defaultDirectory);
			if (DEFAULT_DIRECTORY == null) {
				error = true;
				return;
			}
		}

		Simple.cooldown();

		Simple.logo();
		Simple.printWithSpace("50% Searching for Blockchain key.", 43);
		System.out.println("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▀▀▀░░░░░░░░░░░░░░░░░░░░░░░░░░");
		if(!readBlockChainKey()) {
			error = true;
			return;
		}

		Simple.cooldown();
	}
}
