package mc.spoopy.minespawners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerListener implements Listener {

	@EventHandler
	public void onMineSpawner(BlockBreakEvent e) {
		Block block = e.getBlock();
		if ((block.getState() instanceof CreatureSpawner)) {
			Player player = e.getPlayer();
			e.setExpToDrop(0);
			Enchantment enchant = Config.enchantment;
			if (Config.needEnchantment) {
				Boolean hasEnchantment = player.getInventory().getItemInMainHand().containsEnchantment(enchant);
				if (hasEnchantment) {
					breakSpawner(player, block);
				} else {
					Util.error(player, enchant.toString() + " is needed to harvest spawners!");
				}
			} else {
				breakSpawner(player, block);
			}
		}
	}

	@EventHandler
	public void onSpawnerPlace(BlockPlaceEvent e){
		Player player = e.getPlayer();
		Block block = e.getBlockPlaced();
		if (((block.getState() instanceof CreatureSpawner)) && (!e.isCancelled())) {
			if (Util.hasPerm(player, new String[] {"minespawners.mine.place", "minespawners.mine.*", "minespawners.*"})) {
				CreatureSpawner spawner = (CreatureSpawner)block.getState();
				if (player.getInventory().getItemInMainHand().hasItemMeta()) {
					ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
					if (meta.hasLore()) {
						for (String line : meta.getLore()) {
							if (line.contains(ChatColor.GRAY + "Type: ")) {
								String type = ChatColor.stripColor(line);
								type = type.split(" ")[1];
								spawner.setSpawnedType(EntityType.valueOf(type));
								spawner.update();
								Util.notify(player, "You have placed a " + type.replace("_", " ") + " Spawner!");
								return;
							}
						}
					}
				}
			} else {
				Util.error(player, "Sorry but you do not have permission to place custom spawners!");
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onExplode(EntityExplodeEvent event) {

		Location location = event.getLocation();

		if (Config.waterExplosion) {
			Material mat = location.getBlock().getType();
			if ((mat.equals(Material.WATER)) || (mat.equals(Material.STATIONARY_WATER))) {
				return;
			}
			if (Config.whitelist) {
				boolean contains = Config.worlds.contains(location.getWorld().getName());
				if (contains) findSpawners(event, location);
			} else {
				findSpawners(event, location);
			}
		} else if (Config.whitelist) {
			boolean contains = Config.worlds.contains(location.getWorld().getName());
			if (contains) findSpawners(event, location);
		} else {
			findSpawners(event, location);
		}
	}

	public void checkEntity(EntityExplodeEvent event, Location location) {
		EntityType et = event.getEntityType();
		if (et == EntityType.PRIMED_TNT) {
			findSpawners(event, location);
		} else if ((et == EntityType.CREEPER) && (Config.creeperEnabled))
			findSpawners(event, location);
	}

	public void findSpawners(EntityExplodeEvent event, Location location) {
		int r = 2;
		for (int x = (r * -1); x <= r; x++) {
			for (int y = (r * -1); y <= r; y++) {
				for (int z = (r * -1); z <= r; z++) {
					Block block = location.getWorld().getBlockAt(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
					if (block.getState() instanceof CreatureSpawner) {
						if (Config.playerExplosions) {
							Entity ent = (Entity) event.getEntity().getLastDamageCause();
							if (ent instanceof Player) {
								Player player = (Player) ent;
								if (Util.hasPerm(player, new String[] {"minespawners.mine.explode", "minespawners.mine.*", "minespawners.*"})) {
									explodeSpawner(event, block);
								}
							}
						} else {
							explodeSpawner(event, block);
						}
					}
				}
			}
		}
	}
	
	public void explodeSpawner(EntityExplodeEvent event, Block block) {
		CreatureSpawner spawner = (CreatureSpawner)block.getState();
		String tn = spawner.getCreatureTypeName();
		tn = tn.toLowerCase();
		Location loc = block.getLocation();
		switch(tn) {
		case "villagergolem":
			tn = "IRON_GOLEM";
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn, 1));
			block.setType(Material.AIR);
			break;
		case "pigzombie":
			tn = "PIG_ZOMBIE";
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
			block.setType(Material.AIR);
			break;
		case "cavespider":
			tn = "CAVE_SPIDER";
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
			block.setType(Material.AIR);
			break;
		case "mushroomcow": 		
			tn = "MUSHROOM_COW";
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
			block.setType(Material.AIR);
			break;
		case "lavaslime":
			tn = "MAGMA_CUBE";
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
			block.setType(Material.AIR);
			break;
		case "ozelot":
			tn = "OCELOT";
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
			block.setType(Material.AIR);
			break;
		case "xporb":
			tn = "EXPERIENCE_ORB";
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
			block.setType(Material.AIR);
			break;
		case "entityhorse":
			tn = "HORSE";
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
			block.setType(Material.AIR);
			break;
		case "minecartrideable": 
			tn = "MINECART";
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
			block.setType(Material.AIR);
			break;
		case "endercrystal": 
			tn = "ENDER_CRYSTAL";
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
			block.setType(Material.AIR);
			break;
		case "minecartchest": 
			tn = "MINECART_CHEST";
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
			block.setType(Material.AIR);
			break;
		case "polarbear":
			tn = "POLAR_BEAR";
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
			block.setType(Material.AIR);
			break;
		case "minecartfurnace":
			tn = "MINECART_FURNACE";
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
			block.setType(Material.AIR);
			break;
		case "minecarttnt":
			tn = "MINECART_TNT";
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
			block.setType(Material.AIR);
			break;
		default:
			block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
			block.setType(Material.AIR);
			break;
		}
	}


	public void breakSpawner(Player player, Block block) {
		if (Util.hasPerm(player, new String[] {"minespawners.mine.break", "minespawners.mine.*", "minespawners.*"})) {
			CreatureSpawner spawner = (CreatureSpawner)block.getState();
			Location loc = block.getLocation();
			String tn = spawner.getCreatureTypeName();
			tn = tn.toLowerCase();
			switch(tn) {
			case "villagergolem":
				tn = "IRON_GOLEM";
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn, 1));
				Util.notify(player, "You have harvested a Iron Golem Spawner!");
				break;
			case "pigzombie":
				tn = "PIG_ZOMBIE";
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
				Util.notify(player, "You have harvested a Pig Zombie Spawner!");
				break;
			case "cavespider":
				tn = "CAVE_SPIDER";
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
				Util.notify(player, "You have harvested a Cave Spider Spawner!");
				break;
			case "mushroomcow": 		
				tn = "MUSHROOM_COW";
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
				Util.notify(player, "You have harvested a Mushroom Cow Spawner!");
				break;
			case "lavaslime":
				tn = "MAGMA_CUBE";
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
				Util.notify(player, "You have harvested a Magma Cube Spawner!");
				break;
			case "ozelot":
				tn = "OCELOT";
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
				Util.notify(player, "You have harvested a Ocelot Spawner!");
				break;
			case "xporb":
				tn = "EXPERIENCE_ORB";
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
				Util.notify(player, "You have harvested a Experience Orb Spawner!");
				break;
			case "entityhorse":
				tn = "HORSE";
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
				Util.notify(player, "You have harvested a Horse Spawner!");
				break;
			case "minecartrideable": 
				tn = "MINECART";
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
				Util.notify(player, "You have harvested a Minecart Spawner!");
				break;
			case "endercrystal": 
				tn = "ENDER_CRYSTAL";
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
				Util.notify(player, "You have harvested a Endercrystal Spawner!");
				break;
			case "minecartchest": 
				tn = "MINECART_CHEST";
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
				Util.notify(player, "You have harvested a Minecart Chest Spawner!");
				break;
			case "polarbear":
				tn = "POLAR_BEAR";
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
				Util.notify(player, "You have harvested a Polar Bear Spawner!");
				break;
			case "minecartfurnace":
				tn = "MINECART_FURNACE";
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
				Util.notify(player, "You have harvested a Minecart Furnace Spawner!");
				break;
			case "minecarttnt":
				tn = "MINECART_TNT";
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
				Util.notify(player, "You have harvested a Minecart Tnt Spawner!");
				break;
			default:
				block.getWorld().dropItemNaturally(loc, Spawner.instance.get(tn.toUpperCase(), 1));
				Util.notify(player, "You have harvested a " + tn.toUpperCase() + " Spawner!");
				break;

			}
		}
	}
}