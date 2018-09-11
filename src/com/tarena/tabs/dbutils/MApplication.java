package com.tarena.tabs.dbutils;

import java.util.regex.Pattern;

import com.tarena.tabs.ui.R;

import android.app.Application;
import android.content.SharedPreferences;

public class MApplication extends Application {//Application是系统一开机就要启动的项 ，利用MApplication继承它可以通过修改MApplication来使系统启动时经过它
	SharedPreferences sp;//记住用户名和密码
	DBUtils db;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		sp = getSharedPreferences("com.lq.share", MODE_PRIVATE);// // 获取SharedPreferences对象   
		db = new DBUtils(this);
		if (sp.getBoolean("isfirst", true)) {//若果第一次没有值 是true ，有点花就是存的值
			db.initFlay(getResources().getStringArray(R.array.city),
					getResources().getStringArray(R.array.company));//初始化航班
			sp.edit().putBoolean("isfirst", false).commit();// Editor editor = sp.edit(); 即 获取Editor对象  
		}//这个代码在一个程序中只执行一次
		super.onCreate();//是调用父类的onCreate构造函数
	}
	//验证邮箱格式是否正确
	public static boolean isEmail(String email) {  
        if (email == null || email.length() < 1 || email.length() > 256) {  
            return false;  
        }  
        Pattern pattern = Pattern  
                .compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");  
        return pattern.matcher(email).matches();  
    }  


}
