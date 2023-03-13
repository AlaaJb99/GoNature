package clientGUI;

import java.io.IOException;
import java.text.ParseException;

import client.ClientUI;
import client.ParkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AuthorizedSignInPage {

	@FXML
	private HBox logo;

	@FXML
	private Pane frame;

	@FXML
	private Button btnBack;

	@FXML
	private Label FirstName;

	@FXML
	private TextField txtUserName;

	@FXML
	private TextField txtPassword;

	@FXML
	private Button btnLogIn;

	private Stage stage;

	PopUp pp = new PopUp();

	@FXML
	void Back(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/WelcomePage.fxml").openStream());
		WelcomePageController welcomePageController = loader.getController();
		welcomePageController.start(stage, root);
	}

	@FXML
	void LogIn(ActionEvent event) throws IOException, ParseException {
		FXMLLoader loader = new FXMLLoader();

		String name = txtUserName.getText();
		String password = txtPassword.getText();

		if (name.isEmpty() || password.isEmpty()) {
			ParkClient.popUpTxt = "At least one of the fields is empty";
			Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController errorController = loader.getController();
			errorController.start(root);
			// pp.start();
		}

		else {
			ClientUI.parkCC.accept(String.format("signIn %s %s", name, password));

			if (ParkClient.authorized == null) {
				System.out.println(ParkClient.authorized);
				ParkClient.popUpTxt = "Username Or Password is inValid";
				Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController errorController = loader.getController();
				errorController.start(root);
			}

			else {
				System.out.println(ParkClient.authorized.getRole());
				switch (ParkClient.authorized.getRole()) {

				case "department manager": {
					Pane root = loader
							.load(getClass().getResource("/clientGUI/DepartmentManagerHome.fxml").openStream());
					DepartmentManagerHome dmh = loader.getController();
					dmh.start(stage, root);
					break;
				}

				case "park manager": {
					Pane root = loader.load(getClass().getResource("/clientGUI/ParkManagerHome.fxml").openStream());
					ParkManagerHome pmh = loader.getController();
					pmh.start(stage, root);
					break;
				}

				case "service representative": {
					Pane root = loader
							.load(getClass().getResource("/clientGUI/ServiceRepresentativeHome.fxml").openStream());
					ServiceRepresentativeHome srh = loader.getController();
					srh.start(stage, root);
					break;
				}

				case "park worker": {
					Pane root = loader.load(getClass().getResource("/clientGUI/ParkWorkerHome.fxml").openStream());
					ParkWorkerHome pwh = loader.getController();
					pwh.start(stage, root);
					break;
				}

				default: {
					ParkClient.popUpTxt = "Error User '" + ParkClient.authorized.getFirstName() + " "
							+ ParkClient.authorized.getLastName() + "'is not allowd in the park !!";
					Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
					ErrorController errorController = loader.getController();
					errorController.start(root);

					break;
				}

				}
			}
		}

	}

	public void start(Stage primaryStage, Pane root) throws Exception {
		stage = primaryStage;
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/SignIn.css").toExternalForm());
		stage.setTitle("Authrized User Sign In Page");
		stage.setScene(scene);
	}
}