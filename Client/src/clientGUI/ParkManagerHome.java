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
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ParkManagerHome {
	private int counter = 0;
	private StringBuilder sb = new StringBuilder();
	private Stage stage;

	@FXML
	private HBox logo;

	@FXML
	private Button btnUpdateVisitor;

	@FXML
	private Button btnUpdateDiscount;

	@FXML
	private Button BackBtn;

	@FXML
	private Label welcome;

	@FXML
	private Button btnPrepareReport;

	@FXML
	void PrepareReport(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/PrepareReports.fxml").openStream());
		PrepareReports pr = loader.getController();
		pr.start(stage, root);
	}

	@FXML
	void UpdateDiscount(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/UpdateDiscount.fxml").openStream());
		UpdateDiscount ud = loader.getController();
		ud.start(stage, root);
	}

	@FXML
	void UpdateVisitor(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/UpdateVisitorsParameters.fxml").openStream());
		UpdateVisitorsParameters uvp = loader.getController();
		uvp.start(stage, root);
	}

	@FXML
	void live(ActionEvent event) throws IOException {
		FXMLLoader popup = new FXMLLoader();
		int casual = ParkClient.park.getCasualVisitorsInPark();
		int order = ParkClient.park.getVisitorsInPark() - ParkClient.park.getCasualVisitorsInPark();
		int total = ParkClient.park.getVisitorsInPark();
		double capacity = Double.valueOf(ParkClient.park.getVisitorsInPark())
				/ Double.valueOf(ParkClient.park.getMaxVisitors()) * 100;

		ParkClient.popUpTxt = "casual: " + casual + "\norder: " + order + "\ntotal: " + total + "\ncapacity: "
				+ Math.round(capacity) + " %";
		Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
		ErrorController pop = popup.getController();
		pop.start(root);
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
		root.setBackground(new Background(new BackgroundImage(new Image("file:src/bg2.jpg"), null, null, null,
				new BackgroundSize(1.0, 1.0, true, true, false, false))));
		scene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Park Manager Home Page");
		stage.setScene(scene);
	}

	public void initialize() {

		welcome.setText("Welcome " + ParkClient.authorized.getFirstName() + " " + ParkClient.authorized.getLastName());
		messagesUpdate();
	}

	private void messagesUpdate() {
		int maxReq = ParkClient.park.getMaxRequested();
		int difReq = ParkClient.park.getDifRequested();
		int timeReq = ParkClient.park.getTimeRequested();

		if (maxReq == 0) {
			sb.append("Maximum Number of Visitors\n");
			counter++;
		}
		if (difReq == 0) {
			sb.append("Number of Casual Visitors\n");
			counter++;
		}
		if (timeReq == 0) {
			sb.append("Visitaion Period\n");
			counter++;
		}

		if (counter == 0)
			btnMessages.setText("No Messages");
		else
			btnMessages.setText("New Message");
	}

	@FXML
	private Button btnMessages;

	@FXML
	void Messages(ActionEvent event) throws IOException {
		FXMLLoader popup = new FXMLLoader();

		if (counter != 0) {
			ParkClient.popUpTxt = "The following Requests has been denied:\n" + sb.toString()
					+ "\nthe request is now terminated";
			ParkClient.popUpTitle = "Requests Denied";
			Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
			PopUp pop = popup.getController();
			pop.start(root);

			// terminating request
			ParkClient.park.setMaxRequested(ParkClient.park.getMaxVisitors());
			ParkClient.park.setDifRequested(ParkClient.park.getDifMaxOrderVisitors());
			ParkClient.park.setTimeRequested(ParkClient.park.getVisitationPeriod());
			ClientUI.parkCC.accept("updatePark " + ParkClient.park.toString());

			btnMessages.setText("No Messages");
			counter = 0;
		}

	}
}
