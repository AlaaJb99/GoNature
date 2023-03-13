package clientGUI;

import java.io.IOException;
import java.text.ParseException;
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
import javafx.stage.Stage;
import logic.Order;

public class ParkWorkerIdentify {

	private Stage stage;
	boolean flag = false; // true if visitor have an order for today

	@FXML
	private TextField txtID;

	@FXML
	void getFromCardReader(ActionEvent event) {
		ClientUI.parkCC.accept("getFromCardReader " + ParkClient.park.getParkNum());

		if (ParkClient.getFromCardReader.equals("null")) {
			txtID.setText("no ID registerd in CardReader");
		} else
			txtID.setText(ParkClient.getFromCardReader);
	}

	@FXML
	void entering(ActionEvent event) throws IOException {
		// check if park is full
		FXMLLoader loader = new FXMLLoader();
		FXMLLoader popup = new FXMLLoader();
		if (ParkClient.park.getVisitorsInPark() >= ParkClient.park.getMaxVisitors()) {

			ParkClient.popUpTxt = "Wer are sorry!\nthe number of visitors has reached limit";
			Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController pop = popup.getController();
			pop.start(root);
		}

		else {
			// get today's date in used format
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			String dateToday = formatter.format(new Date());

			// check ID
			if (txtID.getText().isEmpty() || txtID.getText().equals("no ID registerd in CardReader")) {

				ParkClient.popUpTxt = "Error in ID";
				Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController pop = popup.getController();
				pop.start(root);
			}

			else {
				ParkClient.enteringID = txtID.getText();

				// check visitor ID : Guide/Subscriber/not registered
				ClientUI.parkCC.accept("isGuide " + txtID.getText());
				ClientUI.parkCC.accept("isSubscriber " + txtID.getText());
				ClientUI.parkCC.accept("getVisitorOrders " + txtID.getText());

				// if the visitor have an order for today
				for (Order order : ParkClient.orders) {
					if (order.getVisitDate().equals(dateToday)) {
						flag = true;
						ParkClient.order = order;

						// if the visitor is 2 hours early or late he can't enter
						int visitationTimeHour = Integer.parseInt(order.getVisitTime().split(":")[0]);
						SimpleDateFormat formatter2 = new SimpleDateFormat("HH");
						int nowHour = Integer.parseInt(formatter2.format(new Date()));

						if (visitationTimeHour + 2 <= nowHour || visitationTimeHour - 2 >= nowHour) {
							ParkClient.popUpTxt = "Wer are sorry!\nYou are at least two hours early or late";
							Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
							ErrorController pop = popup.getController();
							pop.start(root);
						}

						else {

							Pane root = loader.load(getClass()
									.getResource("/clientGUI/ParkWorkerVisitConfirmationOrder.fxml").openStream());
							ParkWorkerVisitConfirmationOrder pwvc = loader.getController();
							pwvc.start(stage, root);
						}
					}
				}

				// if no order was found for the visitor for this day
				if (!flag) {
					if (ParkClient.park.getCasualVisitorsInPark() >= ParkClient.park.getDifMaxOrderVisitors()) {
						ParkClient.popUpTxt = "Wer are sorry!\nthe number of casual visitors has reached limit";
						Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
						ErrorController pop = popup.getController();
						pop.start(root);
					}

					else {

						Pane root = loader.load(getClass()
								.getResource("/clientGUI/ParkWorkerVisitConfirmationCasual.fxml").openStream());
						ParkWorkerVisitConfirmationCasual pwvcc = loader.getController();
						pwvcc.start(stage, root);
					}
				}
			}
		}

	}

	@FXML
	void exiting(ActionEvent event) throws IOException {
		FXMLLoader popup = new FXMLLoader();
		// check ID
		if (txtID.getText().isEmpty() || txtID.getText().equals("no ID registerd in CardReader")) {
			ParkClient.popUpTxt = "Field is empty";
			Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController pop = popup.getController();
			pop.start(root);
		}

		else {
			ParkClient.exitingID = txtID.getText();

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			String pdate = formatter.format(new Date());
			SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
			String pdate2 = formatter2.format(new Date());

			// update visitors in park status to "Exited"
			// sends: "updateVisitorExit pdate visitorid + status + exittime"
			ClientUI.parkCC.accept("updateVisitorExit " + pdate + " " + ParkClient.exitingID + " Exited " + pdate2);
			ClientUI.parkCC.accept("deleteOrder " + ParkClient.exitingID);

			// if visitor was not actually in park!
			if (ParkClient.updateVisitorExit[0].contentEquals("NotInPark")) {
				ParkClient.popUpTxt = "Visitor ID is not in the park!";
				Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController pop = popup.getController();
				pop.start(root);
			}

			else if (ParkClient.updateVisitorExit[0].contentEquals("Ok")) {

				// updating park numbers
				String exitingStatus = ParkClient.updateVisitorExit[1]; // casual or order
				int exitingNumber = Integer.parseInt(ParkClient.updateVisitorExit[2]);

				ParkClient.park.setVisitorsInPark(ParkClient.park.getVisitorsInPark() - exitingNumber);
				if (exitingStatus.contentEquals("casual"))
					ParkClient.park.setCasualVisitorsInPark(ParkClient.park.getCasualVisitorsInPark() - exitingNumber);

				// update park table in DB
				ClientUI.parkCC.accept("updatePark " + ParkClient.park.toString());

				// pop up
				ParkClient.popUpTxt = "Exiting Registered \nThanks for Visiting us :)";
				Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController pop = popup.getController();
				pop.start(root);
			}

		}

	}

	@FXML
	void back(ActionEvent event) throws IOException, ParseException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/ParkWorkerHome.fxml").openStream());
		ParkWorkerHome pwh = loader.getController();
		pwh.start(stage, root);
	}

	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
		stage.setTitle("Visitor Identification Page");
		stage.setScene(scene);
	}
}