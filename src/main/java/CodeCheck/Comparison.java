package CodeCheck;

public class Comparison {

    boolean equal;

    boolean similar;
    String arg;

    OneFunction oneFunction1;
    OneFunction oneFunction2;

    public Comparison(boolean equal, boolean similar, String arg) {
        this.equal = equal;
        this.similar = similar;
        this.arg = arg;
    }

    public Comparison() {
        this.equal = false;
        this.similar = false;
    }

    public Comparison(boolean equal, String arg) {
        this.equal = equal;
        this.arg = arg;
    }

    public Comparison setFunctions(OneFunction oneFunction1, OneFunction oneFunction2) {
        this.oneFunction1 = oneFunction1;
        this.oneFunction2 = oneFunction2;
        return this;
    }

    @Override
    public String toString() {
        return "{\"equal\"=" +equal+", \"similar\"="+similar+", \"arg\"= \"" + arg.replaceAll("\"", "\\\"") + "\", \"function1\"=\"" + oneFunction1.toString()+"\", \"function2\"=\"" + oneFunction2.toString()+"\"}";
    }
}
