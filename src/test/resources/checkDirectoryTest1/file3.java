public class File2 {
    private void function1(String parameter1) {
        Log.debug("this is a test in File3");
        // and it looks similar to function1 in file2
        if (iLikeChockolate == true) { // but with some more comments
            Shop shop = getShop();
            while(shop.hasChokolate()) {
                if(Bank.account.balance < 200) { // and a diffrent line
                    Log.warning("and more logging");
                    Log.warning("im poor now");
                    break;
                }
                shop.buy("Chockolate");
            }
        }
    }
}