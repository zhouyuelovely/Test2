package com.tarena.tabs.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.tarena.tabs.dbutils.DBUtils;
import com.tarena.tabs.entity.TicketOrder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class OrderManagerActivity extends Activity {
	ListView listview;//声明listview
	DBUtils db;//声明数据库

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		setContentView(R.layout.ordermanager);//加载布局文件
		db = new DBUtils(this);
		listview = (ListView) findViewById(R.id.list);
		ArrayList<HashMap<String, String>> arr = db.getOrder();//调用DBUtils中的getOrder()方法
		if(arr!=null){
			listview.setAdapter(new SimpleAdapter(this, arr, R.layout.item,
					new String[] { TicketOrder.OrderDate,
							"arr", TicketOrder.FlightId,
							"duration", TicketOrder.OrderMoney ,"uname","idcard"}, new int[] { R.id.time,R.id.city,
					R.id.fid,R.id.stime,  R.id.money,R.id.uname ,R.id.idcard}));//TicketOrder为表名。航班号，订购日期，起飞城市，价格，到达城市，用户名
		}
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			final	HashMap<String, String> m = (HashMap<String, String>)parent.getAdapter().getItem(position);
			final SimpleAdapter sa = ((SimpleAdapter)parent.getAdapter());
			//新建一个对话框
			new AlertDialog.Builder(OrderManagerActivity.this).setTitle("提示").setMessage("删除订单信息?").setNegativeButton("取消", null).setPositiveButton("确认",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					db.delOrder(m.get(TicketOrder.ORDERID));//调用DBUtils中的delOrder方法
					ArrayList<HashMap<String, String>> arr = db.getOrder();//调用DBUtils中的getOrder方法
					listview.setAdapter(new SimpleAdapter(OrderManagerActivity.this, arr, R.layout.item,
							new String[] { TicketOrder.OrderDate,
									"arr", TicketOrder.FlightId,
									"duration", TicketOrder.OrderMoney ,"uname"}, new int[] { R.id.time,R.id.city,
							R.id.fid,R.id.stime,  R.id.money,R.id.uname }));
					
				}
			}).create().show();
			}
		});
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
