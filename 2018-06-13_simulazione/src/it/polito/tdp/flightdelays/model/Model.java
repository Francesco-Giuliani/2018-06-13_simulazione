package it.polito.tdp.flightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.flightdelays.simulation.*;
import it.polito.tdp.flightdelays.db.FlightDelaysDAO;
import it.polito.tdp.flightdelays.simulation.Simulator;

public class Model {

	private FlightDelaysDAO fdao;
	private Airline selectedAirline;
	private SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> graph;
	private List<Airport> airports;
	private AirportsIdMap apIdMap;
	private List<Route> routes;
	private FlightsIdMap fidmap;
	
	private Simulator sim;
	
	public Model() {
		fdao = new FlightDelaysDAO();
		this.apIdMap = new AirportsIdMap();
		this.fidmap = new FlightsIdMap();
	}
	public List<Airline> getAllAirlines() {
		return fdao.loadAllAirlines();
	}
	public void createGraph(Airline selectedAirline) {
		this.selectedAirline = selectedAirline;
		this.apIdMap = new AirportsIdMap();
		this.fidmap = new FlightsIdMap();
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.airports = fdao.getAirportsForAirline(this.selectedAirline, apIdMap);
		Graphs.addAllVertices(graph, airports);
		
		this.routes = fdao.getRoutesForAirline(this.selectedAirline, this.apIdMap, this.fidmap);
		for(Route r : this.routes) {
			DefaultWeightedEdge e = graph.addEdge(r.getOriginAirport(), r.getDestinationAirport());
			graph.setEdgeWeight(e, r.getAverageDalay()/r.getDistance());
		}
		System.out.format("Grafo creato con %d vertici e %d archi.\n", graph.vertexSet().size(), graph.edgeSet().size());
	}
	
	public List<Route> worstRoutes(){
		
		if(this.graph==null)
			return null;
		List<Route> worst = new ArrayList<>();
		Collections.sort(this.routes, new RoutesByWeightComparator()); //IN ORDINE DECRESCENTE
		for(int i =0; i<10; i++) {
			worst.add(this.routes.get(i));
		}
		return worst;
	}
	public void simulateFlight(int k, int v, Airline selectedAirline2) {
		
		if(this.selectedAirline == null || !this.selectedAirline.equals(selectedAirline2)) {
			this.createGraph(selectedAirline2);
		}
		sim = new Simulator();
		sim.init(k, v, this);
		sim.run();
	}
	public List<Passenger> getSimulationResults() {
		return sim.getPassengers();
	}
	public FlightDelaysDAO getFdao() {
		return fdao;
	}
	public Airline getSelectedAirline() {
		return selectedAirline;
	}
	public List<Airport> getAirports() {
		return airports;
	}
	public AirportsIdMap getApIdMap() {
		return apIdMap;
	}
	public List<Route> getRoutes() {
		return routes;
	}
	public void setSelectedAirline(Airline airline) {
		this.selectedAirline = airline;
	}
	public FlightsIdMap getFidmap() {
		return fidmap;
	}
	
}
