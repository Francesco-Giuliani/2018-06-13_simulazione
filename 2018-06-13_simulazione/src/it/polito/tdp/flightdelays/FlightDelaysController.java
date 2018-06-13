package it.polito.tdp.flightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Model;
import it.polito.tdp.flightdelays.model.Route;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FlightDelaysController {

	private Model model;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private ComboBox<Airline> cmbBoxLineaAerea;

    @FXML
    private Button caricaVoliBtn, btnSimula;

    @FXML
    private TextField numeroPasseggeriTxtInput;

    @FXML
    private TextField numeroVoliTxtInput;

    @FXML
    void doCaricaVoli(ActionEvent event) {
    	Airline selectedAirline = this.cmbBoxLineaAerea.getValue();
    	if(selectedAirline==null) {
    		this.txtResult.setText("Attenzione: nessuna linea selezionata. Selezionare una linea per vedere le rotte.");
    		return;
    	}
    	model.createGraph( selectedAirline);
    	this.txtResult.appendText("Grafo creato.\n");
    	List<Route> tenWorstRoutes = model.worstRoutes();
    	this.txtResult.appendText("Dieci rotte peggiori: \n");
    	
    	for(Route r: tenWorstRoutes) {
    		this.txtResult.appendText(r.toString() + " weight :"+ r.getAverageDalay()/r.getDistance()+"\n");
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    		System.out.println("Simula!");
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert cmbBoxLineaAerea != null : "fx:id=\"cmbBoxLineaAerea\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert caricaVoliBtn != null : "fx:id=\"caricaVoliBtn\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert numeroPasseggeriTxtInput != null : "fx:id=\"numeroPasseggeriTxtInput\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert btnSimula!=null : "fx:id=\"btnSimuala\" was not injected: check your FXML file 'FlightDelays.fxml'.";
    }
    
	public void setModel(Model model) {
		this.model = model;
		this.cmbBoxLineaAerea.getItems().setAll(model.getAllAirlines());
	}
}
