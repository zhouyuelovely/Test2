package com.tarena.tabs.service;

import java.util.Calendar;
import java.util.List;

import com.tarena.tabs.dbutils.DBUtils;
import com.tarena.tabs.entity.Flight;

public interface FlightService {

	/**
	 * 通过起飞城市，到达城市，起飞日期查询航班信息
	 * @param from
	 * @param to
	 * @param c
	 * @return
	 * @throws Exception
	 */
	List<Flight> findByCityAndDate(String from, String to, Calendar c,DBUtils db) throws Exception;

}
