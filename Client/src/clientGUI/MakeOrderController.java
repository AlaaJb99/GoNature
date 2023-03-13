package clientGUI;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.ClientUI;
import client.OrderController;
import client.ParkClient;
import client.VisitorController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MakeOrderController implements Initializable {

	private String OrderType;

	@FXML
	private Button BackBtn;

	@FXML
	private DatePicker txtDate;

	@FXML
	private ComboBox<String> ParkName;

	@FXML
	private Button SubmitBtn = null;

	@FXML
	private TextField txtEmailAddress;

	@FXML
	private TextField txtNumberOfVisitors;

	@FXML
	private TextField txtTime;

	@FXML
	private TextField txtPhoneNumber;

	@FXML
	private HBox logo;

	private Stage stage;

	private Scene thiScene;

	ObservableList<String> list;

	@FXML
	private ToggleGroup orderType;

	@FXML
	private RadioButton defualtRadio;

	@FXML
	private RadioButton familyRadio;

	@FXML
	private RadioButton smallGroupRadio;

	@FXML
	private RadioButton organizedGroupRadio;

	@FXML
	private Label NumberOfvisitor;

	@FXML
	private Label VisitorsNumber;

	@FXML
	private Label isGuide;

	@FXML
	private Label notGuide;

	private boolean submit = false;

	@FXML
	void getDefualtRadio(ActionEvent event) {
		OrderType = "Individual";
		VisitorsNumber.setVisible(false);
		NumberOfvisitor.setVisible(false);
		txtNumberOfVisitors.setVisible(false);
		txtNumberOfVisitors.setPromptText("");
	}

	@FXML
	void getFamilyRadio(ActionEvent event) {
		OrderType = "Family";
		VisitorsNumber.setVisible(true);
		NumberOfvisitor.setVisible(true);
		txtNumberOfVisitors.setVisible(true);
		txtNumberOfVisitors.setPromptText("");
	}

	@FXML
	void getSmallGroupRadio(ActionEvent event) {
		OrderType = "SmallGroup";
		VisitorsNumber.setVisible(true);
		NumberOfvisitor.setVisible(true);
		txtNumberOfVisitors.setVisible(true);
		txtNumberOfVisitors.setPromptText("");
	}

	@FXML
	void getOrganizedGroupRadio(ActionEvent event) {
		// check if this is a guide
		OrderType = "OrganizedGroup";
		VisitorsNumber.setVisible(true);
		NumberOfvisitor.setVisible(true);
		txtNumberOfVisitors.setVisible(true);
		txtNumberOfVisitors.setPromptText("1-15");
	}

	private void setParkComboBox() {
		ArrayList<String> parkList = new ArrayList<String>();
		parkList.add("1");
		parkList.add("2");
		parkList.add("3");
		list = FXCollections.observableArrayList(parkList);
		ParkName.setItems(list);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setParkComboBox();
	}

	@FXML
	void getBackBtn(ActionEvent event) throws IOException {
		VisitorController.v1.setId("");
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/SignInVisitorPage.fxml").openStream());
		SignInVisitorPageController signInController = loader.getController();
		signInController.start(stage, root);
	}

	@FXML
	void getSubmitBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		if (!submit) {
			String park, date, time, visitorsNumber, email, phoneNum;
			park = getParkName();
			date = getDate();
			time = getTime();
			visitorsNumber = getNumberOfVisitors();
			email = getEmail();
			phoneNum = getPhoneNumber();
			if (park.trim().isEmpty() || date.trim().isEmpty() || time.trim().isEmpty() || email.trim().isEmpty()
					|| phoneNum.trim().isEmpty()) {
				ParkClient.popUpTxt = "You must enter all fields";
				Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController errorController1 = loader.getController();
				errorController1.start(root);
			} else if (time.length() != 5) {
				ParkClient.popUpTxt = "The time must be like 09:00";
				Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController errorController1 = loader.getController();
				errorController1.start(root);
			} else if (OrderType == null) {
				ParkClient.popUpTxt = "You must choose the order type";
				Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController errorController1 = loader.getController();
				errorController1.start(root);
			} else if (!(OrderType.equals("Individual")) && visitorsNumber.trim().isEmpty()) {
				ParkClient.popUpTxt = "You must enter the number of visitors";
				Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController errorController1 = loader.getController();
				errorController1.start(root);
			} else {
				if (OrderType.equals("Individual"))
					visitorsNumber = "1";
				if (organizedGroupRadio.isSelected()
						&& (Integer.parseInt(visitorsNumber) > 15 || Integer.parseInt(visitorsNumber) < 1)) {
					ParkClient.popUpTxt = "Number of visitors should be between 1 and 15 ";
					Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
					ErrorController errorController1 = loader.getController();
					errorController1.start(root);
				} else {
					// we need to check if there is an available place
					ClientUI.parkCC.accept("makeorder " + park + " " + date + " " + time + " " + visitorsNumber + " "
							+ email + " " + phoneNum + " " + OrderType + " " + VisitorController.v1.getId());
					if (OrderController.o1.getOrderNum().equals("NoPlace")) {
						// orderdenied
						loader = new FXMLLoader();
						Pane root = loader.load(getClass().getResource("/clientGUI/OrderDenied.fxml").openStream());
						OrderDeniedController orderDeniedController = loader.getController();
						orderDeniedController.setParametres(stage, root, thiScene, "Make Order");
						orderDeniedController.start();
					} else {
						/* order confirmed */
						submit = true;
						loader = new FXMLLoader();
						Pane root = loader
								.load(getClass().getResource("/clientGUI/OrderConfirmation.fxml").openStream());
						OrderConfirmationController orderConfirmationController = loader.getController();
						orderConfirmationController.start(root, OrderController.price);
					}
				}
			}
		} else {
			ParkClient.popUpTxt = "You have already made an order";
			Pane root = loader.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController errorController1 = loader.getController();
			errorController1.start(root);
		}

	}

	private String getParkName() {
		if (ParkName.getValue() != null)
			return ParkName.getValue();
		return "";
	}

	private String getEmail() {
		return txtEmailAddress.getText();
	}

	private String getNumberOfVisitors() {
		return txtNumberOfVisitors.getText();
	}

	private String getTime() {
		return txtTime.getText();
	}

	private String getDate() {
		if (txtDate.getValue() != null)
			return txtDate.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		return "";
	}

	private String getPhoneNumber() {
		return txtPhoneNumber.getText();
	}

	public void setParametres(Stage primaryStage, Pane root, Scene scene, String title) {
		stage = primaryStage;
		thiScene = new Scene(root);
	}

	public void start() throws IOException {
		thiScene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		VisitorsNumber.setVisible(false);
		NumberOfvisitor.setVisible(false);
		txtNumberOfVisitors.setVisible(false);
		txtNumberOfVisitors.setPromptText("");
		if (ParkClient.isGuide) {
			notGuide.setVisible(false);
			isGuide.setVisible(true);
			organizedGroupRadio.setVisible(true);
			txtNumberOfVisitors.setPromptText("1-15");
		} else {
			isGuide.setVisible(false);
			organizedGroupRadio.setVisible(false);
		}

		stage.setTitle("Make Order");
		stage.setScene(thiScene);
	}
}