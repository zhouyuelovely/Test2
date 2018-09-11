package com.tarena.tabs.ui;

import java.util.ArrayList;
import java.util.List;

import com.tarena.tabs.dbutils.DBHelper;
import com.tarena.tabs.dbutils.DBUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
忘记密码，然后通过重置密码登录
 */
public class FindPwdActivity extends Activity {

	private Spinner zjlxSp;//下拉列表
	private EditText P22;//证件号码旁边的编辑栏
	
	private Button save;//提交验证的按钮

	private String idcardtype = "";//证件类型
	private String idcard = "1";
	DBUtils db;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		setContentView(R.layout.activity_findpwd);//加载布局文件
		db = new DBUtils(this);
		setTitle("找回密码");//设置标题栏
		P22 = (EditText) findViewById(R.id.p22e03);
		zjlxSp = (Spinner) findViewById(R.id.p22sp01);//下拉列表

		List<String> zjlx = new ArrayList<String>();//用ArrayList实现的接口List作为引用类型，那么通过list引用可以访问到接口中定义的方法。
		zjlx.add("身份证");
		zjlx.add("护照");
		zjlx.add("军官证");
		zjlx.add("驾照");//数据源zjlx
		ArrayAdapter<String> cbAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, zjlx);//simple_spinner_item是android自带的下拉样式
		zjlxSp.setAdapter(cbAdapter);

		save = (Button) findViewById(R.id.btn_p22);
		save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				idcard = P22.getText().toString().trim();//身份证号
				idcardtype = zjlxSp.getSelectedItem().toString().trim();//证件类型
				if(db.checkID(idcardtype, idcard+"")){//调用DBUtils中的checkID方法
					AlertDialog.Builder ab = new AlertDialog.Builder(FindPwdActivity.this);//创建一个对话框
					ab.setTitle("重置密码");//对话框的标题栏
					final EditText ed = new EditText(FindPwdActivity.this);//修飾符修飾一個常量  這個常量是不可以被改變的
					ab.setView(ed);
					ab.setPositiveButton("修改", new DialogInterface.OnClickListener() {//PositiveButton为确定按钮
					  @Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if(db.reSetPWD(ed.getText().toString(), idcard)){//调用DBUtils中的reSetPWD方法
								Toast.makeText(getApplicationContext(), "完成", 1000).show();//？
								FindPwdActivity.this.finish();
							}
						}
					});
					ab.setNegativeButton("取消", null);//NegativeButton为取消按钮
					ab.show();//显示对话框
				}
				 
			}
		});
	}

}
