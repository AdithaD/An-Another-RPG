package com.ananotherrpg.event;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class EventDispatcher {
    public enum GameEvent{
        KILL, ITEM, INTERACT, ARRIVAL
    }
    
    private EnumMap<GameEvent, List<IEventObserver>> channels;

    public EventDispatcher(){
        channels = new EnumMap<GameEvent, List<IEventObserver>>(GameEvent.class);
    
        for (List<IEventObserver> channel : channels.values()) {
            channel = new ArrayList<IEventObserver>();
        }
    }

    public void register(IEventObserver observer){
        channels.get(observer.getAssociatedEvent()).add(observer);
    }

    public void publish(EventData data, GameEvent event){
        channels.get(event).forEach(e -> e.update(data));
    }

}