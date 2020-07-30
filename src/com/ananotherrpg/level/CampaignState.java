package com.ananotherrpg.level;

import java.util.Collections;

/**
 * Wraps the mutable state of the Campaign. This will be the data in the save
 * file + Player Data
 */
public class CampaignState {

    private LocationMananger locationManager;

    public CampaignState(LocationGraph maximalGraph) {
        this.locationManager = new LocationMananger(maximalGraph);
    }

    public LocationMananger getLocationManager() {
        return locationManager;
    }

	public Location getLocationByID(int ID) {
		return locationManager.getLocation(ID);
    }
    

}
