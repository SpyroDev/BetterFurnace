package net.roguedraco.betterfurnace;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (BetterFurnacePlugin.permission.playerHas(event.getPlayer(),
				"betterfurnace.update")) {
			BetterFurnacePlugin.getUpdater().updateNeeded(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		checkBlock(event.getClickedBlock());
	}

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		checkBlock(event.getBlock());
	}
	
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		checkBlock(event.getBlock());
	}
	
	@EventHandler
	public void onBlockSpread(BlockSpreadEvent event) {
		checkBlock(event.getBlock());
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		checkBlock(event.getBlock());
	}
	
	public void checkBlock(Block block) {
		Material material = block.getType();
		if(material.equals(Material.FIRE) || material.equals(Material.LAVA) || material.equals(Material.LAVA_BUCKET)) {
			powerFurnace(block);
		}
		else {
			unpowerFurnace(block);
		}
	}
	
	public boolean hasFurnace(Block block) {
		if(block.getRelative(BlockFace.UP).getType().equals(Material.FURNACE)) {
			return true;
		}
		return false;
	}
	
	public void powerFurnace(Block block) {
		if(hasFurnace(block)) {
			Furnace furnace = (Furnace) block.getRelative(BlockFace.UP).getState();
			Short time = Short.parseShort("10000");
			furnace.setBurnTime(time);
		}
	}
	
	public void unpowerFurnace(Block block) {
		if(hasFurnace(block)) {
			Furnace furnace = (Furnace) block.getRelative(BlockFace.UP).getState();
			Short time = Short.parseShort("0");
			furnace.setBurnTime(time);
		}
	}

}
