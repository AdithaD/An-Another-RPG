package com.ananotherrpg.inventory;

import java.util.Random;

import com.ananotherrpg.entity.Entity;

public class Weapon extends Item {

	public Weapon(int itemID, String name, String description, int weight, int sellPrice, int damage, double critChance, double critDamageMultiplier) {
		super(itemID, name, description, weight, sellPrice);
		this.damage = damage;
		this.critChance = critChance;
		this.critDamgeMultiplier = critDamageMultiplier;
	}

	private int damage;

	private double critChance;
	private double critDamgeMultiplier;
	
	public int calculateDamage(){
		Random r = new Random();
		int damage = this.damage;
		if(r.nextDouble() >= critChance){
			damage = (int) Math.round(damage * (1 + critDamgeMultiplier));
		}
		return damage;
	}

	public int calculateDamage(int critChanceBonus, int critMultiplierBonus) {
		Random r = new Random();
		int damage = this.damage;
		if(r.nextDouble() >= critChance + critChanceBonus){
			damage = (int) Math.round(damage * (1 + critDamgeMultiplier + critMultiplierBonus));
		}
		return damage;
	}

	@Override
	public void use(Entity player){
		player.equipWeapon(this);
	}

	public static final Weapon FISTS = new Weapon(0100, "Fists", "A man's right hand (and left)", 0, 0, 1, 0.5, 3);
}
