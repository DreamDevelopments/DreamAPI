package com.github.dreamdevelopments.dreamapi.utils;

import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class TextUtils {

    @NotNull
    public static String formatEnumName(@NotNull String enumName, boolean capitalize) {
        return formatEnumName(enumName, "", capitalize);
    }

    @NotNull
    public static String formatEnumName(@NotNull String enumName, String wordDivider, boolean capitalize) {
        char[] name = enumName.toLowerCase().toCharArray();
        StringBuilder newString = new StringBuilder();
        newString.append(capitalize ? Character.toUpperCase(name[0]) : name[0]);
        for(int i = 1; i < name.length; i++)
            newString.append(name[i] == '_' ? wordDivider + Character.toUpperCase(name[++i]) : name[i]);
        return newString.toString();
    }

    /**
     * Formats a time in seconds to a string
     * @param seconds The time in seconds
     * @return The formatted time (mm:ss)
     */
    @NotNull
    public static String formatTime(int seconds) {
        int minutes = seconds/60;
        seconds = seconds%60;
        return (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    }

    private static final String[] romanNumerals = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    private static final int[] romanNumeralsValues = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

    /**
     * Converts a number to a roman numeral
     * @param number The number to convert
     * @return The roman numeral
     */
    @NotNull
    public static String romanNumeral(int number) {
        if(number <= 0)
            throw new IllegalArgumentException("Number has to be higher than 0");
        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < romanNumeralsValues.length; i++) {
            int times = number / romanNumeralsValues[i];
            number %= romanNumeralsValues[i];

            for (int j = 0; j < times; j++) {
                roman.append(romanNumerals[i]);
            }
        }
        return roman.toString();
    }

}
