import com.github.dreamdevelopments.dreamapi.DreamAPI;
import com.github.dreamdevelopments.dreamapi.ServerType;
import com.github.dreamdevelopments.dreamapi.handlers.PAPIHandler;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.messages.types.ModernMessage;
import com.github.dreamdevelopments.dreamapi.messages.utils.TextConverter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MessageTest {

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test @SuppressWarnings("deprecation")
    public void testSpigotMessages() {
        try(MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
            //TODO: Replace with realistic version example
            bukkit.when(Bukkit::getVersion).thenReturn("Spigot 1.20.1");
            DreamAPI.initialize(null, "dreamAPI", true, false, false, false, false);
            Assertions.assertEquals(ServerType.SPIGOT, DreamAPI.getInstance().getServerType());

            // Test Legacy Messages

            try(MockedStatic<PAPIHandler> papiHandler = Mockito.mockStatic(PAPIHandler.class)) {
                papiHandler.when(() -> PAPIHandler.hasPlaceholders(Mockito.anyString())).thenReturn(false);
                Message message = Message.fromText("&l<red>Test Message</red>");
                Assertions.assertTrue(message.getType().isLegacy());
                Assertions.assertEquals(ChatColor.BOLD + "" + ChatColor.RED + "Test Message", message.toString());

                // Test replace text
                message = Message.fromText("&l<blue>Test Message</blue>");
                Message replacedMessage = message.replaceText("Test", "New");
                Assertions.assertEquals(ChatColor.BOLD + "" + ChatColor.BLUE + "New Message", replacedMessage.toString());
                Assertions.assertTrue(replacedMessage.equals(Message.fromText("&l<blue>New Message</blue>")));

                testGenericMessages(message);

            }

        }
    }

    @Test
    public void testPaperMessages() {
        try(MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
            //TODO: Replace with realistic version example
            bukkit.when(Bukkit::getVersion).thenReturn("Paper 1.20.1");
            DreamAPI.initialize(null, "dreamAPI", true, false, false, false, false);
            Assertions.assertEquals(ServerType.PAPER, DreamAPI.getInstance().getServerType());

            // Test modern messages

            try(MockedStatic<PAPIHandler> papiHandler = Mockito.mockStatic(PAPIHandler.class)) {
                papiHandler.when(() -> PAPIHandler.hasPlaceholders(Mockito.anyString())).thenReturn(false);
                Message message = Message.fromText("&l<red>Test Message</red>");
                Assertions.assertTrue(message.getType().isModern());
                Assertions.assertEquals("<bold><red>Test Message</red>", message.toString());

                Message componentMessage = new ModernMessage(MiniMessage.miniMessage().deserialize("<italic:false><bold><red>Test Message</red>"));
                Assertions.assertTrue(message.equals(componentMessage));

                // Test replace text
                message = Message.fromText("&l<green>Test Message</green>");
                Message replacedMessage = message.replaceText("Test", "New");
                Assertions.assertEquals("<bold><green>New Message</green>", replacedMessage.toString());
                Assertions.assertTrue(replacedMessage.equals(Message.fromText("&l<green>New Message")));

                testGenericMessages(message);
            }
        }
    }

    private void testGenericMessages(Message message) {
        // Test clone
        Assertions.assertTrue(message.equals(message.clone()));

        // Test Empty messages

        Message emptyMessage = Message.fromText("");
        Assertions.assertTrue(emptyMessage.getType().isEmpty());

        message = Message.fromText(null);
        Assertions.assertTrue(message.getType().isEmpty());

        Assertions.assertTrue(emptyMessage.equals(message));

        // Test concat

        message = Message.fromText("<yellow>Test");
        Message result = Message.fromText("<yellow>Test Message");
        Assertions.assertTrue(message.concat(" Message", true).equals(result));

        Assertions.assertTrue(message.concat(Message.fromText(" Message")).equals(result));
    }

    @Test
    public void testTextConverter() {
        // Test legacy to modern
        String legacyMessage = "&l&cTest #FFFFFFMessage<#000000> #FFFFFFa#FFFFFFb#FFFFFFc";
        String modernMessage = "<bold><red>Test <#FFFFFF>Message<#000000> <#FFFFFF>a<#FFFFFF>b<#FFFFFF>c";
        Assertions.assertEquals(modernMessage, TextConverter.legacyToModern(legacyMessage));

        // Test modern to legacy
        legacyMessage = "&l&cTest #FFFFFFMessage#000ab0 #FFFFFF";
        modernMessage = "<bold><red>Test</red> <color:#FFFFFF>Message<#000ab0> #FFFFFF";
        Assertions.assertEquals(legacyMessage, TextConverter.modernToLegacy(modernMessage));
    }

}
