package gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
//import ocsf.server.ConnectionToClient;
import server.ServerUI;

public class ConnectedClients {

	@FXML
	private Button showBtn;

	@FXML
	private TextArea txtIPHost;

	@FXML
	private Button exitBtn = null;

	private static StringBuilder sb = new StringBuilder();
//	private static int numberConnected = 0;

	//private ConnectionToClient client = null;

	public static void addClient(String ip) {
		sb.append(ip + " connected \n");
	//	numberConnected++;
	}

	@FXML
	void getShowBtn(ActionEvent event) {
    	txtIPHost.setText(sb.toString()/*+"total connected: "+numberConnected*/);
	}

	@FXML
	void getExitBtn(ActionEvent event) throws IOException {
		ServerUI.stopServer();
		System.exit(0);
	}
	
	public void start(Stage stage, Pane root) throws Exception {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ClientIp.css").toExternalForm());
		stage.setTitle("Connected Clients Screen");
		stage.setScene(scene);
	}

//	public void setClient(ConnectionToClient client) {
//		this.client = client;
//	}
}