package clientGUI;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import client.ClientUI;
import client.ParkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ParkWorkerVisitConfirmationCasual {

	String orderType;
	private Stage stage;
	int counter = 1;

	@FXML
	private Text id;

	@FXML
	private Text txtOrderType;

	@FXML
	private TextField txtNumVisitors;

	@FXML
	private TextField txtTotalPrice;

	@FXML
	void CalculatePrice(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		if (txtNumVisitors.getText().isEmpty()) {
			// add popup
			ParkClient.popUpTxt = "The visitor information has been entered";
			Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController pop = loader.getController();
			pop.start(root);
		} else {
			int numV = Integer.parseInt(txtNumVisitors.getText());
			// price for order
			double price = VisitPrice.getPrice(numV, true);
			txtTotalPrice.setText(String.format("%.2f", price));
		}
	}

	@FXML
	void back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/ParkWorkerIdentify.fxml").openStream());
		ParkWorkerIdentify pwi = loader.getController();
		pwi.start(stage, root);
	}

	@FXML
	void confirm(ActionEvent event) throws IOException {
		FXMLLoader popup = new FXMLLoader();
		counter++;
		// need
		SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
		String pdate = formatter.format(new Date());

		SimpleDateFormat formatter2 = new SimpleDateFormat("MM/dd/yyyy");
		String pdate2 = formatter2.format(new Date());

		SimpleDateFormat formatter3 = new SimpleDateFormat("HH:mm");
		String pdate3 = formatter3.format(new Date());

		// sends: "addVisitorInPark parknum pdate2 visitorid orderorcasual type
		// numvisitors entertime + status"
		ClientUI.parkCC
				.accept("addVisitorInPark " + ParkClient.park.getParkNum() + " " + pdate2 + " " + ParkClient.enteringID
						+ " casual " + orderType + " " + txtNumVisitors.getText() + " " + pdate3 + " InPark");
		if (ParkClient.addVisitorInPark.contentEquals("Ok")) {
			// update ParkClient.park visitors and casual visitors in park
			ParkClient.park.setVisitorsInPark(
					ParkClient.park.getVisitorsInPark() + Integer.parseInt(txtNumVisitors.getText()));
			ParkClient.park.setCasualVisitorsInPark(
					ParkClient.park.getCasualVisitorsInPark() + Integer.parseInt(txtNumVisitors.getText()));

			// update park table in DB
			ClientUI.parkCC.accept("updatePark " + ParkClient.park.toString());

			// update monthly reports table
			// sends: "confirmEntering parkNum(string) pdate(MM/YYYY)(String)
			// orderType(string) numVisitors(int) totalPrice(double)"
			ClientUI.parkCC.accept("confirmEntering " + ParkClient.park.getParkNum() + " " + pdate + " " + orderType
					+ " " + txtNumVisitors.getText() + " " + txtTotalPrice.getText());

			// adding a a visitor to visitor in park
			ParkClient.popUpTxt = "The visitor information has been entered";
			Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController pop = popup.getController();
			pop.start(root);
		}

		// nod adding if the visitor is already in park
		else if (ParkClient.addVisitorInPark.contentEquals("AlreadyInPark")) {
			ParkClient.popUpTxt = "The visitor is already in park";
			Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController pop = popup.getController();
			pop.start(root);
		}
	}

	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
		stage.setTitle("Visit Confirmation Page Casual");
		stage.setScene(scene);
	}

	public void initialize() {
		orderType = (ParkClient.isGuide) ? "Guide" : (ParkClient.isSubscriber) ? "Subscriber" : "NotRegistered";
		txtOrderType.setText(orderType);
		id.setText(ParkClient.enteringID);
	}
}