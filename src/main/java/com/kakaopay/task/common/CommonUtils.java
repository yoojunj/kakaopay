package com.kakaopay.task.common;

public class CommonUtils {

    public static String getRemainingSpaces(final String target, int maxLength) {

        String spaces = "";
        for(int i = 0; i < maxLength - target.length(); i++) {
            spaces += " ";
        }

        return spaces;
    }
}
