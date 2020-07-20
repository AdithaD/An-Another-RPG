package com.ananotherrpg.inventory;

import com.ananotherrpg.entity.Player;

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

	@Override
	public void use(Player player){
		player.equip(this);
	}

	public static final Weapon UNARMED = new Weapon("Fists", 2, 0.3, 5);
}
