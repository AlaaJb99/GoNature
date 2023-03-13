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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ParkWorkerVisitConfirmationOrder {

	private Stage stage;
	int counter = 1;
	private String orderType;
	FXMLLoader popup = new FXMLLoader();

	@FXML
	private Text txtName;

	@FXML
	private Text txtOrderType;

	@FXML
	private Text txtNumVisitors;

	@FXML
	private Text txtTotalPrice;

	@FXML
	void back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/ParkWorkerIdentify.fxml").openStream());
		ParkWorkerIdentify pwi = loader.getController();
		pwi.start(stage, root);
	}

	@FXML
	void confirm(ActionEvent event) throws IOException {
		counter++;

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
						+ " order " + orderType + " " + ParkClient.order.getVisitorsNum() + " " + pdate3 + " InPark");
		if (ParkClient.addVisitorInPark.contentEquals("Ok")) {
			// update ParkClient.park visitors
			ParkClient.park.setVisitorsInPark(
					ParkClient.park.getVisitorsInPark() + Integer.parseInt(txtNumVisitors.getText()));

			// update park table in DB
			ClientUI.parkCC.accept("updatePark " + ParkClient.park.toString());

			// update monthly reports table
			// sends: "confirmEntering parkNum(string) pdate(MM/YYYY)(String)
			// orderType(string) numVisitors(int) totalPrice(double)"
			ClientUI.parkCC.accept("confirmEntering " + ParkClient.park.getParkNum() + " " + pdate + " " + orderType
					+ " " + ParkClient.order.getVisitorsNum() + " " + Double.parseDouble(txtTotalPrice.getText()));

			ParkClient.popUpTxt = "The visitor information has been entered";
			Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController pop = popup.getController();
			pop.start(root);
		}

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
		stage.setTitle("Visit Confirmation Page Order");
		stage.setScene(scene);
	}

	public void initialize() {
		txtName.setText(ParkClient.enteringID);

		orderType = (ParkClient.isGuide) ? "Guide" : (ParkClient.isSubscriber) ? "Subscriber" : "NotRegistered";
		txtOrderType.setText(orderType);

		txtNumVisitors.setText("" + ParkClient.order.getVisitorsNum());
		double price = VisitPrice.getPrice(ParkClient.order.getVisitorsNum(), true);
		txtTotalPrice.setText(String.format("%.2f", price));
	}
}