package com.tarena.tabs.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Application;

import com.tarena.tabs.entity.User;

public class MyApplication extends Application {
	
	public static ArrayList<Activity> list;//定义把所有的已经过的activity存到一个集合里
	
	public static void addList(Activity ac){
		 if(list==null){
			 list = new ArrayList<Activity>();
			 
		 }
		 list.add(ac);//存已经经过的activity
	}
	public static ArrayList<Activity> getList(){
		return list;
	}


}
