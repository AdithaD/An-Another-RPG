package com.ananotherrpg.level;

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
}
