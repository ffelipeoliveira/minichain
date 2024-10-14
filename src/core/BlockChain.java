package core;

public class BlockChain {
	private static final String DEFAULT_GENESIS_TEXT = "The Times 03/Jan/2009 Chancellor on brink of second bailout for banks";
	Block head;
	int counter;

	public BlockChain() { // Will create a Genesis Block on instance using default text
		String[] genesisData = new String[1];
		genesisData[0] = DEFAULT_GENESIS_TEXT;
		head = new Block(0, genesisData);
		counter = 1;
	}

	public BlockChain(String[] genesisData) { // Will create a Genesis Block on instance receiving data
		head = new Block(1, genesisData);
		counter = 1;
	}

	//Basic BlockChain Functions ########################################

	public void addBlock(int id, String[] data) { // Will set a new Block as the head
		head = new Block(head, id, data);
	}

	public void addBlock(String[] data) { // Will set a new Block as the head
		head = new Block(head, counter, data);
		counter++;
	}

	public Block searchBlock(int id) {
		Block current = head;
		for (int i = 0; (i < this.length() && current != null); i++) {
			if (current.getId() == id) return current;
			current = current.getPrev();
		}

		return null;
	}

	public int length() {
		int counter = 1; //Every Blockchain will have at least a block by default
		Block aux = head;
		while (aux.getPrev() != null) {
			aux = aux.getPrev(); 
			counter++;
		}
		return counter;
	}

	public void validateBlockChain() throws Exception{ //Throws exception if blockChain isn't valid
		Block aux = head;
		while (aux.getPrev() != null) { 
			if (!validateLink(aux, aux.getPrev())) {
				throw new Exception("[X] Invalid BlockChain. Hash " + getBlockHash(aux.getPrev()) + " from block " + aux.getPrev().getId() + " doesn't correspond to the expected hash " + aux.getPrevHash() + ". Please Reset your BlockChain.");
			}
			aux = aux.getPrev();
		}
	}

	public void reset() {
		String[] genesisData = new String[1];
		genesisData[0] = DEFAULT_GENESIS_TEXT;
		head = new Block(0, genesisData);
		counter = 1;
	}

    //Basic Math Functions ##############################################

	public static int getRandomNumber(int min, int max) { //Will take two number, the min and max values, and return a random number
		return (int) ((Math.random() * (max - min)) + min);
	}

    public static int getNumeralQtt(int number) { //Will take a number and return the quantity of numerals on it
        if (!(number == 0)) {
            return (int) Math.floor(Math.log10(Math.abs(number))) + 1;
        }
        return 1;
    }

	// Block validation ##############################################

	public static boolean validateLink(Block block, Block previousBlock) { //Will check if the previous block Hash is valid
		if(block.getPrevHash() == getBlockHash(previousBlock)) return true; 
		return false;
	}

	public static int getBlockHash(Block block) {
		return block.toString().hashCode();
	}

	public static String StrArrayToStr(String[] strArr, String delimiter) {
		String converted = "";
		for (String str : strArr)
			converted+=(str+delimiter);
		return converted;
	}

	public Block getHead() {
		return head;
	}
}

