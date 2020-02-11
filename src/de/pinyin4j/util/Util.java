package de.pinyin4j.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 * A class that offers some general static utility methods. Instantiation of this class is not
 * intended.
 */
public final class Util {

    // ban creation
    private Util() { }

    private static final Random RANDOM = new Random();

    // TODO document
    public static <T> HashSet<T> hashSetOf(T... entries) {
        final HashSet<T> result = new HashSet<T>(entries.length * 2 + 1);
        result.addAll(Arrays.asList(entries));
        return result;
    }
}
