package mc.spoopy.minespawners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ShopListener implements Listener {
	
	private MineSpawners ms;
	List<String> cooldown = new ArrayList<String>();

	public ShopListener(MineSpawners ms) {
		this.ms = ms;
	}

	public void signCD(final UUID id) {
		ms.getServer().getScheduler().scheduleSyncDelayedTask(ms, new Runnable() {
			public void run() {
				cooldown.remove(id);
			}
		}
		, 20L);
	}

	@EventHandler
	public void onSignInteraction(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = e.getClickedBlock();
			if (block.getState() instanceof Sign) {
				Sign sign = (Sign)block.getState();
				if (sign.getLine(0) != null) {
					Economy eco = Vault.economy;
					String line = sign.getLine(0);
					if (line.equalsIgnoreCase(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "[Spawner]")) {
						UUID id = player.getUniqueId();
						boolean cd = cooldown.contains(id);
						if (!cd) {
							if (Util.hasPerm(player, new String[] {"minespawners.signs.use", "minespawners.signs.*", "minespawners.*"})) {
								String type = sign.getLine(2);
								double price = Double.parseDouble(sign.getLine(3).replace("$", ""));
								double bal = eco.getBalance(player);
								if (bal >= price) {
									int amount = Integer.parseInt(sign.getLine(1));
									type = type.toUpperCase();
									type = type.replace(" ", "_");
									player.getInventory().addItem(Spawner.instance.get(type, amount));
									signCD(id);
									eco.withdrawPlayer(player, price);
									Util.notify(player, "You have purchased a " + type.replace("_", " ") + " Spawner for $" + price + "!");
									player.updateInventory();
								} else {
									Util.error(player, "You do not have enough money to purchase this spawner.");
								}
							} else {
								Util.error(player, "You do not have permission to use this type of sign.");
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onSpawnerSignCreation(SignChangeEvent e){
		Block block = e.getBlock();
		BlockState state = block.getState();
		if (state instanceof Sign) {
			String line = e.getLine(0);
			Player player = e.getPlayer();
			if (line.equalsIgnoreCase("[spawner]")) {
				if (Util.hasPerm(player, new String[] {"minespawners.signs.create", "minespawners.signs.*", "minespawners.*"})) {
					e.setLine(0, ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "[Spawner]");
					Util.notify(player, "You have created a spawner sign!");
					return;
				}
				block.breakNaturally();
				Util.error(player, "You do not have permission to make this sign!");
				return;
			}
		}
	}
}
