package com.maxtree.automotive.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Location;
import com.maxtree.automotive.dashboard.service.LocationService;

/**
 * 
 * @author chens
 *
 */
public class LocationCode {
	
	/**
	 * 
	 * @param locationService
	 */
	public LocationCode(LocationService locationService) {
		this.list = locationService.findAll();
	}
	
	/**
     * 
     * @param list
     * @param locationName
     * @return
     */
    private Map<String, String> getLocationMap() {
    	HashMap<String, String> map = new HashMap<>();
    	for(Location l : list) {
    		map.put(l.getName(), l.getCode());
    	}
    	return map;
    }
    
    /**
     * 
     * @param community
     * @return
     */
    public String getCompleteLocationCode(Community community) {
    	Map<String, String> l = getLocationMap();
    	
    	StringBuilder locationCode = new StringBuilder();
    	locationCode.append(l.get(community.getProvince()));
    	if(community.getCity() != null) {
    		locationCode.append(",");
    		locationCode.append(l.get(community.getCity()));
    	}
    	if(community.getDistrict() !=null) {
    		locationCode.append(",");
    		locationCode.append(l.get(community.getDistrict()));
    	}
    	return locationCode.toString();
    }
	
    private List<Location> list = null;
}
