package clientGUI;

import java.io.IOException;

import client.ClientUI;
import client.ParkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UpdateVisitorsParameters {

	@FXML
	private HBox logo;

	@FXML
	private Button BackBtn;

	@FXML
	private TextField txtMax;

	@FXML
	private TextField txtTime;

	@FXML
	private TextField txtDifference;

	@FXML
	private Button btnRequestForUpdates;

	private Stage stage;

	@FXML
	void RequestForUpdates(ActionEvent event) throws IOException {
		FXMLLoader popup = new FXMLLoader();

		int max = Integer.parseInt(txtMax.getText());
		int dif = Integer.parseInt(txtDifference.getText());
		int time = Integer.parseInt(txtTime.getText());

		if (txtMax.getText().isEmpty() || txtTime.getText().isEmpty() || txtDifference.getText().isEmpty()) {
			ParkClient.popUpTxt = "At least one of the fields is empty";
			Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
			PopUp pop = popup.getController();
			pop.start(root);
		}

		else if (dif < 0 || max <= dif || time <= 0 || time > 6) {
			ParkClient.popUpTxt = "Error in one or more parameters \nplease check your choises!";
			Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
			PopUp pop = popup.getController();
			pop.start(root);
		}

		else if (max != ParkClient.park.getMaxVisitors() || dif != ParkClient.park.getDifMaxOrderVisitors()
				|| time != ParkClient.park.getVisitationPeriod()) {
			ParkClient.park.setMaxRequested(max);
			ParkClient.park.setDifRequested(dif);
			ParkClient.park.setTimeRequested(time);
			ClientUI.parkCC.accept("updatePark " + ParkClient.park.toString());

			ParkClient.popUpTxt = "Your request has been sent to your department manager";
			ParkClient.popUpTitle = "Parameters Request Sent";
			Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
			PopUp pop = popup.getController();
			pop.start(root);
		}

	}

	@FXML
	void getBackBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/ParkManagerHome.fxml").openStream());
		ParkManagerHome pmh = loader.getController();
		pmh.start(stage, root);
	}

	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Update Parameters");
		stage.setScene(scene);
	}

	public void initialize() {
		txtMax.setText(String.valueOf(ParkClient.park.getMaxVisitors()));
		txtDifference.setText(String.valueOf(ParkClient.park.getDifMaxOrderVisitors()));
		txtTime.setText(String.valueOf(ParkClient.park.getVisitationPeriod()));
	}
}