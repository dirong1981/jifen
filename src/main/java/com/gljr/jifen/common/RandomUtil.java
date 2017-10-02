package com.gljr.jifen.common;

import java.math.BigDecimal;
import java.util.Random;

public class RandomUtil {

    private static Random getRandom() {
        return java.util.concurrent.ThreadLocalRandom.current();
    }

    public static long nextLong(int length) {
        int min = new BigDecimal(Math.pow(10, length - 1)).intValue();
        int max = new BigDecimal(Math.pow(10, length)).intValue() - 1;
        return (long) (min + ((max - min) * getRandom().nextDouble()));
    }

}
