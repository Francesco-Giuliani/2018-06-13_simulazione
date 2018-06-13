package it.polito.tdp.flightdelays.model;

import java.util.List;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class Route {

	private Airline airline;
	private Airport originAirport, destinationAirport;
	private List<Flight> flights;
	private double distance;
	private double averageDalay;
	
	public Route(Airline airline, Airport originAirport, Airport destinationAirport) {
		super();
		this.airline = airline;
		this.originAirport = originAirport;
		this.destinationAirport = destinationAirport;
		LatLng coordOrgnAp = new LatLng(this.originAirport.getLatitude(), this.originAirport.getLongitude());
		LatLng coordDestAp = new LatLng(this.destinationAirport.getLatitude(), this.originAirport.getLongitude());
		this.distance = LatLngTool.distance(coordOrgnAp, coordDestAp, LengthUnit.KILOMETER);
		this.averageDalay =-1.0;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((airline == null) ? 0 : airline.hashCode());
		result = prime * result + ((destinationAirport == null) ? 0 : destinationAirport.hashCode());
		result = prime * result + ((originAirport == null) ? 0 : originAirport.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Route other = (Route) obj;
		if (airline == null) {
			if (other.airline != null)
				return false;
		} else if (!airline.equals(other.airline))
			return false;
		if (destinationAirport == null) {
			if (other.destinationAirport != null)
				return false;
		} else if (!destinationAirport.equals(other.destinationAirport))
			return false;
		if (originAirport == null) {
			if (other.originAirport != null)
				return false;
		} else if (!originAirport.equals(other.originAirport))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return airline.getName() + ", " + originAirport.getId() + ", " + destinationAirport.getId();
	}
	public List<Flight> getFlights() {
		return flights;
	}
	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}
	public Airline getAirline() {
		return airline;
	}
	public Airport getOriginAirport() {
		return originAirport;
	}
	public Airport getDestinationAirport() {
		return destinationAirport;
	}
	public double getDistance() {
		return distance;
	}
	public void findAvgDelay() {
		if(this.flights == null || this.flights.isEmpty())
			this.averageDalay = -1.0;
		else {
			double avg=0;
			for(Flight f : this.flights)
				avg += f.getArrivalDelay();
			this.averageDalay = avg/this.flights.size();
		}
	}
	public double getAverageDalay() {
		return averageDalay;
	}
	
}
