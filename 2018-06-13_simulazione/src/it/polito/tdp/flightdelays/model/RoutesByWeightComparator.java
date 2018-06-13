package it.polito.tdp.flightdelays.model;

import java.util.Comparator;

public class RoutesByWeightComparator implements Comparator<Route> {

	@Override
	public int compare(Route o1, Route o2) {
		double diff = o1.getAverageDalay()/o1.getDistance() - o2.getAverageDalay()/o2.getDistance();
		if(diff<0)
			return 1;
		else if(diff>0)
			return -1;
		else
			return 0;
	}

}
