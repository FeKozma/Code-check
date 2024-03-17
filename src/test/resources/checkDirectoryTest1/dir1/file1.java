public class File1 {
    private void function1(String parameter1) {
        Log.debug("This is a test in File1...");
        if (iLikeChocolate == true) {
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
}