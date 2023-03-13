package clientGUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import client.ParkClient;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class IncomeReport implements Initializable {

	private Stage stage;

	@FXML
	private Text txtMonth;

	@FXML
	private Text txtIncome;

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
		stage.setTitle("Income Reports");
		stage.setScene(scene);
		stage.show();
		try {
			String month = ParkClient.reportVisitorsNumber[1].substring(0, 2);
			String year = ParkClient.reportVisitorsNumber[1].substring(3, 7);
			String report = "IncomeReports_" + month + year + ".png";
			ImageIO.write(SwingFXUtils.fromFXImage(stage.getScene().snapshot(null), null), "png",
					new File("C:\\Users\\Alaa Jbareen\\Desktop\\" + report));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void initialize(URL arg0, ResourceBundle arg1) {

		txtMonth.setText(ParkClient.reportVisitorsNumber[1]);
		txtIncome.setText(ParkClient.reportVisitorsNumber[6]);

	}
}