package com.goreacraft.plugins.goreaprotect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Methods {
	//private static GoreaProtect plugin = GoreaProtect.plugin;
	//YamlConfiguration playersData = plugin.playersData;
	
	
	
	
	
	static void saveAllPlayersToFiles(){
    	for( String playername : GoreaProtect.playersData.getKeys(false))
    	{
    		File playerfile = new File(GoreaProtect.datafolder +  File.separator + "Protections");
    		File fileplayer= new File(playerfile, playername + ".yml");

    		ConfigurationSection playerdata =  GoreaProtect.playersData.getConfigurationSection(playername);

    		YamlConfiguration PlayersDatayml = new YamlConfiguration();

    		for (String aaa:playerdata.getKeys(false))
    		PlayersDatayml.set(aaa, playerdata.get(aaa));
    		
    		
    		try {
				PlayersDatayml.save(fileplayer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
    	}    
    }

	Location locationFromList(List<String> list){

		World world = GoreaProtect.plugin.getServer().getWorld((String) list.get(0));

		Location loc = new Location ( world, Integer.parseInt(list.get(1)), Integer.parseInt(list.get(2)), Integer.parseInt(list.get(3)));
		return loc;
	}

    static Player findPlayerByString(String name) 
	{
		for ( Player player : Bukkit.getServer().getOnlinePlayers())
		{
			if(player.getName().equals(name)) 
			{
				return player;
			}
		}
		
		return null;
	}
    
static void loadAllPlayers()
	{
		
	chechfolder();
        
        for (File file2 : GoreaProtect.protectionsfolder.listFiles()) 
        {
		    if (file2.isFile() && file2.getName().endsWith(".yml")) 
		    {
		    	
		    	System.out.println("------------------------Checking " + file2.getName().substring(0, file2.getName().lastIndexOf(".")));

		    	loadPlayerFile(file2.getName().substring(0, file2.getName().lastIndexOf(".")));
		    }
		}
	}
        
        static YamlConfiguration loadPlayerFile(String name)
    	{
    		YamlConfiguration PlayersData = new YamlConfiguration();
    		
    		
    		checkplayerfile(name);
    		
    		File playersDataFile= new File(GoreaProtect.protectionsfolder, name + ".yml");
    		PlayersData= YamlConfiguration.loadConfiguration(playersDataFile);
    		HashMap<String, Object> main = new HashMap<String, Object>();
    		Set<String> aaa = PlayersData.getKeys(false);
    		for (String aa: aaa)
    		{
    			
    			main.put(aa, PlayersData.get(aa));			
    		}	
    		
    		GoreaProtect.playersData.createSection(name, main);
    		
    		return GoreaProtect.playersData;
    		
    	}
        
        
        static void chechfolder()
        {
             if(!GoreaProtect.protectionsfolder.exists())
             	{
            	 GoreaProtect.protectionsfolder.mkdirs();
             	}
        }
        static void checkplayerfile(String name){
        	
        	File playersDataFile= new File(GoreaProtect.protectionsfolder, name + ".yml");
        	chechfolder();
    		if (!playersDataFile.exists())
    		{
    	            try {
    	            	playersDataFile.createNewFile();
    	            } catch (IOException e) {
    	                e.printStackTrace();
    	            }
    	            checkFixPlayerData(name); 
    		}
        }
        
        
        static void loadOrCreatePlayerFile(String name){
        	File playerfile = new File(GoreaProtect.plugin.getDataFolder() +  File.separator + "Protections"+  File.separator + name+ ".yml");
        	checkFixPlayerData(name);
        	if(!playerfile.exists())
        	{
        		
        		
        		YamlConfiguration PlayersDatayml = new YamlConfiguration();
        		ConfigurationSection playerdata =  GoreaProtect.playersData.getConfigurationSection(name);
        		for (String aaa:playerdata.getKeys(false))
        		{
            		PlayersDatayml.set(aaa, playerdata.get(aaa));
        		}

        		try {
					PlayersDatayml.save(playerfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
        		
        	} else loadPlayerFile(name);
        	
        }
        static void checkFixPlayerData(String name)
        {
    		
    		 if ( !GoreaProtect.playersData.isConfigurationSection(name))
    			{
    			// System.out.println("-----------------------------------------  checkPlayerData ");
    			 GoreaProtect.playersData.createSection(name);  
    			}
    		 if ( !GoreaProtect.playersData.isConfigurationSection(name + "." + "Friends"))
    		 	{
    		 	GoreaProtect.playersData.createSection(name + "." + "Friends");

    		 	}    		
    		
    		 if ( !GoreaProtect.playersData.isConfigurationSection(name + "." + "Data"))
    			{
    			 GoreaProtect.playersData.createSection(name + "." + "Data");
    			 HashMap<String,Integer> asd = new HashMap<String,Integer>();
    			 			asd.put("MaxClaims", GoreaProtect.MaxClaims);
    			 			asd.put("ClaimsNow", 0);
    			 			asd.put("MaxFriends", GoreaProtect.MaxFriends);
    			 GoreaProtect.playersData.createSection(name + "." + "Data", asd);	 			
    			 }
    		 if ( !GoreaProtect.playersData.isConfigurationSection(name + "." + "Claims"))
 			{
    			 GoreaProtect.playersData.createSection(name + "." + "Claims");
 			}
    	 }
        

		static void playeffect(Player player) {
    		player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
    		for (Location loc:PlayerLocations(player.getName()))
    		{			
    			//player.sendMessage(" " +loc.distanceSquared(player.getLocation()));
    			player.playEffect(loc,Effect.MOBSPAWNER_FLAMES, 4);
    		}
    		
    	}





    	static MyPairs ownerOfLoc(Location loc){
    		//Methods.findPlayerByString("gorea01").sendMessage("ownerofloc");
    	List<String> coords = locationToStringList(loc);
    	MyPairs aaa=null;
    	//String ownername;
    	for (String name: GoreaProtect.playersData.getKeys(false))
    	{
    		//Methods.findPlayerByString("gorea01").sendMessage( "Check for " + name);
    		//Methods.findPlayerByString("gorea01").sendMessage("Check world " + coords.get(0));
    		if (GoreaProtect.playersData.isConfigurationSection(name + ".Claims." + coords.get(0)))
    		{
    			//Methods.findPlayerByString("gorea01").sendMessage("Am gasit world " + coords.get(0));
    			//Methods.findPlayerByString("gorea01").sendMessage("Check chunk " + coords.get(1));
    			if (GoreaProtect.playersData.isConfigurationSection(name + ".Claims." + coords.get(0) + "." + coords.get(1)))
    			{
    				//Methods.findPlayerByString("gorea01").sendMessage("Am gasit chunk " + coords.get(1));
    				//Methods.findPlayerByString("gorea01").sendMessage("Check coords "  + name + ".Claims." + coords.get(0)+ "." + coords.get(1) + "." + coords.get(2) );
    				if (GoreaProtect.playersData.getConfigurationSection(name + ".Claims." + coords.get(0)+ "." + coords.get(1)).getKeys(false).contains(coords.get(2)))
    				{
    					//Set<String> ownernames = GoreaProtect.playersData.getConfigurationSection(name + ".Claims." + coords.get(0)+ "." + coords.get(1) + "." + coords.get(2)).getKeys(false);
    					//ownername = ownernames.iterator().next();
    					//System.out.println(" OWNER NAME " + ownername);
    					//Methods.findPlayerByString("gorea01").sendMessage("am gasit" +coords.get(2));
    					 int value = GoreaProtect.playersData.getInt(name + ".Claims." + coords.get(0)+ "." + coords.get(1) + "." + coords.get(2));
    					 aaa = new MyPairs(name, value);
    					
    				} //else {Methods.findPlayerByString("gorea01").sendMessage("Nu am gasit coords "  + coords.get(2) );}
    			}
    		} 
    	}
		return aaa;
    		
    	}

    	static void setProtection(String name, Location location, Object trust){

    		GoreaProtect.playersData.createSection(name + ".Claims." 
    				+ location.getWorld().getName() + "." 
    				+ ChunkToStringFromLocation(location) + "."
    				+ coordsFromLocation(location));

    		GoreaProtect.playersData.set(name + ".Claims." 
    				+ location.getWorld().getName() + "." 
    				+ ChunkToStringFromLocation(location) + "."
    				+ coordsFromLocation(location)
    				, trust);
    		GoreaProtect.playersData.set(name + ".Data.ClaimsNow", Methods.claimsnow(name));
    		GoreaProtect.playersData.set(name + ".Data.ClaimsNow", Methods.claimsnow(name));
    		} 
    	
    	static void removeProtection( String name, Location location){
    		
    		List<String> loc = locationToStringList(location);		
    		
    		GoreaProtect.playersData.set( name + ".Claims." + loc.get(0) + "." + loc.get(1) + "." + loc.get(2), null);		
    		
    		//Methods.findPlayerByString("gorea01").sendMessage("Check if null: "+ GoreaProtect.playersData.getConfigurationSection(name + ".Claims." 
    			//	+ location.getWorld().getName() + "." 
    			//	+ ChunkToStringFromLocation(location)).getKeys(false).size());
    		if (GoreaProtect.playersData.getConfigurationSection(name + ".Claims." 
    				+ location.getWorld().getName() + "." 
    				+ ChunkToStringFromLocation(location)).getKeys(false).size()==0)
    		{
    			GoreaProtect.playersData.set(name + ".Claims." 
        				+ location.getWorld().getName() + "." 
        				+ ChunkToStringFromLocation(location),null);
    			if (GoreaProtect.playersData.getConfigurationSection(name + ".Claims." 
        				+ location.getWorld().getName()).getKeys(false).size()==0)
    			{
    				GoreaProtect.playersData.set(name + ".Claims." 
        				+ location.getWorld().getName(), null);
    			}
    			
    		}
    		GoreaProtect.playersData.set(name + ".Data.ClaimsNow", Methods.claimsnow(name));
    		} 
    	
    	static Integer claimsnow(String name){
    		int clamisnr=0;
    		for(String world: GoreaProtect.playersData.getConfigurationSection(name + ".Claims.").getKeys(false))
    			for(String chunk: GoreaProtect.playersData.getConfigurationSection(name + ".Claims." + world).getKeys(false))
    				clamisnr += GoreaProtect.playersData.getConfigurationSection(name + ".Claims." + world + "." + chunk).getKeys(false).size();
			return clamisnr;
    		
    	}


    	static List<String> locationToStringList(Location loc){    
    		List<String> string = new ArrayList<String>();
    		string.add(loc.getWorld().getName());
    		string.add(loc.getChunk().getX() + " " +  loc.getChunk().getZ());
    		string.add(loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());

    	return string;
    	}
    	
    	 
    	 
    	static int getint(String name, List<String> coords){

    		int trust = (int) GoreaProtect.playersData.getConfigurationSection(name + ".Claims."  + coords.get(0) + "." + coords.get(1) + "." + coords.get(2)).getValues(true).values().iterator().next();
    		 
    		return trust;
    		 
    	 }

    	 static List<Location> PlayerLocations(String name)
    	 {
    	List<Location> locs = new ArrayList<Location>();
    	if (GoreaProtect.playersData.isConfigurationSection(name))
    	  {
    		 Set<String> worlds = GoreaProtect.playersData.getConfigurationSection(name + ".Claims").getKeys(false);
    		// Methods.findPlayerByString("gorea01").sendMessage("start");
    		 if (worlds != null)
    		 {
    			// Methods.findPlayerByString("gorea01").sendMessage("world not null");
    			 for ( String world:worlds)
    			 {
    				// Methods.findPlayerByString("gorea01").sendMessage("check for world: " + world);
    				 Set<String> chunks = GoreaProtect.playersData.getConfigurationSection(name + ".Claims." + world).getKeys(false);
    				 for ( String chunk:chunks)
    				 {
    					// Methods.findPlayerByString("gorea01").sendMessage("check for chunk " + chunk);
    					 Set<String> coords = GoreaProtect.playersData.getConfigurationSection(name + ".Claims." + world + "." + chunk ).getKeys(false);
    					 for(String coord:coords)
    					 {
    						// Methods.findPlayerByString("gorea01").sendMessage("check for coord " + coord);
    						// String[] xyz = coord.split(" ");
    						// Methods.findPlayerByString("gorea01").sendMessage(world.concat(" " + coord));
    						 locs.add(locationfromString(world.concat(" " + coord)));
    					 }
    				 }
    			 }
    			 
    			return locs;
    		 } 
    	 else
    		 {
    		// Methods.findPlayerByString("gorea01").sendMessage("world is null");
    		 return null;
    		 }
    		 
    		 
    	  }
    		 Methods.findPlayerByString("gorea01").sendMessage("no player in playerdata");
    		 return null;
    	 }
    	 
    	 
    	 static Location locationfromString(String string)
    	 {
    		 String[] locs = string.split(" ");
    		 
    		 //add world
    		 Location loc = new Location(Bukkit.getWorld(locs[0]), Integer.parseInt(locs[1]), Integer.parseInt(locs[2]), Integer.parseInt(locs[3]));
    		 
    		return loc;
    		 
    	 }
    	 
    	 
    	 static String ChunkToStringFromLocation(Location loc)
    	 {
    		 String chunk = loc.getChunk().getX() + " " + loc.getChunk().getZ();
    		return chunk;
    		 
    	 }
    	 
    	 
    	 static String coordsFromLocation(Location loc)
    	 {
    		 String coords=loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
    		return coords;
    	 }
}
