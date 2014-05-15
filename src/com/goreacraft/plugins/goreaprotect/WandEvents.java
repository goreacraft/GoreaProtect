package com.goreacraft.plugins.goreaprotect;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class WandEvents implements Listener{

	//static List<String> blockinfo;
	//static List<String> loc = new ArrayList<String>();
	private static GoreaProtect plugin = GoreaProtect.plugin;
	private static Plugin factions = Bukkit.getServer().getPluginManager().getPlugin("Factions");
	//public static YamlConfiguration blocksProtected;
	//private static YamlConfiguration blocksProtected = GoreaProtect.blocksProtected;
	private static YamlConfiguration playersData = GoreaProtect.playersData;
	int max = 10;
	int initial=5;
	int step=1;
	static HashMap<String, Integer> trustlevel =new HashMap<String, Integer>();
	static HashMap<String, Integer> togler =new HashMap<String, Integer>();
	String Clear = "CLEAR <Claims/Friends>";
	String Show = "SHOW CLAIMS";
	
	
	
	@EventHandler
    public void addFriendEvent(PlayerInteractEntityEvent e)
	{
        Entity targetentity = e.getRightClicked();
        if(targetentity instanceof Player){
        Player target = (Player) e.getRightClicked();
        Player player = (Player) e.getPlayer();
        	if ( player.getItemInHand().getType().equals(GoreaProtect.wand)	&& player.isSneaking())
        	{
        		 if (trustlevel.get(player.getName())==0)
				 {
        			if ( GoreaProtect.playersData.getConfigurationSection(player.getName() + "." + "Friends").getKeys(false).contains(target.getName()) )
        			{
        				playersData.set(player.getName() + "." + "Friends" + "." + target.getName(), null);
        				player.sendMessage(target.getName() + " removed from friends list.");
                		target.sendMessage(player.getName() + " removed you from friends list.");
                		return;
        			} else
        				{
        				player.sendMessage(target.getName() + " is not in your friends list.");
        				return;
        				}
				 }
        		Methods.checkFixPlayerData(player.getName());
        		if (playersData.getConfigurationSection(player.getName() + "." + "Friends").getKeys(false).size()< plugin.getConfig().getInt("MaxFriends") )
        		{
        			playersData.createSection(player.getName() + "." + "Friends" + "." + target.getName());
        			playersData.set(player.getName() + "." + "Friends" + "." + target.getName(), trustlevel.get(player.getName()));
        		
        		player.sendMessage(target.getName() + " adding as friend");
        		target.sendMessage(player.getName() + " added you as friend");
        		return;
        		} else {
        			player.sendMessage("Maximum number of available friends reached");
        			return;
        			}
        	}
                        
        }
        Player player = (Player) e.getPlayer();
        player.sendMessage("hmmm");
        return;
    }
       
    
	





	@EventHandler
	   public void onWandUse(PlayerInteractEvent e)
	   {
		Player player = e.getPlayer();
					
		if (player.getItemInHand().getType().equals(GoreaProtect.wand)) 
			{
			String name = player.getName();
		  if (!trustlevel.containsKey(name))
			{
			trustlevel.put(name, initial);
			}
			e.setCancelled(true);
			Action action = e.getAction();		
			
		if(player.isSneaking())
		{	
					
			if (action.equals(Action.RIGHT_CLICK_BLOCK))
				{	
				
					if (trustlevel.get(name)==-1)
					 {
						Methods.playeffect(player);
							///player.sendMessage(locs);

						
						return;
					 }
					
						Location blockLocation = e.getClickedBlock().getLocation();
						player.sendMessage("1");
						MyPairs isname = Methods.ownerOfLoc(blockLocation);
						 if (isname != null)
						 { 
							 if (isname.name().equals(name))
							 {
								 player.sendMessage("esti tu");
								 if (trustlevel.get(name)==0)
								 {	
									 //DELETE CLAIM
									int claimsnow = playersData.getInt(name + ".Data.ClaimsNow");
									int maxclaims = playersData.getInt(name + ".Data.MaxClaims");
									player.sendMessage("Protection removed for this block. You have now "+ ChatColor.GOLD + (maxclaims-claimsnow)  +  ChatColor.RESET + " protections left" );
									
									Methods.setProtection(name, blockLocation, null);
									
									//playersData.set(name + "." + "Claims" + "." + locationToString(blockLocation).get(2), null);
									//playersData.set(name + "." + "Data" + "." + "ClaimsNow", claimsnow -1);
									Methods.removeProtection(name,blockLocation);
									return;
										//remove protection 
									// add number of blocks left
									
								 } 
								
								if (Methods.getint(name,Methods.locationToStringList(blockLocation))!=trustlevel.get(name)){ 
									//playersData.set(name + "." + "Claims" + "." + locationToString(blockLocation).get(2), trustlevel.get(name));
								
									Methods.setProtection(name, blockLocation,trustlevel.get(name));
								player.sendMessage("Trust level changed to: " + trustlevel.get(name));
								
								
									//change protection
								} else {player.sendMessage("Trust level is: " + trustlevel.get(name));}
							 } 
							else							
							{
								player.sendMessage("Protected by: " + isname.name());
								//deny action
								return;
								
							} 
						}
						else 
						{
							if (trustlevel.get(name)==0)
							 {
								//nu ai ce protectie de scos
								return;
							 }
							if ( factions != null && plugin.getConfig().getBoolean("Factions integration"))
							{
								player.sendMessage("Faction integration");
								
							//====================Factions if in zone allow	
								
							} 
							
							else {
								player.sendMessage(" " + factions);
								
							Methods.checkFixPlayerData(player.getName());
							
							int claimsnow = playersData.getInt(name + ".Data.ClaimsNow");
							int maxclaims = playersData.getInt(name + ".Data.MaxClaims");
							if(claimsnow < maxclaims)
								{
								
								
								Methods.setProtection(name, blockLocation,trustlevel.get(name));
								
								playersData.createSection(name + "." + "Claims" + "." 
										+ blockLocation.getWorld().getName() + "." 
										+ Methods.ChunkToStringFromLocation(blockLocation) + "."
										+ Methods.coordsFromLocation(blockLocation));
								//playersData.createSection(name + "." + "Claims" + "." + ChunkToStringFromLocation(blockLocation));
								playersData.set(name + "." + "Claims" + "." 
										+ blockLocation.getWorld().getName() + "." 
										+ Methods.ChunkToStringFromLocation(blockLocation) + "."
										+ Methods.coordsFromLocation(blockLocation)
										, trustlevel.get(name));
								
								//playersData.set(name + "." + "Data" + "." + "ClaimsNow", claimsnow + 1);
								player.sendMessage("This block is now protected by you: " + trustlevel.get(name) );
								return;
								}
									else 
									{ 
									player.sendMessage("You have reached maximum claims: " + claimsnow);
									return;
									}
							}
						}
						 
				}
	
			}
			if(!player.isSneaking())
			{
				
				if (!trustlevel.containsKey(name))
				{
				trustlevel.put(name, max);
				}
				
						
				if (action.equals(Action.LEFT_CLICK_AIR))
						{	
					if (trustlevel.get(name) <= max)
					{
						int a = trustlevel.get(name);
						trustlevel.put(name, a+step);
					}

					if (trustlevel.get(name) == 0)
					{
						player.sendMessage("Wand set on " + ChatColor.RED + Clear);		
						return;
					} 
							
					if (trustlevel.get(name) <= max )
						{
						player.sendMessage("Trust level: " + ChatColor.GOLD + trustlevel.get(name));	
						return;
						}
						return;
						}
						
				if (action.equals(Action.RIGHT_CLICK_AIR))
						{ 
							if (trustlevel.get(name) > -1)
							{
								int a = trustlevel.get(name);
								trustlevel.put(name, a-step);
								togler.put(name, 0);
							}
							if (trustlevel.get(name) == -1 )
							{
								if (!togler.containsKey(name) || togler.get(name) != 1)
								{
								player.sendMessage("Wand set on " + ChatColor.GREEN + Show);
								togler.put(name, 1);
								} else Methods.playeffect(player);
								
								
								return;
							
							}
							
							if (trustlevel.get(name) == 0)
							{
								player.sendMessage("Wand set on " + ChatColor.RED + Clear);	
								
								return;
							} 
							if (trustlevel.get(name) > 0)
							{
								player.sendMessage("Trust level: " + ChatColor.GOLD + trustlevel.get(name));		
								return;
							} 
							return;
				
						}
				if (action.equals(Action.RIGHT_CLICK_BLOCK))
				{
					if (trustlevel.get(name)==-1)
					 {
						List<Location> allplayerlocations =  Methods.PlayerLocations(name);
						if (allplayerlocations != null)
						{
							
							
								for (Location loc:Methods.PlayerLocations(name))
								{
								//	player.playEffect(loc,Effect.MOBSPAWNER_FLAMES, 0);	
								player.playSound(loc, Sound.ITEM_PICKUP, 0, 0);
								}
							
						}
						return;
					 }
					Location blockLocation = e.getClickedBlock().getLocation();
					MyPairs isname = Methods.ownerOfLoc(blockLocation);
					
					if (isname != null)
					 { 
						 if (isname.name().equals(name))
						 {

							 player.sendMessage("Trust: " + ChatColor.GOLD + isname.trust());

							
							
						 } 
						else							
						{
							player.sendMessage("Block is protected by: " + ChatColor.GOLD + isname.name());
							//deny action
							
						} 
					}
					else 
					{
						player.sendMessage("Block not protected.");
						//say is not protected
						
					}
					
					
					
					
				}
			}
			
		
						
			}
		if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) 
				|| (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
						))
		{
			Location blockLocation = e.getClickedBlock().getLocation();
			MyPairs isname = Methods.ownerOfLoc(blockLocation);
			 
			 if ( isname!= null)
			 {
				 if (!isname.name().equals(player.getName()))
				 {
					 if ( playersData.isConfigurationSection(isname.name()))
					 {
						 if ( playersData.getConfigurationSection(isname.name() + ".Friends").getKeys(false).contains(player.getName()))
						 {
							 int trustonfriend = playersData.getInt(isname.name() + ".Friends." + player.getName());
							 
							 
							 List<String> coords = Methods.locationToStringList(blockLocation);
							 int trustonblock = playersData.getInt(coords.get(0) + "." + coords.get(1) + "." + coords.get(2) + "." + isname.name());
							 if (trustonfriend >= trustonblock)
							 {
								 player.sendMessage("He has full trust in you");
								 
							 } else {
								 if (e.getAction().equals(Action.LEFT_CLICK_BLOCK))
								 {
									 player.sendMessage("Not nice to destroy friends stuff");
									 e.setCancelled(true);
									 return;
								 }
								 
							 }
							 
							 if (trustonfriend <  trustonblock)
							 {
								 player.sendMessage("Friend trusts you with: "+ trustonfriend +", and this block trust level required is: " +  trustonblock);
								 e.setCancelled(true);
								 return;
							 }
						 player.sendMessage("Friend trusts you with: "+ trustonfriend +", and this block trust level required is: " +  trustonblock);
						 return;
						//player.sendMessage("bla");
					 }
						 
					 }
					 player.sendMessage("Protected");
					e.setCancelled(true);
				 }
				 
					 
			 }
		}
		
		
		}

}
