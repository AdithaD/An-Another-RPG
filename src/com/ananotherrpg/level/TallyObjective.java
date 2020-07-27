package com.ananotherrpg.level;

import com.ananotherrpg.event.EventData;
import com.ananotherrpg.event.EventDispatcher.GameEvent;
/**
 *  The Tally objective completes when a game event associated with the given ID has happened a certain amount of times.
 */
public class TallyObjective extends Objective {

    private int targetQuantity;
    private int count;

    public TallyObjective(String name, int targetID, GameEvent gameEvent, int quantity) {
        super(name, targetID, gameEvent);
        this.targetQuantity = quantity;
        count = 0;
    }

    @Override
    public boolean isComplete() {
        return count >= targetQuantity;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getListForm() {
        return name + ": (" + count + "/" +  targetQuantity + ") killed!";
    }

    @Override
    public void update(EventData data) {
        if(data.getID() == targetID){
            count++;
        }

    }

}