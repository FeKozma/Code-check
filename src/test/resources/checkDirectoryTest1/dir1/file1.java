public class File1 {
    private void function1(String parameter1) {
        Log.debug("this is a test in File1");
        if (iLikeChockolate == true) {
            Shop shop = getShop();
            while(shop.hasChokolate()) {
                if(Bank.account.balance < 100) {
                    Log.warning("im poor now");
                    break;
                }
                shop.buy("Chockolate");
            }
        }
    }
}