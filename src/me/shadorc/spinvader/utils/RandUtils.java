package me.shadorc.spinvader.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandUtils {

    public static int randInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public static int randInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(max - min) + min;
    }

    public static float randFloat(float bound) {
        return ThreadLocalRandom.current().nextFloat() * bound;
    }

    public static boolean randBool() {
        return ThreadLocalRandom.current().nextBoolean();
    }

}
