package mc.spoopy.minespawners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

public class Config {
	
	public static boolean needEnchantment;
	public static Enchantment enchantment;
	public static boolean explosionEnabled;
	public static boolean playerExplosions;
	public static boolean creeperEnabled;
	public static boolean waterExplosion;
	public static boolean whitelist;
	public static List<String> worlds = new ArrayList<String>();
	
	private static FileConfiguration config;
	private static File file;
	
	public Config(MineSpawners plugin) { // standard config load
		
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		file = new File(plugin.getDataFolder(), "config.yml");

		if (!file.exists()) {
			plugin.getConfig().options().copyDefaults(true);
			plugin.saveConfig();
		}
		
		config = YamlConfiguration.loadConfiguration(file);
		update();
		load();
	}
	
	public void update() {
		boolean v1_0 = config.getBoolean("silktouch-required"); 
		if (v1_0 || !v1_0) {
			Util.log("Config detected to be out of date!");
			Util.log("Updating configuration values.");
			config.set("silktouch-required", null);
			config.set("harvesting.enchantment-required", true);
			config.set("harvesting.enchantment-id", 33);
			config.set("harvesting.enable-world-whitelist", false);
			worlds.add("world");
			worlds.add("world_nether");
			worlds.add("world_the_end");
			config.set("harvesting.whitelist", worlds);
			config.set("explosions.enable-harvesting", true);
			config.set("explosions.player-permission-needed", false);
			config.set("explosions.enable-creepers", true);
			config.set("explosions.disable-in-water-explosions", true);
			Util.log("Saving new config file.");
			save();
			Util.log("Configuration file successfully saved!");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void load() {
		 needEnchantment = config.getBoolean("harvesting.enchantment-required");
		 enchantment = Enchantment.getById(config.getInt("harvesting.enchantment-id"));
		 whitelist = config.getBoolean("harvesting.enable-world-whitelist");
		 worlds = config.getStringList("harvesting.whitelist");
		 explosionEnabled = config.getBoolean("explosions.enable-harvesting");
		 playerExplosions = config.getBoolean("explosions.player-permission-needed");
		 creeperEnabled = config.getBoolean("explosions.enable-creepers");
		 waterExplosion = config.getBoolean("explosions.disable-in-water-explosions");
	}
	
	public void reload() {
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void save() {
		try {
			config.save(file);
		}
		catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save config.yml!");
		}
	}
	
}

