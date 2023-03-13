package clientGUI;

import java.io.IOException;

import client.ClientUI;
import client.OrderController;
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

public class ConfirmOrderInWaitingList {

	@FXML
	private HBox logo;

	@FXML
	private Button confirmBtn;

	@FXML
	private Button exitBtn;

	private boolean btn;

	private Stage stage;

	@FXML
	void getConfirmBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		if (!btn) {
			ClientUI.parkCC.accept("getWaitingOrder " + VisitorController.v1.getId());
			// delete the order from the waiting list the order confirmed
			ClientUI.parkCC.accept("deleteWaiting " + VisitorController.v1.getId());
			ClientUI.parkCC.accept("makeorder " + OrderController.o1.toString() + " " + VisitorController.v1.getId());
			Pane root = loader.load(getClass().getResource("/clientGUI/OrderConfirmation.fxml").openStream());
			OrderConfirmationController orderConfirmationController = loader.getController();
			orderConfirmationController.start(root, OrderController.price);
		} else {
			ParkClient.popUpTxt = "You Already confirmed";
			Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController errorController1 = loader.getController();
			errorController1.start(root);
		}
	}

	@FXML
	void getExitBtn(ActionEvent event) throws IOException {
		VisitorController.v1.setId("");
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/SignInVisitorPage.fxml").openStream());
		SignInVisitorPageController signInController = loader.getController();
		signInController.start(stage, root);
	}

	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Confirm Order");
		stage.setScene(scene);
	}
}