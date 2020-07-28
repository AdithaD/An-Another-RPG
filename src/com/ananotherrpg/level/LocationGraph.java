package com.ananotherrpg.level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.io.IOManager;
import com.ananotherrpg.util.Graph;

public class LocationGraph {

    private Graph<Location, Path> locationGraph;    

    private Map<Integer, Path> pathList;
    private Map<Integer, Location> locationList;

   

    public LocationGraph(Graph<Location, Path> locationGraph, Map<Integer, Path> pathList,
			Map<Integer, Location> locationList) {
		super();
		this.locationGraph = locationGraph;
		this.pathList = pathList;
		this.locationList = locationList;
	}
    
    public LocationGraph(List<Location> locationList, List<Path> pathList) {
		super();
		this.locationGraph = new Graph<Location, Path>();
		
		this.pathList = pathList.stream().collect(Collectors.toMap(Path::getID, Function.identity()));
		this.locationList = locationList.stream().collect(Collectors.toMap(Location::getID, Function.identity()));;
		
		for (Location location : locationList) {
			locationGraph.addNode(location);
		}
		
		for (Path path : pathList) {
			locationGraph.addLink(path);
		}
	}
    
	public List<Location> getKnownAccessibleLocations(Location start, List<Integer> knownIntegersIDs){
        return locationGraph.getLinks(start).stream() //List<Path> -> Stream
        .peek(e -> System.out.println("Before: " + e.getID()))
        .filter(e -> e.isTraversible()) // Filters stream to get only traversible paths
        .peek(e -> System.out.println("MIddle: " + e.getID()))
        .filter(e -> knownIntegersIDs.contains(e.getID())) // Filters stream to get only known paths
        .peek(e -> System.out.println("End: " + e.getID()))
        .map(e -> e.getOther(start)) // Gets the Location on the other side of the path
        .collect(Collectors.toList()); // Stream -> List<Location>
    }

    public List<Location> getAccessibleLocations(Location start){
        return locationGraph.getLinks(start).stream() //List<Path> -> Stream
        .filter(e -> e.isTraversible()) // Filters stream to get only traversible paths
        .map(e -> e.getOther(start)) // Gets the Location on the other side of the path
        .collect(Collectors.toList()); // Stream -> List<Location>
    }

    public void addNewLocation(Location location){
        if(!containsLocation(location)) locationGraph.addNode(location);
        locationList.put(location.getID(), location);
    }

    public void addPath(Path path){
        locationGraph.addLink(path);
        pathList.put(path.getID(), path);
    }

    public void setPathTraversability(int pathID, boolean status){
        pathList.get(pathID).setTraversible(status);
    }

    public boolean containsLocation(Location location){
        return locationGraph.getNodes().stream().anyMatch(e -> e.equals(location));
    }

	public Location findLocation(int ID) {
        List<Location> locations = locationGraph.getNodes().stream().filter(e -> e.getID() == ID).collect(Collectors.toList());
        
        if(locations.size() > 1){
            throw new IllegalStateException("There is two locations with the same ID!");
        }else if(locations.size() == 0 ){
            throw new IllegalArgumentException("That location doesn't exist");
        }else{
            return locations.get(0);
        }
    }
    
    public Set<Location> getAllLocationIDs(){
        return locationGraph.getNodes();
    }
}