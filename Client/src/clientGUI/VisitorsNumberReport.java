package clientGUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import client.ClientUI;
import client.ParkClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Row;

public class VisitorsNumberReport implements Initializable {

	private Stage stage;

	@FXML
	private Text txtMonth;

	@FXML
	private TableView<Row> table;

	@FXML
	private TableColumn<Row, String> VisitorType;

	@FXML
	private TableColumn<Row, Integer> NumberOfVisitors;

	@FXML
	void Back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/PrepareReports.fxml").openStream());
		PrepareReports pr = loader.getController();
		pr.start(stage, root);
	}

	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
		stage.setTitle("Visitors Number Report");
		stage.setScene(scene);
		stage.show();
		try {
			String month = ParkClient.reportVisitorsNumber[1].substring(0, 2);
			String year = ParkClient.reportVisitorsNumber[1].substring(3, 7);
			String report = "VisitorsNumberReport_" + month + year + ".png";
			ImageIO.write(SwingFXUtils.fromFXImage(stage.getScene().snapshot(null), null), "png",
					new File("C:\\Users\\Alaa Jbareen\\Desktop\\" + report));
			ClientUI.parkCC.accept("depRep " + report);
			// ClientUI.parkCC.client.handleMessageFromClientUI(report);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		txtMonth.setText(ParkClient.reportVisitorsNumber[1]);

		ObservableList<Row> data = FXCollections.observableArrayList(
				new Row("Subscribers", Integer.parseInt(ParkClient.reportVisitorsNumber[2])),
				new Row("Groups", Integer.parseInt(ParkClient.reportVisitorsNumber[3])),
				new Row("Not registered", Integer.parseInt(ParkClient.reportVisitorsNumber[4])),
				new Row("Total", Integer.parseInt(ParkClient.reportVisitorsNumber[5])));

		VisitorType.setCellValueFactory(new PropertyValueFactory<>("s"));
		NumberOfVisitors.setCellValueFactory(new PropertyValueFactory<>("i"));
		table.setItems(data);
	}
}