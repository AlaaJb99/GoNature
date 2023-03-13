package clientGUI;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ProduceReports {

	@FXML
	private HBox logo;

	@FXML
	private Button BackBtn;

	@FXML
	private ComboBox<?> comboboxReportKind;

	@FXML
	private ComboBox<?> comboboxMonth;

	@FXML
	private ComboBox<?> comboboxYear;

	private Stage stage;

	@FXML
	private Button btnProduce;

	@FXML
	void Month(ActionEvent event) {

	}

	@FXML
	void Produce(ActionEvent event) {

	}

	@FXML
	void ReportKind(ActionEvent event) {

	}

	@FXML
	void Year(ActionEvent event) {

	}

	@FXML
	void getBackBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/DepartmentManagerHome.fxml").openStream());
		DepartmentManagerHome dmh = loader.getController();
		dmh.start(stage, root);
	}

	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Produce Reports");
		stage.setScene(scene);
	}

}
