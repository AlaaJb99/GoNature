package cardreader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CardReaderUI extends Application {

	public static CardReader cr;
	public static String popUpTxt = "";

	@SuppressWarnings("static-access")
	public CardReaderUI(String[] park) {
		this.cr.parkNum = park[0];
	};

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		cr = new CardReader("localhost", 5555);
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/cardreader/CardReaderSimulation.fxml").openStream());
		CardReaderSimulation crs = loader.getController();
		crs.start(primaryStage, root);
	}
}
