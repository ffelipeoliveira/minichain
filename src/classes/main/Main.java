package main;

import io.Menu;
import other.Load;
import other.Log;

public class Main {
    public static void main(String[] args) {
        Boolean loop = true;
        Menu m = new Menu();
        Log log = new Log();
        Load data = new Load();

        if (data.error)
            return;

        // Main loop
        while (loop) {
            loop = m.menu(data.directory, data.keyDirectory, data.blockDirectory, data.blockchain,
                    log);
        }
    }

}
