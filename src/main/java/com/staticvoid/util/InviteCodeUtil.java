package com.staticvoid.util;

import org.apache.commons.lang3.RandomStringUtils;

public class InviteCodeUtil {

    private static final int CODE_LENGTH = 8;

    public static final String generateCode() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public static final String[] generateCodes(int numberOfCodes) {
        String[] codes = new String[numberOfCodes];
        for (int i = 0; i < numberOfCodes; i++) {
            codes[i] = generateCode();
        }
        return codes;
    }
}
