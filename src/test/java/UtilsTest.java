import com.github.dreamdevelopments.dreamapi.messages.utils.TextConverter;
import com.github.dreamdevelopments.dreamapi.utils.ColorUtils;
import com.github.dreamdevelopments.dreamapi.utils.FontUtils;
import com.github.dreamdevelopments.dreamapi.utils.TextUtils;

import org.bukkit.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class UtilsTest {

    @Test
    public void testTextUtils() {
        String formattedEnumName = TextUtils.formatEnumName("TEST_eNuM_Name", true);
        Assertions.assertEquals("TestEnumName", formattedEnumName);

        formattedEnumName = TextUtils.formatEnumName("TEST_eNuM_Name", false);
        Assertions.assertEquals("testEnumName", formattedEnumName);

        String formattedTime = TextUtils.formatTime(138);
        Assertions.assertEquals("02:18", formattedTime);

        formattedTime = TextUtils.formatTime(0);
        Assertions.assertEquals("00:00", formattedTime);

        formattedTime = TextUtils.formatTime(60);
        Assertions.assertEquals("01:00", formattedTime);

        formattedTime = TextUtils.formatTime(3600);
        Assertions.assertEquals("60:00", formattedTime);
    }

    @Test
    public void testFontUtils() {
        String fontMessage = FontUtils.toSmallCap("Test MessAgE :)");
        Assertions.assertEquals(String.valueOf(new char[]{0x1D1B, 0x1D07, 0x0455, 0x1D1B, ' ', 0x1D0D, 0x1D07, 0x0455, 0x0455, 0x1D00, 0x0262, 0x1D07, ' ', ':', ')'}), fontMessage);
    }

    @Test
    public void testTextConvertor() {
        String convertedText = TextConverter.legacyToModern("&c&lTest &r&cMessage");
        Assertions.assertEquals("<red><bold>Test <reset><red>Message", convertedText);

        convertedText = TextConverter.modernToLegacy("<red><bold>Test</bold> <red>Message");
        Assertions.assertEquals("&c&lTest &cMessage", convertedText);
    }

    @Test
    public void testColorUtils() {
        Color color = ColorUtils.colorFromHex("#ffabcd");
        Assertions.assertEquals(255, color.getRed());
        Assertions.assertEquals(171, color.getGreen());
        Assertions.assertEquals(205, color.getBlue());

        color = ColorUtils.colorFromHex("#ffffff");
        Assertions.assertEquals(Color.WHITE, color);
    }

}
