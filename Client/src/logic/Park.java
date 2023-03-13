package logic;

import java.util.ArrayList;
import java.util.List;

public class Park {

/*1*/	String parkNum;
	
/*2*/	int maxVisitors;          //max visitors allowed in park	approved by department manger
		int maxOrderVisitors;     //max order visitors allowed in park
		int difMaxOrderVisitors;  //max casual visitors allowed in park approved by department manger
		int visitorsInPark;
		int casualVisitorsInPark;
		int visitationPeriod;     //average visitation period approved by department manger

/*8*/	int maxRequested;  //max visitors allowed in park requested by park manger
		int difRequested;  //max casual visitors allowed in park requested by park manger
		int timeRequested; // average visitation period requested by park manger
	
/*11*/	List<Discount> discountsRequested=new ArrayList<>();
/*12*/	List<Discount> discounts=new ArrayList<>();


	public Park(String parkNum, int maxVisitors, int maxOrderVisitors, int difMaxOrderVisitors, int visitorsInPark,
			int casualVisitorsInPark, int visitationPeriod, int maxRequested,int difRequested , int timeRequested) {
		super();
		this.parkNum = parkNum;
		this.maxVisitors = maxVisitors;
		this.maxOrderVisitors = maxOrderVisitors;
		this.difMaxOrderVisitors = difMaxOrderVisitors;
		this.visitorsInPark = visitorsInPark;
		this.casualVisitorsInPark = casualVisitorsInPark;
		this.visitationPeriod = visitationPeriod;
		this.maxRequested=maxRequested;
		this.difRequested=difRequested;
		this.timeRequested=timeRequested;
	}

	public int available() {
		return difMaxOrderVisitors-casualVisitorsInPark;
	}

	
	@Override
	public String toString() {
		return String.format("%s %d %d %d %d %d %d %d %d %d",parkNum, maxVisitors, maxOrderVisitors, difMaxOrderVisitors, visitorsInPark, casualVisitorsInPark, visitationPeriod, maxRequested, difRequested, timeRequested);
	}



	public String getParkNum() {
		return parkNum;
	}
	public void setParkNum(String parkNum) {
		this.parkNum = parkNum;
	}




	public int getMaxVisitors() {
		return maxVisitors;
	}
	public void setMaxVisitors(int maxVisitors) {
		this.maxVisitors = maxVisitors;
	}




	public int getMaxOrderVisitors() {
		return maxOrderVisitors;
	}
	public void setMaxOrderVisitors(int maxOrderVisitors) {
		this.maxOrderVisitors = maxOrderVisitors;
	}




	public int getDifMaxOrderVisitors() {
		return difMaxOrderVisitors;
	}
	public void setDifMaxOrderVisitors(int difMaxOrderVisitors) {
		this.difMaxOrderVisitors = difMaxOrderVisitors;
	}




	public int getCasualVisitorsInPark() {
		return casualVisitorsInPark;
	}
	public void setCasualVisitorsInPark(int casualVisitorsInPark) {
		this.casualVisitorsInPark = casualVisitorsInPark;
	}




	public int getVisitationPeriod() {
		return visitationPeriod;
	}
	public void setVisitationPeriod(int visitationPeriod) {
		this.visitationPeriod = visitationPeriod;
	}


	
	public int getVisitorsInPark() {
		return visitorsInPark;
	}
	public void setVisitorsInPark(int visitorsInPark) {
		this.visitorsInPark = visitorsInPark;
	}

	
	
	public int getMaxRequested() {
		return maxRequested;
	}
	public void setMaxRequested(int maxRequested) {
		this.maxRequested = maxRequested;
	}

	
	
	public int getDifRequested() {
		return difRequested;
	}
	public void setDifRequested(int difRequested) {
		this.difRequested = difRequested;
	}
	
	
	public int getTimeRequested() {
		return timeRequested;
	}
	public void setTimeRequested(int timeRequested) {
		this.timeRequested = timeRequested;
	}
	
	
	public void addDiscount(Discount discount) {
		discounts.add(discount);
	}
	public void removeDiscount(Discount discount) {
		discounts.remove(discount);
	}

}
