package it.polito.tdp.flightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.flightdelays.model.AiportsIdMap;
import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Airport;
import it.polito.tdp.flightdelays.model.Flight;
import it.polito.tdp.flightdelays.model.Route;

public class FlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		String sql = "SELECT id, airline from airlines";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getString("ID"), rs.getString("airline")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> loadAllAirports() {
		String sql = "SELECT id, airport, city, state, country, latitude, longitude FROM airports";
		List<Airport> result = new ArrayList<Airport>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport = new Airport(rs.getString("id"), rs.getString("airport"), rs.getString("city"),
						rs.getString("state"), rs.getString("country"), rs.getDouble("latitude"), rs.getDouble("longitude"));
				result.add(airport);
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights() {
		String sql = "SELECT id, airline, flight_number, origin_airport_id, destination_airport_id, scheduled_dep_date, "
				+ "arrival_date, departure_delay, arrival_delay, air_time, distance FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight flight = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
						rs.getString("origin_airport_id"), rs.getString("destination_airport_id"),
						rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
						rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
						rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> getAirportsForAirline(Airline selectedAirline, AiportsIdMap apIdMap) {
		String sql = "SELECT distinct id, airport, city, state, country, latitude, longitude \r\n" + 
				"from airports where id in (select ORIGIN_AIRPORT_ID\r\n" + 
				"								 from flights f\r\n" + 
				"								 where f.AIRLINE = ? )\r\n" + 
				"					or id in (	select DESTINATION_AIRPORT_ID\r\n" + 
				"								 from flights f\r\n" + 
				"								 where f.AIRLINE = ? )";
		List<Airport> result = new ArrayList<Airport>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, selectedAirline.getId());
			st.setString(2, selectedAirline.getId());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport = new Airport(rs.getString("id"), rs.getString("airport"), rs.getString("city"),
						rs.getString("state"), rs.getString("country"), rs.getDouble("latitude"), rs.getDouble("longitude"));
				
				result.add(apIdMap.getOrPut(airport));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Route> getRoutesForAirline(Airline selectedAirline, AiportsIdMap apIdMap) {
		String sql = "select *\r\n" + 
				"from flights f\r\n" + 
				"where f.AIRLINE = ?\r\n" + 
				"and f.ORIGIN_AIRPORT_ID in (select id from airports)\r\n" + 
				"and f.DESTINATION_AIRPORT_ID in (select id from airports)\r\n" + 
				"order by f.ORIGIN_AIRPORT_ID, f.DESTINATION_AIRPORT_ID";
		List<Route> result = new ArrayList<>();
		List<Flight> routeFlights = new ArrayList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, selectedAirline.getId());
			ResultSet rs = st.executeQuery();

			Route precedentRoute = null;
			Route last = null;
			boolean first=true, change = false;
			while (rs.next()) {
				Route route = new Route(selectedAirline, apIdMap.get(rs.getString("ORIGIN_AIRPORT_ID")), apIdMap.get(rs.getString("DESTINATION_AIRPORT_ID")));
				last=route;
				if(first) {
					precedentRoute = route;
					first=false;
					
					Flight flight = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
							rs.getString("origin_airport_id"), rs.getString("destination_airport_id"),
							rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
							rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
							rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
					
					routeFlights.add(flight);
					
				}else {
					
					if(route.equals(precedentRoute)) {
						Flight flight = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
								rs.getString("origin_airport_id"), rs.getString("destination_airport_id"),
								rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
								rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
								rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
						
						routeFlights.add(flight);
						
					}else {
						precedentRoute.setFlights(new ArrayList<>(routeFlights));
						precedentRoute.findAvgDelay();
						routeFlights.clear();
						result.add(precedentRoute);
						
						Flight flight = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
								rs.getString("origin_airport_id"), rs.getString("destination_airport_id"),
								rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
								rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
								rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
						
						routeFlights.add(flight);
						
						precedentRoute = route;
					}
				}
				
			}
			if(precedentRoute.equals(last)) {
				precedentRoute.setFlights(new ArrayList<>(routeFlights));
				precedentRoute.findAvgDelay();
				result.add(precedentRoute);
			}else {
				last.setFlights(routeFlights);
				last.findAvgDelay();
				result.add(last);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	/*public double getAvgDelaysForRoute(Route r) {
		String sql = "SELECT id, airline, flight_number, origin_airport_id, destination_airport_id, scheduled_dep_date, "
				+ "arrival_date, departure_delay, arrival_delay, air_time, distance FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight flight = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
						rs.getString("origin_airport_id"), rs.getString("destination_airport_id"),
						rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
						rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
						rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}*/
}
