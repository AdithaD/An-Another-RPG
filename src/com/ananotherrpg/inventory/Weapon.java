package com.ananotherrpg.inventory;

import java.util.Random;

import com.ananotherrpg.entity.Entity;

public class Weapon extends Item {

	public Weapon(int itemID, String name, String description, int weight, int sellPrice, int damage, double critChance, double critDamageMultiplier) {
		super(itemID, name, description, weight, sellPrice, false);
		this.damage = damage;
		this.critChance = critChance;
		this.critDmgMulti = critDamageMultiplier;
	}

	private int damage;

	private double critChance;
	private double critDmgMulti;
	
	public int calculateDamage(){
		Random r = new Random();
		int damage = this.damage;
		if(r.nextDouble() >= critChance){
			damage = (int) Math.round(damage * (1 + critDmgMulti));
		}
		return damage;
	}

	public int calculateDamage(double critChanceBonus, double critDmgMultiBonus) {
		Random r = new Random();
		int damage = this.damage;
		if(r.nextDouble() >= critChance + critChanceBonus){
			damage = (int) Math.round(damage * (1 + critDmgMulti + critDmgMultiBonus));
		}
		return damage;
	}

	@Override
	public void use(Entity player){
		player.equipWeapon(this);
	}
	
	@Override
	public String getListForm() {
		return "Weapon: " + name;
	}
	public static final Weapon FISTS = new Weapon(0100, "Fists", "A man's right hand (and left)", 0, 0, 1, 0.5, 3);
}
