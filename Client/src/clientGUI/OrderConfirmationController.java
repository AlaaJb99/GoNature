package clientGUI;

import java.io.IOException;
import client.OrderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OrderConfirmationController {

	@FXML
	private Button OkBtn;

	@FXML
	private Label Email;

	@FXML
	private Label Phone;

	@FXML
	private Pane icon;

	@FXML
	private Text txtPrice;

	private Stage stage;

	@FXML
	void getOkBtn(ActionEvent event) {
		stage.close();
	}

	public void start(Pane root, String price) throws IOException {
		Scene scene = new Scene(root);
		stage = new Stage();
		Email.setText(OrderController.o1.getEmail());
		Phone.setText(OrderController.o1.getTelNum());
		txtPrice.setText(String.format("%.2f ₪", Double.parseDouble(price)));
		scene.getStylesheets().add(getClass().getResource("/clientGUI/OrderConfirmation.css").toExternalForm());
		stage.setTitle("Simulation");
		stage.setScene(scene);
		stage.show();
	}
}