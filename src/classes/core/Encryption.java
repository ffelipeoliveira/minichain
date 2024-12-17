package core;

import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.File;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.Key;

public class Encryption {

	//Get the SHA 256 code from a string or object
	public static String hashCode(String str) {
		try {
			var md = MessageDigest.getInstance("SHA-256");
			md.update(str.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(md.digest()).toString();
		} catch (Exception e) {
			return null;
		}
	}

	//Get the hash of a public Key
	public static String calculateKeyHash(PublicKey publicKey) throws Exception {
		var md = MessageDigest.getInstance("SHA-256");
		md.update(publicKey.toString().getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(md.digest());
	}

	// Can generate a key pair
	// A key pair consists of a Public key (which anybody can see) and a private key (should be kept private)
	// It uses EC encryption algorithm, the same one from Bitcoin (I guess)
	public static KeyPair generateKeyPair() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		keyPairGenerator.initialize(256, random);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}

	// Can store a key pair
	// It will store in separate files, to simulate what happens in a real Blockchain
	// Also the default folder is ./keys/ every key must be stored in it
	public static void storeKeys(String defaultFolder, KeyPair keyPair)  throws Exception {
		FileOutputStream fos = new FileOutputStream(defaultFolder + "myPublic.key");
		fos.write(keyPair.getPublic().getEncoded());
		fos.close();
		fos = new FileOutputStream(defaultFolder + "myPrivate.key");
		fos.write(keyPair.getPrivate().getEncoded());
		fos.close();
	}

	// Overloading of the storeKey method for generating more than one pair of keys.
	// With this you can store keys with different names
	// It also separates the key in private and public keys
	public static void storeKeys(String defaultFolder, KeyPair keyPair, String fileName)  throws Exception {
		FileOutputStream fos = new FileOutputStream(defaultFolder + fileName + "Public.key");
		fos.write(keyPair.getPublic().getEncoded());
		fos.close();
		fos = new FileOutputStream(defaultFolder + fileName  + "Private.key");
		fos.write(keyPair.getPrivate().getEncoded());
		fos.close();
	}

	// Can read a Public key file
	public static PublicKey readPublicKey(String defaultFolder) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		File publicKeyFile = new File(defaultFolder + "myPublic.key");
		byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
		KeyFactory keyFactory = KeyFactory.getInstance("EC");
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		return keyFactory.generatePublic(publicKeySpec);
	}

	// Overloading of readPublicKey with filename support
	public static PublicKey readPublicKey(String defaultFolder, String fileName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		File publicKeyFile = new File(defaultFolder + fileName);
		byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
		KeyFactory keyFactory = KeyFactory.getInstance("EC");
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		return keyFactory.generatePublic(publicKeySpec);
	}

	// Can read the default BlockchainKey
	public static PublicKey readBlockchainKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		File publicKeyFile = new File("BlockchainPublic.key");
		byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
		KeyFactory keyFactory = KeyFactory.getInstance("EC");
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		return keyFactory.generatePublic(publicKeySpec);
	}

	// Can read a private key file
	public static PrivateKey readPrivateKey(String defaultFolder) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		File privateKeyFile = new File(defaultFolder + "myPrivate.key");
		byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
		KeyFactory keyFactory = KeyFactory.getInstance("EC");
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		return keyFactory.generatePrivate(privateKeySpec);
	}

	// Overload of readPrivateKey() with filename support
	public static PrivateKey readPrivateKey(String defaultFolder, String fileName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		File privateKeyFile = new File(defaultFolder + fileName);
		byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
		KeyFactory keyFactory = KeyFactory.getInstance("EC");
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		return keyFactory.generatePrivate(privateKeySpec);
	}

	// Can encrypt a string using a public key
	public static String encryptString(Key publicKey, String string) throws Exception {
		Cipher encryptCipher = Cipher.getInstance("SHA256withECDSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return Base64.getEncoder().encodeToString(encryptCipher.doFinal(string.getBytes(StandardCharsets.UTF_8)));
	}

	// Can decrypt a string using a private key
	public static String decryptString(Key privateKey, String string) throws Exception {
		Cipher decryptCipher = Cipher.getInstance("SHA256withECDSA");
		decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String( decryptCipher.doFinal(string.getBytes()), StandardCharsets.UTF_8);
	}

	// Can sign a transaction using a private key
	public static String signTransaction(String transactionData,  PrivateKey privateKey) throws Exception {
		Signature signature = Signature.getInstance("SHA256withECDSA");
		signature.initSign(privateKey);
		signature.update(transactionData.getBytes("UTF-8"));
		byte[] signed = signature.sign();
		return Base64.getEncoder().encodeToString(signed);
	}

	// can verify a signature using a public key
	public static boolean verifySignature(String transaction, String signature, PublicKey publicKey) throws Exception {
		Signature verifier = Signature.getInstance("SHA256withECDSA");
		verifier.initVerify(publicKey);
		verifier.update(transaction.getBytes("UTF-8"));
		byte[] decodedSignature = Base64.getDecoder().decode(signature);
		return verifier.verify(decodedSignature);
	}
}

