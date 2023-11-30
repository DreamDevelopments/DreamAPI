import com.github.dreamdevelopments.dreamapi.DreamAPI;
import com.github.dreamdevelopments.dreamapi.ServerType;
import com.github.dreamdevelopments.dreamapi.handlers.PAPIHandler;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.messages.types.ModernMessage;
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
            DreamAPI.initializeServer();
            Assertions.assertEquals(ServerType.SPIGOT, DreamAPI.getServerType());

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
            DreamAPI.initializeServer();
            Assertions.assertEquals(ServerType.PAPER, DreamAPI.getServerType());

            // Test modern messages

            try(MockedStatic<PAPIHandler> papiHandler = Mockito.mockStatic(PAPIHandler.class)) {
                papiHandler.when(() -> PAPIHandler.hasPlaceholders(Mockito.anyString())).thenReturn(false);
                Message message = Message.fromText("&l<red>Test Message</red>");
                Assertions.assertTrue(message.getType().isModern());
                Assertions.assertEquals("<bold><red>Test Message</red>", message.toString());

                Message componentMessage = new ModernMessage(MiniMessage.miniMessage().deserialize("<bold><red>Test Message</red>"));
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

    }

}
