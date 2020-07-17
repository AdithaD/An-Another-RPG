package com.ananotherrpg.level;

import java.util.List;

import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.entity.Player;
import com.ananotherrpg.util.Graph;

public class CampaignState {

    public Graph<Location> locations;
    public Location currentLocation;
    
    public List<Quest> activeQuests;

    public Player player;

    public boolean isComplete;

    public CampaignState(Graph<Location> locations, Location currentLocation, List<Quest> activeQuests, Player player, boolean isComplete){
        this.locations = locations;
        this.currentLocation = currentLocation;
        this.activeQuests = activeQuests;
        this.player = player;
        this.isComplete = isComplete;
    }

    public List<Location> getAccessibleLocations(){
        return locations.getAdjacentNodes(currentLocation);
    }

    public List<Entity> getEntitiesInCurrentLocation(){
        return currentLocation.getPermanentEntities();
    }

    public void setCurrentLocation(Location location){
        this.currentLocation = location;
    }
}