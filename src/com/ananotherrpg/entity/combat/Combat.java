package com.ananotherrpg.entity.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.entity.Attributes.Attribute;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.inventory.ItemStack;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;


/**
 * A representation of a combat in the game world. 
 * 
 * It manages the turn order and user choice in combat. 
 */
public class Combat {

    private boolean finished = false;

    private enum CombatAction {
        ATTACK, DEFEND, ITEM, FLEE
    }

    private static Map<String, CombatAction> actions = new HashMap<String, CombatAction>();
    static {
        actions.put("Attack", CombatAction.ATTACK);
        actions.put("Defend", CombatAction.DEFEND);
        actions.put("Use Item", CombatAction.ITEM);
        actions.put("Flee", CombatAction.FLEE);
    }

    private Combatant player;
    private Set<Combatant> opponents;

    /**
     *  The class constuctor
     * 
     *  <p> Instanstiates a combat instance between a player and a single target
     */
    public Combat(Entity player, Entity target) {
        opponents = new HashSet<Combatant>();

        opponents.add(new Combatant(target));
        this.player = new Combatant(player);
    }

    private static final Comparator<Combatant> INTIATIVE_ORDER = new Comparator<Combatant>() {
        public int compare(Combatant e1, Combatant e2) {
            if (e1.getEntity().getAttributes().calculateInitiative() > e2.getEntity().getAttributes()
                    .calculateInitiative())
                return 1;
            else if (e1.getEntity().getAttributes().calculateInitiative() < e2.getEntity().getAttributes()
                    .calculateInitiative())
                return -1;
            else
                return 0;
        };
    };

    /**
     * Starts the combat loop
     * 
     * <p> The loop will repeat until:
     * <ul>
     *      <li> All opponents are dead
     *      <li> The player is dead
     *      <li> The player has fleed
     * 
     */
    public void start() {
        List<Combatant> combatants = new ArrayList<Combatant>();
        combatants.addAll(opponents);
        combatants.add(player);
        // Combat Loop
        while (!finished) {

            Collections.sort(combatants, INTIATIVE_ORDER);

            for (Combatant combatant : combatants) {
                if (!combatant.isDead()) {
                    if (player.equals(combatant)) {
                        doPlayerTurn();
                    } else {
                        doOpponentTurn(combatant);
                    }
                }

                if (opponents.stream().allMatch(e -> e.isDead()) || player.isDead()) {
                    endCombat();
                    break;
                }
            }
        }
    }

    /**
     * Conducts a turn for the specific non-player combatant
     * @param combatant A non-player combatant
     */
    private void doOpponentTurn(Combatant combatant) {
        combatant.attack(player);
    }

    /**
     * Conducts the player's turn with input from the user
     */
    private void doPlayerTurn() {
        IOManager.println("What will you do? ");

        boolean actionComplete = false;
        while (!actionComplete) { // Loops until an action successful completes.
            Optional<String> actionName = IOManager.listAndQueryUserInputAgainstStrings(
                    new ArrayList<String>(actions.keySet()), ListType.ONE_LINE, SelectionMethod.TEXT, false);

            if (!actionName.isPresent())
                throw new IllegalStateException("No action chosen during combat");

            CombatAction action = actions.get(actionName.get());
            switch (action) {
                case ATTACK:
                    Optional<Combatant> target = queryOpponents();
                    if (target.isPresent()) {
                        player.attack(target.get());
                        actionComplete = true;
                    }

                case DEFEND:
                    player.defend();
                    actionComplete = true;

                    break;
                case ITEM:
                    Optional<ItemStack> itemStack = IOManager.listAndQueryUserInputAgainstIQueryables(
                            player.getCombatItems(), ListType.NUMBERED, SelectionMethod.NUMBERED, true);

                    if (itemStack.isPresent()) {
                        player.combatUse(itemStack.get().getItem());
                        actionComplete = true;
                    }
                    break;
                case FLEE:
                    // The player can only flee if they have an agility that is 5 or more than every enemy's agility.
                    if (opponents.stream()
                            .anyMatch(e -> player.getEntity().getAttributes().getAttributePoints(Attribute.AGILITY)
                                    - e.getEntity().getAttributes().getAttributePoints(Attribute.AGILITY) < 5)) {
                        IOManager.println("Fleeing unsuccessful");
                    } else {
                        IOManager.println("You start to get away");
                        actionComplete = true;
                        endCombat();
                    }
                    break;
                default:
                    break;
            }

        }

    }

    private void endCombat() {
        IOManager.println("The battle draws to a close.");
        finished = true;
    }

    /**
     * Queries the user to select an opponent
     * @return The chosen opponent
     */
    private Optional<Combatant> queryOpponents() {
        Map<String, Combatant> nameCombatantMap = opponents.stream()
                .collect(Collectors.toMap(e -> e.getEntity().getName(), Function.identity()));
        return IOManager.listAndQueryUserInputAgainstCustomMap(nameCombatantMap, ListType.NUMBERED,
                SelectionMethod.NUMBERED, true);
    }

    public Set<Combatant> getKilledOpponents(){
        return opponents.stream().filter(e -> e.isDead()).collect(Collectors.toSet());
    }
}
