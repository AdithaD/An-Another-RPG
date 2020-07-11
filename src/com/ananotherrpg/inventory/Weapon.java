package com.ananotherrpg.inventory;

public class Weapon extends Item {
	private int damage; 
	private int criticalHitChance;
	private int criticalHitMultiplier;
	
	public int calculateDamage() {
		return damage;
	}
}
