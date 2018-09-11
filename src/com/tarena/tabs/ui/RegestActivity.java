package com.tarena.tabs.ui;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tarena.tabs.dbutils.DBHelper.TABLEUSER;
import com.tarena.tabs.dbutils.DBUtils;
import com.tarena.tabs.dbutils.MApplication;
import com.tarena.tabs.entity.User;

public class RegestActivity extends Activity {

	private String[] idTypes;//证件类型
	private Spinner sp;//下拉列表
	private Button btReg;//注册按钮
	private EditText etNum;//身份证号
	private EditText etEmail;//邮箱
	private EditText etPwd2;//确认密码
	private EditText etPwd;//密码
	private EditText etName;//用户名
	private EditText etRealName;//用户真实姓名
	//private EditText etType;
	private EditText etPhone;//电话
	private DBUtils db;
	ProgressDialog dialog ;//对话框
	private ServiceContext serviceContext = ServiceContext.getServiceContext();//获取serviceContext对象
	private Handler handler = new Handler() {//先创建一个 Handler 对象
		////Handler 可以根据 Message 中的 what 值的不同来分发处理，Handler 中提供了 handleMessage 来让开发人员进行 Override
		public void handleMessage(android.os.Message msg) {
			if(dialog!=null && dialog.isShowing()){
				dialog.dismiss();//对话框消失
			}
			switch (msg.what) {
			case HANDLER_REGIST_MSG:
				String res = (String) msg.obj;
				if (res.equals("ok")) {
					Toast.makeText(RegestActivity.this, "注册成功",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(RegestActivity.this, res, Toast.LENGTH_SHORT)
							.show();
				}
				break;
			}
		};
	};

	private static final int HANDLER_REGIST_MSG = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		setContentView(R.layout.activity_regest);//加载布局文件
		db = new DBUtils(this);
		setData();//获取数据的方法
		setViews();//使用视图布局文件里的
		setListeners();//设置监听器
	}

	private void setListeners() {
		btReg.setOnClickListener(new OnClickListener() {//btReg为注册的按钮
			public void onClick(View v) {
				dialog =DBUtils.initProgressDialog(RegestActivity.this, true, getIntent().getStringExtra("f")==null?"正在注册":"正在保存", null);
				new Thread() {
					//开启一个线程
					public void run() {
						if(getIntent().getStringExtra("f")!=null){
							//调用DBUtils中的setUser(String id,String type,String phone,String rname,String email,String uname)方法。即修改已注册用户的信息
							db.setUser(etNum.getText().toString(), sp.getSelectedItem().toString()+"", 
									etPhone.getText().toString(), etRealName.getText().toString(),
									etEmail.getText().toString(), etName.getText().toString());
							Message msg = new Message();
							msg.what = HANDLER_REGIST_MSG;
							msg.obj = "ok";
							handler.sendMessageDelayed(msg, 1500);//延时1500ms
							return;
						}
					
						if (etName.getText().toString().equalsIgnoreCase("")) {
							// 用户名不能为空
							Message msg = new Message();
							msg.what = HANDLER_REGIST_MSG;
							msg.obj = "用户名不能为空";
							handler.sendMessageDelayed(msg, 1500);
							return;
						}
						else if(etPwd.getText().toString().equalsIgnoreCase("")) {
							// 密码不能为空
							Message msg = new Message();
							msg.what = HANDLER_REGIST_MSG;
							msg.obj = "密码不能为空";
							handler.sendMessageDelayed(msg, 1500);
							return;
						}
						else if(etPwd2.getText().toString().equalsIgnoreCase("")) {
							// 确认密码不能为空
							Message msg = new Message();
							msg.what = HANDLER_REGIST_MSG;
							msg.obj = "确认密码不能为空";
							handler.sendMessageDelayed(msg, 1500);
							return;
						}
						else if(!etPwd.getText().toString().equals(etPwd2.getText().toString())){
							// 两次密码不能不一致
							Message msg = new Message();
							msg.what = HANDLER_REGIST_MSG;
							msg.obj = "两次密码不一致";
							handler.sendMessageDelayed(msg, 1500);
							return;
						}
						else if(etRealName.getText().toString().equalsIgnoreCase("")) {
							// 真实姓名不能为空
							Message msg = new Message();
							msg.what = HANDLER_REGIST_MSG;
							msg.obj = "真实姓名不能为空";
							handler.sendMessageDelayed(msg, 1500);
							return;
						}
						else if(etPhone.getText().toString().equalsIgnoreCase("")||etPhone.getText().toString().length()!=11||!etPhone.getText().toString().startsWith("1")) {
							// 电话不能为空/手机号长度不能不是11位/手机号不能不以1开头
							Message msg = new Message();
							msg.what = HANDLER_REGIST_MSG;
							msg.obj = "手机号不正确";
							handler.sendMessageDelayed(msg, 1500);
							return;
						}
						//调用MApplication中的isEmail(String email)方法验证邮箱格式是否正确
						else if(etEmail.getText().toString().equalsIgnoreCase("")|| !MApplication.isEmail(etEmail.getText().toString())) {
							// 邮箱不能为空或者是邮箱格式不能不正确
							Message msg = new Message();
							msg.what = HANDLER_REGIST_MSG;
							msg.obj = "邮箱不正确";
							handler.sendMessageDelayed(msg, 1500);
							return;
						}
						else if(etNum.getText().toString().equalsIgnoreCase("")||etNum.getText().toString().length()!=18) {
							// 身份证号码不能为空或者是身份证号码的长度不能不为18位
							Message msg = new Message();
							msg.what = HANDLER_REGIST_MSG;
							msg.obj = "身份证号码不正确";
							handler.sendMessageDelayed(msg, 1500);
							return;
						}
						//方法为空，往里面写值
						User user = new User();
						user.setUserEmail(etEmail.getText().toString());
						user.setPassword(etPwd.getText().toString());
						user.setUserCertifType(sp.getSelectedItem().toString());
						user.setUserCertifNum(etNum.getText().toString());
						user.setUserName(etRealName.getText().toString());
						user.setUserLoginName(etName.getText().toString());
						user.setUserTelephone(etPhone.getText().toString());

						String res = serviceContext.regist(user, db);

						Message msg = new Message();
						msg.what = HANDLER_REGIST_MSG;
						msg.obj = res;
						handler.sendMessageDelayed(msg, 1500);
						
					}
				}.start();
			}
		});
	}

