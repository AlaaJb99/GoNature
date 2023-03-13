package cardreader;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CardReaderSimulation {
	FXMLLoader popup = new FXMLLoader();
	FXMLLoader loader = new FXMLLoader();
	private Stage stage;
	
    @FXML
    private TextField txtID;
    
    @FXML
    void register(ActionEvent event) throws IOException {
    	if(txtID.getText().isEmpty()) {
    		
    		CardReaderUI.popUpTxt = "ID field is empty";
    		Pane root = popup.load(getClass().getResource("/cardreader/Error.fxml").openStream());
    		ErrorController pop = popup.getController();
    		pop.start(root);
    	}
    	
    	else {
    		CardReaderUI.cr.handleMessageFromClientUI("CardReaderInput "+CardReader.parkNum+" "+txtID.getText());   		
    		
    		CardReaderUI.popUpTxt = "Thank You \nPlease proceed to the park worker";
    		Pane root = popup.load(getClass().getResource("/cardreader/Error.fxml").openStream());
    		ErrorController pop = popup.getController();
    		pop.start(root);
    		
    		txtID.clear();
    	}
    	
    }	
	
	
	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
	    root.setBackground(new Background(new BackgroundImage(new Image("file:src/bg.jpg"),null,null,null,new BackgroundSize(1.0, 1.0, true, true, false, false))));
		stage.setTitle("Card Reader Simulation");
		stage.setScene(scene);
		stage.show();
	}    

}


