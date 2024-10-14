package core;

public class Block {
	private int id, hash, prevHash;
	private Block prev;
	private String[] data;

	public Block(Block prev, int id, String[] data) {
		this.prev = prev;
		prevHash = BlockChain.getBlockHash(prev);
		this.id = id;
		this.data = data;
		hash = BlockChain.getBlockHash(this);
	}

	public Block(int id, String[] data) {
		prev = null;
		prevHash = 0;
		this.id = id;
		this.data = data;
		hash = BlockChain.getBlockHash(this);
	}

	public int getHash() {
		return hash;
	}

	public int getId() {
		return id;
	}

	public Block getPrev() {
		return prev;
	}

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) { // dangerous
		this.data = data;
		hash = BlockChain.getBlockHash(this);
	}

	public void recalculateHash() {
		prevHash = BlockChain.getBlockHash(prev);
		hash = BlockChain.getBlockHash(this);
	}

	public int getPrevHash() {
		return prevHash;
	}

	@Override
	public String toString() {
		return (BlockChain.StrArrayToStr(data, "") + id + prevHash + prev);
	}
}
