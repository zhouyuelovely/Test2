package com.tarena.tabs.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.tarena.tabs.dbutils.DBUtils;
import com.tarena.tabs.entity.Flight;
import com.tarena.tabs.service.FlightService;


public class FlightServiceImpl implements FlightService { //开发FlightService的实现类FlightServiceImpl(是一个接口)

	public List<Flight> findByCityAndDate(String from, String to, Calendar c,DBUtils db) throws Exception {
//调用DBUtils的searchFlight方法
      List<Flight> flights = db.searchFlight(from, to, new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()));//获取指定日期

		return flights;
	}
}

























