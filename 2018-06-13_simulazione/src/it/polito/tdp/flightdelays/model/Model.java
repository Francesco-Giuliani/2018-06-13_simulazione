package it.polito.tdp.flightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.flightdelays.db.FlightDelaysDAO;

public class Model {

	private FlightDelaysDAO fdao;
	private Airline selectedAirline;
	private SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> graph;
	private List<Airport> airports;
	private AiportsIdMap apIdMap;
	private List<Flight> flights;
	private List<Route> routes;
	
	public Model() {
		fdao = new FlightDelaysDAO();
		this.apIdMap = new AiportsIdMap();
	}
	
	
	public List<Airline> getAllAirlines() {
		return fdao.loadAllAirlines();
	}


	public void createGraph(Airline selectedAirline) {
		this.selectedAirline = selectedAirline;
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.airports = fdao.getAirportsForAirline(this.selectedAirline, this.apIdMap);
		Graphs.addAllVertices(graph, airports);
		
		this.routes = fdao.getRoutesForAirline(this.selectedAirline, this.apIdMap);
		for(Route r : this.routes) {
			DefaultWeightedEdge e = graph.addEdge(r.getOriginAirport(), r.getDestinationAirport());
			graph.setEdgeWeight(e, r.getAverageDalay()/r.getDistance());
		}
		System.out.format("Grafo creato con %d vertici e %d archi.\n", graph.vertexSet().size(), graph.edgeSet().size());
		for(Airport a: graph.vertexSet())
			System.out.println(a);
		for(Route r:this.routes)
			System.out.println(r);
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

}
