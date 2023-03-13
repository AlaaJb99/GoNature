package logic;

public class Order implements Comparable<Order> {
	
	private String orderNum;
	private String parkName;
	private String visitDate;
	private String visitTime;
	private int visitorsNum;
	private String email;
	private String telNum;
	private String orderType;

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Order(String orderNum, String parkName, String visitDate, String visitTime, int visitorsNum, String email,
			String telNum, String orderType) {
		this.orderNum = orderNum;
		this.parkName = parkName;
		this.visitDate = visitDate;
		this.visitTime = visitTime;
		this.visitorsNum = visitorsNum;
		this.email = email;
		this.telNum = telNum;
		this.orderType = orderType;
	}

	public Order() {
		super();
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	public String getParkName() {
		return parkName;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitTime(String visitTime) {
		this.visitTime = visitTime;
	}

	public String getVisitTime() {
		return visitTime;
	}

	public void setVisitorsNum(int visitorsNum) {
		this.visitorsNum = visitorsNum;
	}

	public int getVisitorsNum() {
		return visitorsNum;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}

	public String getTelNum() {
		return telNum;
	}

	@Override
	public int compareTo(Order order) {
		if (equals(order))
			return 0;
		return order.orderNum.compareTo(orderNum);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Order))
			return false;
		Order order = (Order) obj;
		return order.orderNum.equals(orderNum) && order.parkName.equals(parkName) && order.visitDate.equals(visitDate)
				&& order.visitTime.equals(visitTime) && order.visitorsNum == visitorsNum && order.email.equals(email)
				&& order.telNum.equals(telNum) && order.orderType.equals(orderType);
	}

	public String toString() {
		return parkName + " " + visitDate + " " + visitTime + " " + visitorsNum + " " + email + " " + telNum + " "
				+ orderType;
	}
}