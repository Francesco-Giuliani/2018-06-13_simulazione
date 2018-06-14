package it.polito.tdp.flightdelays.simulation;

import java.time.LocalDateTime;

public class Event implements Comparable<Event>{

	private EventType type;
	private Passenger passenger;
	private LocalDateTime time;
	
	public Event(EventType type, Passenger passenger, LocalDateTime time) {
		super();
		this.type = type;
		this.passenger = passenger;
		this.time = time;
	}

	public EventType getType() {
		return type;
	}

	public Passenger getPassenger() {
		return passenger;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return type+ " "+ passenger.getId() + " " + time;
	}

	@Override
	public int compareTo(Event o) {
		return this.time.compareTo(o.getTime());
	}
	
}
