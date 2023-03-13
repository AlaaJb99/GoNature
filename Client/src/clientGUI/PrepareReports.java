package clientGUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import client.ParkClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PrepareReports implements Initializable {

	private Stage stage;

	@FXML
	private HBox logo;

	@FXML
	private Button BackBtn;

	@FXML
	private ComboBox<String> comboChoseReportKind;

	@FXML
	private ComboBox<String> comboChoseMonth;

	@FXML
	private ComboBox<String> comboChoseYear;

	@FXML
	void getPrepareReports(ActionEvent event) throws IOException {

		// sends: generateReport + VisitorsNumber/Usage/Income + park num + month + year
		ClientUI.parkCC.accept("generateReport " + comboChoseReportKind.getValue() + " " + ParkClient.park.getParkNum()
				+ " " + comboChoseMonth.getValue() + " " + comboChoseYear.getValue());

		if (comboChoseReportKind.getValue().equals("VisitorsNumber")) {
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/clientGUI/VisitorsNumberReport.fxml").openStream());
			VisitorsNumberReport vnr = loader.getController();
			vnr.start(stage, root);
		}

		else if (comboChoseReportKind.getValue().equals("Capacity")) {
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/clientGUI/CapacityReport.fxml").openStream());
			CapacityReport cr = loader.getController();
			cr.start(stage, root);
		}

		else if (comboChoseReportKind.getValue().equals("Income")) {
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/clientGUI/IncomeReport.fxml").openStream());
			IncomeReport ir = loader.getController();
			ir.start(stage, root);
		}
	}

	@FXML
	void getBackBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/ParkManagerHome.fxml").openStream());
		ParkManagerHome pmh = loader.getController();
		pmh.start(stage, root);
	}

	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Perpare Reports");
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		ObservableList<String> reports = FXCollections.observableArrayList("VisitorsNumber", "Capacity", "Income");
		comboChoseReportKind.setItems(reports);

		ObservableList<String> months = FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07",
				"08", "09", "10", "11", "12");
		comboChoseMonth.setItems(months);

		ObservableList<String> years = FXCollections.observableArrayList("2018", "2019", "2020", "2021");
		comboChoseYear.setItems(years);
	}

}