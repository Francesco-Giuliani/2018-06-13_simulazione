package it.polito.tdp.flightdelays.model;

import java.util.HashMap;

public class AirportsIdMap extends HashMap<String, Airport> {

	public Airport getOrPut(Airport searched) {
		Airport old = this.get(searched.getId());
		if(old==null) {
			old = searched;
			this.put(old.getId(), old);
		}
		return old;
	}
}
