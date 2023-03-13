package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
//import ocsf.server.ConnectionToClient;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;

import db.DBConnection;
import gui.ServerPortConnection;

public class ServerUI extends Application {
	// final public static int DEFAULT_PORT = 5555;
	private static GoNatureServer sv;

	public static void main(String args[]) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/ServerPort.fxml").openStream());
		ServerPortConnection aFrame = loader.getController();
		aFrame.start(root);
	}

	public static void stopServer() throws IOException {
		sv.close();
	}

	public static void runServer(String p) {
		int port = 0; // Port to listen on
		DBConnection.connectToDB();
		/* to send message befor one day for all the orders */
//		Timer timer = new Timer();
//		Calendar date = Calendar.getInstance();
//		date.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
//		date.set(Calendar.HOUR, 23);
//		date.set(Calendar.MINUTE, 57);
//		date.set(Calendar.SECOND, 0);
//		date.set(Calendar.MILLISECOND, 0);
//		timer.schedule(new MyTimerTask(), 1000*2, 1000);

		try {
			port = Integer.parseInt(p); // Set port to 5555
		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
		}

		sv = new GoNatureServer(port);
		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");

		}
	}
}