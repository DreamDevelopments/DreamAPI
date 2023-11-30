import com.github.dreamdevelopments.dreamapi.DreamAPI;
import com.github.dreamdevelopments.dreamapi.ServerType;
import com.github.dreamdevelopments.dreamapi.handlers.PAPIHandler;
import com.github.dreamdevelopments.dreamapi.messages.Message;
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

            try(MockedStatic<PAPIHandler> papiHandler = Mockito.mockStatic(PAPIHandler.class)) {
                papiHandler.when(() -> PAPIHandler.hasPlaceholders(Mockito.anyString())).thenReturn(false);
                Message message = Message.fromText("&l<red>Test Message</red>");
                Assertions.assertTrue(message.getType().isLegacy());
                Assertions.assertEquals(ChatColor.BOLD + "" + ChatColor.RED + "Test Message", message.toString());

                message = Message.fromText(null);
                Assertions.assertTrue(message.getType().isEmpty());

                message = Message.fromText("");
                Assertions.assertTrue(message.getType().isEmpty());
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

            try(MockedStatic<PAPIHandler> papiHandler = Mockito.mockStatic(PAPIHandler.class)) {
                papiHandler.when(() -> PAPIHandler.hasPlaceholders(Mockito.anyString())).thenReturn(false);
                Message message = Message.fromText("&l<red>Test Message</red>");
                Assertions.assertTrue(message.getType().isModern());
                Assertions.assertEquals("<bold><red>Test Message</red>", message.toString());

                message = Message.fromText(null);
                Assertions.assertTrue(message.getType().isEmpty());

                message = Message.fromText("");
                Assertions.assertTrue(message.getType().isEmpty());
            }
        }
    }

}
