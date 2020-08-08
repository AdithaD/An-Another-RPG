package com.ananotherrpg.entity.combat;

import java.util.List;
import java.util.Random;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.inventory.Item;
import com.ananotherrpg.entity.inventory.ItemStack;
import com.ananotherrpg.io.IOManager;

/**
 * An wrapper class for an entity participating in a <code>Combat</code>.
 * 
 * <p>This class stores combat specific information such as combat HP and defending status.
 */
public class Combatant {

    private boolean defending;
    private int hp;
    private Entity entity;

    /**
     * Class constructor
     * @param entity The entity to represent in combat
     */
    public Combatant(Entity entity) {
        this.entity = entity;
        this.hp = entity.getAttributes().calculateMaxHp();
        defending = false;
    }

    /**
     * Applies the damage from the member entity to the target entity, and outputs a message indicating so to the user
     * @param target The combatant to attack
     */
    public void attack(Combatant target) {
        int damage = entity.calculateDamage();

        IOManager.println(entity.getName() + " tries to deal " + damage + " damage to " + target.getEntity().getName());
        target.recieveDamage(entity.calculateDamage());
    }

    /**
     * Decrease HP based on the passed damage after calculating reductions.
     * 
     * <p>An combatant has the possibility to evade an attack based on evasion chance 
     * which is calculated from the entity's evasion attribute. Also, if the combatant 
     * chose to defend during its turn the damage will be reduced in half and rounded down.
     * @param damage The amount of damage incoming from another combatant.
     */
    private void recieveDamage(int damage) {

        Random r = new Random();
        if (r.nextDouble() < entity.getAttributes().calculateEvasion()) { // Evasion is dependent on Agility 
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

        if (isDead()) {
            IOManager.println(entity.getName() + " dies!");
            entity.die();
        }
    }

    /**
     * Sets the combatant to defending mode
     */
    public void defend() {
        defending = true;
    }

    /**
     *  Removes effects that have worn out thier turn duration
     */
    public void startNewTurn(){
        defending = false; // Defending only lasts a turn 

        entity.getAttributes().tick();
    }

    /**
     * Gets items from the entities inventory that are relevant in combat
     * @return A list of items relevant in combat
     */
    public List<ItemStack> getCombatItems() {
        return entity.getInventory().getItems();
    }

    /**
     * Uses an item and reports it to the user
     */
	public void combatUse(Item item) {
        IOManager.println(entity.getName() + " spends thier turn to use " + item.getName() + ". ");
        entity.use(item);
	}

	public boolean isDead() {
		return hp <= 0;
    }
    
    public Entity getEntity() {
        return entity;
    }

}