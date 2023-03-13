package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.Authorized;
import logic.Discount;
import logic.Order;
import logic.Park;
import ocsf.client.AbstractClient;

public class ParkClient extends AbstractClient {
	// Instance variables **********************************************

	ClientController CC;
	private VisitorController v1 = new VisitorController();
	private OrderController o1 = new OrderController();
	public static Set<Order> orders;// = new TreeSet<Order>();
	public static ArrayList<Order> available = new ArrayList<Order>();
	public static Authorized authorized = null;
	public static Park park = null;
	public static String popUpTxt, popUpTitle = "Error Message";
	public static String discountRequest = null; // send or denied or exist
	public static ObservableList<Discount> discountRequestsList = FXCollections.observableArrayList();
	public static boolean awaitResponse = false;
	public static boolean isGuide = false;
	public static boolean isSubscriber = false;
	public static Order order;
	public static int price = 80;
	public static String enteringID;
	public static String exitingID;
	public static String getFromCardReader;
	public static String[] updateVisitorExit = new String[3]; // status:"Ok or "NotInPark" + casual/order + number
	public static String[] discount;
	public static String addVisitorInPark;
	public static String[] reportVisitorsNumber = new String[7];
	public static String[] reportCapacity=new String[13];

	public ParkClient(String host, int port, ClientController CC) throws IOException {
		super(host, port); // Call the superclass constructor
		this.CC = CC;
		// openConnection();
	}

	public void handleMessageFromServer(Object msg) {
		awaitResponse = false;
		String[] dataArr;
		if (msg instanceof String[])
			dataArr = (String[]) msg;
		else {
			String dataToString = (String) msg;
			dataArr = dataToString.split("\\s");
		}

		switch (dataArr[0]) {
		case "signin": {
			v1.visitorMaker(dataArr[1] + " " + dataArr[2]);
			break;
		}

		case "makeorder": {
			o1.makeOrder(dataArr);
			break;
		}

		case "getVisitorOrders": {
			int i = 1;
			orders = new TreeSet<Order>();
			while (i < dataArr.length) {
				Order order = new Order(dataArr[i], dataArr[i + 1], dataArr[i + 2], dataArr[i + 3],
						Integer.parseInt(dataArr[i + 4]), dataArr[i + 5], dataArr[i + 6], dataArr[i + 7]);
				orders.add(order);
				i += 8;
			}
			break;
		}

		case "cancelOrder": {
			Order order = new Order();
			if (!dataArr[1].equals("NotFound")) {
				order = new Order(dataArr[1], dataArr[2], dataArr[3], dataArr[4], Integer.parseInt(dataArr[5]),
						dataArr[6], dataArr[7], dataArr[8]);
				orders.remove(order);
			}
			o1.makeOrder(dataArr);
			break;
		}

		case "anotherTime": {
			int i = 1;
			while (i < dataArr.length) {
				Order order = new Order();
				order.setOrderNum("0");
				order.setParkName(OrderController.o1.getParkName());
				order.setVisitDate(dataArr[i]);
				order.setVisitTime(dataArr[i + 1]);
				order.setVisitorsNum(OrderController.o1.getVisitorsNum());
				order.setEmail(OrderController.o1.getEmail());
				order.setTelNum(OrderController.o1.getTelNum());
				order.setOrderType(OrderController.o1.getOrderType());
				available.add(order);
				i += 2;
			}
			break;
		}

		case "authorized": {
			updateAuthorized(dataArr);
			break;
		}

		case "park": {
			updatePark(dataArr);
			break;
		}

		case "discountRequest": {
			updateDiscountRequest(dataArr[1]);
		}

		case "discountRequestsList": {
			updateDiscountRequestsList(dataArr);
		}

		case "isGuide": {
			if (dataArr[1].equals("true"))
				isGuide = true;
			else
				isGuide = false;
			break;
		}

		case "isSubscriber": {
			if (dataArr[1].equals("true"))
				isSubscriber = true;
			else
				isSubscriber = false;
			break;
		}

		case "addVisitorInPark": {
			addVisitorInPark = dataArr[1]; // "Ok" or "AlreadyInPark"
			break;
		}

		case "updateVisitorExit": {
			updateVisitorExit[0] = dataArr[1]; // "Ok" or "NotInPark"
			if (dataArr[1].contentEquals("Ok")) {
				updateVisitorExit[1] = dataArr[2];
				updateVisitorExit[2] = dataArr[3];
			}

			break;
		}

		case "getFromCardReader": {
			getFromCardReader = dataArr[1];
			break;
		}

		case "generateReport":{
			if(dataArr[1].equals("VisitorsNumber") || dataArr[1].equals("Income")){
				if(dataArr[2].contentEquals("null"))
					reportVisitorsNumber=null;
				else {
					for(String s:dataArr)
						System.out.println(s);
					for(int i=0;i<7;i++)
						reportVisitorsNumber[i]=dataArr[i+2];
				}		
			}
			
			else if(dataArr[1].equals("Capacity")) {
				if(dataArr[2].contentEquals("null"))
					reportCapacity=null;
				else {
					for(String s:dataArr)
						System.out.println(s);
					for(int i=0;i<13;i++)
						reportCapacity[i]=dataArr[i+2];
				}		
			}		
		}
		
		case "getWaitingOrder":{
			o1.makeOrder(dataArr);
			break;
		}
		
		default:
			break;
		}
	}

	private void updateAuthorized(String[] s) {
		authorized = new Authorized(s[1], s[2], s[3], s[4], s[5], s[6]);
		System.out.println(authorized.toString());
	}

	private void updatePark(String[] s) {
		park = new Park(s[1], Integer.parseInt(s[2]), Integer.parseInt(s[3]), Integer.parseInt(s[4]),
				Integer.parseInt(s[5]), Integer.parseInt(s[6]), Integer.parseInt(s[7]), Integer.parseInt(s[8]),
				Integer.parseInt(s[9]), Integer.parseInt(s[10]));
		System.out.println(park);
	}

	private void updateDiscountRequest(String s) {
		discountRequest = s;
		System.out.println(s);
	}

	private void updateDiscountRequestsList(String[] s) {
		for (String s1 : s)
			System.out.println(s1);
		for (int i = 1; i < s.length; i++) {
			String[] discount = s[i].split(" ");
			Discount discount2 = new Discount(discount[0], Double.parseDouble(discount[1]), discount[2], discount[3]);
			if (!discountRequestsList.contains(discount2))
				discountRequestsList.add(discount2);
		}
	}

	public void handleMessageFromClientUI(String message) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(message);
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
			CC.display("Could not send message to server: Terminating client." + e);
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
}