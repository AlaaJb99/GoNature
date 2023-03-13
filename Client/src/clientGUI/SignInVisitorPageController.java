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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SignInVisitorPageController {

	@FXML
	private HBox logo;

	@FXML
	private Pane frame;

	@FXML
	private Button signInBtn = null;

	@FXML
	private Button backBtn = null;

	@FXML
	private TextField txtVistorId;

	private Stage stage;

	private String getID() {
		return txtVistorId.getText();
	}

	@FXML
	void getBackBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/WelcomePage.fxml").openStream());
		WelcomePageController welcomePageController = loader.getController();
		welcomePageController.start(stage, root);
	}

	@FXML
	void getSignInBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Scene scene = signInBtn.getScene();
		String id;
		// check if the id entered is valid
		id = getID();
		if (id.trim().isEmpty()) {
			ParkClient.popUpTxt = "You must enter an id or subscription number";
			Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController errorController1 = loader.getController();
			errorController1.start(root);

		} else {
			ClientUI.parkCC.accept("signin " + id);
			if (VisitorController.chara) {
				ParkClient.popUpTxt = "Id Or Subscription number Contains only numbers";
				Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController errorController2 = loader.getController();
				errorController2.start(root);
				VisitorController.chara = false;
			} else if (VisitorController.wating && !VisitorController.available) {
				ParkClient.popUpTxt = "You are In the waiting list";
				Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController errorController2 = loader.getController();
				errorController2.start(root);
				VisitorController.wating = false;
			} else if (VisitorController.haveOrder && !(VisitorController.beforDay)) {
				ClientUI.parkCC.accept("getVisitorOrders " + VisitorController.v1.getId());
				Pane root = loader.load(getClass().getResource("/clientGUI/MyOrdersPage.fxml").openStream());
				MyOrdersPageController myOrdersPageController = loader.getController();
				// myOrdersPageController.setParametres(, scene, "Sign In Visitor");
				myOrdersPageController.start(stage, root);
				VisitorController.haveOrder = false;
			} else if (VisitorController.haveOrder && VisitorController.beforDay) {
				Pane root = loader
						.load(getClass().getResource("/clientGUI/ConfirmOrderBeforeOneDay.fxml").openStream());
				ConfirmOrderBeforeOneDay confirmOrder = loader.getController();
				confirmOrder.start(stage, root);
				VisitorController.haveOrder = false;
				VisitorController.beforDay = false;
			} else if (VisitorController.available) {
				// open new window
				Pane root = loader
						.load(getClass().getResource("/clientGUI/ConfirmOrderInWaitingList.fxml").openStream());
				ConfirmOrderInWaitingList confirmOrder = loader.getController();
				confirmOrder.start(stage, root);
				VisitorController.available = false;
			} else {
				/* check if this is a guide to show the Organized Group */
				ClientUI.parkCC.accept("isGuide " + VisitorController.v1.getId());
				Pane root = loader.load(getClass().getResource("/clientGUI/MakeOrderPage.fxml").openStream());
				MakeOrderController makeOrderController = loader.getController();
				makeOrderController.setParametres(stage, root, scene, "Sign In Visitor");
				makeOrderController.start();
			}
		}
	}

	public void start(Stage primaryStage, Pane root) throws IOException {
		stage = primaryStage;
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/SignIn.css").toExternalForm());
		stage.setTitle("Sign In Visitor");
		stage.setScene(scene);
	}
}