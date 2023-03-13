package clientGUI;

import java.io.IOException;
import client.ClientUI;
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

public class ConfirmUpdates {

	boolean maxFlag = false;
	boolean difFlag = false;
	boolean timeFlag = false;

	@FXML
	private HBox logo;

	@FXML
	private Button BackBtn;

	@FXML
	private Button btnMaxAccept;

	@FXML
	private Button btnMaxDecline;

	@FXML
	private Label maxExisting;

	@FXML
	private Label maxRequested;

	@FXML
	private Button btnDifAccept;

	@FXML
	private Button btnDifDecline;

	@FXML
	private Label difExisting;

	@FXML
	private Label difRequested;

	@FXML
	private Button btnTimeAccept;

	@FXML
	private Button btnTimeDecline;

	@FXML
	private Label timeExisting;

	@FXML
	private Label timeRequested;

	private Stage stage;

	@FXML
	void difAccept(ActionEvent event) throws IOException {
		FXMLLoader popup = new FXMLLoader();
		if (!difRequested.getText().equals("Declined")) {
			if (ParkClient.park.getDifMaxOrderVisitors() != ParkClient.park.getDifRequested()) {
				ParkClient.park.setDifMaxOrderVisitors(ParkClient.park.getDifRequested());
				ParkClient.park.setMaxOrderVisitors(
						ParkClient.park.getMaxVisitors() - ParkClient.park.getDifMaxOrderVisitors());
				difRequested.setText("Updated");
				difExisting.setText(Integer.toString(ParkClient.park.getDifMaxOrderVisitors()));
				ParkClient.popUpTitle = "Request Accepted";
				ParkClient.popUpTxt = "You have accepted to change the \nNumber of Casual Visitor Allowed to: "
						+ ParkClient.park.getDifMaxOrderVisitors();

				Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
				PopUp pop = popup.getController();
				pop.start(root);

				ClientUI.parkCC.accept("updatePark " + ParkClient.park.toString());
			}
		} else {
			ParkClient.popUpTxt = "You have already denied this request";
			Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController pop = popup.getController();
			pop.start(root);
		}
	}

	@FXML
	void difDecline(ActionEvent event) throws IOException {
		FXMLLoader popup = new FXMLLoader();

		if (difRequested.getText().equals("Updated")) {
			ParkClient.popUpTxt = "You have already accepted this request";
			Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController pop = popup.getController();
			pop.start(root);
		} else {
			if (ParkClient.park.getDifMaxOrderVisitors() != ParkClient.park.getDifRequested()) {
				ParkClient.popUpTitle = "Request Denied";
				ParkClient.popUpTxt = "You have denied the manager request";
				difRequested.setText("Declined");
				Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
				PopUp pop = popup.getController();
				pop.start(root);
				ParkClient.park.setDifRequested(0);
				ClientUI.parkCC.accept("updatePark " + ParkClient.park.toString());
			}
		}
	}

	@FXML
	void maxAccept(ActionEvent event) throws IOException {
		FXMLLoader popup = new FXMLLoader();

		if (maxRequested.getText().equals("Declined")) {
			ParkClient.popUpTxt = "You have already denied this request";
			Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController pop = popup.getController();
			pop.start(root);
		} else {
			if (ParkClient.park.getMaxVisitors() != ParkClient.park.getMaxRequested()) {
				ParkClient.park.setMaxVisitors(ParkClient.park.getMaxRequested());

				maxRequested.setText("Updated");
				maxExisting.setText(Integer.toString(ParkClient.park.getMaxVisitors()));

				ParkClient.popUpTitle = "Request Accepted";
				ParkClient.popUpTxt = "You have accepted to change the \nMaximum Number of Visitors to: "
						+ ParkClient.park.getMaxVisitors();
				Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
				PopUp pop = popup.getController();
				pop.start(root);

				ClientUI.parkCC.accept("updatePark " + ParkClient.park.toString());
			}
		}
	}

