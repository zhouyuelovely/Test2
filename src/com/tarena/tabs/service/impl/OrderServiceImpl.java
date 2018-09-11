package com.tarena.tabs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

import com.tarena.tabs.dbutils.DBUtils;
import com.tarena.tabs.entity.TicketOrder;
import com.tarena.tabs.service.OrderService;
import com.tarena.tabs.ui.MyApplication;


public class OrderServiceImpl implements OrderService{
	//提交订单
	public String orderSubmit(TicketOrder order,DBUtils db) throws Exception {

		db.submitOrder(order);//调用DBUtils中的submitOrder(TicketOrder order)方法
		return "ok";
	}
}
