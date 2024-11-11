package classes.core;

import java.security.PublicKey;

public class Transaction {
	private final Transaction previous;
	private String data;
	private String signature;
	private PublicKey senderPublicKey;
	private PublicKey receiverPublicKey;
	private float tokenAmount;


	public Transaction(Transaction previous, PublicKey senderPublicKey, PublicKey receiverPublicKey, float tokenAmount, String signature) {
		this.previous = previous;
		this.senderPublicKey = senderPublicKey;
		this.receiverPublicKey = receiverPublicKey;
		this.tokenAmount = tokenAmount;
		this.signature = signature;
	}

	public Transaction(PublicKey senderPublicKey, PublicKey receiverPublicKey, float tokenAmount, String signature) {
		this.previous = null;
		this.senderPublicKey = senderPublicKey;
		this.receiverPublicKey = receiverPublicKey;
		this.tokenAmount = tokenAmount;
		this.signature = signature;
	}

	public Transaction(Transaction previous, String data) {
		this.previous = previous;
		this.data = data;
	}

	public Transaction(String data) {
		this.previous = null;
		this.data = data;
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

	@Override
	public String toString() {
		if (data == null) return ("$[(" + tokenAmount + ") " + senderPublicKey.hashCode() + "]=>[" + receiverPublicKey.hashCode() + "]");
		else return data;
	}
}
