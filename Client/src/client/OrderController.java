package client;

import java.util.TreeSet;
import logic.Order;

public class OrderController {
	public static Order o1;
	public static TreeSet<Integer> orderNumlist = new TreeSet<Integer>();
	public static boolean place;
	public static String price;

	public void makeOrder(String[] result) {
		o1 = new Order();
		if (result[0].equals("makeorder")) {
			price = result[10];
			o1.setOrderNum(result[9]);
			o1.setParkName(result[1]);
			o1.setVisitDate(result[2]);
			o1.setVisitTime(result[3]);
			o1.setVisitorsNum(Integer.parseInt(result[4]));
			o1.setEmail(result[5]);
			o1.setTelNum(result[6]);
			o1.setOrderType(result[7]);
		} else if (result[0].equals("cancelOrder") || result[0].equals("getWaitingOrder")) {
			//o1.setOrderNum(result[1]);
			o1.setParkName(result[1]);
			o1.setVisitDate(result[2]);
			o1.setVisitTime(result[3]);
			o1.setVisitorsNum(Integer.parseInt(result[4]));
			o1.setEmail(result[5]);
			o1.setTelNum(result[6]);
			o1.setOrderType(result[7]);
		}
	}
}