package clientGUI;

import java.io.IOException;

import client.ClientUI;
import client.ParkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ServiceSubscrptionRegister {

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
	private TextField txtId;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtPhone;

	@FXML
	private TextField txtCardHolder;

	@FXML
	private TextField txtExpiredDate;

	@FXML
	private TextField txtCVV;

	@FXML
	private TextField txtNumOfMembers;

	@FXML
	private Label FamilyNum;

	@FXML
	private TextField txtCreditCard;

	@FXML
	private Text familystar;

	@FXML
	private ToggleGroup subType;

	@FXML
	private RadioButton individualRadio;

	@FXML
	private RadioButton familyRadio;

	@FXML
	private HBox cvvIcon;

	private Stage stage;

	@FXML
	void SignUp(ActionEvent event) throws IOException {
		FXMLLoader popup = new FXMLLoader();
		String fname, lname, id, email, phone, memberNum, cardNum, cardHoldId, expDate, cvv;
		fname = txtFirstName.getText();
		lname = txtLastName.getText();
		id = txtId.getText();
		email = txtEmail.getText();
		phone = txtPhone.getText();
		cardNum = txtCreditCard.getText();
		cardHoldId = txtCardHolder.getText();
		expDate = txtExpiredDate.getText();
		cvv = txtCVV.getText();
		if (!signUpClicked) {
			if (fname.isEmpty() || lname.isEmpty() || id.isEmpty() || email.isEmpty() || phone.isEmpty()) {
				ParkClient.popUpTxt = "You must enter all fields with *";
				Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController error = popup.getController();
				error.start(root);
			} else if ((!cardNum.isEmpty()) && (cardHoldId.isEmpty() || expDate.isEmpty() || cvv.isEmpty())) {
				ParkClient.popUpTxt = "You must enter all credit card details";
				Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController error = popup.getController();
				error.start(root);
			} else if (cardNum.isEmpty() && (!cardHoldId.isEmpty() || !expDate.isEmpty() || !cvv.isEmpty())) {
				ParkClient.popUpTxt = "You must enter credit card number if you want to pay via card";
				Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController error = popup.getController();
				error.start(root);
			} else if (individualRadio.isSelected() || familyRadio.isSelected()) {
				if (individualRadio.isSelected()) {
					memberNum = "1";
				} else
					memberNum = txtNumOfMembers.getText();
				signUpClicked = true;
				ClientUI.parkCC.accept(String.format("AddSub %s %s %s %s %s %s %s %s %s %s", fname, lname, id, email,
						phone, memberNum, cardNum, cardHoldId, expDate, cvv));
				ParkClient.popUpTitle = "Added new subscrption";
				ParkClient.popUpTxt = "A new subscriber has added to go nature family";
				Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
				PopUp pop = popup.getController();
				pop.start(root);
			} else {
				ParkClient.popUpTxt = "You must select the Subscrption Type";
				Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController error = popup.getController();
				error.start(root);
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

	@FXML
	void getFamilyRadio(ActionEvent event) {
		FamilyNum.setVisible(true);
		familystar.setVisible(true);
		txtNumOfMembers.setVisible(true);
	}

	@FXML
	void getIndividualRadio(ActionEvent event) {
		FamilyNum.setVisible(false);
		familystar.setVisible(false);
		txtNumOfMembers.setVisible(false);
	}

	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/cvvIcon.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Family Subscription");
		stage.setScene(scene);
	}
}