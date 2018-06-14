package it.polito.tdp.flightdelays.simulation;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Model;

public class TestSimulator {

	public static void main(String[] args) {
		
		Model m = new Model();

		m.createGraph(new Airline("VX", "Prova simulator"));
		
		Simulator s = new Simulator();
		s.init(10, 10, m);
		for(Passenger p: s.getPassengers())
			System.out.println(p.printInfo());
		s.run();
		System.out.println("RESULTS OF LAST RUN: ");
		for(Passenger p: s.getPassengers())
			System.out.println(p.printInfo());
	}

}
