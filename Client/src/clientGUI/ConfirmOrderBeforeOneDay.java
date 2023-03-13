package clientGUI;

import java.io.IOException;

import client.ClientUI;
import client.ParkClient;
import client.VisitorController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Order;

public class ConfirmOrderBeforeOneDay {

	@FXML
	private HBox logo;

	@FXML
	private Button confirmBtn;

	@FXML
	private Button cancelBtn;

	private boolean btn;

	private Stage stage;

	@FXML
	void getConfirmBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		if (!btn) {
			// deleteBeforOneDay
			ClientUI.parkCC.accept("deleteBeforOneDay " + VisitorController.v1.getId());
			ParkClient.popUpTxt = "Your Order Confirmed";
			btn = true;
		} else {
			ParkClient.popUpTxt = "You Already confirmed";
			Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController errorController1 = loader.getController();
			errorController1.start(root);
		}
	}

	@FXML
	void getCancelBtn(ActionEvent event) throws IOException {
		ClientUI.parkCC.accept("getVisitorOrders " + VisitorController.v1.getId());
		Order o = new Order();
		for (Order order : ParkClient.orders)
			o = order;
		ClientUI.parkCC.accept("cancelOrder " + VisitorController.v1.getId() + " " + o.toString());

	}

	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Confirm Order");
		stage.setScene(scene);
	}

}