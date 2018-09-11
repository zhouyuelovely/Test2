package com.tarena.tabs.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tarena.tabs.dbutils.DBUtils;
import com.tarena.tabs.entity.Passanger;

public class ModifyPassangerActivity extends Activity {
	private Passanger passanger;//？
	private String[] idTypes;//身份证选择
	private Spinner sp;//下拉列表框
	private Button modPsgBtn;//确认修改按钮
	private EditText etName;//乘客姓名
	private EditText etTypeNum;//身份证证号
	private EditText etEmail;//用户邮箱
	private EditText etPhone;//电话号码
	private ProgressDialog dialog=null;
	private DBUtils db; 
	private ServiceContext serviceContext = ServiceContext.getServiceContext();//获取上下文对象

	private static final int HANDLER_MODIFY_PASSANGER_RESULT = 0;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(dialog !=null && dialog.isShowing()){
				dialog.dismiss();//对话框消失
			}
			switch (msg.what) {
			case HANDLER_MODIFY_PASSANGER_RESULT:
				String res=(String)msg.obj;
				if(res.equals("ok")){
					Toast.makeText(ModifyPassangerActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
					finish();
					Intent intent = new Intent(ModifyPassangerActivity.this, ListPassangerActivity.class);//跳转回ListPassangerActivity.class界面上
					startActivity(intent);
				}else{
					Toast.makeText(ModifyPassangerActivity.this, res, Toast.LENGTH_SHORT).show();
					finish();
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		db = new DBUtils(ModifyPassangerActivity.this);
		setContentView(R.layout.activity_modify_passanger);//加载布局文件
		setData();
		setViews();
		setListeners();
	}

	private void setListeners() {
		modPsgBtn.setOnClickListener(new OnClickListener() {//点击更新联系人按钮
			public void onClick(View arg0) {
				dialog = DBUtils.initProgressDialog(ModifyPassangerActivity.this, true, "请稍等", null);
				new Thread() {
					public void run() {
						// 启动一个线程 修改联系人信息
						String psgName=etName.getText().toString();
						String psgEmail=etEmail.getText().toString();
						String psgTypeNum=etTypeNum.getText().toString();
						String psgPhone=etPhone.getText().toString();
						String psg=(String)sp.getSelectedItem();
						String id=passanger.getPsgId();
						Passanger ps=new Passanger();//
						ps.setPsgName(psgName);
						ps.setPsgEmail(psgEmail);
						ps.setPsgPhone(psgPhone);
						ps.setCertifNum(psgTypeNum);
						ps.setPsgCertifType(psg);
						ps.setPsgId(id);
						String res = serviceContext.updatePassangerInfo(ps,db);//调用DBUtils中的updatePassangerInfo方法
						
						Message msg = new Message();
						msg.what = HANDLER_MODIFY_PASSANGER_RESULT;
						msg.obj = res;
						handler.sendMessageDelayed(msg,1500);
					}
				}.start();

			}
		});

	}

	private void setData() {
		idTypes = getResources().getStringArray(R.array.id_type);////从strings.xml中获取id_types数组
	}

	private void setViews() {
		etName = (EditText) findViewById(R.id.psg_add_name);
		sp = (Spinner) findViewById(R.id.psg_add_type);
		etEmail = (EditText) findViewById(R.id.psg_add_email);
		etTypeNum = (EditText) findViewById(R.id.psg_add_typeNum);
		modPsgBtn = (Button) findViewById(R.id.modpsg_submitbtn);
		etPhone = (EditText) findViewById(R.id.etPhone);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, idTypes);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);

		// 获取用户选择的乘客显示乘客信息  从OrderInputActivity传来的参数
		passanger = (Passanger) (getIntent().getSerializableExtra("passanger"));
		if (passanger != null) {
			etName.setText(passanger.getPsgName());
			etEmail.setText(passanger.getPsgEmail());
			int selectedIndex = 0;
			for (int i = 0; i < idTypes.length; i++) {
				if (idTypes[i].equals(passanger.getPsgCertifType())) {
					selectedIndex = i;
					break;
				}
			}
			sp.setSelection(selectedIndex);//指数 - 一个整数，指定列表项选择，其中0指定列表中的第一项，-1表示不选择
			etTypeNum.setText(passanger.getCertifNum());
			etPhone.setText(passanger.getPsgPhone());
		}

	}

}
