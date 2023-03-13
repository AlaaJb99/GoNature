package clientGUI;

import java.io.IOException;

import client.ClientUI;
import client.ParkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ServiceGuideRegister {

	private boolean signUpClicked = false;

	@FXML
	private HBox logo;

	@FXML
	private Button BackBtn;

	@FXML
	private TextField txtFirstName;

	@FXML
	private Button btnSignUp;

	@FXML
	private TextField txtLastName;

	@FXML
	private TextField txtID;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtPhone;

	@FXML
	private TextField txtCreditCard;

	@FXML
	private TextField txtCardHolder;

	@FXML
	private TextField txtExpiredDate;

	@FXML
	private TextField txtCVV;

	@FXML
	private HBox cvvIcon;

	private Stage stage;

	@FXML
	void SignUp(ActionEvent event) throws IOException {
		FXMLLoader popup = new FXMLLoader();
		String fname, lname, id, email, phone, cardNum, cardHoldId, expDate, cvv;
		fname = txtFirstName.getText();
		lname = txtLastName.getText();
		id = txtID.getText();
		email = txtEmail.getText();
		phone = txtPhone.getText();
		cardNum = txtCreditCard.getText();
		cardHoldId = txtCardHolder.getText();
		expDate = txtExpiredDate.getText();
		cvv = txtCVV.getText();
		if (!signUpClicked) {
			if (fname.isEmpty() || lname.isEmpty() || id.isEmpty() || email.isEmpty() || phone.isEmpty()) {
				ParkClient.popUpTxt = "You must enter all fields";
				Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController error = popup.getController();
				error.start(root);
			} else if ((!cardNum.isEmpty()) && (cardHoldId.isEmpty() || expDate.isEmpty() || cvv.isEmpty())) {
				ParkClient.popUpTxt = "You must enter all credit card details ";
				Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController error = popup.getController();
				error.start(root);
			} else if (cardNum.isEmpty() && (!cardHoldId.isEmpty() || !expDate.isEmpty() || !cvv.isEmpty())) {
				ParkClient.popUpTxt = "You must enter credit card number if you want to pay via card";
				Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController error = popup.getController();
				error.start(root);
			} else {
				signUpClicked = true;
				ClientUI.parkCC.accept(String.format("AddGuide %s %s %s %s %s %s %s %s %s", fname, lname, id, email,
						phone, cardNum, cardHoldId, expDate, cvv));
				ParkClient.popUpTitle = "Added new subscrption";
				ParkClient.popUpTxt = "A new guide has been added to go nature family";
				Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
				PopUp pop = popup.getController();
				pop.start(root);
			}
		} else {
			ParkClient.popUpTxt = "You have already Signed Up";
			Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController error = popup.getController();
			error.start(root);
		}
	}

	@FXML
	void getBackBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/ServiceRepresentativeHome.fxml").openStream());
		ServiceRepresentativeHome srh = loader.getController();
		srh.start(stage, root);
	}

	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/cvvIcon.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Guide/Individual Subscription");
		stage.setScene(scene);
	}
}