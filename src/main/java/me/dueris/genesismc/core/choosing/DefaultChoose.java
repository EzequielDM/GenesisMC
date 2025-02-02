package me.dueris.genesismc.core.choosing;

import me.dueris.genesismc.core.GenesisMC;
import me.dueris.genesismc.core.entity.OriginPlayer;
import me.dueris.genesismc.core.events.OriginChangeEvent;
import me.dueris.genesismc.core.events.OriginChooseEvent;
import me.dueris.genesismc.core.factory.CraftApoli;
import me.dueris.genesismc.core.items.OrbOfOrigins;
import me.dueris.genesismc.core.utils.SendCharts;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import static me.dueris.genesismc.core.items.OrbOfOrigins.orb;
import static me.dueris.genesismc.core.utils.BukkitColour.AQUA;
import static org.bukkit.Bukkit.getServer;

public class DefaultChoose {

    public static void DefaultChoose(Player p) {
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);

        //default choose
        p.closeInventory();
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 10, 2);
        p.sendMessage(Component.text("You have chosen an origin!").color(TextColor.fromHexString(AQUA)));
        p.spawnParticle(Particle.CLOUD, p.getLocation(), 100);
        p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation(), 6);
        p.setCustomNameVisible(false);
        p.setHealthScaled(false);

        OriginChooseEvent chooseEvent = new OriginChooseEvent(p);
        getServer().getPluginManager().callEvent(chooseEvent);
        OriginChangeEvent Event = new OriginChangeEvent(p);
        getServer().getPluginManager().callEvent(Event);

        if (p.getInventory().getItemInMainHand().isSimilar(OrbOfOrigins.orb) && !OriginPlayer.hasOrigin(p, CraftApoli.nullOrigin().getTag())) {
            int amt = p.getInventory().getItemInMainHand().getAmount();
            if(p.getGameMode().equals(GameMode.CREATIVE)) return;
            p.getInventory().getItemInMainHand().setAmount(amt - 1);
        } else {
            if (p.getInventory().getItemInOffHand().isSimilar(orb) && !OriginPlayer.hasOrigin(p, CraftApoli.nullOrigin().getTag())) {
                int amt = p.getInventory().getItemInOffHand().getAmount();
                if(p.getGameMode().equals(GameMode.CREATIVE)) return;
                p.getInventory().getItemInOffHand().setAmount(amt - 1);
            }
        }

        SendCharts.originPopularity(p);

    }
}
