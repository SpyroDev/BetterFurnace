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
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.material.MaterialData;

public class Events implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (BetterFurnacePlugin.permission.playerHas(event.getPlayer(),
				"betterfurnace.update")) {
			BetterFurnacePlugin.getUpdater().updateNeeded(event.getPlayer());
		}
	}
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		 
		Material bucket = event.getBucket();
		 
		if (bucket.toString().contains("LAVA")) {
			checkBlock(event.getBlockClicked().getRelative(event.getBlockFace()));
		}
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
		if(material.equals(Material.FIRE) || material.equals(Material.LAVA) || material.equals(Material.LAVA_BUCKET) || material.equals(Material.STATIONARY_LAVA)) {
			powerFurnace(block);
		}
		else if(material.equals(Material.FURNACE) || material.equals(Material.BURNING_FURNACE)) {
			checkBlock(block.getRelative(BlockFace.DOWN));
		}
		else {
			unpowerFurnace(block);
		}
	}
	
	public boolean hasFurnace(Block block) {
		if(block.getRelative(BlockFace.UP).getType().equals(Material.FURNACE) || block.getRelative(BlockFace.UP).getType().equals(Material.BURNING_FURNACE)) {
			return true;
		}
		return false;
	}
	
	public void powerFurnace(Block block) {
		if(hasFurnace(block)) {
			Furnace furnace = (Furnace) block.getRelative(BlockFace.UP).getState();
			Short time = Short.parseShort("10000");
			MaterialData tempData = furnace.getData();
			furnace.setType(Material.BURNING_FURNACE);
			furnace.setData(tempData);
			furnace.update(true);
			furnace.setCookTime(time);
			furnace.setBurnTime(time);
			furnace.update(true);
			BetterFurnacePlugin.debug("Furnace Powered");
		}
	}
	
	public void unpowerFurnace(Block block) {
		if(hasFurnace(block)) {
			Furnace furnace = (Furnace) block.getRelative(BlockFace.UP).getState();
			Short time = Short.parseShort("0");

			MaterialData tempData = furnace.getData();
			furnace.setType(Material.FURNACE);
			furnace.setData(tempData);
			furnace.setCookTime(time);
			furnace.setBurnTime(time);
			furnace.update(true);
			BetterFurnacePlugin.debug("Furnace Un-Powered");
		}
	}

}
