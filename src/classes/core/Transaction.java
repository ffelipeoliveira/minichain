package core;

import java.security.PublicKey;
import java.io.Serializable;

public class Transaction implements Serializable{
	private final Transaction previous;
	private String data;
	private String signature;
	private PublicKey senderPublicKey;
	private PublicKey receiverPublicKey;
	private float tokenAmount;
	private boolean isFee;
	private final long timestamp;

	public Transaction(Transaction previous, PublicKey senderPublicKey, String signature, String data) {
		this.previous = previous;
		this.senderPublicKey = senderPublicKey;
		this.signature = signature;
		this.timestamp = System.currentTimeMillis();
	}

	public Transaction(Transaction previous, PublicKey senderPublicKey, PublicKey receiverPublicKey, float tokenAmount,
			String signature, boolean isFee) {
		this.previous = previous;
		this.senderPublicKey = senderPublicKey;
		this.receiverPublicKey = receiverPublicKey;
		this.tokenAmount = tokenAmount;
		this.signature = signature;
		this.timestamp = System.currentTimeMillis();
	}

	public Transaction(PublicKey senderPublicKey, PublicKey receiverPublicKey, float tokenAmount, String signature) {
		this.previous = null;
		this.senderPublicKey = senderPublicKey;
		this.receiverPublicKey = receiverPublicKey;
		this.tokenAmount = tokenAmount;
		this.signature = signature;
		this.timestamp = System.currentTimeMillis();
	}

	public Transaction(Transaction previous, String data) {
		this.previous = previous;
		this.data = data;
		this.timestamp = System.currentTimeMillis();
	}

	public Transaction(String data) {
		this.previous = null;
		this.data = data;
		this.timestamp = System.currentTimeMillis();
	}

	public Transaction getPrevious() {
		return previous;
	}

	public PublicKey getSenderPublicKey() {
		return senderPublicKey;
	}

	public PublicKey getReceiverPublicKey() {
		return receiverPublicKey;
	}

	public float getTokenAmount() {
		return tokenAmount;
	}

	public String getSignature() {
		return signature;
	}

	public boolean isFee() {
		return isFee;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		if (data == null)
			return ("(" + (tokenAmount - tokenAmount / 100) + ") " + senderPublicKey.hashCode() + "=>"
					+ receiverPublicKey.hashCode() + timestamp);
		else
			return data + timestamp;
	}
}
