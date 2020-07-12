package com.ananotherrpg.inventory;

public class Weapon extends Item {
	private int damage; 
	private double criticalHitChance;
	private int criticalHitMultiplier;
	
	
	
	public Weapon(String name, int damage, double criticalHitChance, int criticalHitMultiplier) {
		super(name);
		this.damage = damage;
		this.criticalHitChance = criticalHitChance;
		this.criticalHitMultiplier = criticalHitMultiplier;
	}



	public int calculateDamage() {
		return damage;
	}
}
