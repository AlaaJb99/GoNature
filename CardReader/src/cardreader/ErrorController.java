package cardreader;

import java.io.IOException;
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

	private Stage newStage;

	@FXML
	void getOkBtn(ActionEvent event) {
		newStage.close();
	}

	public void start(Pane root) throws IOException {
		newStage = new Stage();
		message.setText(CardReaderUI.popUpTxt);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/Error.css").toExternalForm());
		newStage.setTitle("Error");
		newStage.setScene(scene);
		newStage.show();
	}
}