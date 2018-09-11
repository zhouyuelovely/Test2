package com.tarena.tabs.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.tarena.tabs.service.BaiduLocationListenner;
import com.tarena.tabs.service.OnLocationChanged;

public class MenuActivity extends Activity implements OnLocationChanged {
	private GridView gv;//网格视图
	private String[] texts={"预定机票","订单管理","乘机人管理","用户账户管理","机型展示","软件帮助","订阅设置","周边售票","用户反馈"};
	private int[] images={R.drawable.ticket,R.drawable.order,R.drawable.memb,R.drawable.user,R.drawable.plane,R.drawable.branch,R.drawable.subscribe,R.drawable.route,R.drawable.help};
	// 定位相关
	LocationClient mLocationClient;//功能：定位SDK的核心类。
	BDLocation currentLocation;
	
	public class MyLocationListener implements BDLocationListener {
		//BDLocation类，封装了定位SDK的定位结果，在BDLocationListener的onReceive方法中获取。通过该类用户可以获取error code，位置的坐标，精度半径等信息。
		@Override
		public void onReceiveLocation(BDLocation location) {//重写BaiduLocationListenner中的 onReceiveLocation(BDLocation arg0)方法
			if (location == null)
				return;
			
			currentLocation = location;
			
		
			mLocationClient.stop();//关闭
		}

		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}
	}
	// 请求一次定位信息
	private void getLocationOnce() {

		mLocationClient = new LocationClient(this
				.getApplicationContext()); // 实例化LocationClient类
		mLocationClient.registerLocationListener(new MyLocationListener()); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开GPS 
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(500000);// 设置发起定位请求的间隔时间为500000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);//设置定位参数 
		mLocationClient.start();// 开始定位
	}

	// 定位结果
	@Override
	public void onChanged(BDLocation arg0) {
		// TODO Auto-generated method stub
		//mLocClient.stop();
		currentLocation = arg0;
	}

	/** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       // 在使用SDK各组件之前初始化context信息，传入ApplicationContext，注意该方法要再setContentView方法之前实现 
        //百度地图初始化
        SDKInitializer.initialize(this.getApplicationContext());//getApplicationContext() 返回应用的上下文，生命周期是整个应用
        setContentView(R.layout.activity_menu);//加载布局文件
        
       SharedPreferences sp = getSharedPreferences("lq", 1);//记住用户名和密码登录
        
        ((TextView)findViewById(R.id.names)).setText(sp.getString("NAME", ""));//获取MainActivity。class中的存在SharedPreferences的NAME键中所对应的值
        
        ((Button)findViewById(R.id.ex)).setOnClickListener(new OnClickListener() {//退出登录的按钮
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(MenuActivity.this,MainActivity.class));//启动一个已经声明的意图，跳转回登录界面
				MenuActivity.this.finish();
			}
		});
        getLocationOnce();//请求一次定位信息
        ExitApplication.getInstance().addActivity(this);//退出系统
        
        gv=(GridView)findViewById(R.id.grid_view_01);
        
        List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();//声明一个map值
        
        for (int i = 0; i < 9; i++) {
        	Map<String,Object> map=new HashMap<String,Object>();
        	map.put("image", images[i]);
        	map.put("text", texts[i]);
        	data.add(map);
			
		}
        
        //简单适配器 this是传递上下文的一个对象，data是给定的一个数据源，当前gridview布局文件
        SimpleAdapter adpater=new SimpleAdapter(this,data,R.layout.activity_menu_item,new String[]{"image","text"},new int[]{R.id.image_item,R.id.text_item});
        gv.setAdapter(adpater);
        
        gv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        			long arg3) {
        		// 预定机票跳到航班查询
        		if(arg2==0){
        			Intent it=new Intent(MenuActivity.this,FlightSearchActivity.class);
        	        startActivity(it);
        		}
        		
                // 点击订单管理，跳转到订单管理界面
        		if(arg2==1){
        			Intent it1=new Intent(MenuActivity.this,OrderManagerActivity.class);
        	        startActivity(it1);
        		}
        		  // 点击乘机人管理，跳转到乘机人管理界面
        		if(arg2==2){
        			Intent it2=new Intent(MenuActivity.this,ListPassangerActivity.class);
        	        startActivity(it2);
        		}
        		  // 点击用户账户管理，跳转到用户账户灌咯界面
        		if(arg2==3){
        			Intent it3=new Intent(MenuActivity.this,UserAdminActivity.class);
        	        startActivity(it3);
        		}
        		 // 点击机型展示，跳转到机型展示界面
        		if(arg2==4){
        			Intent it4=new Intent(MenuActivity.this,PlaneSearchActivity.class);
        	        startActivity(it4);
        		}
        		 // 点击软件帮助，跳转到软件帮助界面
        		if(arg2==5){
        			
        	        aboutdialog();//调用方法
        		}
        		 // 点击订阅设置，跳转到订阅设置界面
        		if(arg2==6){
        			Intent it6=new Intent(MenuActivity.this,SubscribeActivity.class);
        	        startActivity(it6);
        		}
        		 // 点击周边售票，跳转到周边售票界面
        	
        		if(arg2==7){
        			if(currentLocation==null){
        				Toast.makeText(getApplicationContext(), "定位中，稍后再试", 1000).show();
        				return;
        			}
        			Intent it7=new Intent(MenuActivity.this,LocalPOIActivity.class);//声明一个意图，跳转到LocalPOIActivity.class界面
        			it7.putExtra("lo", currentLocation.getLongitude());//获取当前位置的经度并传递参数到lo中
        			it7.putExtra("la", currentLocation.getLatitude());//获取当前位置的纬度并传递参数到la中
        			it7.putExtra("city", currentLocation.getCity());//获取当前位置的城市并传递参数到city中
        	        startActivity(it7);//启动这个意图
        		}
        		 // 点击进入用户反馈界面
        		if(arg2==8){
        			Intent it8=new Intent(MenuActivity.this,FeedBackActivity.class);
        	        startActivity(it8);
        	      
        		}
        		        		
        	}
		});
      
   
    }
    protected void aboutdialog() {//软件帮助说明
		
		String copyright;
		AlertDialog.Builder builder = new Builder(this);
		copyright = "本软件是基于android的航空手机订票系统，提供用户登录注册、航班查询、机票预定，订单管理、用户账户管理等功能，并且本软件有着有直观简洁的客户端界面，方便用户进行各种操作。";
		builder.setMessage(copyright);
		builder.setTitle("软件帮助说明");
		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();//让对话框消失
					}
			});
		builder.create().show();
	}
    public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {//退出当前系统
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

























