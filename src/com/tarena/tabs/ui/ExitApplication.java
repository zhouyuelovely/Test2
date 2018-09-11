package com.tarena.tabs.ui;

/**按key-down键退出程序**/
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.KeyEvent;

public class ExitApplication extends Application {


		private List<Activity> activityList = new LinkedList<Activity>();
		private static ExitApplication instance;

		private ExitApplication()
		{
		}
		//单例模式中获取唯一的MyApplication实例
		public static ExitApplication getInstance()
		{
		if(null == instance)
		{
		instance = new ExitApplication();
		}
		return instance;
		}
		//添加Activity到容器中
		public void addActivity(Activity activity)
		{
		activityList.add(activity);
		}
		//遍历所有Activity并finish
		public void exit()
		{
		for(Activity activity:activityList)
		{
		activity.finish();
		}
		System.exit(0);
		}
		
}