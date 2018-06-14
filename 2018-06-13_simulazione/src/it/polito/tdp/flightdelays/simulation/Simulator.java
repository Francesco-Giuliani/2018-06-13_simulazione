package it.polito.tdp.flightdelays.simulation;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Airport;
import it.polito.tdp.flightdelays.model.Flight;
import it.polito.tdp.flightdelays.model.Model;

public class Simulator {
	
	//SIMULATION PARAM
	private int TOT_PASSENGERS;
	private int TOT_FLIGHTS;
	private final Year YEAR = Year.of(2015);
	private final LocalDateTime START_DATE = LocalDateTime.of(LocalDate.of(YEAR.getValue(), 1, 1), LocalTime.of(0, 0));

	//MONDO
	private Airline airline;
	private Model model;
	private List<Passenger> passengers;
	
	//EVENTI
	private PriorityQueue<Event> queue;
	
	//RISULTATI
	

	public void init(int k, int v, Model model) {
		//SIM PARAM
		this.TOT_PASSENGERS = k;
		this.TOT_FLIGHTS = v;
		
		//WORLD
		this.model = model;
		this.airline= model.getSelectedAirline();
		this.passengers = new ArrayList<>();		
		this.createPassengers();
		
		//EVENTS
		this.queue = new PriorityQueue<>();
		
		for(Passenger p: this.passengers) {
			
			Flight f = model.getFdao().getFirstFlightForAirlineAirportInDateTime(airline, p.getCurrentAirport(), this.START_DATE, model.getApIdMap(), model.getFidmap());
			if(f!=null) {
				p.setCurrentFlight(f);
				p.addFlight(f);
				this.queue.add(new Event(EventType.DEPARTURE, p, p.getCurrentFlight().getScheduledDepartureDate()));
			}
		}
		
	}

	private void createPassengers() {
		for(int i =1; i<=this.TOT_PASSENGERS; i++) {
			Airport deptAirport = this.randomlyAssignAirport();
			Passenger p = new Passenger(i, deptAirport);
			this.passengers.add(p);
		}
		
	}

	private Airport randomlyAssignAirport() {
		int airportIndex = (int)(Math.random()*(model.getAirports().size()));
		return model.getAirports().get(airportIndex);
	}

	public List<Passenger> getPassengers() {
		return this.passengers;
	}

	public void run() {

		Event e =null;
		while(( e = this.queue.poll())!=null) {
			Passenger p = e.getPassenger();
			Flight f = p.getCurrentFlight();
			switch(e.getType()) {
			case DEPARTURE: 				
				Event arr = new Event(EventType.ARRIVAL, p, f.getArrivalDate());
				this.queue.add(arr);
				break;
			case ARRIVAL:
				if(p.getNumFlights()<this.TOT_FLIGHTS) {
				
					p.incrementNumFlights();
					p.incrementTotDelay(f.getArrivalDelay());
					p.setCurrentAirport(f.getDestinationAirport());
					
					f = model.getFdao().getFirstFlightForAirlineAirportInDateTime(this.airline, p.getCurrentAirport(), 
							f.getArrivalDate(), model.getApIdMap(), model.getFidmap());
					if(f!=null) {
						p.setDestAirport(f.getDestinationAirport());
						p.setCurrentFlight(f);
						p.addFlight(f);
						Event av = new Event(EventType.ARRIVAL, p, f.getArrivalDate());
						this.queue.add(av);
					}
				}				
				break;
			}
		}
	}
	
	

}
