package clientGUI;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import client.ClientUI;
import client.ParkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Discount;

public class UpdateDiscount {

	@FXML
	private HBox logo;

	@FXML
	private Button BackBtn;

	@FXML
	private TextField txtPercent;

	@FXML
	private Button btnSubmit;

	@FXML
	private DatePicker startDate;

	@FXML
	private DatePicker endDate;

	private Stage stage;

	@FXML
	void Percent(ActionEvent event) {

	}

	@FXML
	void getBackBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/ParkManagerHome.fxml").openStream());
		ParkManagerHome pmh = loader.getController();
		pmh.start(stage, root);
	}

	@FXML
	void submit(ActionEvent event) throws IOException {
		FXMLLoader popup = new FXMLLoader();
		String precent = txtPercent.getText();
		if (precent.isEmpty() || startDate.getValue() == null || endDate.getValue() == null) {
			ParkClient.popUpTxt = "At least one of the fields is empty";
			Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
			ErrorController pop = popup.getController();
			pop.start(root);
		} else if (!precent.isEmpty())
			if (Integer.parseInt(precent) < 0 || Integer.parseInt(precent) > 100) {
				ParkClient.popUpTxt = "The discount should be between 0 and 100";
				Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController pop = popup.getController();
				pop.start(root);
			}

			else if (startDate.getValue().isAfter(endDate.getValue())) {
				ParkClient.popUpTxt = "Start Date can't be After End Date\nPlease reenter currect dates";
				Pane root = popup.load(getClass().getResource("/clientGUI/Error.fxml").openStream());
				ErrorController pop = popup.getController();
				pop.start(root);
			}

			else {
				String parkNum = ParkClient.park.getParkNum();
				double discount = Integer.parseInt(precent) / 100.0;
				String startD = startDate.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
				String endD = endDate.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
				Discount d = new Discount(parkNum, discount, startD, endD);

				ClientUI.parkCC.accept("discountRequest " + d);

				switch (ParkClient.discountRequest) {
				case "sent": {
					ParkClient.popUpTitle = "Discount Request";
					ParkClient.popUpTxt = "Discount Request submitted";
					Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
					PopUp pop = popup.getController();
					pop.start(root);
					break;
				}
				case "denied": {
					ParkClient.popUpTitle = "Discount Request";
					ParkClient.popUpTxt = "Discount Request denied\nPlease choose available dates";
					Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
					PopUp pop = popup.getController();
					pop.start(root);
					break;
				}
				case "exist": {
					ParkClient.popUpTitle = "Discount Request";
					ParkClient.popUpTxt = "Discount Request denied\nyou already submitted a discount for this start date";
					Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
					PopUp pop = popup.getController();
					pop.start(root);
					break;
				}
				}
			}
	}

	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Update Discount");
		stage.setScene(scene);
	}
}