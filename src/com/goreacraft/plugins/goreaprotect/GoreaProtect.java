package com.goreacraft.plugins.goreaprotect;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;



/**
 * @author gorea
 *
 */
public class GoreaProtect extends JavaPlugin implements Listener
{
	public final Logger logger = Logger.getLogger("minecraft");





	private List<String> aliases;



	

	public static Plugin mcore;
	public static Plugin factions = Bukkit.getServer().getPluginManager().getPlugin("Factions");



	public static HashMap<String, HashMap<String, Integer>> playersDataToHashMap;


	


//	public static JavaPlugin plugin;
	
	public static GoreaProtect plugin;
	//public File playersDataFile;	
	//public static File blocksProtectedFile;
	//public static File playersDataFile;
	public static File protectionsfolder;
	//public static File userfiles;
	static Material wand;
	static List<Material> hoppers;
	static Integer MaxClaims;
	static Integer MaxFriends;
	public static File datafolder ;
	 //public static YamlConfiguration friends ;
	//public static YamlConfiguration blocksProtected;
	public static YamlConfiguration playersData;
	
		
    @Override
    public void onEnable()
    {
    	plugin= this;
    	
    	PluginDescriptionFile pdfFile = this.getDescription();
    	this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " has been enabled! " + pdfFile.getWebsite());
    	getConfig().options().copyDefaults(true);
    	saveConfig();
    	datafolder =  getDataFolder();
    	playersData = new YamlConfiguration();
    	
    	
    	protectionsfolder = new File(getDataFolder() +  File.separator + "Protections");
    	MaxClaims = getConfig().getInt("MaxClaims");
    	MaxFriends= getConfig().getInt("MaxFriends");
    	if (Material.getMaterial(getConfig().getString("Wand")) != null)
    	{
    	wand = Material.getMaterial(getConfig().getString("Wand"));
    	}else
    	 {
    		 System.out.println("[WARNING] Entry 'wand'- wrong material type. Setting it to BONE by default.");
    		 wand = Material.BONE;
	       }
    	//Methods.loadAllPlayers();
    	hoppers= new ArrayList<Material>();
    	List<String> hopperss = getConfig().getStringList("CheckOn.Hoppers");
    	for(String aaa:hopperss)    		
    		hoppers.add(Material.getMaterial(aaa));
    	
    	aliases = getCommand("goreaprotect").getAliases();
    	//Bukkit.getPluginCommand("goreaprotect").setAliases(aliases);
    	Bukkit.getServer().getPluginManager().registerEvents(this, this);
    	Bukkit.getServer().getPluginManager().registerEvents(new Events(), this);
    	Bukkit.getServer().getPluginManager().registerEvents(new WandEvents(), this);

    	Methods.loadAllPlayers();  
    	System.out.println("======================== Searching for Factions" );
    	
    	
    	
