package com.ananotherrpg.inventory;

import java.util.Random;

import com.ananotherrpg.entity.Entity;

public class Weapon extends Item {

	private int damage;

	private double criticalHitChance;
	private double criticalHitMultiplier;
	
	public int calculateDamage(){
		Random r = new Random();
		int damage = this.damage;
		if(r.nextDouble() >= criticalHitChance){
			damage = (int) Math.round(damage * (1 + criticalHitMultiplier));
		}
		return damage;
	}

	public int calculateDamage(int critChanceBonus, int critMultiplierBonus) {
		Random r = new Random();
		int damage = this.damage;
		if(r.nextDouble() >= criticalHitChance + critChanceBonus){
			damage = (int) Math.round(damage * (1 + criticalHitMultiplier + critMultiplierBonus));
		}
		return damage;
	}

	@Override
	public void use(Entity player){
		player.equipWeapon(this);
	}

	//public static final Weapon UNARMED = new Weapon("Fists", 2, 0.3, 5);
}
