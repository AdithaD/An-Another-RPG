package com.ananotherrpg.entity;

import java.security.InvalidParameterException;
import java.util.EnumMap;
import java.util.List;

/**
 * The Attributes class calculates all stats for entities from four values.
 * Strength, Agility, Constitution, Charisma
 */
public class Attributes {
    public enum Attribute {
        STRENGTH, AGILITY, CONSTITUTION, CHARISMA
    }

    // Strength -> damage scaling
    private int strength;
    private static final int BASE_UNARMED_DAMAGE = 2;
    private static final double BASE_DAMAGE_SCALING_FACTOR = 0.05;
    private static final double CRITICAL_MULTIPLIER_SCALING_FACTOR = 0.02;
    // Agility -> Evasion, Critical Hit Chance Bonus,
    private int agility;
    private static double BASE_EVASION = 0.05;
    private static final double BASE_EVASION_SCALING_FACTOR = 0.01;

    private static double BASE_CRIT_CHANCE = 0.1;
    private static final double CRITICAL_CHANCE_SCALING_FACTOR = 0.02;

    // Constitution -> HP
    private int constitution;
    private static final int BASE_HP = 20;

    private static final double HP_SCALING_FACTOR = 0.06;

    // Used in combination with other attributes in persuasion checks
    private int charisma;
    private static final double PERSUASION_FACTOR = 0.2;

    // Temporary effects
    private List<AttributeModifier> modifiers;

    public Attributes(int strength, int agility, int constitution, int charisma) {
        this.strength = strength;
        this.agility = agility;
        this.constitution = constitution;
        this.charisma = charisma;
    }

    public Attributes(EnumMap<Attribute, Integer> attributeValues) {
        this.strength = attributeValues.get(Attribute.STRENGTH);
        this.agility = attributeValues.get(Attribute.AGILITY);
        this.constitution = attributeValues.get(Attribute.CONSTITUTION);
        this.charisma = attributeValues.get(Attribute.CHARISMA);
    }

    /**
     * Calculates the damage scaling for attacks based on strength
     * 
     * @return A percentage of how much weapon base damage should be scaled
     */
    public double calculateBaseDamageScaling() {
        return strength * BASE_DAMAGE_SCALING_FACTOR;
    }

    /**
     * Calculates the amount of damage an entity does without a weapon equipped.
     * 
     * @return The amount of unarmed damage.
     */
    public int calculateUnarmedDamage() {
        return (int) Math.floor((BASE_UNARMED_DAMAGE + strength) * calculateBaseDamageScaling());
    }

    /**
     * Calculates the initiative based on Agility. Initiative dictates turn order,
     * where higher intiative has their turns first.
     * 
     * @return The initiative of the entity.
     */
    public double calculateInitiative() {
        return Math.log(agility);
    }

    /**
     * Calculates the critical hit chance that should be addivitively combined with
     * the weapon's base critical hit chance based on Agility
     * 
     * @return A percentage representing bonus crit chance
     */
    public double calculateCritChanceBonus() {
        return (agility * CRITICAL_CHANCE_SCALING_FACTOR) + BASE_CRIT_CHANCE;
    }

    /**
     * Calculates the critical hit damage multiplier that should be addivitively
     * combined with the weapon's base critical hit damage multiplier based on
     * Strength
     * 
     * @return A percentage representing bonus crit damage
     */
    public double calculateCritMultiBonus() {
        return (strength * CRITICAL_MULTIPLIER_SCALING_FACTOR) + BASE_CRIT_CHANCE;
    }

    /**
     * Calculates the chance to evade an attack based on Agility
     * 
     * @return The chance to evade an attack
     */
    public double calculateEvasion() {
        return (agility * BASE_EVASION_SCALING_FACTOR) + BASE_EVASION;

    }

    /**
     * Calculates the maximum HP for the entity based on Constitution
     * 
     * @return The max HP
     */
    public int calculateMaxHp() {
        return (int) Math.floor(Math.pow(HP_SCALING_FACTOR, constitution) + BASE_HP);

    }

    /**
     * Returns a persuasiveness number, based on an attribute, for persausion checks
     * in dialogue.
     * 
     * @param attribute What attribute are we attempting to persuade with?
     * @return Persausiveness number
     */
    public int calculatePersuasiveness(Attribute attribute) {
        if (attribute == Attribute.CHARISMA) {
            return getAttributePoints(Attribute.CHARISMA);
        } else {
            return (int) Math.round((getEffectiveAttributePoints(attribute) * PERSUASION_FACTOR) + charisma);
        }

    }

    public int getAttributePoints(Attribute attribute) {
        switch (attribute) {
            case STRENGTH:
                return strength;
            case AGILITY:
                return agility;
            case CONSTITUTION:
                return constitution;
            case CHARISMA:
                return charisma;
            default:
                throw new InvalidParameterException();

        }
    }

    /**
     * Gets attribute after applying modifiers
     * 
     * @param attribute The target attribute
     * @return Attribute + modifers
     */
    private int getEffectiveAttributePoints(Attribute attribute) {
        int totalAttributeModifier = 0;
        for (AttributeModifier attributeModifier : modifiers) {
            if (attributeModifier.getAttribute() == attribute) {
                totalAttributeModifier += attributeModifier.getModifier();
            }
        }

        return getAttributePoints(attribute) + totalAttributeModifier;
    }

    public boolean hasRequiredAttributePoints(Attribute attribute, int testAmount) {
        return getEffectiveAttributePoints(attribute) < testAmount;
    }

    public void addModifier(AttributeModifier attributeModifier) {
        modifiers.add(attributeModifier);
    }

    /**
     * Signifies that a turn has passed, and to update effects accordingly
     */
    public void tick() {
        modifiers.removeIf(e -> e.isModifierOver());

        modifiers.stream().forEach(e -> e.tick());
    }

}
