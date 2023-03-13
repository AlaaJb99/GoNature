package clientGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AddWaitListController {

	@FXML
	private Pane icon;

	@FXML
	private Button OkBtn;

	@FXML
	void getOkBtn(ActionEvent event) {
		Scene scene = OkBtn.getScene();
		Stage stage = (Stage) scene.getWindow();
		stage.close();
	}
}