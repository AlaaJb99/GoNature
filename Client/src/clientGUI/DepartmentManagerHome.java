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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DepartmentManagerHome {
	private int counter = 0;
	private Stage stage;

	@FXML
	private HBox logo;

	@FXML
	private Button BackBtn;

	@FXML
	private Label welcome;

	@FXML
	private Button btnProduceReports;

	@FXML
	private Button btnGenerateReports;

	@FXML
	private Button btnConfirmUpdates;

	@FXML
	private Text txtCounter;

	@FXML
	private Text discountCount;

	@FXML
	private Button btnConfirmDiscounts;

	@FXML
	void live(ActionEvent event) throws IOException {
		int casual = ParkClient.park.getCasualVisitorsInPark();
		int order = ParkClient.park.getVisitorsInPark() - ParkClient.park.getCasualVisitorsInPark();
		int total = ParkClient.park.getVisitorsInPark();
		double capacity = Double.valueOf(ParkClient.park.getVisitorsInPark())
				/ Double.valueOf(ParkClient.park.getMaxVisitors()) * 100;

		FXMLLoader popup = new FXMLLoader();
		ParkClient.popUpTxt = "Visitors in park:\ncasual: " + casual + "\norder: " + order + "\ntotal: " + total
				+ "\ncapacity: " + Math.round(capacity) + " %";
		Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
		ErrorController pop = popup.getController();
		pop.start(root);
	}

	@FXML
	void ConfirmDiscounts(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/ConfirmDiscounts.fxml").openStream());
		ConfirmDiscounts cd = loader.getController();
		cd.start(stage, root);
	}

	@FXML
	void ConfirmUpdates(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/ConfirmUpdates.fxml").openStream());
		ConfirmUpdates cu = loader.getController();
		cu.start(stage, root);
	}

	@FXML
	void GenerateReports(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/GenerateReports.fxml").openStream());
		GenerateReports gr = loader.getController();
		gr.start(stage, root);
	}

	@FXML
	void ProduceReports(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/ProduceReports.fxml").openStream());
		ProduceReports prs = loader.getController();
		prs.start(stage, root);
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
		stage.setTitle("Department Manager Home Page");
		stage.setScene(scene);
	}

	public void initialize() {
		ClientUI.parkCC.accept("getdiscountRequests");
		welcome.setText("Welcome " + ParkClient.authorized.getFirstName() + " " + ParkClient.authorized.getLastName());
		txtCounter.setText(setCounter());
		discountCount.setText("" + ParkClient.discountRequestsList.size());
	}

	private String setCounter() {
		int max = ParkClient.park.getMaxVisitors();
		int dif = ParkClient.park.getDifMaxOrderVisitors();
		int time = ParkClient.park.getVisitationPeriod();
		int maxReq = ParkClient.park.getMaxRequested();
		int difReq = ParkClient.park.getDifRequested();
		int timeReq = ParkClient.park.getTimeRequested();

		if (maxReq != 0 && maxReq != max)
			counter++;
		if (difReq != 0 && difReq != dif)
			counter++;
		if (timeReq != 0 && timeReq != time)
			counter++;

		return String.valueOf(counter);
	}
}