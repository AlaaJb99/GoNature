package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.Discount;

public class DBConnection {
	private static Connection con;
	private static Set<Integer> orderNumlist;// = new TreeSet<Integer>();
	private static Set<Integer> subNum;
	public static List<String> beforDay = new ArrayList<String>();

	public static void connectToDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/gonature?serverTimezone=IST", "root",
					"AlaaJbareen259999");
			System.out.println("SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	/*
	 * method to search in database if the id of the visitor exist
	 */

	public static String InDB(String v) throws SQLException {
		String query = "SELECT visitorId FROM orders WHERE visitorId=" + v;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next())
			return "Order";// which client
		return "NoOrder";
	}

	/*
	 * this method checks if there is place for this order in the wanted date and
	 * time
	 */
	public static boolean CheckTimeDB(String[] order) throws SQLException {
		int VisitationPeriod = 0, MaxOrderVisitors = 0;
		String q = "SELECT * FROM park WHERE ParkNum=" + order[1];
		Statement s = con.createStatement();
		ResultSet r = s.executeQuery(q);
		if (r.next()) {
			VisitationPeriod = Integer.parseInt(r.getString("VisitationPeriod"));
			MaxOrderVisitors = Integer.parseInt(r.getString("MaxOrderVisitors"));
		}

		String query = "SELECT * FROM orders WHERE visitDate='" + order[2] + "' AND park=" + order[1];/// park number
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		int current = 0, n, visitTime = Integer.parseInt(order[3].substring(0, 2));
		while (rs.next()) {
			String st = rs.getString("visitTime");
			n = Integer.parseInt(st.substring(0, 2));// time-4<=?<=12
			if (visitTime - VisitationPeriod < n && n < visitTime + VisitationPeriod)// 27/12****
				current += rs.getInt(5);
		}
		int available = MaxOrderVisitors - current;
		if (Integer.parseInt(order[4]) <= available)
			return true;
		return false;
	}

	/*
	 * method to add the order to the data base go over all the exist orders get the
	 * order number of each order and give the new order unused number
	 */
	public static String addOrderToDb(String[] order) throws SQLException {
		orderNumlist = new TreeSet<Integer>();
		String orderNum = "0";
		String query = "SELECT orderNum FROM orders";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next())
			orderNumlist.add(Integer.parseInt(rs.getString("orderNum")));

		for (int i = 1; i < 7200; i++) {
			if (!(orderNumlist.contains(i))) {
				orderNumlist.add(i);
				orderNum = Integer.toString(i);
				break;
			}
		}
		String[] sub = new String[2];
		sub[1] = order[8];

		/* calculate the price */
		double discount = 1;
		double price = 80;
		int visitorsNum = Integer.parseInt(order[4]);
		if (order[7].equals("Family") || order[7].equals("Individual")) {
			discount *= 0.85;// discount 15%
			/* check if this visitor is a subscriber */
			if (isSubscriber(sub))
				discount *= 0.80;// discount 20%
		} else
			discount *= 0.75;// discount 25%

		String[] isDiscount = new String[3];
		isDiscount[1] = order[2];// visitationDate
		isDiscount[2] = order[1];// parkNum

		/* check if there is an discount in the day of this order */
		String[] disCounts = checkDiscount(isDiscount);
		if (disCounts != null)
			for (String dis : disCounts)
				discount *= (1 - Double.parseDouble(dis));
		price *= visitorsNum;
		price *= discount;

		PreparedStatement orderDetails = con.prepareStatement("INSERT INTO orders VALUES (?,?,?,?,?,?,?,?,?,?)"); // download
		orderDetails.setString(1, orderNum);
		orderDetails.setString(2, order[1]);// parkNum
		orderDetails.setString(3, order[2]);// visitationDate
		orderDetails.setString(4, order[3]);// visitationTime
		orderDetails.setString(5, order[4]);// visitorsNum
		orderDetails.setString(6, order[5]);// email
		orderDetails.setString(7, order[6]);// telNum
		orderDetails.setString(8, order[7]);// orderType
		orderDetails.setString(9, order[8]);// visitorId
		orderDetails.setString(10, Double.toString(price));
		orderDetails.executeUpdate();
		return orderNum + " " + price;
	}

	/*
	 * method to get all the orders of this visitor
	 */
	public static ArrayList<String> getVisitorOrders(String vId) throws SQLException {
		String query = "SELECT * FROM orders WHERE visitorId=" + vId;
		String result;
		ArrayList<String> list = new ArrayList<String>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			// orderNum park visitDate visitTime visitorNum email telNum orderType visitorId
			result = rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " "
					+ rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7) + " " + rs.getString(8);
			list.add(result);
		}
		return list;
	}

	/*
	 * method check if this order in DB
	 */

	public static String OrderIn(String Num) throws SQLException {
		/* check if there is an order for this id number */
		String q = "SELECT * FROM orders WHERE visitorId=" + Num;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(q);
		if (rs.next())
			return rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " "
					+ rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7) + " " + rs.getString(8) + " "
					+ rs.getString(9) + " " + rs.getString(10);
		else
			return "NotFound";
	}

	/*
	 * cancel order delete it from the DB
	 */

	public static void cancelOrder(String[] order) throws SQLException {
		String query = "DELETE FROM orders WHERE visitorId=" + order[1];
		PreparedStatement cancelOrder = con.prepareStatement(query);
		cancelOrder.executeUpdate();
		PreparedStatement addCanceledOrder = con.prepareStatement("INSERT INTO canceled VALUES ( ? , ? , ? , ?)");
		addCanceledOrder.setString(1, order[2]);
		addCanceledOrder.setString(2, order[3]);
		addCanceledOrder.setString(3, order[4]);
		addCanceledOrder.setString(4, order[5]);
		addCanceledOrder.executeUpdate();
	}

	public static void deleteOrder(String vid) throws SQLException {
		String query = "DELETE FROM orders WHERE visitorId=" + vid;
		PreparedStatement cancelOrder = con.prepareStatement(query);
		cancelOrder.executeUpdate();
	}

	public static void addToWaitList(String[] order) throws SQLException {
		// order[0]="AddWait", order[1]=parkName, order[2]=visitDate,
		// order[3]=visitTime, order[4]=visitorsNum, order[5]=email, order[6]=visitorid
		String query = "SELECT COUNT(*)\r\n" + "FROM waitinglist as wl\r\n" + "WHERE wl.park='" + order[1]
				+ "' AND wl.visitDate= '" + order[2] + "' AND wl.visitTime = '" + order[3] + "';";

		int NewPlace = 0;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			NewPlace = rs.getInt(1);
		}
		NewPlace++; // number of people in the same waiting list + 1
		// now we insert the new client into the waiting list, with the new place
		PreparedStatement AddClientToWaitingList = con.prepareStatement(
				"INSERT INTO waitinglist (Place, Park, VisitDate, VisitTime, VisitorNum, Email, telNum, orderType, VisitorId)\r\n"
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
		AddClientToWaitingList.setString(1, Integer.toString(NewPlace)); // Place
		AddClientToWaitingList.setString(2, order[1]); // Park
		AddClientToWaitingList.setString(3, order[2]); // VisitDate
		AddClientToWaitingList.setString(4, order[3]); // ViisitTime
		AddClientToWaitingList.setString(5, order[4]); // VisitNum maybe we need to cast to integer... will see
		AddClientToWaitingList.setString(6, order[5]); // Email
		AddClientToWaitingList.setString(7, order[6]); // TelNum 27/12/2020
		AddClientToWaitingList.setString(8, order[7]); // orderType 27/12/2020
		AddClientToWaitingList.setString(9, order[8]); // visitorId 27/12/2020
		AddClientToWaitingList.executeUpdate();
		// return or fail
	}

	public static String getWaitingOrder(String id) throws SQLException {
		String query = "SELECT * FROM waitinglist WHERE visitorId=" + id;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next())
			return rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5) + " "
					+ rs.getString(6) + " " + rs.getString(7) + " " + rs.getString(8) + " " + rs.getString(9);
		else
			return "NotFound";
	}

	public static ArrayList<String> getAvailableDates(String[] order) throws SQLException {
		ArrayList<String> list = new ArrayList<String>();
		String[] o = new String[5];
		o[1] = order[1];
		o[4] = order[4];
		String date = order[2];
		String month = order[2].substring(0, 2);
		String day = order[2].substring(3, 5);
		String year = order[2].substring(6, 10);
		String minut = "00", hour = "08", time = hour + ":" + minut;
		int c = 0, h;// = Integer.parseInt(hour);
		int m = Integer.parseInt(month), d = Integer.parseInt(day), y = Integer.parseInt(year);
		while (c != 7) {
			hour = "08";
			time = hour + ":" + "00";
			o[2] = date;
			while (!time.equals("16:30")) {
				h = Integer.parseInt(hour);
				o[3] = time;
				if (CheckTimeDB(o))
					list.add(o[2] + " " + o[3]);
				if (!minut.equals("30"))
					minut = "30";
				else {
					minut = "00";
					h++;
				}
				if (h < 10)
					hour = "0" + h;
				else
					hour = h + "";
				time = hour + ":" + minut;
			}
			if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
				if (m == 12 && d == 31) {
					m = 1;
					y++;
				}
				if (d == 31)
					d = 1;
				else
					d++;
			} else if (m == 4 || m == 6 || m == 9 || m == 11) {
				if (d == 30)
					d = 1;
				else
					d++;
			}
			if (m < 10)
				month = "0" + m;
			else
				month = m + "";
			if (d < 10)
				day = "0" + d;
			else
				day = d + "";
			date = month + "/" + day + "/" + y;
			c++;
		}
		return list;
	}

	public static String[] returnClient(String[] signInData) {

		String[] result = null;
		String name = signInData[1];
		String pass = signInData[2];

		try {
			PreparedStatement aps = con
					.prepareStatement("SELECT * FROM authorized WHERE username=(?) AND password=(?)");
			aps.setString(1, name);
			aps.setString(2, pass);
			ResultSet ars = aps.executeQuery();

			if (ars.next())
				result = new String[] { "authorized", ars.getString(1), ars.getString(2), ars.getString(3),
						ars.getString(4), ars.getString(5), ars.getString(6) };

			ars.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String[] returnPark(String parkNum) {

		String[] result = null;

		PreparedStatement pps;
		try {
			pps = con.prepareStatement("SELECT * FROM park WHERE ParkNum=(?)");
			pps.setString(1, parkNum);
			ResultSet prs = pps.executeQuery();

			if (prs.next())
				result = new String[] { "park", prs.getString(1), prs.getString(2), prs.getString(3), prs.getString(4),
						prs.getString(5), prs.getString(6), prs.getString(7), prs.getString(8), prs.getString(9),
						prs.getString(10) };
			prs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	// UPDATE ALL PARK PARAMETER
	public static void updatePark(String[] park) {

		try {
			PreparedStatement parkUpdate = con
					.prepareStatement("UPDATE park SET MaxVisitors=(?),MaxOrderVisitors=(?),DifMaxOrderVisitors=(?),"
							+ "VisitorsInPark=(?),CasualVisitorsInPark=(?),VisitationPeriod=(?),MaxRequested=(?),DifRequested=(?),timeRequested=(?) WHERE parkNum=(?)");
			parkUpdate.setString(1, park[2]);
			parkUpdate.setString(2, park[3]);
			parkUpdate.setString(3, park[4]);
			parkUpdate.setString(4, park[5]);
			parkUpdate.setString(5, park[6]);
			parkUpdate.setString(6, park[7]);
			parkUpdate.setString(7, park[8]);
			parkUpdate.setString(8, park[9]);
			parkUpdate.setString(9, park[10]);
			parkUpdate.setString(10, park[1]);
			parkUpdate.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void requestParametersUpdate(String[] parameters) {
		try {
			for (int i = 0; i < parameters.length; i++)
				System.out.println(parameters[i]);
			PreparedStatement parkUpdate = con.prepareStatement(
					"UPDATE park SET MaxRequested = (?) , DifRequested=(?) , TimeRequested=(?) WHERE parkNum=(?)");
			parkUpdate.setInt(1, Integer.parseInt(parameters[2]));
			parkUpdate.setString(2, parameters[3]);
			parkUpdate.setString(3, parameters[4]);
			parkUpdate.setString(4, parameters[1]);
			parkUpdate.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public static String[] addDiscountRequest(String[] discount) {
		String discountReq = null;

		try {

			// check if it doesn't overlap with existing discount
			PreparedStatement checkDiscount = con.prepareStatement("SELECT * FROM discounts WHERE startDate=(?)");
			System.out.println(discount[3]);
			checkDiscount.setString(1, discount[3]);
			ResultSet rs = checkDiscount.executeQuery();

			if (rs.next() == true)
				discountReq = "discountRequest denied";

			// check if a request for this start date has already been made
			PreparedStatement checkRequest = con.prepareStatement("SELECT * FROM discountrequests WHERE startDate=(?)");
			checkRequest.setString(1, discount[3]);
			ResultSet rs2 = checkRequest.executeQuery();

			if (rs2.next() == true)
				discountReq = "discountRequest exist";

			// add to discount requests table
			else {
				PreparedStatement addDiscountRequest = con
						.prepareStatement("INSERT INTO discountrequests VALUES ( ? , ? , ? , ?)");
				addDiscountRequest.setString(1, discount[1]);
				addDiscountRequest.setString(2, discount[2]);
				addDiscountRequest.setString(3, discount[3]);
				addDiscountRequest.setString(4, discount[4]);

				addDiscountRequest.executeUpdate();

				discountReq = "discountRequest sent";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return (discountReq.split(" "));
	}

	public static String[] returnDiscountRequests() {
		ObservableList<Discount> discountRequestsList = FXCollections.observableArrayList();
		Statement rdr;

		try {
			rdr = con.createStatement();
			ResultSet drrs = rdr.executeQuery("SELECT * FROM discountrequests");

			while (drrs.next())
				discountRequestsList.add(new Discount(drrs.getString(1), Double.parseDouble(drrs.getString(2)),
						drrs.getString(3), drrs.getString(4)));

			rdr.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println(discountRequestsList);
		int length = discountRequestsList.size();
		String[] result = new String[length + 1];
		result[0] = "discountRequestsList";

		for (int i = 0; i < length; i++) {
			result[i + 1] = discountRequestsList.get(i).toString();
		}

		return result;
	}

	public static void addDiscount(String[] discount) {
		PreparedStatement addDiscount;
		try {
			addDiscount = con.prepareStatement("INSERT INTO discounts VALUES ( ? , ? , ? , ?)");
			addDiscount.setString(1, discount[1]);
			addDiscount.setString(2, discount[2]);
			addDiscount.setString(3, discount[3]);
			addDiscount.setString(4, discount[4]);

			addDiscount.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteDiscountRequest(String[] discount) {

		PreparedStatement deleteDiscountRequest;
		try {

			deleteDiscountRequest = con.prepareStatement("DELETE FROM discountrequests WHERE startDate=?");
			deleteDiscountRequest.setString(1, discount[3]);
			deleteDiscountRequest.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean isSubscriber(String[] subscriber) throws SQLException {
		String query = "SELECT * FROM subscription WHERE id=" + subscriber[1];
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next())
			return true;
		return false;
	}

	public static boolean isGuide(String[] guide) throws SQLException {
		String query = "SELECT * FROM guides WHERE id=" + guide[1];
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next())
			return true;
		return false;
	}

	public static void addSubscriberToDB(String[] subscriber) throws SQLException {
		String[] sub = new String[4];
		subNum = new TreeSet<>();
		String subsNum = "1000";

		String sql = "SELECT subNum FROM subscription";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next())
			subNum.add(Integer.parseInt(rs.getString("subNum")));

		for (int i = 1000; i < 1000000; i++) {
			if (!(subNum.contains(i))) {
				subNum.add(i);
				subsNum = Integer.toString(i);
				break;
			}
		}

		for (int i = 0; i < 4; i++) {
			if (subscriber.length == 7)
				sub[i] = "";
			else
				sub[i] = subscriber[7 + i];
		}

		String query = "INSERT INTO subscription VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		PreparedStatement subsDetails = con.prepareStatement(query);
		subsDetails.setString(1, subscriber[1]);
		subsDetails.setString(2, subscriber[2]);
		subsDetails.setString(3, subscriber[3]);
		subsDetails.setString(4, subscriber[4]);
		subsDetails.setString(5, subscriber[5]);
		subsDetails.setInt(6, Integer.parseInt(subscriber[6]));
		subsDetails.setString(7, sub[0]);
		subsDetails.setString(8, sub[1]);
		subsDetails.setString(9, sub[2]);
		subsDetails.setString(10, sub[3]);
		subsDetails.setString(11, subsNum);
		subsDetails.executeUpdate();
	}

	public static void addGiudeToDB(String[] guide) throws SQLException {
		String[] guid = new String[4];

		for (int i = 0; i < 4; i++) {
			if (guide.length == 6)
				guid[i] = "";
			else
				guid[i] = guide[6 + i];
		}

		String query = "INSERT INTO guides VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		PreparedStatement subsDetails = con.prepareStatement(query);
		subsDetails.setString(1, guide[1]);
		subsDetails.setString(2, guide[2]);
		subsDetails.setString(3, guide[3]);
		subsDetails.setString(4, guide[4]);
		subsDetails.setString(5, guide[5]);
		subsDetails.setString(6, guid[0]);
		subsDetails.setString(7, guid[1]);
		subsDetails.setString(8, guid[2]);
		subsDetails.setString(9, guid[3]);
		subsDetails.executeUpdate();
	}

	/*
	 * method to get the discount value if there is in this date
	 */
	public static String[] checkDiscount(String[] dis) throws SQLException {
		ArrayList<String> discount = new ArrayList<String>();
		String date = dis[1];
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate orderDate = LocalDate.parse(date, dtf);
		String query = "SELECT * FROM discounts WHERE ParkNum=" + dis[2];
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		// discount.add("checkDiscount");
		while (rs.next()) {
			String start = rs.getString(3);
			LocalDate startDate = LocalDate.parse(start, dtf);
			String end = rs.getString(4);
			LocalDate endDate = LocalDate.parse(end, dtf);
			if (orderDate.isAfter(startDate) && orderDate.isBefore(endDate))
				discount.add(rs.getString(2));
			if (orderDate.isEqual(startDate) || orderDate.isEqual(endDate))
				discount.add(rs.getString(2));
		}
		String[] disCounts = new String[discount.size()];
		discount.toArray(disCounts);
		return disCounts;
	}

	// UPDATE MONTHLY REPORTS TABLE
	public static void confirmEntering(String[] visitation) throws NumberFormatException, SQLException {
		// INPUT 'visitation' = "confirmEntering parkNum(string) pdate(MM/YYYY)(String)
		// orderType(string) numVisitors(int) totalPrice(double)"
		// 0 1 2 3 4 5

		boolean flag = false; // if month exists

		String query = "SELECT * FROM monthlyreports WHERE pdate='" + visitation[2] + "'";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next())
			flag = true;

		if (visitation[3].contentEquals("Subscriber")) {

			if (flag) {
				PreparedStatement updateMonthlyReports = con
						.prepareStatement("UPDATE monthlyreports SET subscriber=?, total=?, revenue=? WHERE pdate=?");

				updateMonthlyReports.setInt(1, rs.getInt(3) + Integer.parseInt(visitation[4])); // add numVisitors to
																								// subscriber
				updateMonthlyReports.setInt(2, rs.getInt(6) + Integer.parseInt(visitation[4])); // add numVisitors to
																								// total
				updateMonthlyReports.setDouble(3, rs.getDouble(7) + Double.parseDouble(visitation[5])); // add price to
																										// revenue
				updateMonthlyReports.setString(4, visitation[2]); // where pdate
				updateMonthlyReports.executeUpdate();

			}

			else {
				String q = "INSERT INTO monthlyreports VALUES (?,?,?,?,?,?,?)";
				PreparedStatement enterMonthlyReports = con.prepareStatement(q);
				enterMonthlyReports.setString(1, visitation[1]); // park number
				enterMonthlyReports.setString(2, visitation[2]); // pdate
				enterMonthlyReports.setInt(3, Integer.parseInt(visitation[4]));
				enterMonthlyReports.setInt(4, 0);
				enterMonthlyReports.setInt(5, 0);
				enterMonthlyReports.setInt(6, Integer.parseInt(visitation[4]));
				enterMonthlyReports.setDouble(7, Double.parseDouble(visitation[5]));
				enterMonthlyReports.executeUpdate();
			}

		}

		else if (visitation[3].contentEquals("Guide")) {
			if (flag) {
				PreparedStatement updateMonthlyReports = con
						.prepareStatement("UPDATE monthlyreports SET guide=?, total=?, revenue=? WHERE pdate=?");

				updateMonthlyReports.setInt(1, rs.getInt(4) + Integer.parseInt(visitation[4])); // add numVisitors to
																								// guide
				updateMonthlyReports.setInt(2, rs.getInt(6) + Integer.parseInt(visitation[4])); // add numVisitors to
																								// total
				updateMonthlyReports.setDouble(3, rs.getDouble(7) + Double.parseDouble(visitation[5])); // add price to
																										// revenue
				updateMonthlyReports.setString(4, visitation[2]);
				updateMonthlyReports.executeUpdate();
			}

			else {
				String q = "INSERT INTO monthlyreports VALUES (?,?,?,?,?,?,?)";
				PreparedStatement enterMonthlyReports = con.prepareStatement(q);
				enterMonthlyReports.setString(1, visitation[1]);
				enterMonthlyReports.setString(2, visitation[2]);
				enterMonthlyReports.setInt(3, 0);
				enterMonthlyReports.setInt(4, Integer.parseInt(visitation[4]));
				enterMonthlyReports.setInt(5, 0);
				enterMonthlyReports.setInt(6, Integer.parseInt(visitation[4]));
				enterMonthlyReports.setDouble(7, Double.parseDouble(visitation[5]));
				enterMonthlyReports.executeUpdate();
			}

		}

		else {
			if (flag) {
				PreparedStatement updateMonthlyReports = con.prepareStatement(
						"UPDATE monthlyreports SET notregistered=?, total=?, revenue=? WHERE pdate=?");

				updateMonthlyReports.setInt(1, rs.getInt(5) + Integer.parseInt(visitation[4])); // add numVisitors to
																								// notregistered
				updateMonthlyReports.setInt(2, rs.getInt(6) + Integer.parseInt(visitation[4])); // add numVisitors to
																								// total
				updateMonthlyReports.setDouble(3, rs.getDouble(7) + Double.parseDouble(visitation[5])); // add price to
																										// revenue
				updateMonthlyReports.setString(4, visitation[2]);
				updateMonthlyReports.executeUpdate();
			}

			else {
				String q = "INSERT INTO monthlyreports VALUES (?,?,?,?,?,?,?)";
				PreparedStatement enterMonthlyReports = con.prepareStatement(q);
				enterMonthlyReports.setString(1, visitation[1]);
				enterMonthlyReports.setString(2, visitation[2]);
				enterMonthlyReports.setInt(3, 0);
				enterMonthlyReports.setInt(4, 0);
				enterMonthlyReports.setInt(5, Integer.parseInt(visitation[4]));
				enterMonthlyReports.setInt(6, Integer.parseInt(visitation[4]));
				enterMonthlyReports.setDouble(7, Double.parseDouble(visitation[5]));
				enterMonthlyReports.executeUpdate();
			}

		}
	}

	public static String addVisitorInPark(String[] visitor) throws SQLException {
		// visitor = "addVisitorInPark parknum pdate2 visitorid orderorcasual type
		// numvisitors entertime status"
		String query = "SELECT * FROM visitorsinpark WHERE visitorid='" + visitor[3] + "'";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next()) {
			return "addVisitorInPark AlreadyInPark";
		}

		else {
			PreparedStatement addVisitorInPark = con
					.prepareStatement("INSERT INTO visitorsinpark VALUES (?,?,?,?,?,?,?,null,?,null)");
			addVisitorInPark.setString(1, visitor[1]);
			addVisitorInPark.setString(2, visitor[2]);
			addVisitorInPark.setString(3, visitor[3]);
			addVisitorInPark.setString(4, visitor[4]);
			addVisitorInPark.setString(5, visitor[5]);
			addVisitorInPark.setString(6, visitor[6]);
			addVisitorInPark.setString(7, visitor[7]);
			addVisitorInPark.setString(8, visitor[8]);
			addVisitorInPark.executeUpdate();

			return "addVisitorInPark Ok";
		}

	}

	public static String updateVisitorExit(String[] visitor) throws SQLException {
		// visitor = "addVisitorInPark pdate visitorid status exittime"
		for (String s : visitor)
			System.out.println(s);

		String query = "SELECT * FROM visitorsinpark WHERE pdate='" + visitor[1] + "' AND visitorid='" + visitor[2]
				+ "'";
		System.out.println(query);
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		if (rs.next()) {
			if (rs.getString(9).contentEquals("InPark")) {
				// calculate staying period:
				String[] enteringtime = rs.getString(7).split(":");
				SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
				String[] timeNow = formatter.format(new Date()).split(":");

				int stayingPeriodInMinutes = (Integer.parseInt(timeNow[0]) - Integer.parseInt(enteringtime[0])) * 60
						+ (Integer.parseInt(timeNow[1]) - Integer.parseInt(enteringtime[1]));
				int hours = stayingPeriodInMinutes / 60;
				int minutes = stayingPeriodInMinutes % 60;
				String stayingPeriod = hours + ":" + minutes;
				;

				// Updating visitor in park
				PreparedStatement updateVisitorInPark = con.prepareStatement(
						"UPDATE visitorsinpark SET status=?, exittime=?, stayingperiod=? WHERE pdate=? AND visitorid=?");
				updateVisitorInPark.setString(1, visitor[3]);
				updateVisitorInPark.setString(2, visitor[4]);
				updateVisitorInPark.setString(3, stayingPeriod);
				updateVisitorInPark.setString(4, visitor[1]);
				updateVisitorInPark.setString(5, visitor[2]);
				updateVisitorInPark.executeUpdate();

				return "updateVisitorExit Ok " + rs.getString(4) + " " + rs.getString(6);
				// casual/order number exiting
			} else // if ID has exited
				return "updateVisitorExit NotInPark";
		}

		// if ID never entered
		else
			return "updateVisitorExit NotInPark";

	}

	public static String generateReport(String[] reportinfo) throws SQLException {
		// report info = generateReport + VisitorsNumber/Usage/Income + park num + month
		// + year

		if (reportinfo[1].contentEquals("VisitorsNumber") || reportinfo[1].contentEquals("Income")) {
			String query = "SELECT * FROM monthlyreports WHERE parknum='" + reportinfo[2] + "' AND pdate='"
					+ reportinfo[3] + "/" + reportinfo[4] + "'";
			System.out.println(query);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				return "generateReport " + reportinfo[1] + " " + rs.getString(1) + " " + rs.getString(2) + " "
						+ rs.getInt(3) + " " + rs.getInt(4) + " " + rs.getInt(5) + " " + rs.getInt(6) + " "
						+ rs.getDouble(7);
			}

			else
				return "generateReport " + reportinfo[1] + " null";
		}

		else if (reportinfo[1].contentEquals("Capacity")) {

			String query = "SELECT * FROM capacity WHERE parknum='" + reportinfo[2] + "' AND pmonth='" + reportinfo[3]
					+ "/" + reportinfo[4] + "'";
			System.out.println(query);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				String s = "generateReport " + reportinfo[1] + " " + rs.getString(1) + " " + rs.getString(2) + " "
						+ rs.getInt(3) + " " + rs.getInt(4) + " " + rs.getInt(5) + " " + rs.getInt(6) + " "
						+ rs.getInt(7) + " " + rs.getInt(8) + " " + rs.getInt(9) + " " + rs.getInt(10) + " "
						+ rs.getInt(11) + " " + rs.getInt(12) + " " + rs.getInt(13);
				System.out.println(s);
				return s;
			} else
				return "generateReport " + reportinfo[1] + " null";
		}

		else
			return null;
	}

	public static void sendMessageBeforOneDay() throws SQLException {
		List<String> list = new ArrayList<String>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDateTime todayDate = LocalDateTime.now();
		LocalDateTime nexDayDate = todayDate.plusDays(1);
		String dateNextDay = nexDayDate.format(dtf);
		String query = "SELECT * FROM orders WHERE visitDate='" + dateNextDay + "'";
		String s;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			s = rs.getString(9);
			list.add(s);
		}
		beforDay = list;
	}

	/**
	 * method to check if this visitor is in the waiting list
	 * 
	 * @param id is the id number of the visitor
	 * @return string "waiting" if he is in the waiting list else string "NoWaiting"
	 * @throws SQLException
	 */

	public static String InWaitingList(String id) throws SQLException {
		String query = "SELECT * FROM waitinglist WHERE visitorId=" + id;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next())
			return "waiting";
		return "NoWaiting";
	}

	/**
	 * method to send message to the visitor that have an available place
	 * 
	 * @param order is an array with all the details of the order At the head of the
	 *              queue
	 * @throws SQLException
	 */

	public static void sendWaitingOrder(String[] order) throws SQLException {
		int maxorders = 0;
		int allOrders = 0;
		String sql = "SELECT visitorNum FROM orders WHERE visitDate= '" + order[2] + "' AND visitTime = '" + order[3]
				+ "';";
		Statement statement = con.createStatement();
		ResultSet r = statement.executeQuery(sql);
		while (r.next())
			allOrders += r.getInt(1);
		String q = "SELECT * FROM park WHERE ParkNum=" + order[1];
		Statement st = con.createStatement();
		ResultSet resultSet = st.executeQuery(q);
		if (resultSet.next())
			maxorders = resultSet.getInt(3);
		String query = "SELECT * FROM waitinglist WHERE park='" + order[1] + "' AND visitDate= '" + order[2]
				+ "' AND visitTime = '" + order[3] + "';";
		String Id = "Error";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next()) {
			int dif = maxorders - allOrders + Integer.parseInt(order[4]);
			if (rs.getInt(5) <= dif) {
				Id = rs.getString(9);
				PreparedStatement sendWait = con.prepareStatement("INSERT INTO available VALUES (?)");
				sendWait.setString(1, Id);
				sendWait.execute();
				final String id = Id;
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						try {
							if (!InAvailable(id).equals("No")) {
								// The time ends (One hour and the visitor didn't approve)
								deleteWaitingList(id);
								deleteAvailable(id);
								// timer.cancel();
								sendWaitingOrder(order);
							} else
								return;
							timer.cancel();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}, 30 * 1000);// 3600*1000 Milliseconds= One Hour(60 Minutes)
			}
		}
	}

	/**
	 * method to check if there is an available place for this visitor
	 * 
	 * @param id is the id number of the visitor
	 * @return String of the id if it's in the table else returns string "No"
	 * @throws SQLException
	 */

	public static String InAvailable(String id) throws SQLException {
		String query = "SELECT * FROM available WHERE id=" + id;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next())
			return "available";
		return "No";
	}

	/**
	 * delete the visitor from the waiting list
	 * 
	 * @param id
	 * @throws SQLException
	 */
	public static void deleteWaitingList(String id) throws SQLException {
		String query = "DELETE FROM waitinglist WHERE visitorId=" + id;
		PreparedStatement deleteorder = con.prepareStatement(query);
		deleteorder.executeUpdate();
	}

	public static void deleteAvailable(String id) throws SQLException {
		String query = "DELETE FROM available WHERE id=" + id;
		PreparedStatement delete = con.prepareStatement(query);
		delete.executeUpdate();
	}

	public static void deleteBeforOneDay(String id) throws SQLException {
		beforDay.remove(id);
	}

	public static String isBeforOneDay(String id) throws SQLException {
		String query = "SELECT * FROM available WHERE id=" + id;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next())
			return "Yes";
		return "No";
	}

	/**
	 * 
	 * @param capacity
	 * @throws SQLException
	 */
	public static void updateCapacity(String[] capacity) throws SQLException {
		// capacity = updateCapacity parkNum date hour VisitorsInParkNum
		for (String s : capacity)
			System.out.println(s);

		PreparedStatement getCapacity;
		getCapacity = con.prepareStatement("SELECT * FROM capacity WHERE parknum=? AND pmonth=?");
		getCapacity.setString(1, capacity[1]);
		getCapacity.setString(2, capacity[2]);
		ResultSet rs = getCapacity.executeQuery();

		if (rs.next()) {
			System.out.println("found one");
			// add current number of visitors to given hour
			int newVisitorsNum = Integer.parseInt(capacity[4])
					+ Integer.parseInt(rs.getString(Integer.parseInt(capacity[3]) - 6));
			System.out.println(newVisitorsNum);

			PreparedStatement updateCapacity = con
					.prepareStatement("UPDATE capacity SET " + "h" + capacity[3] + "=? WHERE parknum=? AND pmonth=?");
			updateCapacity.setInt(1, newVisitorsNum);
			updateCapacity.setString(2, capacity[1]);
			updateCapacity.setString(3, capacity[2]);
			updateCapacity.executeUpdate();

		}

		else {
			PreparedStatement newCapacityMonth = con
					.prepareStatement("Insert into capacity values (?,?,0,0,0,0,0,0,0,0,0,0,0)");
			newCapacityMonth.setString(1, capacity[1]);
			newCapacityMonth.setString(2, capacity[2]);
			newCapacityMonth.executeUpdate();

			PreparedStatement addCapacity = con
					.prepareStatement("UPDATE capacity SET " + "h" + capacity[3] + "=? WHERE parknum=? AND pmonth=?");
			addCapacity.setInt(1, Integer.parseInt(capacity[4]));
			addCapacity.setString(2, capacity[1]);
			addCapacity.setString(3, capacity[2]);
			addCapacity.executeUpdate();
		}
		rs.close();

	}
}