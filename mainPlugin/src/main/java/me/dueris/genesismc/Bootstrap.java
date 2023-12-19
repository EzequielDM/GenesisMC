package me.dueris.genesismc;

import io.papermc.paper.configuration.PaperConfigurations;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import joptsimple.OptionSet;
import me.dueris.genesismc.enchantments.WaterProtectionNMSImpl;
import me.dueris.genesismc.utils.BukkitUtils;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.RegistryAccess.Frozen;
import net.minecraft.core.RegistryAccess.RegistryEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DeathMessageType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_20_R3.CraftRegistry;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Bootstrap implements PluginBootstrap {
    public static WaterProtectionNMSImpl waterProtection;

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        try {
            File datapackDir = new File(this.parseDatapackPath(context));
            copyOriginDatapack(datapackDir.toPath());
        } catch (Exception e) {
            // ignore
        }
        // hurt by water damage type
        EquipmentSlot[] slots = {EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.HEAD, EquipmentSlot.LEGS};
        WaterProtectionNMSImpl waterProtection = new WaterProtectionNMSImpl(net.minecraft.world.item.enchantment.Enchantment.Rarity.COMMON, EnchantmentCategory.ARMOR, slots);
        registerEnchantment("water_protection", waterProtection);
        Bootstrap.waterProtection = waterProtection;
    }

    private static Enchantment registerEnchantment(String name, Enchantment enchantment) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation("origins", name), enchantment);
    }

    public static void deleteDirectory(Path directory, boolean ignoreErrors) throws IOException {
        if (Files.exists(directory)) {
            Files.walk(directory)
                    .sorted((a, b) -> b.compareTo(a)) // Sort in reverse order for correct deletion
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                            Files.delete(path);
                        } catch (IOException e) {
                            if(!ignoreErrors){
                                System.err.println("Error deleting: " + path + e);
                            }
                        }
                    });
        }
    }
    public static ArrayList<String> oldDV = new ArrayList<>();
    static {
        oldDV.add("OriginsGenesis");
        oldDV.add("Origins-Genesis");
        oldDV.add("Origins-GenesisMC");
        oldDV.add("Origins-GenesisMC[0_2_2]");
    }

    public static void copyOriginDatapack(Path datapackPath) {
        for(String string : oldDV){
            if (Files.exists(datapackPath)) {
                String path = Path.of(datapackPath + File.separator + string).toAbsolutePath().toString();
                try {
                    deleteDirectory(Path.of(path), false);
                } catch (IOException e) {

                }
            }
        }
        try {
            CodeSource src = BukkitUtils.class.getProtectionDomain().getCodeSource();
            URL jar = src.getLocation();
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            while (true) {
                ZipEntry e = zip.getNextEntry();
                if (e == null)
                    break;
                String name = e.getName();

                if (!name.startsWith("datapacks/")) continue;
                if (FilenameUtils.getExtension(name).equals("zip")) continue;
                if (name.equals("datapacks/")) continue;

                name = name.substring(10);
                File file = new File(datapackPath.toAbsolutePath().toString().replace(".\\", "") + File.separator + name);
                if (!file.getName().contains(".")) {
                    Files.createDirectory(Path.of(file.getAbsolutePath()));
                    continue;
                }
                Files.writeString(Path.of(file.getAbsolutePath()), new String(zip.readAllBytes()));
            }
            zip.close();
        } catch (Exception e) {
            // ignore
        }
    }

    public String parseDatapackPath(BootstrapContext context){
        OptionSet optionset;
        try {
            org.bukkit.configuration.file.YamlConfiguration bukkitConfiguration = YamlConfiguration.loadConfiguration(Paths.get("bukkit.yml").toFile());
            File container;
            container = new File(bukkitConfiguration.getString("settings.world-container", "."));
            String s = (String) Optional.ofNullable(
                    "world"
            ).orElseGet(() -> {
                Path propPath = Paths.get("server.properties");
                if (propPath.toFile().exists()) {
                    Properties properties = new Properties();
                    try (FileInputStream input = new FileInputStream(propPath.toFile())) {
                        properties.load(input);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return properties.getProperty("level-name", "world");
                } else {
                    return "world";
                }
            });
                    Path datapackFolder = Paths.get(container.getAbsolutePath() + File.separator + s + File.separator + "datapacks");
            return datapackFolder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
