package it.polito.tdp.flightdelays.model;

import java.util.HashMap;

public class FlightsIdMap extends HashMap<Integer, Flight> {

	public Flight getOrPutNew(Flight f) {
		Flight old = this.get(f.getId());
		if(old!= null) {
			return old;
		}else {
			this.put(f.getId(), f);
			return f;
		}
	}
	
}
