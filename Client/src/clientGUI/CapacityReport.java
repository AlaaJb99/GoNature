package clientGUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

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

public class CapacityReport implements Initializable {

	private Stage stage;

	@FXML
	private Text txtMonth;

	@FXML
	private TableView<Row> table;

	@FXML
	private TableColumn<Row, String> VisitationHour;

	@FXML
	private TableColumn<Row, String> NumberOfVisitors;

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
		stage.setTitle("Capacity Report");
		stage.setScene(scene);
		stage.show();
		try {
			String month = ParkClient.reportVisitorsNumber[1].substring(0, 2);
			String year = ParkClient.reportVisitorsNumber[1].substring(3, 7);
			String report = "CapacityReport_" + month + year + ".png";
			ImageIO.write(SwingFXUtils.fromFXImage(stage.getScene().snapshot(null), null), "png",
					new File("C:\\Users\\Alaa Jbareen\\Desktop\\" + report));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		txtMonth.setText(ParkClient.reportCapacity[1]);

		SimpleDateFormat formatter = new SimpleDateFormat("dd");
		String days = formatter.format(new Date());
		int x = (Integer.parseInt(days));

		ObservableList<Row> data = FXCollections.observableArrayList(
				new Row("09",
						(int) (Double.parseDouble(ParkClient.reportCapacity[2]) * 100
								/ (ParkClient.park.getMaxVisitors() * x))),
				new Row("10",
						(int) (Double.parseDouble(ParkClient.reportCapacity[3]) * 100
								/ (ParkClient.park.getMaxVisitors() * x))),
				new Row("11",
						(int) (Double.parseDouble(ParkClient.reportCapacity[4]) * 100
								/ (ParkClient.park.getMaxVisitors() * x))),
				new Row("12",
						(int) (Double.parseDouble(ParkClient.reportCapacity[5]) * 100
								/ (ParkClient.park.getMaxVisitors() * x))),
				new Row("13",
						(int) (Double.parseDouble(ParkClient.reportCapacity[6]) * 100
								/ (ParkClient.park.getMaxVisitors() * x))),
				new Row("14",
						(int) (Double.parseDouble(ParkClient.reportCapacity[7]) * 100
								/ (ParkClient.park.getMaxVisitors() * x))),
				new Row("15",
						(int) (Double.parseDouble(ParkClient.reportCapacity[8]) * 100
								/ (ParkClient.park.getMaxVisitors() * x))),
				new Row("16",
						(int) (Double.parseDouble(ParkClient.reportCapacity[9]) * 100
								/ (ParkClient.park.getMaxVisitors() * x))),
				new Row("17",
						(int) (Double.parseDouble(ParkClient.reportCapacity[10]) * 100
								/ (ParkClient.park.getMaxVisitors() * x))),
				new Row("18",
						(int) (Double.parseDouble(ParkClient.reportCapacity[11]) * 100
								/ (ParkClient.park.getMaxVisitors() * x))),
				new Row("19", (int) (Double.parseDouble(ParkClient.reportCapacity[12]) * 100
						/ (ParkClient.park.getMaxVisitors() * x))));

		VisitationHour.setCellValueFactory(new PropertyValueFactory<>("s"));
		NumberOfVisitors.setCellValueFactory(new PropertyValueFactory<>("i"));
		table.setItems(data);

	}
}