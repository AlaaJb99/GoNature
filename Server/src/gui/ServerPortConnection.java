package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import server.ServerUI;

public class ServerPortConnection {

	public static ConnectedClients connectedClients;

	@FXML
	private Button btnExit = null;
	@FXML
	private Button btnDone = null;

	@FXML
	private TextField portxt;
	
	private Stage stage= new Stage();
	
	//ConnectedClients connectedClients = new ConnectedClients();

	private String getport() {
		return portxt.getText();
	}

	public void Done(ActionEvent event) throws Exception {
		String p;
		p = getport();
		if (p.trim().isEmpty()) {
			System.out.println("You must enter a port number");
		} else {
			//((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/gui/ClientIp.fxml").openStream());
			connectedClients = loader.getController();
			connectedClients.start(stage, root);
			ServerUI.runServer(p);
		}
	}

	public void start(Pane root) throws Exception {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ServerPort.css").toExternalForm());
		stage.setTitle("Server");
		stage.setScene(scene);
		stage.show();
	}

	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("Exit Go Nature");
		System.exit(0);
	}

}