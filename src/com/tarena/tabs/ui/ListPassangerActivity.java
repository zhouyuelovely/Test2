package com.tarena.tabs.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.tarena.tabs.dbutils.DBUtils;
import com.tarena.tabs.entity.Passanger;

public class ListPassangerActivity extends Activity {

	private ImageView addPassangerView;//添加乘机人的按钮
	ImageView iv; //返回
	private ListView psgsListView;//ListVeiw 用来展示列表的View。
	private DBUtils db;
	private ServiceContext serviceContext = ServiceContext.getServiceContext();
	private List<Passanger> passangers; //Passanger是类名 List<Passanger>是存储Passanger类型的链表passangers是方法名
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_DELPASSANGER:
				String res=(String)msg.obj;
				if(res.equals("ok")){ 
					ListPassangerActivity.this.finish();
					Toast.makeText(ListPassangerActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(ListPassangerActivity.this, ListPassangerActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(ListPassangerActivity.this, res, Toast.LENGTH_SHORT).show();//res见后面的
				}
				break;
			case HANDLER_LOAD_PASSANGERS_OK:
				setAdapter();
				break;
			default:
				break;
			}
		}
	};
	
	private static final int HANDLER_DELPASSANGER=0;
	private static final int HANDLER_LOAD_PASSANGERS_OK=1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		db = new DBUtils(this);
		setContentView(R.layout.activity_list_passanger);//加载布局文件
		setData();
		setViews();
		setListeners();
		 iv = (ImageView)findViewById(R.id.imageView2);
		    iv.setOnClickListener(new OnClickListener(){
				public void onClick(View v) { 
						  finish();//这个是关键 
						  } 
				     });  
		
	}

	private void setListeners() {
		addPassangerView.setOnClickListener(new OnClickListener() {//点击添加乘机人的按钮之后跳转到添加联系人的界面
			public void onClick(View v) {
				startActivity(new Intent(ListPassangerActivity.this, AddPassangerActivity.class));
			}
		});

		psgsListView.setOnItemClickListener(new OnItemClickListener() {//点击列表触发事件
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Passanger passanger = passangers.get(position);
				Intent intent = new Intent(ListPassangerActivity.this, ModifyPassangerActivity.class);//跳转到修改联系人的界面
				intent.putExtra("passanger", passanger);//传递对象参数
				startActivity(intent);
			}
		});
		
		psgsListView.setOnItemLongClickListener(new OnItemLongClickListener() {//长按列表触发事件
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
				final AlertDialog ad = new AlertDialog.Builder(ListPassangerActivity.this).setTitle("注意").setMessage("确认删除吗？")
						.setNeutralButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								Passanger p = passangers.get(position);//
								final int ps = (Integer.valueOf(p.getPsgId()));//获取passangers中的psgId;的值

								//起一个线程删除该联系人
								new Thread(){
									public void run() {
										String res = serviceContext.removeMyPassangers(ps,db);//调用serviceContext中的removeMyPassangers方法
										Message msg = new Message();
										msg.what = HANDLER_DELPASSANGER;
										msg.obj = res;
										handler.sendMessage(msg);
									}
								}.start();
								
							}
						}).setPositiveButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								
							}
						}).create();
				ad.show();

				return false;
			}
		});
	}
	SimpleAdapter adapter;//简单适配器
	private void setAdapter() {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for (int i = 0; i < passangers.size(); i++) {
			Map<String, String> dm = new HashMap<String, String>();
			dm.put("data", passangers.get(i).toString());//i是行数
			data.add(dm);
		}
		  adapter = new SimpleAdapter(this, data, R.layout.activity_passanger_item,
				new String[] { "data" }, new int[] { R.id.psgMsgTv });//psgMsgTv是指？
		psgsListView.setAdapter(adapter);
		//刷新布局
		psgsListView.postInvalidate(); //使用postInvalidate则比较简单，不需要handler，直接在线程中调用postInvalidate即可
	}

	private void setData() {
		new Thread(){
			public void run() {
				passangers = serviceContext.getMyPassangers(db);//调用serviceContext中的获取当前用户所添加过的所有的乘机人信息方法
				handler.sendEmptyMessage(HANDLER_LOAD_PASSANGERS_OK);
			}
		}.start();
	}

	private void setViews() {
		addPassangerView = (ImageView) findViewById(R.id.addPassanger);
		psgsListView = (ListView) findViewById(R.id.passangerListView);
	}

	protected void reg() {

	}

	protected void onResume() {
		super.onResume();
		new Thread(){
			public void run() {
				passangers = serviceContext.getMyPassangers(db);
				handler.sendEmptyMessage(HANDLER_LOAD_PASSANGERS_OK);
			}
		}.start();
	}
}
