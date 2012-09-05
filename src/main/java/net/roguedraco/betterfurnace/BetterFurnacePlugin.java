package net.roguedraco.betterfurnace;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;
import net.roguedraco.betterfurnace.lang.Lang;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterFurnacePlugin extends JavaPlugin {
	
	public static Logger logger;

	public static String pluginName;
	public static String pluginVersion;
	
	public static BetterFurnacePlugin plugin;
	
	public static Permission permission = null;
	
	public static Lang lang;
	
	private static UpdateCheck updater;

	public void onEnable() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		BetterFurnacePlugin.logger = Logger.getLogger("Minecraft");
		BetterFurnacePlugin.plugin = this;
		BetterFurnacePlugin.pluginName = this.getDescription().getName();
		BetterFurnacePlugin.pluginVersion = this.getDescription().getVersion();
		
		BetterFurnacePlugin.lang = new Lang(this);
		lang.setupLanguage();
		
		if (getServer().getPluginManager().getPlugin("Vault") != null) {
			setupPermissions();
		} else {
			log(ChatColor.RED
					+ "Missing dependency: Vault. Please install this for the plugin to work.");
			getServer().getPluginManager().disablePlugin(plugin);
			return;
		}
		
		PluginManager pm = getServer().getPluginManager();
		Listener events = new Events();
		pm.registerEvents(events, this);
		
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :-(
		}
		
		BetterFurnacePlugin.updater = new UpdateCheck(this, "http://dev.bukkit.org/server-mods/betterfurnace/files.rss");
		
		log(Lang.get("plugin.enabled"));
	}
	
	public void onDisable() {
		lang.saveLanguage();
		log(Lang.get("plugin.disabled"));
	}
	
	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}
	
	public static void log(String message) {
		log(Level.INFO, message);
	}

	public static void log(Level level, String message) {
		if (plugin.getConfig().getBoolean("useFancyConsole") == true
				&& level == Level.INFO) {
			ConsoleCommandSender console = Bukkit.getServer()
					.getConsoleSender();
			console.sendMessage("[" + ChatColor.LIGHT_PURPLE + pluginName
					+ " v" + pluginVersion + ChatColor.GRAY + "] " + message);
		} else {
			BetterFurnacePlugin.logger.log(level, "[" + pluginName + " v"
					+ pluginVersion + "] " + message);
		}
	}

	public static void debug(String message) {
		if (plugin.getConfig().getBoolean("debug")) {
			if (plugin.getConfig().getBoolean("useFancyConsole") == true) {
				ConsoleCommandSender console = Bukkit.getServer()
						.getConsoleSender();
				console.sendMessage("[" + ChatColor.LIGHT_PURPLE + pluginName
						+ " v" + pluginVersion + " Debug" + ChatColor.GRAY
						+ "] " + message);
			} else {
				System.out.println("[" + pluginName + " v" + pluginVersion
						+ " Debug" + "] " + message);
			}
		}
	}

	public static UpdateCheck getUpdater() {
		return updater;
	}
	
}
