package me.dueris.genesismc.core;

import me.dueris.genesismc.core.api.entity.OriginPlayer;
import me.dueris.genesismc.core.api.events.OriginKeybindExecuteEvent;
import me.dueris.genesismc.core.utils.ShulkUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.geysermc.geyser.api.GeyserApi;

import java.util.ArrayList;

import static me.dueris.genesismc.core.factory.powers.Powers.launch_into_air;
import static me.dueris.genesismc.core.factory.powers.Powers.phantomize;
import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.RED;

public class KeybindHandler implements Listener {

    @EventHandler
    public void OnPressMainKey(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("Geyser-Spigot")) {
            if (!GeyserApi.api().isBedrockPlayer(p.getUniqueId())) {

                keybindTriggerMethod(p, e);

            }
        }else{
            keybindTriggerMethod(p, e);
        }
    }

    public static void keybindTriggerMethod(Player p, PlayerSwapHandItemsEvent e){
        OriginKeybindExecuteEvent event = new OriginKeybindExecuteEvent(p);
        getServer().getPluginManager().callEvent(event);

        PersistentDataContainer data = p.getPersistentDataContainer();
        if (OriginPlayer.getOriginTag(e.getPlayer()).contains("genesis:origin-shulk")) {
            e.setCancelled(true);

            ArrayList<ItemStack> vaultItems = ShulkUtils.getItems(p);

            Inventory vault = Bukkit.createInventory(p, InventoryType.DROPPER, "Shulker Inventory");

            vaultItems.stream()
                    .forEach(itemStack -> vault.addItem(itemStack));

            p.openInventory(vault);

        }
        if (phantomize.contains(OriginPlayer.getOriginTag(e.getPlayer()))) {
            e.setCancelled(true);
            int phantomid = data.get(new NamespacedKey(GenesisMC.getPlugin(), "in-phantomform"), PersistentDataType.INTEGER);
            if (phantomid == 1) {
                if(p.getGameMode() != GameMode.SPECTATOR) {

                    if(p.getFoodLevel() > 6){
                        p.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "in-phantomform"), PersistentDataType.INTEGER, 2);
                        p.sendActionBar(DARK_AQUA + "Activated Phantom Form");
                        p.setSilent(true);
                        p.setCollidable(false);

                    }else{
                        p.sendMessage(RED + "You must be able to sprint to switch forms");
                    }

                }else{p.sendMessage(ChatColor.RED + "You are unable to switch forms while inside a block or in spectator mode.");}
            } else if (phantomid == 2) {
                if(p.getGameMode() != GameMode.SPECTATOR) {

                    p.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "in-phantomform"), PersistentDataType.INTEGER, 1);
                    p.sendActionBar(DARK_AQUA + "Deactivated Phantom Form");
                    p.setSilent(false);
                    p.setCollidable(true);

                }else{p.sendMessage(ChatColor.RED + "You are unable to switch forms while inside a block or in spectator mode.");}
            } else {
                p.sendMessage(RED + "Error: Switching could not be executed");
            }
        }
        if(launch_into_air.contains(OriginPlayer.getOriginTag(e.getPlayer()))){
            p.setVelocity(new Vector(0, 2, 0));
            e.setCancelled(true);
        }

    }

}
