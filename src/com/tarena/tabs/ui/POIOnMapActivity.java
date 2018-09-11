package com.tarena.tabs.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

/***
 * 传递POIinfo相关信息，提供地图预览，可行路径等
 * 
 * @author Kevin
 *
 */
public class POIOnMapActivity extends Activity {
	//声明百度地图相关控件
	MapView mMapView;
	BaiduMap mBaiduMap;
	double la, lo;//纬度和经度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		setContentView(R.layout.activity_poi_map);//加载布局文件
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		
		String laa = getIntent().getStringExtra("la");//获得纬度
		String lnn = getIntent().getStringExtra("lo");//获得经度
		la = Double.parseDouble(laa);//把字符串转换为浮点数
		lo = Double.parseDouble(lnn);
		// 此处设置开发者获取到的方向信息，顺时针0-360
		MyLocationData locData = new MyLocationData.Builder()
				.direction(100).latitude(la).longitude(lo).build();
		mBaiduMap.setMyLocationData(locData); //设置定位数据 
		
		LatLng ll = new LatLng(la, lo);//当前位置？
		//移动当前位置
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.animateMapStatus(u);
		//缩放到18级别
		u = MapStatusUpdateFactory.zoomTo(18);
		mBaiduMap.animateMapStatus(u);
		//实例化
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(ll).icon(bitmap);//标注
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
