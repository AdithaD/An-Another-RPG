package com.ananotherrpg.level;

import java.util.List;
import java.util.Map;

import com.ananotherrpg.util.Graph;

public class CampaignState {

    private List<Quest> quests;

    private Map<Integer, Location> IDtoLocationMap;
    private Graph<Location> locations; 
    
    private Map<Integer, Quest> IDtoQuestMap;

}
