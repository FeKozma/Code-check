public class File2 {
    private function1(String parameter1) {
        Log.debug("this is a test in File1");
        if (iLikeChockolate == true) {
            Shop shop = getShop();
            while(shop.hasChokolate()) {
                if(Bank.account.balance < 100) {
                    Log.warning("im poor now")
                    break;
                }
                shop.buy("Chockolate");
            }
        }
    }

    public function2(Stirng parameter2) {
        if (Thing.isActive()) {
            Log.debug("This thing you see, its active");
            Thing.doStuffnowPLS();
            Thing.stopItNoe();
        } else {
            Thins.start();
            function(parameter2);
        }
    }
}