package com.goreacraft.plugins.goreaprotect;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener {
	
	
	@EventHandler
    public void checkPlayerOnJoin(PlayerJoinEvent e){
		Methods.loadOrCreatePlayerFile(e.getPlayer().getName());
		
	}

	
	
	@EventHandler
	public void onEntityChangeBlockEvent(EntityExplodeEvent e)
	{
		//for (GoreaProtect.playersData.getKeys(false))
		//poate caut lumea mai intai sa scad din checks
		for ( Block block : e.blockList())
		{
			if (Methods.ownerOfLoc(block.getLocation()) != null)
			{
				e.blockList().remove(block);
			}
		}
	}
	
	@EventHandler
	public void onBlockPistonExtendEvent(BlockPistonExtendEvent e)
	{
		//for (GoreaProtect.playersData.getKeys(false))
		//poate caut lumea mai intai sa scad din checks
		
		
			for ( Block block : e.getBlocks())
			{
				if (Methods.ownerOfLoc(block.getLocation()) != null)
				{
					e.setCancelled(true);
				}
			}
		
		
	}
	
	@EventHandler
	public void onBlockPistonEvents(BlockPistonRetractEvent  e)
	{
		if (e.isSticky())
			
		{  
			Location loc = e.getRetractLocation();
			
			if (Methods.ownerOfLoc(loc) != null )
			{
				
				e.setCancelled(true);
			}
		}
	}
	
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e)
	{
		Location loc = e.getBlock().getLocation();
		MyPairs isname = Methods.ownerOfLoc(loc);
		if(isname != null)
		{ String name = e.getPlayer().getName();
			if(!isname.name().equals(name))
			{
				e.setCancelled(true);
				return;
			} else {
				
				Methods.removeProtection(name, loc);
				return;
			}			
		}		
	}
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent e)
	{
		//Methods.findPlayerByString("GIoana").sendMessage( "block PLACED " + e.getBlockPlaced().getType().toString());
		//Methods.findPlayerByString("GIoana").sendMessage( "hoppers " + GoreaProtect.hoppers);
		if(GoreaProtect.hoppers.contains(e.getBlockPlaced().getType()))
		{	
			//Methods.findPlayerByString("GIoana").sendMessage( "HOPPER PLACED ");
			List<Location> near = new ArrayList<Location>();
			
			near.add(e.getBlock().getLocation().add(0, 0, -1));//north
			near.add(e.getBlock().getLocation().add(1, 0, 0));//east
			near.add(e.getBlock().getLocation().add(0, 0, 1));//south
			near.add(e.getBlock().getLocation().add(-1, 0, 0));//west
			near.add(e.getBlock().getLocation().add(0, 1, 0));//up
			near.add(e.getBlock().getLocation().add(0, -1, 0));//down
			for (Location loc:near)
			{
				MyPairs isname = Methods.ownerOfLoc(loc);
				if (isname != null)
				{
					if(!isname.name().equals(e.getPlayer().getName()))
					{
						e.setCancelled(true);
					}
				}
				
			}
		}
		Location loc = e.getBlock().getLocation();
		MyPairs isname = Methods.ownerOfLoc(loc);
		if(isname != null)
		{ String name = e.getPlayer().getName();
			if(!isname.name().equals(name))
			{
				e.setCancelled(true);
				return;
			} else {
				
				Methods.removeProtection(name, loc);
				return;
			}			
		}		
	}
	
	
}
