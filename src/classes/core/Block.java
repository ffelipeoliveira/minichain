package core;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import io.Simple;

public class Block {
	private int id;
	private double nonce;
	private String hash, previousHash;
	private Block previous;
	private Transaction[] data;
	private final long timestamp;

	public Block(Block previous, int id, double nonce, Transaction[] data) {
		this.previous = previous;
		this.nonce = nonce;
		this.id = id;
		this.data = data;
		this.timestamp = System.currentTimeMillis()/1000;

		try {
			var md = MessageDigest.getInstance("SHA-256");
			if (this.previous != null) {
				md.update(this.previous.toString().getBytes(StandardCharsets.UTF_8));
			 	this.previousHash = Base64.getEncoder().encodeToString(md.digest());
			}
			else this.previousHash = null;
			md.update(this.toString().getBytes(StandardCharsets.UTF_8));
			this.hash = Base64.getEncoder().encodeToString(md.digest());
			
		}
		catch (Exception e) {
			System.err.println(e);
			Simple.pause();
		}
	}

	public String getHash() {
		return this.hash;
	}

	public int getId() {
		return this.id;
	}

	public Block getPrevious() {
		return this.previous;
	}

	public Transaction[] getData() {
		return this.data;
	}

	public String getPrevHash() {
		return this.previousHash;
	}

	public Double getNonce() {
		return this.nonce;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		String converted = "";
		for (Transaction transaction : this.data)
			converted+=(transaction.toString());
		return (converted + this.id + this.nonce + this.previousHash);
	}

	// For testing ##########################################################

	public void setData(Transaction[] data) {
		this.data = data;
		try {
			this.hash = App.calculateBlockHash(this);
		} catch (Exception e) {
			System.err.println("[X] A error occourred: " + e.getMessage());
		}
	}

	public void setPrevious(Block block) {
		this.previous = block;
	}
}
