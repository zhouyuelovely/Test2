package com.tarena.tabs.ui;

import com.tarena.tabs.dbutils.DBUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModpwdActivity extends Activity {

	private EditText oldpwd;//旧密码
	private EditText newpwd;//新密码
	private EditText newpwd2;//确认新密码
	private Button submitBtn;//提交按钮
	private ProgressDialog dialog=null;
	private ServiceContext serviceContext = ServiceContext.getServiceContext();//获取上下文对象
	private DBUtils db;
	private static final int HANDLER_MODIFYPWD_RESULT = 0;
	private static final int HANDLER_MODIFYPWD_SAMEPWD = 1;
	private Handler handler = new Handler() {//Handler就承担着接受子线程传过来的(子线程用sedMessage()方法传弟)Message对象，(里面包含数据)  , 把这些消息放入主线程队列中，配合主线程进行更新UI。
		public void handleMessage(android.os.Message msg) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();//对话框消失
			}
			switch (msg.what) {
			case HANDLER_MODIFYPWD_RESULT:
				String res = (String) msg.obj;
				if (res.equals("ok")) {
					Toast.makeText(ModpwdActivity.this, "修改成功",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(ModpwdActivity.this, res, Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case HANDLER_MODIFYPWD_SAMEPWD:
				Toast.makeText(ModpwdActivity.this, "两次密码输入不一致",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		db = new DBUtils(this);
		setContentView(R.layout.activity_modpwd);//加载布局文件
		setViews();
		setListeners();
	}

    private void setListeners() {
		submitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog = DBUtils.initProgressDialog(ModpwdActivity.this, true,
						"请稍等", null);
				new Thread() {
					//开启一个新线程修改密码
					public void run() {
						String op = oldpwd.getText().toString();
						String np1 = newpwd.getText().toString();
						String np2 = newpwd2.getText().toString();
						if (np1.equals(np2)) {//如果密码和确认密码一致的话
							// 提交请求修改密码
							String res = serviceContext.modifyPwd("", op, np1,//调用serviceContext中的modifyPwd方法
									db);
							Message msg = new Message();
							msg.what = HANDLER_MODIFYPWD_RESULT;
							msg.obj = res;
							handler.sendMessageDelayed(msg, 1500);
						} else {
							Message msg = new Message();
							msg.what = HANDLER_MODIFYPWD_SAMEPWD;
							msg.obj = "两次密码输入不一致";
							handler.sendMessageDelayed(msg, 1500);
						}
					}
				}.start();

				startActivity(new Intent(ModpwdActivity.this,
						MenuActivity.class));//修改完成后返回主界面
			}
		});
	}

	private void setViews() {
		oldpwd = (EditText) findViewById(R.id.oldpwd);
		newpwd = (EditText) findViewById(R.id.newpwd);
		newpwd2 = (EditText) findViewById(R.id.newpwd2);
		submitBtn = (Button) findViewById(R.id.modpwdBtn);
	}

}
