
package com.tarena.tabs.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tarena.tabs.dbutils.DBUtils;
import com.tarena.tabs.dbutils.MApplication;
import com.tarena.tabs.entity.Passanger;

public class AddPassangerActivity extends Activity {

	/**
	增加乘机人信息
	 */
	private Passanger passanger;//声明passanger对象
	private String[] idTypes;//证件类型
	private Spinner sp;//下拉列表
	private Button addPsgBtn;//添加按钮
	private EditText addPsgName;//乘机人姓名
	private EditText addPsgType;//身份证号码
	private EditText addPsgEmail;//邮箱
	private EditText addPsgPhone;//电话
	
	private ProgressDialog dialog=null;
	private DBUtils db;
	private ServiceContext serviceContext = ServiceContext.getServiceContext();//获取上下文对象
	private Handler handler = new Handler() {//先创建一个 Handler 对象
		public void handleMessage(Message msg) {//Handler 可以根据 Message 中的 what 值的不同来分发处理，Handler 中提供了 handleMessage 来让开发人员进行 Override
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();//对话框消失
			}
			switch (msg.what) {
			case HANDLER_MSG_ADD_PASSANGER:
				String res = (String) msg.obj;//	见后面msg.obj = res;
				if (res.equals("ok")) {
					Toast.makeText(AddPassangerActivity.this, "添加乘机人成功",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(AddPassangerActivity.this, res,
							Toast.LENGTH_SHORT).show();
					//finish();
				}// 所做的操作
				break;

			default:
				break;// 所做的操作

			}
		}
	};
	private static final int HANDLER_MSG_ADD_PASSANGER = 0;//msg.what=HANDLER_MSG_ADD_PASSANGER = 0

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DBUtils(this);
		setContentView(R.layout.activity_add_passanger);//加载添加乘机人界面的布局文件
		setData();//获取数据的方法
		setViews();//使用视图布局文件里的
		setListeners();//设置监听器
	}

	

	private void setListeners() {
		addPsgBtn.setOnClickListener(new OnClickListener() {//addPsgBtn为添加乘机人信息的按钮
			public void onClick(View v) {
				dialog = DBUtils.initProgressDialog(AddPassangerActivity.this,//调用DBUtils中的initProgressDialog方法
						true, "请稍候,正在添加", null);
				new Thread() {
					// 起一个线程 添加乘机人
					public void run() {
						
						if(addPsgName.getText().toString().equals("")){
							Message msg = new Message();
							msg.what = HANDLER_MSG_ADD_PASSANGER;
							msg.obj = "用户名不为空";//
							handler.sendMessageDelayed(msg, 0);//都会将一个消息送到一个消息对队列中，不延时
							return;
						}
						// 邮箱不能为空或者是邮箱格式不能不正确
						else if(addPsgEmail.getText().toString().equalsIgnoreCase("")||!MApplication.isEmail(addPsgEmail.getText().toString())){//调用MApplication中的isEmail(String email)方法验证邮箱格式是否正确
							Message msg = new Message();
							msg.what = HANDLER_MSG_ADD_PASSANGER;
							msg.obj = "邮箱不正确";//
							handler.sendMessageDelayed(msg, 0);//都会将一个消息送到一个消息对队列中，不延时
							return;
						}
					
						// 身份证号码不能为空或者是身份证号码的长度不能不为18位
						else if(addPsgType.getText().toString().equals("")||addPsgType.getText().toString().length()!=18){
							Message msg = new Message();
							msg.what = HANDLER_MSG_ADD_PASSANGER;
							msg.obj = "身份证件号不正确";//
							handler.sendMessageDelayed(msg, 0);//都会将一个消息送到一个消息对队列中，延时1500ms
							return;
						}
						// 电话不能为空/手机号长度不能不是11位/手机号不能不以1开头
						else if(addPsgPhone.getText().toString().equals("")||addPsgPhone.getText().toString().length()!=11|| !addPsgPhone.getText().toString().startsWith("1")){
							Message msg = new Message();
							msg.what = HANDLER_MSG_ADD_PASSANGER;
							msg.obj = "手机号不正确";//
							handler.sendMessageDelayed(msg, 0);//都会将一个消息送到一个消息对队列中，延时1500ms
							return;
						}
						passanger = new Passanger();//
						passanger.setPsgName(addPsgName.getText().toString());
						passanger.setPsgEmail(addPsgEmail.getText().toString());
						passanger.setPsgPhone(addPsgPhone.getText().toString());
						passanger.setCertifNum(addPsgType.getText().toString());
						passanger.setPsgCertifType((String) sp//下拉列表spinner
								.getSelectedItem());
						String res = serviceContext.addPassanger(passanger, db);//调用serviceContext中的addPassanger方法，增加一个乘机人信息
						Message msg = new Message();
						msg.what = HANDLER_MSG_ADD_PASSANGER;
						msg.obj = res;//
						handler.sendMessageDelayed(msg, 1500);//都会将一个消息送到一个消息对队列中，延时1500ms
					}
				}.start();
			}
		});

	}

	private void setData() {
		idTypes = getResources().getStringArray(R.array.id_type);//从strings.xml中获取id_types数组
	}

	private void setViews() {
		addPsgName = (EditText) findViewById(R.id.addPsgName);
		sp = (Spinner) findViewById(R.id.addPsgType);
		addPsgEmail = (EditText) findViewById(R.id.addPsgEmail);
		addPsgType = (EditText) findViewById(R.id.addPsgTypeNum);
		addPsgBtn = (Button) findViewById(R.id.addPsgBtn);
		addPsgPhone = (EditText) findViewById(R.id.addPsgPhone);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,//配置适配器
				android.R.layout.simple_spinner_item, idTypes);//idTypes = getResources().getStringArray(R.array.id_type);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//// 设置Spinner弹出的样式，安卓自带的
		sp.setAdapter(adapter);
	}

}
