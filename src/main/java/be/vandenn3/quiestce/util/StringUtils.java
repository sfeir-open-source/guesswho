package be.vandenn3.quiestce.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;

public class StringUtils {
    public static String secureRandomAlphanum(int count) {
        return RandomStringUtils.random(count, 0, 0, true, true, null, new SecureRandom());
    }
}
