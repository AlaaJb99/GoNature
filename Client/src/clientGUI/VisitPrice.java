package clientGUI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import client.ClientUI;
import client.ParkClient;

public class VisitPrice {

	public static double getPrice(int visitorNum, boolean isOrder) {
		double discount = 1;
		double price = ParkClient.price * visitorNum;
		if (ParkClient.isGuide && isOrder)
			price *= 0.75;
		else if (ParkClient.isGuide)
			price *= 0.90;
		else if (ParkClient.isSubscriber && isOrder)
			price *= 0.85 * 0.80;
		else if(isOrder)
			price *= 0.85;
		else if (ParkClient.isSubscriber)
			price *= 0.80;

		String[] diS;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDateTime todayDate = LocalDateTime.now();
		ClientUI.parkCC.accept("checkDiscount " + todayDate.format(dtf) + " " + ParkClient.park.getParkNum());
		diS = ParkClient.discount;
		if (diS != null)
			for (String d : diS)
				discount *= (1 - Double.parseDouble(d));
		price *= discount;
		return price;
	}
}