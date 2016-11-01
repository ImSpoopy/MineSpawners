package mc.spoopy.minespawners;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Spawner
{
  public static Spawner instance = new Spawner();

  public ItemStack get(String entityType, int amount)
  {
    ItemStack spawner = new ItemStack(Material.MOB_SPAWNER);
    ItemMeta meta = spawner.getItemMeta();

    spawner.setAmount(amount);
    meta.setDisplayName(ChatColor.GREEN + WordUtils.capitalizeFully(entityType.replace("_", " ")) + " Spawner");

    List<String> lore = new ArrayList<String>();

    lore.add(ChatColor.GRAY + "Type: " + ChatColor.GREEN + entityType);

    meta.setLore(lore);

    spawner.setItemMeta(meta);

    return spawner;
  }
}