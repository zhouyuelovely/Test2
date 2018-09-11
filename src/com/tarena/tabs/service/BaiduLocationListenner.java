package com.tarena.tabs.service;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class BaiduLocationListenner implements BDLocationListener {
	//BDLocationListener接口类 作用：获取定位结果，获取POI信息。该类有2个方法： 

	private OnLocationChanged onchange;//声明对象

	public void setOnLocationChanged(OnLocationChanged onchange) {
		this.onchange = onchange;
	}

	@Override
	public void onReceiveLocation(BDLocation arg0) {//定位请求回调函数
		// TODO Auto-generated method stub
		if (onchange != null) {//若位置发生了变化
			onchange.onChanged(arg0);
		}
	}

	public void onReceivePoi(BDLocation arg0) {//获取POI信息
		// TODO Auto-generated method stub

	}

}
