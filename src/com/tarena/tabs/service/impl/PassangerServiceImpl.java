package com.tarena.tabs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Paint.Join;
import android.util.Log;

import com.tarena.tabs.dbutils.DBUtils;
import com.tarena.tabs.entity.Passanger;
import com.tarena.tabs.service.PassangerService;


public class PassangerServiceImpl implements PassangerService {
	//添加乘机人
	public String addPassanger(Passanger passanger,DBUtils db) throws Exception {

		db.insertPassanger(passanger);//数据库的加入
		return "ok";
	}
//通过id列表删除我的乘机人
	public String removeAllByIds(int ids,DBUtils db) throws Exception {

		if(db.delPassanger(ids+"")){
			return "ok";
		}else{
			return "error";
		}
		
	}
//获取当前用户所添加过的所有的乘机人信息
	@Override
	public List<Passanger> findMyPassangers(DBUtils db) throws Exception {
	
		List<Passanger> ps = db.getPassangers();//调用DBUtils中的getPassangers()方法

		return ps;
	}
//修改更新乘机人信息
	public String updatePassanger(Passanger passanger,DBUtils db) throws Exception {

	
		if(db.moPassanger(passanger)){//调用DBUtils中的moPassanger()方法
			return "ok";
		}else{
			return "error";
		}
		
	}

}
