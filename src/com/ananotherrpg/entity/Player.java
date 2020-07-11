package com.ananotherrpg.entity;

public class Player extends Combatant {
	private int xp;

	
	public void gainXp(int xpgain) {
		xp += xpgain;
	}
	
	private void levelUp() {
		
	}
}
