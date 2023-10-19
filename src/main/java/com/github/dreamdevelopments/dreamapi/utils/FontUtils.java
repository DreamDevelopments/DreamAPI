package com.github.dreamdevelopments.dreamapi.utils;

import java.util.HashMap;

public class FontUtils {

    private final static HashMap<Character, Character> normalToSmallCap;

    static {
        normalToSmallCap = new HashMap<>();
        char[] normalAlphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] smallCapAlphabet = new char[]{
                0x1D00, 0x0299, 0x1D04, 0x1D05, 0x1D07, 0xA730, 0x0262, 0x029C, 0x026A, 0x1D0A,
                0x1D0B, 0x029F, 0x1D0D, 0x0274, 0x1D0F, 0x1D18, 0x01EB, 0x0280, 0x0455, 0x1D1B,
                0x1D1C, 0x1D20, 0x1D21, 0x0445, 0x028F, 0x1D22
        };
        for(int i = 0; i < normalAlphabet.length; i++) {
            normalToSmallCap.put(normalAlphabet[i], smallCapAlphabet[i]);
        }
    }
    public static String toSmallCap(String string) {
        char[] newString = string.toLowerCase().toCharArray();
        for(int i = 0; i < newString.length; i++)
            if(normalToSmallCap.containsKey(newString[i]))
                newString[i] = normalToSmallCap.get(newString[i]);
        return String.valueOf(newString);
    }

}
