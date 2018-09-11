package com.tarena.tabs.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tarena.tabs.dbutils.DBUtils;
import com.tarena.tabs.entity.City;
import com.tarena.tabs.entity.Flight;

public class FlightSearchActivity extends Activity {
	private List<City> cities;  //City是类名 List<City>是存储City类型的链表cities是方法名//
	private int d;//定义日期的日
	private int m;//定义日期的月
	private int y;//定义日期的年
	private ListView lv; //列表的显示需要三个元素：ListVeiw 用来展示列表的View；适配器 用来把数据映射到ListView上的中介；数据    具体的将被映射的字符串，图片，或者基本组件。
	private Button btSearch; //查询按钮
	private TextView tvD;  //日
	private TextView tvM;  //月
	private TextView tvY;  //年
	private Spinner spTo;//到达城市的下拉列表
	private Spinner spFrom;  //起飞城市的下拉列表
	private LinearLayout ll;//线性布局
	ImageView iv; //返回
	private ServiceContext serviceContext = ServiceContext.getServiceContext();//实例化serviceContext对象
	private List<Flight> flights;//Flight是类名 List<Flight>是存储Flight类型的链表flights是方法名//

	private static final int HANDLER_FLIGHT_SEARCH_RESULT = 0;
	private static final int HANDLER_FLIGHT_INTERNET_ERROE = 1;
	private static final int HANDLER_CITY_SEARCH_RESULT = 2;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(dialog!=null && dialog.isShowing()){
				dialog.dismiss();//对话框消失
			}
			switch (msg.what) {
			case HANDLER_FLIGHT_SEARCH_RESULT://查询航班之后的结果
				ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
				
				for(Flight f:flights){
					HashMap<String, String> mp = new HashMap<String, String>();
					mp.put("1", f.getFlightId());
					mp.put("3", f.getPlanFromDate()+" "+f.getFromTime());
					mp.put("2", f.getPlanToDate()+" "+f.getToTime());
					mp.put("4", f.getFromCity());
					mp.put("5", f.getToCity());
					mp.put("6", f.getPrice());
					data.add(mp);
				}
				//ArrayAdapter<Flight> adapter = new ArrayAdapter<Flight>(FlightSearchActivity.this, android.R.layout.simple_list_item_1, flights);
				SimpleAdapter adapter = new SimpleAdapter(FlightSearchActivity.this, data, R.layout.item_list, new String[]{"1","2","3","4","5","6"}, new int[]{R.id.fid,R.id.stime,R.id.etime,R.id.fc,R.id.tc,R.id.price});
				
				if(adapter.isEmpty()){
					ArrayAdapter<Flight> noAdapter = new ArrayAdapter<Flight>(FlightSearchActivity.this, android.R.layout.simple_list_item_1, flights);
					lv.setAdapter(noAdapter);
					Toast.makeText(FlightSearchActivity.this, "无相应航班,清重新进行选择", Toast.LENGTH_SHORT).show();
				}else{
					lv.setAdapter(adapter);
				}//进行航班查询的操作
				break;
				//需要删除吗?
						case HANDLER_CITY_SEARCH_RESULT://查询所有城市的结果
				List<String> list = new ArrayList<String>();
				for (City c : cities) {
					list.add(c.getCityName());
				}
				ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>(FlightSearchActivity.this, android.R.layout.simple_spinner_item, list);
				citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spFrom.setAdapter(citiesAdapter);
				spTo.setAdapter(citiesAdapter);
				btSearch.setEnabled(true);
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_flight_search);
        db = new DBUtils(this);
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
private ProgressDialog dialog;
	private void setData() {
		try {
			new Thread(){
				public void run() {
					System.out.println("FSA:Get Cities Starting...");
					cities = serviceContext.findAllCities(getResources().getStringArray(R.array.city));//获取strings.xml中的city数组的数据
					if(cities.isEmpty()){
						 
						Message msg = new Message();
						msg.what = HANDLER_FLIGHT_INTERNET_ERROE;
						handler.sendMessageDelayed(msg, 1000);
					}else{
						Message msg = new Message();
						msg.what = HANDLER_CITY_SEARCH_RESULT;
						handler.sendMessage(msg);
					}
				};
			}.start();
		} catch (Exception e) {
			Toast.makeText(this, "....", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		c.set(2015, 05, 10);//初始
		y = c.get(Calendar.YEAR);
		m = c.get(Calendar.MONTH);
		d = c.get(Calendar.DAY_OF_MONTH);
	}

	private void setViews() {
		spFrom = (Spinner) findViewById(R.id.spinner1);
		spTo = (Spinner) findViewById(R.id.spinner2);
		tvD = (TextView) findViewById(R.id.textView4);
		tvM = (TextView) findViewById(R.id.textView6);
		tvY = (TextView) findViewById(R.id.textView8);
		btSearch = (Button) findViewById(R.id.button1);
		btSearch.setEnabled(false);
		lv = (ListView) findViewById(R.id.listViewPassanger);
		ll = (LinearLayout) findViewById(R.id.linearLayout2);
		tvY.setText(String.valueOf(y));
		tvM.setText(String.valueOf(m + 1));
		tvD.setText(String.valueOf(d));
	}

	private void setListeners() {
		ll.setOnClickListener(new OnClickListener() {//整个布局上设置监听日期
			public void onClick(View v) {
				selectDate();
			}
		});

		btSearch.setOnClickListener(new OnClickListener() {//查询按钮上设置监听 查询方法
			public void onClick(View v) {
				search();
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {//回调方法
				Flight flight = flights.get(position);//位置行数
				Intent intent = new Intent(FlightSearchActivity.this, OrderInputActivity.class);
				intent.putExtra("flight", flight);//传参数到orderInput
				intent.putExtra("date", tvY.getText()+"-"+tvM.getText()+"-"+tvD.getText());
				startActivity(intent);
				MyApplication.addList(FlightSearchActivity.this);
			}
		});
	}
//時間選擇
	protected void selectDate() {
		DatePickerDialog dialog = new DatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {//回調方法
				y = year;
				m = monthOfYear;
				d = dayOfMonth;
				tvY.setText(String.valueOf(y));
				tvM.setText(String.valueOf(m + 1));
				tvD.setText(String.valueOf(d));
			}
		}, y, m, d);
		dialog.show();
	}
private DBUtils db;
	protected void search() {//查詢方法
		try {
			dialog = DBUtils.initProgressDialog(this, true, "请稍等", null);
			new Thread() {
				public void run() {
					String fromCity = spFrom.getSelectedItem().toString();
					String toCity = spTo.getSelectedItem().toString();
					Calendar c = Calendar.getInstance();
					c.set(y, m, d);
					flights = serviceContext.findFlightByCityAndDate(fromCity, toCity, c,db);
					if(flights!=null){
						Message msg = new Message();
						msg.what = HANDLER_FLIGHT_SEARCH_RESULT;
						handler.sendMessageDelayed(msg,1500);
					}else{
						Message msg = new Message();
						msg.what = HANDLER_FLIGHT_INTERNET_ERROE;
						handler.sendMessageDelayed(msg,1500);
					}
				}
			}.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
