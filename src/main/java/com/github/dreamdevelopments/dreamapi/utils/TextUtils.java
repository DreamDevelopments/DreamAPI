package com.github.dreamdevelopments.dreamapi.utils;

import org.jetbrains.annotations.NotNull;

public class TextUtils {

    @NotNull
    public static String formatEnumName(@NotNull String enumName, boolean capitalize) {
        char[] name = enumName.toLowerCase().toCharArray();
        StringBuilder newString = new StringBuilder();
        newString.append(capitalize ? Character.toUpperCase(name[0]) : name[0]);
        for(int i = 1; i < name.length; i++)
            newString.append(name[i] == '_' ? Character.toUpperCase(name[++i]) : name[i]);
        return newString.toString();
    }

    @NotNull
    public static String formatTime(int seconds) {
        int minutes = seconds/60;
        seconds = seconds%60;
        return (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    }

}
