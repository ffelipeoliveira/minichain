package main;

import core.BlockChain;
import io.Menu;
import other.Log;

public class Main {
	public static void main(String[] args) {
        Boolean loop = true;
        BlockChain blockChain = new BlockChain();
        Log log = new Log();
        
        while(loop) {
            loop = Menu.menu(blockChain, log);
        }
	}

}
