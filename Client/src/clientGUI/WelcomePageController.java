package clientGUI;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class WelcomePageController {
	@FXML
	private Button SignInVisitorBtn;

	@FXML
	private Button SignInAuthorizedBtn;

	@FXML
	private Button SignUpBtn;

	@FXML
	private Hyperlink contactUsBtn;

	@FXML
	private Hyperlink aboutGoNatureBtn;

	@FXML
	private HBox background;

	@FXML
	private Button MakeOrderBtn;

	private Stage stage;

	private Scene thiScene;


	@FXML
	void getSignInAuthorizedBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/AuthorizedSignInPage.fxml").openStream());
		AuthorizedSignInPage authorizedSignInPage = loader.getController();
		authorizedSignInPage.start(stage, root);
	}

	@FXML
	void getSignInVisitorBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/SignInVisitorPage.fxml").openStream());
		SignInVisitorPageController signInController = loader.getController();
		signInController.start(stage, root);
	}

	public void start(Stage primaryStage, Pane root) throws Exception {
		stage = primaryStage;
		thiScene = new Scene(root);
		thiScene.getStylesheets().add(getClass().getResource("/clientGUI/WelcomePage.css").toExternalForm());
		stage.setTitle("Welcome Page");
		stage.setScene(thiScene);
		stage.show();
	}
}