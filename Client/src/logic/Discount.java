package logic;

public class Discount {
	private String parkNum;
	private double discount;
	private String startDate;
	private String endDate;

	public Discount(String parkNum, Double discount, String startDate, String endDate) {
		super();
		this.parkNum = parkNum;
		this.discount = discount;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getParkNum() {
		return parkNum;
	}

	public void setParkNum(String parkNum) {
		this.parkNum = parkNum;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDAte) {
		this.endDate = endDAte;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Discount))
			return false;
		Discount discount = (Discount) obj;
		return parkNum.equals(discount.parkNum) && this.discount == discount.discount
				&& startDate.equals(discount.startDate) && endDate.equals(discount.endDate);
	}

	@Override
	public String toString() {
		return String.format("%s %f %s %s", parkNum, discount, startDate, endDate);
	}

}
