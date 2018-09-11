package com.tarena.tabs.ui;



import com.tarena.tabs.dbutils.DBHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class UserAdminActivity extends Activity {
	
		
	
	Button modpwdBtn;//修改密码按钮
	Button btRegist;//更新用户注册的按钮
	ImageView iv; //返回

	 protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		setContentView(R.layout.activity_user_admin);//加载布局文件
		 iv = (ImageView)findViewById(R.id.imageView2);
		    iv.setOnClickListener(new OnClickListener(){
				public void onClick(View v) { 
						  finish();//这个是关键 
						  } 
				     });  
		modpwdBtn = (Button)findViewById(R.id.modpwdBtn);
		
		modpwdBtn.setOnClickListener(new OnClickListener(){
			   public void onClick(View v) { 
					Intent intent=new Intent();
					intent.setClass(UserAdminActivity.this,ModpwdActivity.class);//跳转到修改密码的界面
					startActivity(intent);

			            } 

			    });         
		btRegist = (Button) findViewById(R.id.btRegist);
		
		btRegist.setOnClickListener(new OnClickListener(){
			   public void onClick(View v) { 
				 
				   
					Intent intent=new Intent();//声明一个意图
					intent.putExtra("f", "f");//传参数
					intent.setClass(UserAdminActivity.this,RegestActivity.class);//跳转到注册界面
					startActivity(intent);//启动一个意图

			            } 

			    });         
	}

	
	
	}  
	
	
