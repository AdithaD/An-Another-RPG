package com.ananotherrpg.level.quest;

import com.ananotherrpg.IQueryable;
import com.ananotherrpg.event.EventData;
import com.ananotherrpg.event.EventDispatcher.GameEvent;
import com.ananotherrpg.event.IEventObserver;
/**
 * An objective for a <code>Quest</code>. 
 * 
 * <p> Has method for checking if its complete.
 */
public abstract class Objective implements IQueryable, IEventObserver{

    protected String name;
	protected int targetID;

    // The GameEvent that this objective will update with
    private GameEvent gameEvent;

    public abstract boolean isComplete();

    public abstract void update(EventData data);

    public abstract String getListForm();

    public String getName(){
        return name;
    }

    @Override
	public GameEvent getAssociatedEvent() {
		return gameEvent;
    }
    
	public int getTargetID() {
		return targetID;
    }
    
    public GameEvent getGameEvent() {
        return gameEvent;
    }

    public Objective(String name, int targetID, GameEvent gameEvent){
        this.name = name;
        this.targetID = targetID;
        this.gameEvent = gameEvent;
    }
    
}
