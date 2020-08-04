package com.ananotherrpg.entity.combat;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.inventory.Item;
import com.ananotherrpg.inventory.ItemStack;
import com.ananotherrpg.inventory.Potion;
import com.ananotherrpg.inventory.Weapon;
import com.ananotherrpg.io.IOManager;

public class Combatant {

    private boolean defending;
    private int hp;
    private Entity entity;

    public Combatant(Entity entity) {
        this.entity = entity;
        this.hp = entity.getAttributes().calculateMaxHp();
        defending = false;
    }

    public void attack(Combatant target) {
        int damage = entity.calculateDamage();

        IOManager.println(entity.getName() + " tries to deal " + damage + " damage to " + target.getEntity().getName());
        target.recieveDamage(entity.calculateDamage());
    }

    private void recieveDamage(int damage) {
        Random r = new Random();
        if (r.nextDouble() < entity.getAttributes().calculateEvasion()) {
            IOManager.println(entity.getName() + " evades damage");
        } else {
            int damageToBeTaken = 0;
            if (defending) {
                damageToBeTaken = (int) (damage * 0.5);
            } else {
                damageToBeTaken = damage;
            }
            hp -= damageToBeTaken;
            IOManager.println(entity.getName() + " takes " + damageToBeTaken + " points of damage! Remaining: " + hp + " / " + entity.getAttributes().calculateMaxHp());
        }

        if (hp <= 0) {
            IOManager.println(entity.getName() + " dies!");
            entity.die();
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public void defend() {
        defending = true;
    }

    public List<ItemStack> getCombatItems() {
        // TODO Filter items 
        return entity.getInventory().getItems();
    }

	public void combatUse(Item item) {
        entity.use(item);
	}

	public boolean isDead() {
		return hp <= 0 || entity.isDead();
	}

}