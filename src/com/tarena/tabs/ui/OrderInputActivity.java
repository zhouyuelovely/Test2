package com.tarena.tabs.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tarena.tabs.dbutils.DBUtils;
import com.tarena.tabs.entity.Flight;
import com.tarena.tabs.entity.Passanger;
import com.tarena.tabs.entity.TicketOrder;

public class OrderInputActivity extends Activity {

	private TextView flightTitle;
	private TextView fromTv;
	private TextView toTv;
	private TextView costTicket;
	private TextView costBuild;
	private TextView costFire;

	private ListView psgListView;
	private ImageButton showPsgs;
	private Button submitOrder;
	private ProgressDialog pd;
	
	private List<Passanger> allPassangers;
	private List<Passanger> subPassangers;
	String[] names;
	boolean[] bs;
	private Flight flight;
	private ServiceContext serviceContext = ServiceContext.getServiceContext();

	private static final int HANDLER_WHAT_SUBMITORDER = 0;
	private static final int HANDLER_DELETE_MSGS_OK = 1;
	private static final int HANDLER_DELETE_MSGS_ERROR = 2;
	private static final int HANDLER_PASSANGERS_LOAD_OK = 3;
	private static final int HANDLER_CLICK_SHOWPASSANGERS_BUTTON = 4;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(dialog!=null && dialog.isShowing()){
				dialog.dismiss();
			}
			switch (msg.what) {
			case HANDLER_WHAT_SUBMITORDER:
				handlerDoSubmitOrder(msg.obj);
				break;
			case HANDLER_DELETE_MSGS_OK:
				if(subPassangers!=null){
					subPassangers.clear();
					setAdapter();
				}
				Toast.makeText(OrderInputActivity.this, "删除成功 请重新加载乘机人列表", Toast.LENGTH_SHORT).show();
				break;
			case HANDLER_DELETE_MSGS_ERROR:
				Toast.makeText(OrderInputActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			case HANDLER_PASSANGERS_LOAD_OK:
				
				//等待框消失
				pd.dismiss();
				break;
			case HANDLER_CLICK_SHOWPASSANGERS_BUTTON:
				//我的总乘机人信息加载完毕
				pd.dismiss(); //提示框消失
				names = new String[allPassangers.size()];//初始化一个数组，长度是查出来的联系人的个数
				bs = new boolean[allPassangers.size()];//根据标记看选中的
				
				for (int i = 0; i < allPassangers.size(); i++) {
					names[i] = allPassangers.get(i).getPsgName();
				}
				
				AlertDialog ad = new AlertDialog.Builder(OrderInputActivity.this).setTitle("选择联系人")
						//多选
						.setMultiChoiceItems(names, bs, new OnMultiChoiceClickListener() {
							public void onClick(DialogInterface arg0, int position, boolean selected) {
								bs[position] = selected;
							}
						}).setNeutralButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								//选中的乘客
								subPassangers = new ArrayList<Passanger>();
								for (int i = 0; i < names.length; i++) {
									if (bs[i]) {//如果选中的
										subPassangers.add(allPassangers.get(i));//把所有乘客里面选中的存到一起
									}
								}
								setAdapter();
							}
						}).setPositiveButton("新建联系人", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								startActivity(new Intent(OrderInputActivity.this, AddPassangerActivity.class));
							}
						}).create();
				ad.show();
				break;
			}
		};
	};

	private void handlerDoSubmitOrder(Object msg) {//提交
		String ms = (String) msg;
		if (!ms.equals("ok")) {
			AlertDialog dialog = new AlertDialog.Builder(OrderInputActivity.this).setTitle("生成订单失败").setMessage("请选择乘机人")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).create();
			dialog.show();
		} else {
		  Toast.makeText(this, "生成订单成功", 1).show();
		  OrderInputActivity.this.finish();
		 for(Activity a: MyApplication.getList()){//遍历MyApplication.getList()
			 a.finish();//获取他进行销毁
		 }
		 startActivity(new Intent(OrderInputActivity.this,OrderManagerActivity.class));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		db = new DBUtils(this);
		setContentView(R.layout.activity_order_input);//加载布局
		setViews();
		setData();
		setListeners();
	}

	private void setViews() {
		this.flightTitle = (TextView) findViewById(R.id.flightTitle);
		this.fromTv = (TextView) findViewById(R.id.fromtv);
		this.toTv = (TextView) findViewById(R.id.totv);
		this.costTicket = (TextView) findViewById(R.id.costTicket);
		this.costBuild = (TextView) findViewById(R.id.costBuild);
		this.costFire = (TextView) findViewById(R.id.costFire);
		this.showPsgs = (ImageButton) findViewById(R.id.showPsgs);
		this.psgListView = (ListView) findViewById(R.id.psgShowList);
		this.submitOrder = (Button) findViewById(R.id.submitOrder);
	}
	

	private void setData() {
		// 设置所选择的航班信息
		flight = (Flight) (getIntent().getSerializableExtra("flight"));//获取参数 fightsearch
		flightTitle.setText(flight.getTitle());
		fromTv.setText(flight.getFromInfo());
		toTv.setText(flight.getToInfo());
		//格式化字符串，保留带小数点后一位
		costTicket.setText(String.format("%.1f", Float.valueOf(flight.getPrice())));//机票
		costBuild.setText(String.format("%.1f", Float.valueOf(flight.getPrice())*0.015 ));//机建
		costFire.setText(String.format("%.1f", Float.valueOf(flight.getPrice())*0.008 ));//燃油
	}


	
	private void setAdapter() {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for (int i = 0; i < subPassangers.size(); i++) {
			Map<String, String> dm = new HashMap<String, String>();
			dm.put("data", subPassangers.get(i).toString());
			data.add(dm);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.activity_passanger_item,
				new String[] { "data" }, new int[] { R.id.psgMsgTv });
		psgListView.setAdapter(adapter);
	}

	
	private void setListeners() {
		showPsgs.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				pd = ProgressDialog.show(OrderInputActivity.this, "", "加载联系人 请稍后");
				new Thread(){
					public void run() {
						allPassangers = getPassangers();
						Message msg = new Message();
						msg.what = HANDLER_CLICK_SHOWPASSANGERS_BUTTON;
						handler.sendMessage(msg);
					}
				}.start();
		
			}
		});

		// 点击乘机人列表
		psgListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Passanger selectedPassanger = subPassangers.get(position);
				Intent intent = new Intent(OrderInputActivity.this, ModifyPassangerActivity.class);
				intent.putExtra("passanger", selectedPassanger);//传参数到ModifyPassangerActivity118行 
				startActivity(intent);
			}
		}); 
		// 点击按钮
		submitOrder.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 发起线程 提交订单
				dialog  = DBUtils.initProgressDialog(OrderInputActivity.this, true, "请稍等", null);
				new Thread() {
					public void run() {
						// 调用controller 提交乘机人id与 flightId
						if(subPassangers==null || subPassangers.size()==0){
							runOnUiThread(new Runnable() {//在主线程中去执行这个方法
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									dialog.dismiss();
									Toast.makeText(OrderInputActivity.this,"没有选中乘机人", Toast.LENGTH_SHORT).show();
								}
							});
							return;
						}
						String res  =""; 
						for (int i = 0; i < subPassangers.size(); i++) {
							
							String passanger = subPassangers.get(i).getPsgId();
							TicketOrder order = new TicketOrder(getIntent().getStringExtra("date"),"DD"+new SimpleDateFormat("yyyyMMddHHMMssSSS").format(new Date()), passanger+"", flight.getPrice(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()), "0", flight.getFlightId());
							res = serviceContext.orderSubmit(order,db);
						} 
						Message msg = new Message();
						msg.what = HANDLER_WHAT_SUBMITORDER;
						msg.obj = res;
						handler.sendMessageDelayed(msg,1500);
					};
				}.start();
			}
		});

	}
private DBUtils db;
	private ProgressDialog dialog;
	public List<Passanger> getPassangers() {
		
		List<Passanger> ps = serviceContext.getMyPassangers(db);
		
		return ps;
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.regest, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	
		}
	}


