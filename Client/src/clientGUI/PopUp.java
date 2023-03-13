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

public class PopUp {

	@FXML
	private Button OkBtn;

	@FXML
	private HBox icon;

	@FXML
	private Text message;

	private Stage stage;

	@FXML
	void getOkBtn(ActionEvent event) {
		stage.close();
	}

	public void start(Pane root) throws IOException {
		message.setText(ParkClient.popUpTxt);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/OrderConfirmation.css").toExternalForm());
		stage = new Stage();
		stage.setTitle(ParkClient.popUpTitle);
		stage.setScene(scene);
		stage.show();
	}
}