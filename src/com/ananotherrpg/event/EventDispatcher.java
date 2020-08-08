package com.ananotherrpg.event;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 *  Manages the dispatching GameEvent data to registered IEventObservers
 */
public class EventDispatcher {
    public enum GameEvent{
        KILL, ITEM, INTERACT, ARRIVAL
    }
    
    private EnumMap<GameEvent, List<IEventObserver>> channels;

    public EventDispatcher(){
        channels = new EnumMap<GameEvent, List<IEventObserver>>(GameEvent.class);
    }

    /**
     * Registers an observer to be notified when a specific <code>GameEvent</code> happens.
     */
    public void register(IEventObserver observer){
        if(!channels.containsKey(observer.getAssociatedEvent())) channels.put(observer.getAssociatedEvent(), new ArrayList<IEventObserver>());

        channels.get(observer.getAssociatedEvent()).add(observer);
    }

    /**
     * Sends all observers of the specified <code>GameEvent</code> the <code>EventData</code> package.
     * @param data The data to be sent.
     * @param event The GameEvent that occurred.
     */
    public void publish(EventData data, GameEvent event){
        if(channels.containsKey(event)){
            channels.get(event).forEach(e -> e.update(data));
        }
    }

}