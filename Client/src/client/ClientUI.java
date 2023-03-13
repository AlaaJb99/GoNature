package client;
import clientGUI.WelcomePageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClientUI extends Application {
	public static ClientController parkCC; // only one instance

	public static void main(String args[]) throws Exception {		
	    launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		parkCC = new ClientController("localhost", 5555);
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/WelcomePage.fxml").openStream());
		WelcomePageController aFrame = loader.getController();
		aFrame.start(primaryStage, root);
	}
}