package com.ananotherrpg.event;

import com.ananotherrpg.event.EventDispatcher.GameEvent;

public interface IEventObserver {

    public void update(EventData event);

    public GameEvent getAssociatedEvent();
    
}
