package com.tarena.tabs.ui;


import com.tarena.tabs.ui.R;
import com.tarena.tabs.ui.ExitApplication;
import com.tarena.tabs.dbutils.DBHelper;
import com.tarena.tabs.dbutils.DBUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btLogin;//登录按钮
	private Button btRegist;//注册按钮
	private Button findpwdBtn;//忘记密码按钮
	SharedPreferences sp;//记住用户名和密码
	private EditText etPwd;//密码框
	private EditText etName;//用户名框
	private ProgressDialog dialog=null;
	private CheckBox savePasswordCB;//记住密码复选框
	private ServiceContext context = ServiceContext.getServiceContext();//获取上下文的对象
	private DBUtils db;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(dialog!=null && dialog.isShowing()){
				dialog.dismiss();//对话框消失
			}
			switch (msg.what) {
			case 0:
				login(msg.obj);
			}
		}
	};
	
	private void login(Object msg) {
		//showMenu();
		if(msg.equals("ok")){
			Toast.makeText(this, "登录成功",1).show();
			System.out.println("登入ok");
			showMenu();
			finish();
		}else{
			System.out.println("登入失败");
			new AlertDialog.Builder(MainActivity.this).setMessage(msg.toString()).setTitle("登入失败").show();
		}
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		db = new DBUtils(this);
		sp = getSharedPreferences("lq", 1);//lq为数据库的名字，1为私有
		setContentView(R.layout.activity_main);//加载布局文件
		ExitApplication.getInstance().addActivity(this);//点击key-down键时退出系统
		setViews();
		setListeners();
	}

	private void setViews() {
		etName = (EditText) findViewById(R.id.etUsername);
		etPwd = (EditText) findViewById(R.id.etPwd);
		etName.setText(sp.getString("NAME", ""));
		etPwd.setText(sp.getString("PWD", ""));
		btLogin = (Button) findViewById(R.id.btLogin);
		btRegist = (Button) findViewById(R.id.btRegist);
		findpwdBtn = (Button) findViewById(R.id.findpwdBtn);
		savePasswordCB = (CheckBox)findViewById(R.id.savePasswordCB);
		savePasswordCB.setChecked(true);
	}

	private void setListeners() {
		btLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				if(savePasswordCB.isChecked()){
//					//当记住密码的复选被选中时，提取出EditText里面的内容，放到SharedPreferences里面的NAME和PWD中
//					sp.edit().putString("NAME", etName.getText().toString()).putString("PWD",
//							etPwd.getText().toString()).commit();//提交用户名和密码
//				}else{
//					//保存用户信息
//					sp.edit().putString("NAME", "").putString("PWD",
//							"").commit();
//				}
//				login();//login方法
				startActivity(new Intent(MainActivity.this,MenuActivity.class));
			}
		});
		
		btRegist.setOnClickListener(new OnClickListener() {//注册按钮
			public void onClick(View v) {
				showReg();
			}
		});
		findpwdBtn.setOnClickListener(new OnClickListener() {//找回密码
			public void onClick(View v) {
				findpwd();
			}
		});
	}

	protected void login() {
		dialog = DBUtils.initProgressDialog(this, true, "登录中，请稍后", null);
		
		new Thread() {
			//开启一个新线程进行登录
			public void run() {
				String name = etName.getText().toString();
				String pwd = etPwd.getText().toString();
				String result=context.login(name, pwd,db);//见ServiceContext方中的login(String name, String pwd, DBUtils db)方法
				if(name.equalsIgnoreCase("")){
					Message msg = new Message();
					msg.what = 0;
					msg.obj = "用户名不能为空";
					handler.sendMessageDelayed(msg, 1500);
					return;	
				}
				else if(pwd.equalsIgnoreCase("")){
					Message msg = new Message();
					msg.what = 0;
					msg.obj = "密码不能为空";
					handler.sendMessageDelayed(msg, 1500);
					return;
				}
				Message msg = new Message();
				msg.what=0;
				msg.obj=result;
				handler.sendMessageDelayed(msg, 1500);
			}
		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
   //注册
	public void showReg() {
		Intent intent = new Intent(this, RegestActivity.class);//声明一个意图，跳转到注册界面
		startActivity(intent);//启动这个意图
	}
	//找回密码
	public void findpwd() {
		Intent intent = new Intent(this, FindPwdActivity.class);//声明一个意图。跳转到找回密码的界面
		startActivity(intent);
	}
	
	public void showMenu(){
		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);
		finish();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {String copyright;
		AlertDialog.Builder builder = new Builder(this);
		copyright = "确认退出当前系统吗？";
		builder.setMessage(copyright);
		
		builder.setTitle("提示");

		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						ExitApplication.getInstance().exit();
					}
			});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
	       });
		builder.create().show();
	}
		return true;
		}

}


