package com.whr.sell.util;

import java.util.Random;

public class KeyUtils {
    public static synchronized String genUniqueKey(){
        Random rdm=new Random();
        final int number = rdm.nextInt(900000) + 100000;
        return System.currentTimeMillis()+String.valueOf(number);
    }

}
