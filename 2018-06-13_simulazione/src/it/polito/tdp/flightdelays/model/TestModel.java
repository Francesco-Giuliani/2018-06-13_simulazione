package it.polito.tdp.flightdelays.model;

public class TestModel {

	public static void main(String[] args) {
		Model m = new Model();
		String alId = "AA";
		Airline a=null;
		for(Airline ai: m.getAllAirlines()) {
			if(ai.getId().compareTo(alId)==0) {
				a=ai;
				break;
			}
		}
		System.out.println("Airline: "+a);

		m.createGraph(a);
	}

}
