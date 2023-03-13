package clientGUI;

import java.io.IOException;
import client.ParkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ServiceRepresentativeHome {

	@FXML
	private HBox logo;

	@FXML
	private Button btnGuide;

	@FXML
	private Button btnFamilyIndividual;

	@FXML
	private Button BackBtn;

	@FXML
	private Label welcome;

	private Stage stage;

	@FXML
	void getFamilyIndividual(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/ServiceSubscrptionRegister.fxml").openStream());
		ServiceSubscrptionRegister sfs = loader.getController();
		sfs.start(stage, root);
	}

	@FXML
	void Guide(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/ServiceGuideRegister.fxml").openStream());
		ServiceGuideRegister sgs = loader.getController();
		sgs.start(stage, root);
	}

	@FXML
	void getBackBtn(ActionEvent event) throws Exception {
		ParkClient.authorized = null;
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/AuthorizedSignInPage.fxml").openStream());
		AuthorizedSignInPage authorizedSignInPage = loader.getController();
		authorizedSignInPage.start(stage, root);
	}

	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Service Representative Home Page");
		stage.setScene(scene);
	}

	public void initialize() {
		welcome.setText("Welcome " + ParkClient.authorized.getFirstName() + " " + ParkClient.authorized.getLastName());
	}
}