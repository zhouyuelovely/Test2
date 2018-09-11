package com.tarena.tabs.service;

import java.util.List;

import com.tarena.tabs.entity.City;


public interface CityService {
	
	/**
	 * 查询所有的城市列表 
	 * @return
	 * @throws Exception
	 */
	List<City> findAll(String [] city) throws Exception;
	
}
