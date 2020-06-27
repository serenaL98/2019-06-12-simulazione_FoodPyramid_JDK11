package it.polito.tdp.food;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtCalorie;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private ComboBox<String> boxIngrediente;

    @FXML
    private Button btnDietaEquilibrata;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaDieta(ActionEvent event) {

    	txtResult.clear();
    	
    	txtResult.appendText("La dieta perfetta: \n");
    	
    	String scelto = this.boxIngrediente.getValue();
    	if(scelto == null) {
    		txtResult.setText("Scegliere un ingrediente dalla lista!");
    		return;
    	}
    	
    	txtResult.appendText("\n"+this.model.dieta(scelto));
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	String calorie = this.txtCalorie.getText();
    	if(calorie == null) {
    		txtResult.setText("Inerire un numero reale per indicare le calorie.\n");
    		return;
    	}

    	try {
    		float cal = Float.parseFloat(calorie);
    		
    		this.boxIngrediente.getItems().addAll(this.model.ingredienti(cal));
    		
    		txtResult.appendText("Crea grafo...");
    		
    		this.model.creaGrafo();
    		
    		txtResult.appendText("\n\n#VERTICI: "+this.model.numeroVertici());
    		txtResult.appendText("\n#ARCHI: "+this.model.numeroArchi());
    		
    		txtResult.appendText("\n\n"+this.model.calorieCibi());
    		
    	}catch(NumberFormatException e) {
    		txtResult.setText("Non è stato inserito un numero reale!\n");
    		return;
    	}
    }

    @FXML
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxIngrediente != null : "fx:id=\"boxIngrediente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDietaEquilibrata != null : "fx:id=\"btnDietaEquilibrata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
