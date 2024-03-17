public class File2 {
    private void function1(String parameter1) {
        Log.debug("This is a test in File3");
        // And it looks similar to function1 in file2.
        if (Chocolate == true) { // But with some more comments. =)
            Shop shop = getShop();
            while (shop.hasChocolate()) {
                if (Bank.account.balance < 200) { // And a diffrent line. :O
                    Log.warning("More logging, however...");
                    Log.warning("I'm poor now. :(");
                    Log.warning("Also, the lines with \"Log\" or a comment will be ignored.");
                    break;
                    Log.info("This will never happen.");
                }
                shop.buy("Chocolate");
            }
        }
    }
}