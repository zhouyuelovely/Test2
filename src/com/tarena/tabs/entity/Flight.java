package com.tarena.tabs.entity;

import java.io.Serializable;

public class Flight implements Serializable {//serialization 允许你将实现了Serializable接口的对象转换为字节序列，这些字节序列可以被完全存储以备以后重新生成原来的对象。  

	public static final String TABLENAME ="flight";

	public static final String FlightNum ="FlightNum"; 
	public static final String PlanFromDate ="PlanFromDate";
	public static final String PlanToDate ="PlanToDate";
	public static final String FromTime ="FromTime";
	public static final String ToTime ="ToTime";
	
	public static final String Price ="Price";
	public static final String FromAirport ="FromAirport";
	public static final String ToAirport ="ToAirport";
	public static final String Days ="Days";
	public static final String FromCity ="FromCity";
	public static final String ToCity ="ToCity";
	public static final String CurrentPrice ="CurrentPrice";
	public static final String Tax1Price ="Tax1Price";
	public static final String Tax2Price ="Tax2Price";
	private String id;
	private String flightId;
	private String flightNum;
	private String planFromDate;
	private String planToDate;
	private String fromTime;
	private String toTime;
	private String plane;
	private String price;
	private String fromAirport;
	private String toAirport;
	private String days;
	private String fromCity;
	private String toCity;

	public Flight() {
		super();//调用父类的构造方法
	}

	public Flight(String id, String flightId, String planFromDate, String planToDate, String fromTime, String toTime,
			String plane, String price, String fromAirport, String toAirport, String days, String fromCity,
			String toCity) {
		super();//调用父类的方法
		this.id = id;
		this.flightId = flightId;
		this.planFromDate = planFromDate;//起飞日期
		this.planToDate = planToDate;//到达日期
		this.fromTime = fromTime;//起飞时间
		this.toTime = toTime;//到达时间
		this.plane = plane;
		this.price = price;
		this.fromAirport = fromAirport;
		this.toAirport = toAirport;
		
		this.fromCity = fromCity;
		this.toCity = toCity;
	}

	public String getFlightNum() {
		return flightNum;
	}

	public void setFlightNum(String flightNum) {
		this.flightNum = flightNum;
	}

	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlightId() {
		return flightId;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	public String getPlanFromDate() {
		return planFromDate;
	}

	public void setPlanFromDate(String planFromDate) {
		this.planFromDate = planFromDate;
	}

	public String getPlanToDate() {
		return planToDate;
	}

	public void setPlanToDate(String planToDate) {
		this.planToDate = planToDate;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public String getPlane() {
		return plane;
	}

	public void setPlane(String plane) {
		this.plane = plane;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getFromAirport() {
		return fromAirport;
	}

	public void setFromAirport(String fromAirport) {
		this.fromAirport = fromAirport;
	}

	public String getToAirport() {
		return toAirport;
	}

	public void setToAirport(String toAirport) {
		this.toAirport = toAirport;
	}

	

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	@Override
	public String toString() {
		return "Flight [\nid=" + id + ", \nflightId=" + flightId + ", \nplanFromDate=" + planFromDate
				+ ", \nplanToDate=" + planToDate + ", \nfromTime=" + fromTime + ", \ntoTime=" + toTime + ", \nplane="
				+ plane + ", \nprice=" + price + ", \nfromAirport=" + fromAirport + ", \ntoAirport=" + toAirport
				+ ", \ndays=" + days + ", \nfromCity=" + fromCity + ", \ntoCity=" + toCity + "]";
	}
	public String getTitle() {
		StringBuffer sbf = new StringBuffer();
		sbf.append(this.planFromDate + "   "+this.plane + "   " +this.flightId);
		return sbf.toString();
	}
//获取去的信息
	public CharSequence getFromInfo() {
		return planFromDate+"    "+toTime  + "    "+fromAirport;
	}
//获取到达的信息
	public CharSequence getToInfo() {
		return planToDate+"    "+fromTime+"    "+toAirport;
	}

	
}