	private void setData() {
		idTypes = getResources().getStringArray(R.array.id_type);//从strings。xml文件中获取string-array name="id_type"里面的值
	}

	private void setViews() {
		etName = (EditText) findViewById(R.id.regist_name);//用户名
		etRealName = (EditText) findViewById(R.id.regist_realname);//用户真实姓名
		etPwd = (EditText) findViewById(R.id.regist_pwd1);//密码
		etPwd2 = (EditText) findViewById(R.id.regist_pwd2);//确认密码
		etEmail = (EditText) findViewById(R.id.regist_email);//邮箱
		sp = (Spinner) findViewById(R.id.regist_cerifType);//下拉列表
		etNum = (EditText) findViewById(R.id.regist_cerifNum);//身份证号码
		btReg = (Button) findViewById(R.id.regist_btn);//注册按钮
		etPhone = (EditText) findViewById(R.id.regist_phone);//电话
		//当光标改变时（如从一个EditText移到另一个EditText）onFocusChange(View v, boolean hasFocus)方法就会触发，在onFocusChange(View v, boolean hasFocus)方法中可以自定义行为。
		etPwd.setOnFocusChangeListener(new OnFocusChangeListener() {//当把光标移到密码这边时会显示用户名已经存在了
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg0.getId()== R.id.regist_pwd1 && etName.getText().toString()!=null && arg1){//当密码和用户名不为空时
					if(db.checkUserExist(etName.getText().toString())){//调用DBUtils中的checkUserExist的(String username)方法检查用户名是否已经存在了
						Toast.makeText(RegestActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				//它显示的是一组字符串，每个项是按照系统默认的布局文件android.R.layout.simple_list_item
		   android.R.layout.simple_spinner_item, idTypes);//数据源 idTypes = getResources().getStringArray(R.array.id_type)
		//设置Spinner弹出的样式  
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    sp.setAdapter(adapter);
		    
		    if(getIntent().getStringExtra("f")!=null){//如果值不为空的话
		    	 ArrayList<HashMap<String, Object>> arr = db.getUser();//调用DBUtils中的getUser()方法，查询所有用户的信息
		    	 etName.setText(arr.get(0).get(TABLEUSER.USERNAME).toString());//setText这是编辑框中的内容   获取第一行
		    	 etRealName.setText(arr.get(0).get(TABLEUSER.REALNAME).toString());
		    	 etEmail.setText(arr.get(0).get(TABLEUSER.EMAIL).toString());
		    	 etPhone.setText(arr.get(0).get(TABLEUSER.PHONE).toString());
		    	 etNum.setText(arr.get(0).get(TABLEUSER.IDCARD).toString());
		    	 etPwd.setVisibility(View.GONE);//隐藏（GONE）密码
		    	 etPwd2.setVisibility(View.GONE);//隐藏（GONE）确认密码
                  //遍历数组
		    	 for(int i =0;i<idTypes.length;i++){
		    		 if(arr.get(0).get(TABLEUSER.IDTYPE).toString().equals(idTypes[i])){//、下拉框
		    			 sp.setSelection(i);//设置下拉框选中的位置
		    		 }
		    	 }
		    	
		    	 btReg.setText("保存");
		    	 etName.setEnabled(false);//不允许修改已注册的用户名，显示灰色
		    }

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.regest, menu);
		return true;
	}

}

