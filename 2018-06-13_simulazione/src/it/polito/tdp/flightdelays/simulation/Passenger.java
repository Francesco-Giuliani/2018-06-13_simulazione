package it.polito.tdp.flightdelays.simulation;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.flightdelays.model.Airport;
import it.polito.tdp.flightdelays.model.Flight;

public class Passenger {
	
	private int id;
	private Airport currentAirport;
	private Airport destAirport;
	private Flight currentFlight;
	private List<Flight> flightsTaken;
	private int numFlights;
	private double totDelay;
	
	public Passenger(int id, Airport currentAirport) {
		super();
		this.id = id;
		this.currentAirport = currentAirport;
		this.flightsTaken = new ArrayList<>();
		this.numFlights =0;
		this.totDelay = 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Passenger other = (Passenger) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public Airport getCurrentAirport() {
		return currentAirport;
	}

	public Airport getDestAirport() {
		return destAirport;
	}

	public Flight getCurrentFlight() {
		return currentFlight;
	}

	public List<Flight> getFlightsTaken() {
		return flightsTaken;
	}

	public int getNumFlights() {
		return numFlights;
	}

	public double getTotDelay() {
		return totDelay;
	}

	public void setCurrentAirport(Airport currentAirport) {
		this.currentAirport = currentAirport;
	}

	public void setDestAirport(Airport destAirport) {
		this.destAirport = destAirport;
	}

	public void setCurrentFlight(Flight currentFlight) {
		this.currentFlight = currentFlight;
		this.destAirport = this.currentFlight.getDestinationAirport();
	}

	public void setFlightsTaken(List<Flight> flightsTaken) {
		this.flightsTaken = flightsTaken;
	}
	public void incrementNumFlights() {
		this.numFlights++;
	}
	public void incrementTotDelay(double minDelay) {
		this.totDelay+=minDelay;
	}
	public void addFlight(Flight flight) {
		this.flightsTaken.add(flight);
	}

	@Override
	public String toString() {
		return id + ", " + numFlights + ", " + totDelay;
	}

	public int getId() {
		return id;
	}

	public String printInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("Passenger: "+this.id+" from ");
		sb.append(this.currentAirport.getId()+" to ");
		sb.append(this.destAirport.getId()+"\n");
		sb.append("Numero voli presi: "+this.numFlights+"\n");
		sb.append("Ritardo accumulato: "+this.totDelay+"\n");
		return sb.toString();
	}
	
}
