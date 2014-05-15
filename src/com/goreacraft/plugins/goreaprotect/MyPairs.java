package com.goreacraft.plugins.goreaprotect;

public class MyPairs {
	
	private final String name;
	private final Integer trust;
	
	public MyPairs(String Vname, Integer Vtrust) {
		
		name = Vname;
		trust = Vtrust;
	}
	public String name() { return name; }
	public Integer trust() { return trust; }

}
