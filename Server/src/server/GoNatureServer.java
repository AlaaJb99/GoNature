package server;

import java.util.ArrayList;
import db.DBConnection;
import gui.ConnectedClients;
import ocsf.server.*;

public class GoNatureServer extends AbstractServer {
	final public static int DEFAULT_PORT = 5555;
	private String[][] CardReaderInput = new String[][] { { "1", null }, { "2", null }, { "3", null } };

	public GoNatureServer(int port) {
		super(port);
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object obj, ConnectionToClient client) {
		ConnectedClients.addClient(client.toString());

		String dataToString = (String) obj;
		String[] dataArr = dataToString.split("\\s");
		try {
			switch (dataArr[0]) {

			case "signin": {
				String message = "";
				/* *case of having a character in the number */
				for (int i = 0; i < dataArr[1].length(); i++)
					if (!(Character.isDigit(dataArr[1].charAt(i)))) {
						message = "Error";
						break;
					}
				if (!(message.equals("Error"))) {
					message = DBConnection.InDB(dataArr[1]);
					// message =OrderNoWaiting
					// message=NoOrderwaiting
					// message=NoOrderNoWaiting
					message += DBConnection.InWaitingList(dataArr[1]);
					// check if there is available place
					if (message.equals("NoOrderwaiting"))
						// NoOrderwaitingavailable or NoOrderwaitingNo
						message += DBConnection.InAvailable(dataArr[1]);
					else if (message.equals("OrderNoWaiting"))
						message += DBConnection.beforDay.contains(dataArr[1]);
				}
				this.sendToAllClients("signin " + dataArr[1] + " " + message);
				break;
			}

			case "makeorder": {
				if (DBConnection.CheckTimeDB(dataArr)) {
					String orderNum = DBConnection.addOrderToDb(dataArr);
					client.sendToClient(dataToString + " " + orderNum);
				} else
					client.sendToClient(dataToString + " NoPlace" + " 0");
				break;
			}

			case "getVisitorOrders": {
				ArrayList<String> orders = DBConnection.getVisitorOrders(dataArr[1]);
				String AllOrder = new String(dataArr[0] + " ");
				for (String str : orders) {
					AllOrder += str;
					AllOrder += " ";
				}
				client.sendToClient(AllOrder);
				break;
			}

			case "cancelOrder": {
				String order = DBConnection.OrderIn(dataArr[1]);
				if (!(order.equals("NotFound"))) {
					DBConnection.cancelOrder(dataArr);
				}
				client.sendToClient(null);
				break;
			}

			case "AddWait": {
				DBConnection.addToWaitList(dataArr);
				String message1 = DBConnection.getWaitingOrder(dataArr[8]);
				client.sendToClient("AddWait " + " " + message1);
				break;
			}

			case "anotherTime": {
				ArrayList<String> order1 = DBConnection.getAvailableDates(dataArr);
				String all = new String(dataArr[0] + " ");
				for (String str : order1) {
					all += str;
					all += " ";
				}
				client.sendToClient(all);
				break;
			}

			case "updatePark": {
				DBConnection.updatePark(dataArr);
				client.sendToClient(null);
				break;
			}

			case "signIn": {
				String[] authorized = DBConnection.returnClient(dataArr);
				client.sendToClient(authorized);
				if (authorized != null)
					client.sendToClient(DBConnection.returnPark(authorized[6]));
				break;
			}

			case "ManagerUpdate": {
				DBConnection.requestParametersUpdate(dataArr);
				client.sendToClient(null);
				break;
			}

			case "discountRequest": {
				client.sendToClient(DBConnection.addDiscountRequest(dataArr));
				break;
			}

			case "getdiscountRequests": {
				client.sendToClient(DBConnection.returnDiscountRequests());
				break;
			}

			case "acceptDiscount": {
				for (String a : dataArr)
					System.out.println(a);

				DBConnection.addDiscount(dataArr);
				client.sendToClient(null);
				break;
			}

			case "deleteDiscountRequest": {
				DBConnection.deleteDiscountRequest(dataArr);
				client.sendToClient(null);
				break;
			}

			case "AddSub": {
				DBConnection.addSubscriberToDB(dataArr);
				client.sendToClient(null);
				break;
			}

			case "AddGuide": {
				DBConnection.addGiudeToDB(dataArr);
				client.sendToClient(null);
				break;
			}

			case "isGuide": {
				if (DBConnection.isGuide(dataArr))
					dataArr[1] = "true";
				else
					dataArr[1] = "false";
				client.sendToClient(dataArr);
				break;
			}

			case "checkOrder": {
				String order = DBConnection.OrderIn(dataArr[1]);
				client.sendToClient("checkOrder " + order);
				break;
			}

			case "checkDiscount": {
				String[] discounts = DBConnection.checkDiscount(dataArr);
				client.sendToClient(discounts);
				break;
			}

			case "isSubscriber": {
				String isSub = "isSubscriber ";
				if (DBConnection.isSubscriber(dataArr))
					isSub += "true";
				else
					isSub += "false";
				client.sendToClient(isSub);
				break;
			}

			case "confirmEntering": {
				DBConnection.confirmEntering(dataArr);
				client.sendToClient(null);
				break;
			}

			case "addVisitorInPark": {
				client.sendToClient(DBConnection.addVisitorInPark(dataArr));
				break;
			}

			case "updateVisitorExit": {
				client.sendToClient(DBConnection.updateVisitorExit(dataArr));
				break;
			}

			case "CardReaderInput": {
				int parknum = Integer.parseInt(dataArr[1]);
				String id = dataArr[2];
				CardReaderInput[parknum - 1][1] = id;
				client.sendToClient(null);
				break;
			}

			case "getFromCardReader": {
				int parkNum = Integer.parseInt(dataArr[1]);
				client.sendToClient("getFromCardReader " + CardReaderInput[parkNum - 1][1]);
				CardReaderInput[parkNum - 1][1] = null;
				break;
			}

			case "generateReport": {
				client.sendToClient(DBConnection.generateReport(dataArr));
				break;
			}

			case "updateCapacity": {
				DBConnection.updateCapacity(dataArr);
				client.sendToClient(null);
				break;
			}

			case "sendWaitng": {
				DBConnection.sendWaitingOrder(dataArr);
				client.sendToClient(null);
				break;
			}

			case "getWaitingOrder": {
				String order = "getWaitingOrder ";
				order += DBConnection.getWaitingOrder(dataArr[1]);
				client.sendToClient(order);
				break;
			}

			case "deleteWaiting": {
				DBConnection.deleteWaitingList(dataArr[1]);
				DBConnection.deleteAvailable(dataArr[1]);
				client.sendToClient(null);
				break;
			}

			case "deleteBeforOneDay": {
				DBConnection.deleteBeforOneDay(dataArr[1]);
				DBConnection.beforDay.remove(dataArr[1]);
				client.sendToClient(null);
				break;
			}

			case "deleteOrder": {
				DBConnection.deleteOrder(dataArr[1]);
				client.sendToClient(null);
				break;
			}

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}
}