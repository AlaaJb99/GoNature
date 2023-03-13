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

public class OrderDeniedController {

	@FXML
	private HBox logo;

	@FXML
	private Button AddToWaitListBtn;

	@FXML
	private Button AnotherTimeBtn;

	@FXML
	private Button ExitBtn;

	private Stage stage;

	private Scene backScene;

	private Scene thiScene;

	private String backSceneTitle;

	private boolean btn = false;

	@FXML
	void getAddToWaitListBtn(ActionEvent event) throws IOException {
		// add the client to waiting list
		if (!btn) {
			btn = true;
			ClientUI.parkCC.accept("AddWait " + OrderController.o1.toString() + " " + VisitorController.v1.getId());
			Pane root = FXMLLoader.load(getClass().getResource("/clientGUI/AddWaitList.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/clientGUI/AddWaitList.css").toExternalForm());
			Stage newStage = new Stage();
			newStage.setScene(scene);
			newStage.show();
		} else {
			FXMLLoader loader = new FXMLLoader();
			ParkClient.popUpTxt = "You Already added to the waiting list";
			Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController errorController2 = loader.getController();
			errorController2.start(root);
		}
	}

	@FXML
	void getAnotherTimeBtn(ActionEvent event) throws IOException {
		ClientUI.parkCC.accept("anotherTime " + OrderController.o1.toString());
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/Availableplaces.fxml").openStream());
		AvailablePlacesController availablePlacesController = loader.getController();
		availablePlacesController.setParametres(stage, root, thiScene, "Order Denied");
		availablePlacesController.start();
	}

	@FXML
	void getExitBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/SignInVisitorPage.fxml").openStream());
		SignInVisitorPageController signInController = loader.getController();
		signInController.start(stage, root);
	}

	public void setParametres(Stage primaryStage, Pane root, Scene scene, String title) {
		backSceneTitle = title;
		stage = primaryStage;
		backScene = scene;
		thiScene = new Scene(root);
	}

	public void start() throws IOException {
		thiScene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Order Denied");
		stage.setScene(thiScene);
	}
}