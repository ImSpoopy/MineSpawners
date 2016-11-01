package mc.spoopy.minespawners;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Util {


	private static final Logger log = Logger.getLogger("Minecraft");
	public static String plugin = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "MineSpawners" + ChatColor.DARK_GRAY + "] ";

	public static void log(String s) {
		log.info(plugin + s);
	}

	public static void msg(Player player, String s) {
		player.sendMessage(plugin + ChatColor.GRAY + s);
	}

	public static void error(Player player, String s) {
		player.sendMessage(plugin + ChatColor.RED + s);
	}

	public static void notify(Player player, String s) {
		player.sendMessage(plugin + ChatColor.GREEN + s);
	}

	public static boolean hasPerm(Player player, String[] array) {
		for(String s: array) {
			if (player.hasPermission(s)) return true;
		}
		return false;
	}

}
