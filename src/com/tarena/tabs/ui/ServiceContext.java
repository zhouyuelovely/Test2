package com.tarena.tabs.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.tarena.tabs.dbutils.DBUtils;

import com.tarena.tabs.entity.City;
import com.tarena.tabs.entity.Flight;
import com.tarena.tabs.entity.Passanger;
import com.tarena.tabs.entity.TicketOrder;
import com.tarena.tabs.entity.User;

import com.tarena.tabs.service.CityService;
import com.tarena.tabs.service.FlightService;
import com.tarena.tabs.service.OrderService;
import com.tarena.tabs.service.PassangerService;
import com.tarena.tabs.service.UserService;

import com.tarena.tabs.service.impl.CityServiceImpl;
import com.tarena.tabs.service.impl.FlightServiceImpl;
import com.tarena.tabs.service.impl.OrderServiceImpl;
import com.tarena.tabs.service.impl.PassangerServiceImpl;
import com.tarena.tabs.service.impl.UserServiceImpl;

public class ServiceContext {

	private static ServiceContext serviceContext;//ServiceContext 代表为 Hessian 客户端提供服务的上下文环境
	private UserService userService;//用户
	private CityService cityService;//城市
	private FlightService flightService;//航班
	
	private PassangerService passangerService;//乘客
	private OrderService orderService;//订单

	private ServiceContext() {//对之前的service的实现类的初始化
		this.userService = new UserServiceImpl();
		this.cityService = new CityServiceImpl();
		this.flightService = new FlightServiceImpl();
		
		this.passangerService = new PassangerServiceImpl();
		this.orderService = new OrderServiceImpl();
	}

	public static ServiceContext getServiceContext() {//ServiceContext 的get方法获得控件当前的设置值
		if (serviceContext == null) {
			serviceContext = new ServiceContext();
		}
		return serviceContext;
	}
//把几个service中的方法统一再写到这边来，方便调用
	/**
	 * @param name
	 * @param pwd
	 * @return
	 */
	public String login(String name, String pwd, DBUtils db) {
		String result = "";
		try {
			result = userService.login(name, pwd, db);
		} catch (Exception e) {
			return e.getMessage();
		}
		return result;
	}

	/**
	 * @return
	 */
	//城市
	public List<City> findAllCities(String[] city) {// 查询所有的城市列表 
		List<City> cities = new ArrayList<City>();
		try {
			cities = cityService.findAll(city);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cities;
	}
//通过航班，出发城市，到达城市，目前日期来查询航班信息
	public List<Flight> findFlightByCityAndDate(String fromCity, String toCity,
			Calendar c, DBUtils db) {
		List<Flight> flights = new ArrayList<Flight>();
		try {
			flights = flightService.findByCityAndDate(fromCity, toCity, c, db);
		} catch (Exception e) {
			e.printStackTrace();
			return flights = null;
		}
		return flights;
	}

	// 获取当前用户所添加过的所有的乘机人信息
	public List<Passanger> getMyPassangers(DBUtils db) {
		List<Passanger> ps = null;
		try {
			ps = passangerService.findMyPassangers(db);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ps;
	}
	//增加一个乘机人信息
		public String addPassanger(Passanger passanger, DBUtils db) {
			String res = "";
			try {
				res = passangerService.addPassanger(passanger, db);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
		}
	
//更新乘机人信息
	public String updatePassangerInfo(Passanger passanger, DBUtils db) {
		String res = "";
		try {
			res = passangerService.updatePassanger(passanger, db);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	//删除乘机人信息
		public String removeMyPassangers(int ids, DBUtils db) {
			String res = "";
			try {
				res = passangerService.removeAllByIds(ids, db);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
		}
//提交订单
	public String orderSubmit(TicketOrder order, DBUtils db) {
		String res = "";
		try {
			res = orderService.orderSubmit(order, db);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

//注册用户信息
	public String regist(User user, DBUtils db) {
		String res = "ok";//默认值
		try {
			res = userService.regist(user, db);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

//修改密码
	public String modifyPwd(String who, String op, String np1, DBUtils db) {
		String res = "";
		try {
			res = userService.modifyPwd(who, op, np1, db);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
