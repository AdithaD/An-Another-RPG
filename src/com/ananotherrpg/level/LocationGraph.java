package com.ananotherrpg.level;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.util.Graph;

/**
 * A queryable representation of <code>Location</code>s in the world and <code>Path</code>s between them
 */
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
    
    /**
     * Returns a List of <code>Location</code>s that are connected by a known <code>Path</code> (by the player) to the specified <code>Location</code>
     * and are traversible.
     * @param start The location to find traversible locations from
     * @param knownPathIDs The IDs of the paths that the player knows
     * @return A List of known traversible locations
     */
	public List<Location> getKnownTraversibleLocations(Location start, List<Integer> knownPathIDs){
        return locationGraph.getLinks(start).stream() //List<Path> -> Stream
        .filter(e -> e.isTraversible()) // Filters stream to get only traversible paths
        .filter(e -> knownPathIDs.contains(e.getID())) // Filters stream to get only known paths
        .map(e -> e.getOther(start)) // Gets the Location on the other side of the path
        .collect(Collectors.toList()); // Stream -> List<Location>
    }

    /**
     * Returns a List of <code>Paths</code>s that are known the the player stemming from the specified node and are traversible.
     * @param start The location to find traversible locations from
     * @param knownPathIDs The IDs of the paths that the player knows
     * @return A List of known <code>Path</code>s stemming from the specified node.
     */
    public List<Path> getKnownTraversiblePaths(Location start, List<Integer> knownPathIDs){
        return locationGraph.getLinks(start).stream() //List<Path> -> Stream
        .filter(e -> e.isTraversible()) // Filters stream to get only traversible paths
        .filter(e -> knownPathIDs.contains(e.getID())) // Filters stream to get only known paths
        .collect(Collectors.toList()); // Stream -> List<Location>
    }


     
    /**
     * Returns a List of <code>Location</code>s that are connected by <code>Path</code> to the specified <code>Location</code> and are traversible
     * 
     * @param start The location to find traversible locations from
     * @param knownPathIDs The IDs of the paths that the player knows
     * @return A List of known traversible locations
     */
    public List<Location> getTraversibleLocations(Location start){
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
    
    public Set<Location> getAllLocations(){
        return locationGraph.getNodes();
    }

	public Set<Path> getAllPaths() {
		return locationGraph.getAllLinks();
	}
}