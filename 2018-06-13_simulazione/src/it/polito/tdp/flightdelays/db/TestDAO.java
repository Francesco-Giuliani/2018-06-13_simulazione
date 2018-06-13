package it.polito.tdp.flightdelays.db;

public class TestDAO {

	public static void main(String[] args) {

		FlightDelaysDAO dao = new FlightDelaysDAO();

		System.out.println(dao.loadAllAirlines());
		System.out.println(dao.loadAllAirports());
		System.out.println(dao.loadAllFlights());
	}

}
