package clientGUI;

import java.io.IOException;

import client.ParkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ErrorController {

	@FXML
	private Button OkBtn;

	@FXML
	private HBox icon;

	@FXML
	private Text message;

	private Stage primaryStage;

	@FXML
	void getOkBtn(ActionEvent event) {
		primaryStage.close();
	}

	public void start(Pane root) throws IOException {
		primaryStage = new Stage();
		message.setText(ParkClient.popUpTxt);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/Error.css").toExternalForm());
		primaryStage.setTitle("Error");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}