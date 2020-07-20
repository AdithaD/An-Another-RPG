package com.ananotherrpg.level;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ananotherrpg.Identifiable;
import com.ananotherrpg.entity.Entity;
import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.io.IOManager.ListType;
import com.ananotherrpg.io.IOManager.SelectionMethod;
import com.ananotherrpg.util.Graph;

public class LocationManager {

    private Graph<Location> campaignLocationGraph;
    private Location currentLocation; 

    public Location getCurrentLocation(){
        return currentLocation;
    }

	public List<Entity> getPermanentEntitiesInCurrentLocation() {
		return currentLocation.getPermanentEntities();
    }
    
    public List<Location> getAccessibleLocations(){
        return campaignLocationGraph.getAdjacentNodes(currentLocation);
    }
    
    public void moveTo() {

		if (getAccessibleLocations().isEmpty()) {
			IOManager.println("There is no where to go to!");
		} else {
			IOManager.println("Where would you like to go ?");

			Optional<Location> opLocation = IOManager.listAndQueryUserInputAgainstIdentifiers(getAccessibleLocations(),
					ListType.NUMBERED, SelectionMethod.NUMBERED);

			if (opLocation.isPresent()) {
				currentLocation = opLocation.get();
				IOManager.println("You move to " + getCurrentLocation().getName());
			} else {
				IOManager.println("Staying here is fine for now...");
			}

		}

	}

	public void lookAround() {
		currentLocation.printLocationDetails();

		if (getAccessibleLocations().isEmpty()) {
			IOManager.println("I can't see anywhere to go right now");
		} else {
			IOManager.listIdentifiers(getAccessibleLocations(), ListType.BULLET);
		}
	}

	public List<? extends Identifiable> getListOfIdentifiablesInCurrentLocation() {
		return List.of(currentLocation.getPermanentEntities(), currentLocation.getItemStacks(), getAccessibleLocations()).stream().flatMap(Collection::stream).collect(Collectors.toList());
	}
}
