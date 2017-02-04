package shared;

public class ThreadHelper {
    public static void waitFor(long timespan) {
        try {
            Thread.sleep(timespan);
        } catch (InterruptedException ie) {
            ie.printStackTrace(); }
    }
}
