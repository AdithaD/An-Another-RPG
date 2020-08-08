package com.ananotherrpg.event;

import com.ananotherrpg.event.EventDispatcher.GameEvent;

/**
 *  An observer for a GameEvent. 
 * 
 * <p> The <code>IEventObserver</code> interface provides a method for updating once a GameEvent is recieved.
 * 
 * <p> The <code>IEventObserver</code> interface provides for retrieving the GameEvent it is associated with.
 */
public interface IEventObserver {

    public void update(EventData event);

    public GameEvent getAssociatedEvent();
    
}
