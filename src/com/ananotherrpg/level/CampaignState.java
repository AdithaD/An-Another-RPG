package com.ananotherrpg.level;

/**
 * Wraps the mutable state of the Campaign. This will be the data in the save
 * file + Player Data
 */
public class CampaignState {

    private LocationGraph locationGraph;

    public CampaignState(LocationGraph maximalGraph) {
        this.locationGraph = maximalGraph;
    }

    public LocationGraph getLocationGraph() {
        return locationGraph;
    }

	public Location getLocationByID(int ID) {
		return locationGraph.findLocation(ID);
    }
    

}
