package other;

public class Log {
	private static final int MAX_LOG = 5;
	private int length = 0;
	private String[][] log = new String[MAX_LOG + 1][];

    public void addToLog(String Operation, int id, int hash) {
        int counter = 0;
        if(length < MAX_LOG) {
            for (int i = 0; i < MAX_LOG; i++) {
                if(log[i] == null) {
                    log[i] = new String[3];
                    log[i][0] = Operation;
                    log[i][1] = ("" + id);
                    log[i][2] = ("" + hash);
                    length++;
                    return;
                }
            }
        }
        else {
            for (int i = 0; i < MAX_LOG - 1; i++) {
                log[i] = log[i+1];
                counter = i;
            }
            log[counter + 1] =  new String[3];
            log[counter + 1][0] = Operation;
            log[counter + 1][1] = ("" + id);
            log[counter + 1][2] = ("" + hash);
            length++;
        }
    }


	public void printLog() {
        if(length > 0) {
            System.out.println(length + " results found.");
            if (length > MAX_LOG) System.out.println("Showing the latest " + (MAX_LOG) + " results.");
            for (int i = 0; i <= MAX_LOG; i++) {
                if(log[i] == null) return;
                else System.out.println((i + 1) + ": " + log[i][0] + " block " + log[i][1] + ", hash " + log[i][2] + ".");
            }
        }
        else {
            System.out.println("[!] No operations found.");
        }
    }
}
