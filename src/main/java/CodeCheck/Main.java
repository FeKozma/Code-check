package CodeCheck;

public class Main {
    public static void main(String[] args) throws Exception {
        Log.log("Starting up...");
        Log.log("Running on %s.".formatted(System.getProperty("os.name")));

        CodeCheck.execute();

        Log.log("Shutting down...");
    }
}