package cardreader;

import java.io.IOException;

import ocsf.client.AbstractClient;

public class CardReader extends AbstractClient {

	public static boolean awaitResponse = false;
	public static String parkNum;
	public static boolean IDupdated = false;	
	
	
	public CardReader(String host, int port) {
		super(host, port);

	}

	public void handleMessageFromClientUI(Object data) {

		try {
			openConnection(); // in order to send more than one message
			awaitResponse = true;
			sendToServer(data);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			// CC.display("Could not send message to server: Terminating client."+ e);
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		awaitResponse = false;
		
		IDupdated=true;
	}

}
