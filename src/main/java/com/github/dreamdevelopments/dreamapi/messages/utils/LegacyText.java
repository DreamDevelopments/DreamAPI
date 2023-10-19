package com.github.dreamdevelopments.dreamapi.messages.utils;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;

/**
 * Utility class for managing legacy color codes (&a) and hex codes (#FFFFFF)
 */
public class LegacyText {

    /**
     * Replaces all legacy color codes and hex codes in a string with ChatColor
     * @param string The string to replace the color codes in
     * @return The string with the color codes replaced
     */
    @NotNull
    public static String replaceAllColorCodes(@Nullable String string) {
        if(string == null)
            return "";
        return ChatColor.translateAlternateColorCodes('&', translateHexColorCodes("#", "", string));
    }

    /**
     * Replaces all hex codes in a string with ChatColor
     * @param startTag The start tag of the hex code (Example: "#")
     * @param endTag The end tag of the hex code; can be empty
     * @param message The string to replace the hex colors in
     * @return The string with the hex colors replaced
     */
    @NotNull
    public static String translateHexColorCodes(@NotNull String startTag, @NotNull String endTag, @NotNull String message)
    {
        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

}
