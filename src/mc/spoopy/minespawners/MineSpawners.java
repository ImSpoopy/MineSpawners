package mc.spoopy.minespawners;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MineSpawners extends JavaPlugin {
	
  public void onEnable() {
    Util.log("is Enabling...");
    new Config(this);
    Vault.vault.setup();
    loadListeners();
    Util.log("is Enabled!");
  }

  public void onDisable() {
    Util.log("is Disabling...");
 
    Util.log("is Disabled.");
  }

  public void loadListeners() {
    PluginManager pm = Bukkit.getServer().getPluginManager();
    pm.registerEvents(new SpawnerListener(), this);
    pm.registerEvents(new ShopListener(this), this);
  }
}