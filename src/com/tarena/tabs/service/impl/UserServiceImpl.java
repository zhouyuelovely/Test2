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

	@Override//�޸�����
	public String modifyPwd(String who,String oldpwd, String newpwd, DBUtils db)
			throws Exception {
		if(db.mPWD(who, newpwd, oldpwd)){//����DBUtils�е�mPWD(String name, String pwd, String opwd)����
			return "ok";
		}else{
			return "error";
		}
	}

	@Override//ע��ҵ��
	public String regist(User user, DBUtils db) throws Exception {
		//�ж���ע�� ���û����Ƿ��Ѿ�������
		boolean s = db.insertUser(user.getUserEmail(), user.getUserTelephone(),//����DBUtils�е�minsertUser()����
				user.getUserCertifNum(), user.getUserCertifType(),
				user.getPassword(), user.getUserName(), user.getUserLoginName());
		if(s){
			return "ok";
		}else{
			return "�Ѵ���";
		}
		
	}
     //��¼ҵ��
	public String login(String name, String pwd, DBUtils db) throws Exception {
		if(db.checkLogin(name, pwd)){//����DBUtils�е�checkLogin����
			return "ok";
		}else{ 
			return "error";
		}

	}
}
