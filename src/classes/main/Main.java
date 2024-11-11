package classes.main;

import classes.core.BlockChain;
import classes.io.Menu;
import classes.other.Log;

public class Main {
	public static void main(String[] args) {
        Boolean loop = true;
        BlockChain blockChain = new BlockChain();
        Menu m = new Menu();
        Log log = new Log();
        
        // Main loop
        while(loop) {
            loop = m.menu(blockChain, log);
        }
	}

}
