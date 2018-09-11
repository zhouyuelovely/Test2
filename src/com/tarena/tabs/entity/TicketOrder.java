package com.tarena.tabs.entity;

import java.sql.Timestamp;

public class TicketOrder implements java.io.Serializable {
	public static final String ORDERID ="orderId";
	public static final String FlightId ="FlightId";
	public static final String OrderState ="OrderState";
	public static final String OrderDate ="OrderDate";
	public static final String UserIdCard ="UserIdCard";
	public static final String OrderMoney ="OrderMoney";
	public static final String TABLENAME ="tb_order";

	public static final String FLAY_DAY ="flyday";//时间
	
	private String orderId;//订单项号
	private String userId;//用户ID
	private String orderMoney;
	private String orderDate;
	private String orderState;
	public String flightId,fly_day;//

	/** default constructor */
	public TicketOrder() {
	}

	/** minimal constructor */
	public TicketOrder(String orderId, String userId, String orderMoney, String orderDate, String flightId) {
		this.orderId = orderId;//订单项号
		this.userId = userId;//用户id
		this.orderMoney = orderMoney;//机票价格
		this.orderDate = orderDate;//订单时间
		this.flightId = flightId;//航班号
	}

	/** full constructor */
	public TicketOrder(String d,String orderId, String userId, String orderMoney, String orderDate,String orderState,
			String flightId) {
		this.orderId = orderId;
		this.userId = userId;
		this.orderMoney = orderMoney;
		this.orderDate = orderDate;
		this.orderState = orderState;
		this.flightId = flightId;
		fly_day =d;//时间
	}

	// Property accessors

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderMoney() {
		return this.orderMoney;
	}

	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}

	public String getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}


	public String getFlightId() {
		return this.flightId;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

}