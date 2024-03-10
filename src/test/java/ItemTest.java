import com.github.dreamdevelopments.dreamapi.DreamAPI;
import com.github.dreamdevelopments.dreamapi.ServerType;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.messages.types.LegacyMessage;
import com.github.dreamdevelopments.dreamapi.messages.types.ModernMessage;
import com.github.dreamdevelopments.dreamapi.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class ItemTest {

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    //TODO: Add test for empty line in lore

    @Test @SuppressWarnings("deprecation")
    public void testSpigotItemUtils() {
        try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
            //TODO: Replace with realistic version example
            bukkit.when(Bukkit::getVersion).thenReturn("Spigot 1.20.1");
            DreamAPI.initialize(null, "dreamAPI", true, false, false, false, false);
            Assertions.assertEquals(ServerType.SPIGOT, DreamAPI.getInstance().getServerType());

            // Test lore with messages

            ItemMeta meta = Mockito.mock(ItemMeta.class);

            List<String> legacyLore = new ArrayList<>();
            legacyLore.add("Initial Lore");
            legacyLore.add("Line 2");
            Mockito.when(meta.getLore()).thenReturn(legacyLore);

            ArgumentCaptor<List<String>> legacyLoreCapture = ArgumentCaptor.forClass(List.class);
            Mockito.doNothing().when(meta).setLore(legacyLoreCapture.capture());

            LegacyMessage loreToAdd = (LegacyMessage) Message.fromText("<bold><yellow>Lore Message");
            List<Message> lore = ItemUtils.getItemLore(meta);
            lore.add(1, loreToAdd);
            ItemUtils.setItemLore(meta, lore);

            List<String> newLore = legacyLoreCapture.getValue();
            Assertions.assertEquals(3, newLore.size());
            Assertions.assertEquals(loreToAdd.getMessage(), newLore.get(1));
        }
    }

    @Test
    public void testPaperItemUtils() {
        try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
            //TODO: Replace with realistic version example
            bukkit.when(Bukkit::getVersion).thenReturn("Paper 1.20.1");
            DreamAPI.initialize(null, "dreamAPI", true, false, false, false, false);
            Assertions.assertEquals(ServerType.PAPER, DreamAPI.getInstance().getServerType());

            // Test lore with messages

            ItemMeta meta = Mockito.mock(ItemMeta.class);

            List<Component> modernLore = new ArrayList<>();
            modernLore.add(MiniMessage.miniMessage().deserialize("Initial Lore"));
            modernLore.add(MiniMessage.miniMessage().deserialize("Line 2"));
            Mockito.when(meta.lore()).thenReturn(modernLore);

            ArgumentCaptor<List<Component>> modernLoreCapture = ArgumentCaptor.forClass(List.class);
            Mockito.doNothing().when(meta).lore(modernLoreCapture.capture());

            ModernMessage loreToAdd = (ModernMessage) Message.fromText("<bold><yellow>Lore Message");
            List<Message> lore = ItemUtils.getItemLore(meta);
            lore.add(1, loreToAdd);
            ItemUtils.setItemLore(meta, lore);

            List<Component> newLore = modernLoreCapture.getValue();
            Assertions.assertEquals(3, newLore.size());
            Assertions.assertEquals(loreToAdd.getMessage(), newLore.get(1));
        }
    }
}