            if (getServer().getPluginManager().getPlugin("Factions") != null) {
                System.out.println(" -------------- FACTIONS DETECTED ");
                factions = Bukkit.getServer().getPluginManager().getPlugin("Factions");
            }
            if (getServer().getPluginManager().getPlugin("mcore") != null) {
                System.out.println(" -------------- MSCORE DETECTED ");
                mcore = getServer().getPluginManager().getPlugin("mcore");
            }
           //RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            
    }
   
    
    
    @Override
    public void onDisable()
    {      
    	PluginDescriptionFile pdfFile = this.getDescription();
    	this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " has been disabled!" + pdfFile.getWebsite());
    	
    	Methods.saveAllPlayersToFiles();
    
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
        	if(aliases.contains(label))
        	{
        		
        	
        		if (args.length==1 )
        		{
        			
    		        	if(args[0].equals("reload"))
    		        	{
    		        		if(sender.hasPermission("gp.reload") || sender.isOp())
    		        		{
    		        		plugin.reloadConfig();
    		        	//	Methods.findPlayerByString("gorea01").sendMessage("MaxClaims: "+ MaxClaims);
    		        	//	Methods.findPlayerByString("gorea01").sendMessage("MaxClaims in configs: "+ getConfig().getInt("MaxClaims"));
    		        		//if(MaxClaims!=getConfig().getInt("MaxClaims"))
    		        		{
    		        			
    		        			MaxClaims=getConfig().getInt("MaxClaims");
    		        			MaxFriends=getConfig().getInt("MaxFriends");
    		        		//	Methods.findPlayerByString("gorea01").sendMessage("not equal: " +MaxClaims );
    		        			for(String name: playersData.getKeys(false))
    		        			{    		        				
    		        				playersData.set(name+ ".Data.MaxClaims", MaxClaims);
    		        				playersData.set(name+ ".Data.MaxFriends", MaxFriends);
    		        			}
    		        		}
    		        		sender.sendMessage("Plugin reloaded");
    		        		return true;
    		        	}else {
    		        		//no perm
    		        		
    		        	}
    		        		
    		        	}
		        	if(args[0].equals("load"))
		        	{
		        		if(sender.hasPermission("gp.load") || sender.isOp())
		        		{
			        		Methods.loadAllPlayers();
			        		sender.sendMessage("Files loaded");
			        		return true;
		        		} else {
		        			//message no perm
		        		}
		        		
		        	}
        			
		        	if(args[0].equals("save") )
		        	{
		        		if(sender.hasPermission("gp.save") || sender.isOp())
		        		{
			        		Methods.saveAllPlayersToFiles();
			        		File alldata= new File(datafolder, "AllClaims.yml");
			        		try {
								playersData.save(alldata);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        		sender.sendMessage("Files saved");
			        		return true;
		        		} else { 
		        			//no perm
		        		}
		        	}
        			//if(sender.hasPermission("gp.claims") || sender.isOp())
		        	if(args[0].equals("claims"))
        				{
		        		sender.sendMessage("You have claimed "
        				+ ChatColor.GOLD + playersData.getInt(sender.getName() +".Data.ClaimsNow") 
        				+ ChatColor.RESET + " out of "
        				+ ChatColor.GOLD + playersData.getInt(sender.getName() + ".Data.MaxClaims") 
        				+ ChatColor.RESET + " available.");
        			return true;
        				}
		        	
		        	if(args[0].equals("claimslist"))
    				{
	        		sender.sendMessage("Claims: ["
    				+ ChatColor.GOLD + playersData.getInt(sender.getName() +".Data.ClaimsNow") 
    				+ ChatColor.RESET + "/"
    				+ ChatColor.GOLD + playersData.getInt(sender.getName() + ".Data.MaxClaims") 
    				+ ChatColor.RESET + "]");
	        		for( Location loc:Methods.PlayerLocations(sender.getName()))
	        		{
	        			sender.sendMessage(Methods.locationToStringList(loc).get(0) + " " + Methods.coordsFromLocation(loc));
	        		}
	        		if(args[0].equals("friends"))
    				{
	        		sender.sendMessage("Firends: ["
    				+ ChatColor.GOLD + playersData.getList(sender.getName() +".Friends").size() 
    				+ ChatColor.RESET + "/"
    				+ ChatColor.GOLD + playersData.getInt(sender.getName() + ".Data.MaxFriends") 
    				+ ChatColor.RESET + "]");
	        		for( Location loc:Methods.PlayerLocations(sender.getName()))
	        		{
	        			sender.sendMessage(Methods.locationToStringList(loc).get(0) + " " + Methods.coordsFromLocation(loc));
	        		}
	        		
	        		
    			return true;
    				}
        	}
        	
        	}	
        		
        		
    		
        	
        }
		//return false;
    } else {
    	//not player
    	}
    
		return false;
    }

    public static YamlConfiguration loadPlayerFile(String name)
	{
		YamlConfiguration PlayersData = new YamlConfiguration();
		
        
        File playersDataFile= new File(protectionsfolder, name + ".yml");
		 
		if (!playersDataFile.exists())
		{
	            try {
	            	playersDataFile.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
		}
		PlayersData= YamlConfiguration.loadConfiguration(playersDataFile);
		HashMap<String, Object> main = new HashMap<String, Object>();
		Set<String> aaa = PlayersData.getKeys(false);
		for (String aa: aaa)
		{
			
			main.put(aa, PlayersData.get(aa));			
		}	
		
		playersData.createSection(name, main);
		
		return playersData;
		
	}
	

	
	

	
   
   
		
	

}

	