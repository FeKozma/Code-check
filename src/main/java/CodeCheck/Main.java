package CodeCheck;

public class Main {
    public static void main(String[] args) throws Exception {
        Log.log("Starting up...");
        Log.log("Running on %s.", "\n");

        CodeCheck.execute();

        Log.log("Shutting down...");
    }
}