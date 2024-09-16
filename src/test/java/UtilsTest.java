import com.github.dreamdevelopments.dreamapi.messages.utils.TextConverter;
import com.github.dreamdevelopments.dreamapi.utils.ColorUtils;
import com.github.dreamdevelopments.dreamapi.utils.FontUtils;
import com.github.dreamdevelopments.dreamapi.utils.TextUtils;

import org.bukkit.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


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

    @Test
    public void testRomanNumerals() {
        Assertions.assertEquals("V", TextUtils.romanNumeral(5));
        Assertions.assertEquals("IV", TextUtils.romanNumeral(4));
        Assertions.assertEquals("VI", TextUtils.romanNumeral(6));
        Assertions.assertEquals("II", TextUtils.romanNumeral(2));
        Assertions.assertEquals("XI", TextUtils.romanNumeral(11));


    }

    @Test
    public void testGoogle() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        /*URL url = new URL("https://currentmillis.com/time/minutes-since-unix-epoch.php");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        long minutes = Long.parseLong(in.readLine());
        in.close();
        con.disconnect();
        long epochSeconds = minutes * 60;
        System.out.println("SEONDS: " + epochSeconds + " : : : REAL: " + System.currentTimeMillis() / 1000);*/

        /*String publicKeyBase = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArgxbhPNjnJfyWZllEmefi72M+/yddcnZzi/xZGaPT5F8FC7iEz/PnzOHldi7gvQTOMbHKqk6vE0laEfezoA+Igh4uiojpvqE7lVovjd2OuY8s3xQXfkLMztq2/yIVRZPgBqD2zj+H0nnABdZ9Xo9Ql9mFkmFYZS1MuODzjP9yL5GNqTmWYjvjcbiavTIxO0vLGoSiRa8XzD0JDckwUgu6PbNiPwj9kTf5zd3eGlwibmNp45CP/uuSg0IEFKoKRkaRB8NTRPbu6TDFvGIR+YEahYyhHOuT5OhKgjQ6KjQnbEd0du4uaDWjSJFJDTn6xW4EbeYdXvpTQZcU0fm5E12eQIDAQAB";
        String privateKeyBase = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCuDFuE82Ocl/JZmWUSZ5+LvYz7/J11ydnOL/FkZo9PkXwULuITP8+fM4eV2LuC9BM4xscqqTq8TSVoR97OgD4iCHi6KiOm+oTuVWi+N3Y65jyzfFBd+QszO2rb/IhVFk+AGoPbOP4fSecAF1n1ej1CX2YWSYVhlLUy44POM/3IvkY2pOZZiO+NxuJq9MjE7S8sahKJFrxfMPQkNyTBSC7o9s2I/CP2RN/nN3d4aXCJuY2njkI/+65KDQgQUqgpGRpEHw1NE9u7pMMW8YhH5gRqFjKEc65Pk6EqCNDoqNCdsR3R27i5oNaNIkUkNOfrFbgRt5h1e+lNBlxTR+bkTXZ5AgMBAAECggEAEwr6FnVU0ETVuNTuOJHK5Iy0OLvedI1FvegD9tK72l5WH5UzmhyQuF/4dT414fGXis5A61uBuCmOrrayW5Cve6TGpMPIi9P8KGGC6NEDqBUZzh+LYYTGo2/cVL5wIjMjNqZwB9PYBg9dbWagqpmZy0HJ1yVrKQ6Otscq3CxXF5f/tLPuuIecVR8oMkkDd2LfrRw+F8DIzAzVlpX6O6ggT2iV56/FVUkx8ly8M5C2CQ2NJg7BuawDE/Q+QWMQA0sBi05GkORfANKn928b6ZgupvHVsJqDDNhdfdeqN/3GTgyeu7W+zM8rAsMZ+Wz2zxbnglE5h9KxzTXdUA8bxdI/QQKBgQDc3uZtMI1oR8tMLUdfxXxvnArPKfSvfFgtq+lPc1bcJeu2m5N48pekI6sUlFpqqjYpQ6y2Eksd91ABpGIygFU5YhYnUu2VXTz6eXbsD73DrECAg/nMSOBHflWyC0bOOrolHr9YfoHHjPMfsK3fjkXhGxpcIfsVHgiXuY/dTg4uqQKBgQDJuwIPQH+Kddv6uc4fNKjjH6Dt7/GsLP6q9usSEG71j55ZMre6ocNugxB7FgI4YbuLP2o9YxdXJd4YaSSoxYW1owuZMcHik0Gbqc+s4oXj10FCmLOW5ZKCqrZZcicSp7GFdc/CaLa1ZECdKJdt5YrTFO8/iaMUjq8iRFc86Sn7UQKBgEKBw/ThB9N8utSnVbSa7MtWu1KCGbG1yX+HImvQGTmVGZMU6aUjt1Gvg9ZgjZFMy55FSH5Sp9fQHoFAFGmMhx0OUglpSS0OtEvfw1V1r/h5p7qLAu7mitI8UmWYS6vRtzdyOL/W6BFEefjy5IhKePQ9iwRvHwHO11uIfq9uz4pxAoGATtb8dhOCBQ4M3A9z5DRDcQhyb6tf6ydmnFCGDHhJtUTc4QV2UsVXyMAGV+SBF6HylvhAwB8TBycwhp/JCQCxQgynwsZ0fOWKNZkHJg0dwK7mvW1KesXcHZ80Rv7bEGpjccJaYSNljLzSl9k+XV/hgp9i5ZUYHDercJv+8Do8AxECgYEAyRabXDUHVrRttEI10NBwjW4/0dAGr0WUB4VNWjul7XBpNpvuNiH+QgJAZfgYoh0RfPlfz9KMe1Xcp7kzMPVGatjJgVxGk47t1HjvML7d0m04joSet9PFCulMZ2CRLKyp91aefnJOQ1C1j2jDtYP2LoQsawOMGvLEWeVsXHL+PRc=";

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey1 = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyBase)));
        PrivateKey privateKey1 = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyBase)));

        String payload = "3bb6cf03-955b-4664-965b-9163ec6e31bf_1723111800";

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey1);
        signature.update(payload.getBytes());
        byte[] signedPayload = signature.sign();
        System.out.println("SIGNATURE: " + new String(Base64.getEncoder().encode(signedPayload)));

        byte[] signedPayload2 = Base64.getDecoder().decode("Q4IkNE/33BWmEoBAZmrdG/3WNin/pJmFTvmTwujIilTooGmXoWjbiz3Yj64txvDRM/5qcXFLJTcdhU/lMSiw/Ur9ZF2zclfQgTqi88pExZYMrz5pbfXVsBMSZP+LBF7qyME5C/OhZj932JUgsws0w6uMUfC+rxBMW8O4BviuUeKqSR6q84I+GabbPUMMyEh+UEd0KqsIFGgKCzub8YP0ZfkFVQ4woXfgpQWgYbms56v1cngp0xVEpjOt97ebdkYo3WfhvzBD1ytbHt5Vad43fsFif8iDunFNKTPWIIIyHcNd2Wb+dgU6ZwKoL2eNqkPFMxmIFrD7GmrbtYtswb1jsA==");

        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey1);
        verifier.update(payload.getBytes());
        boolean verified = verifier.verify(signedPayload2);
        if (verified) {
            // Signature is valid
            System.out.println("valid");
        } else {
            // Signature is invalid
            System.out.println("invalid");
        }*/

    }

}
