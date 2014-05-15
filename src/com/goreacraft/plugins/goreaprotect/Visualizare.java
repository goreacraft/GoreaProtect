package com.goreacraft.plugins.goreaprotect;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Visualizare {
	
	int time=10;
	//static int t=0;
	static int task;
	GoreaProtect main;

		public Visualizare(GoreaProtect plugin) {
			main = plugin;
			}

		public void effect(final Player p, final Location loc){
			
			task = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){

			public void run(){
				if (time !=-1)
				{ --time;}
				
				
				p.playEffect(loc,Effect.MOBSPAWNER_FLAMES,(byte) 0);
				//p.sendBlockChange(loc, Material.SPONGE, (byte) 2);
				if (time<0)  Bukkit.getServer().getScheduler().cancelTask(task);
				{
					
				}
			}
				
			},0L,5L);
			
		}
		
		
}
