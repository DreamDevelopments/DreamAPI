import com.github.dreamdevelopments.dreamapi.messages.utils.TextConverter;
import com.github.dreamdevelopments.dreamapi.utils.FontUtils;
import com.github.dreamdevelopments.dreamapi.utils.TextUtils;

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
}