	@FXML
	void maxDecline(ActionEvent event) throws IOException {
		FXMLLoader popup = new FXMLLoader();

		if (maxRequested.getText().equals("Updated")) {
			ParkClient.popUpTxt = "You have already accepted this request";
			Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController pop = popup.getController();
			pop.start(root);
		} else {
			if (ParkClient.park.getMaxVisitors() != ParkClient.park.getMaxRequested()) {
				ParkClient.popUpTitle = "Request Denied";
				ParkClient.popUpTxt = "The request is denied";

				maxRequested.setText("Declined");

				Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
				PopUp pop = popup.getController();
				pop.start(root);
				ParkClient.park.setMaxRequested(0);
				ClientUI.parkCC.accept("updatePark " + ParkClient.park.toString());
			}
		}
	}

	@FXML
	void timeAccept(ActionEvent event) throws IOException {
		FXMLLoader popup = new FXMLLoader();

		if (timeRequested.getText().equals("Declined")) {
			ParkClient.popUpTxt = "You have already denied this request";

			Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController pop = popup.getController();
			pop.start(root);
		} else {
			if (ParkClient.park.getVisitationPeriod() != ParkClient.park.getTimeRequested()) {
				ParkClient.park.setVisitationPeriod(ParkClient.park.getTimeRequested());
				timeRequested.setText("Updated");
				timeExisting.setText(Integer.toString(ParkClient.park.getVisitationPeriod()));
				ParkClient.popUpTitle = "Request Accepted";
				ParkClient.popUpTxt = "You have accepted to change the \nVisitation Period to: "
						+ ParkClient.park.getVisitationPeriod();
				Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
				PopUp pop = popup.getController();
				pop.start(root);
				ClientUI.parkCC.accept("updatePark " + ParkClient.park.toString());
			}
		}
	}

	@FXML
	void timeDecline(ActionEvent event) throws IOException {
		FXMLLoader popup = new FXMLLoader();

		if (timeRequested.getText().equals("Updated")) {
			ParkClient.popUpTxt = "You have already accepted this request";

			Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController pop = popup.getController();
			pop.start(root);
		} else {
			if (ParkClient.park.getVisitationPeriod() != ParkClient.park.getTimeRequested()) {
				ParkClient.popUpTitle = "Request Denied";
				ParkClient.popUpTxt = "You have denied the manager request";

				timeRequested.setText("Declined");

				Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
				PopUp pop = popup.getController();

				pop.start(root);
				ParkClient.park.setTimeRequested(0);
				ClientUI.parkCC.accept("updatePark " + ParkClient.park.toString());
			}
		}
	}

	@FXML
	void getBackBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/DepartmentManagerHome.fxml").openStream());
		DepartmentManagerHome dmh = loader.getController();
		dmh.start(stage, root);
	}

	public void start(Stage primaryStage, Pane root) {
		if (ParkClient.park.getMaxVisitors() != ParkClient.park.getMaxRequested())
			maxRequested.setText(String.valueOf(ParkClient.park.getMaxRequested()));
		else
			maxRequested.setText("No");
		if (ParkClient.park.getDifMaxOrderVisitors() != ParkClient.park.getDifRequested())
			difRequested.setText(String.valueOf(ParkClient.park.getDifRequested()));
		else
			difRequested.setText("No");
		if (ParkClient.park.getVisitationPeriod() != ParkClient.park.getTimeRequested())
			timeRequested.setText(String.valueOf(ParkClient.park.getTimeRequested()));
		else
			timeRequested.setText("No");
		stage = primaryStage;
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Confirm Updates");
		stage.setScene(scene);
	}

	public void initialize() {
		maxExisting.setText(String.valueOf(ParkClient.park.getMaxVisitors()));
		difExisting.setText(String.valueOf(ParkClient.park.getDifMaxOrderVisitors()));
		timeExisting.setText(String.valueOf(ParkClient.park.getVisitationPeriod()));
	}
}