package clientGUI;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cardreader.CardReaderUI;
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

public class ParkWorkerHome {

	private Stage stage;
	static String errorr = null;

	@FXML
	private HBox background;

	@FXML
	private Label welcome;

	@FXML
	private Button btnUpdate;

	@FXML
	void update(ActionEvent event) {

	}

	@FXML
	void register(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/ParkWorkerIdentify.fxml").openStream());
		ParkWorkerIdentify pwi = loader.getController();
		pwi.start(stage, root);
	}

	@FXML
	void cardReaderSimulation(ActionEvent event) throws Exception {

		Stage stage = new Stage();
		CardReaderUI crs = new CardReaderUI(ParkClient.park.toString().split(" "));
		crs.start(stage);
	}

	@FXML
	void check(ActionEvent event) throws IOException {

		FXMLLoader popup = new FXMLLoader();
		ParkClient.popUpTxt = "Number of casual available places:\n" + ParkClient.park.available();
		ParkClient.popUpTitle = "Park Available Places";
		Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
		PopUp pop = popup.getController();
		pop.start(root);
	}

	@FXML
	void Back(ActionEvent event) throws Exception {
		ParkClient.authorized = null;
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/AuthorizedSignInPage.fxml").openStream());
		AuthorizedSignInPage authorizedSignInPage = loader.getController();
		authorizedSignInPage.start(stage, root);
	}

	public void start(Stage primaryStage, Pane root) throws ParseException {
		stage = primaryStage;
		Scene scene = new Scene(root);
		stage.setTitle("Park Worker Home Page");
		stage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		DateFormat dateFormatter = new SimpleDateFormat("HH:mm");
		Date date = dateFormatter.parse("09:00");
		Timer timer = new Timer();
		timer.schedule(new MyTimeTask(), date, 3600000);
	}

	// updates capacity every hour starting when opening ParkWorkerHome page(for
	// capacity monthly report)
	private static class MyTimeTask extends TimerTask {

		public void run() {
			SimpleDateFormat formatter = new SimpleDateFormat("HH");
			String hour = formatter.format(new Date());
			SimpleDateFormat formatter2 = new SimpleDateFormat("MM/YYYY");
			String date = formatter2.format(new Date());

			if (Integer.parseInt(hour) >= 9 && Integer.parseInt(hour) <= 19) {
				System.out.println("updating db capacity report");
				ClientUI.parkCC.accept("updateCapacity " + ParkClient.park.getParkNum() + " " + date + " " + hour + " "
						+ ParkClient.park.getVisitorsInPark());
			}

		}
	}

	public void initialize() {
		welcome.setText("Welcome " + ParkClient.authorized.getFirstName() + " " + ParkClient.authorized.getLastName());
	}
}