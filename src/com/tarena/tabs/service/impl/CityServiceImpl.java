package com.tarena.tabs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;

import com.tarena.tabs.entity.City;
import com.tarena.tabs.service.CityService;

/*
 * 
 * 开发CityService的实现类CityServiceImpl(是一个接口)
 * */
public class CityServiceImpl implements CityService{//
		
	public List<City> findAll(String[] city) throws Exception {//查找所有城市的方法

		List<City> cities = new ArrayList<City>();//声明一个list集合 cities
		 
		for (int i=0; i<city.length; i++) {
			 
			City c = new City(city[i]);
			cities.add(c);
		}
		
		return cities;
		
	}
}
























