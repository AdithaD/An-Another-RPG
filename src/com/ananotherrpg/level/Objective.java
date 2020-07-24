package com.ananotherrpg.level;

import com.ananotherrpg.IQueryable;
import com.ananotherrpg.event.EventData;
import com.ananotherrpg.event.EventDispatcher.GameEvent;
import com.ananotherrpg.event.IEventObserver;
/**
 * Any class that can act as an objective for a quest
 */
public abstract class Objective implements IQueryable, IEventObserver{

    protected String name;
	protected int targetID;

    protected GameEvent gameEvent;

    public abstract boolean isComplete();

    public abstract String getName();

    public abstract String getListForm();

    public abstract void update(EventData data);

    @Override
	public GameEvent getAssociatedEvent() {
		return gameEvent;
	}

}
