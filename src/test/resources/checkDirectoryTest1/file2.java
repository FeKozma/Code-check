public class File2 {
    private void function1(String parameter1) {
        Log.debug("This is a test in File1...");
        if (Chocolate == true) {
            Shop shop = getShop();
            while (shop.hasChocolate()) {
                if (Bank.account.balance < 100) {
                    Log.warning("I'm poor now. :(");
                    break;
                }
                shop.buy("Chocolate");
            }
        }
    }

    public void function2(Stirng parameter2) {
        if (Thing.isActive()) {
            Log.debug("This thing you see -- its active!");
            Thing.doStuffnowPLS();
            Thing.stopItNow();
        } else {
            Thing.start();
            function(parameter2);
        }
    }
}