package com.tarena.tabs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
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
import com.tarena.tabs.entity.User;
import com.tarena.tabs.service.UserService;
import com.tarena.tabs.ui.MyApplication;


public class UserServiceImpl implements UserService {

	@Override//修改密码
	public String modifyPwd(String who,String oldpwd, String newpwd, DBUtils db)
			throws Exception {
		if(db.mPWD(who, newpwd, oldpwd)){//调用DBUtils中的mPWD(String name, String pwd, String opwd)方法
			return "ok";
		}else{
			return "error";
		}
	}

	@Override//注册业务
	public String regist(User user, DBUtils db) throws Exception {
		//判断所注册 的用户名是否已经存在了
		boolean s = db.insertUser(user.getUserEmail(), user.getUserTelephone(),//调用DBUtils中的minsertUser()方法
				user.getUserCertifNum(), user.getUserCertifType(),
				user.getPassword(), user.getUserName(), user.getUserLoginName());
		if(s){
			return "ok";
		}else{
			return "已存在";
		}
		
	}
     //登录业务
	public String login(String name, String pwd, DBUtils db) throws Exception {
		if(db.checkLogin(name, pwd)){//调用DBUtils中的checkLogin方法
			return "ok";
		}else{ 
			return "error";
		}

	}
}
