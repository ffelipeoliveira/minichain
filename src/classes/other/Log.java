package other;

public class Log {
    private static final int MAX_LOG = 5;
    private int length = 0;
    private String[][] log = new String[MAX_LOG + 1][];

    double start; // Used to calculate the time spent on a method
    double finish;

    public void addToLog(String Operation, double timeElapsed) {
        int counter = 0;
        if (length < MAX_LOG) {
            for (int i = 0; i < MAX_LOG; i++) {
                if (log[i] == null) {
                    log[i] = new String[3];
                    log[i][0] = Operation;
                    log[i][1] = ("" + timeElapsed);
                    length++;
                    return;
                }
            }
        } else {
            for (int i = 0; i < MAX_LOG - 1; i++) {
                log[i] = log[i + 1];
                counter = i;
            }
            log[counter + 1] = new String[3];
            log[counter + 1][0] = Operation;
            log[counter + 1][1] = ("" + timeElapsed);
            length++;
        }
    }

    public void printLog() {
        if (length > 0) {
            System.out.println(length + " results found.");
            if (length > MAX_LOG)
                System.out.println("Showing the latest " + (MAX_LOG) + " results.");
            for (int i = 0; i <= MAX_LOG; i++) {
                if (log[i] == null)
                    return;
                else
                    System.out.println("[" + (i + 1) + "]: " + log[i][0] + ", time elapsed:  " + log[i][1]);
            }
        } else {
            System.out.println("[!] No operations found.");
        }
    }

    // Log's auxiliary functions ################################

    public void start() {
        start = System.nanoTime();
    }

    // Return the time elapsed from the start to end
    public double finish() {
        finish = System.nanoTime();
        return finish - start;
    }
}
