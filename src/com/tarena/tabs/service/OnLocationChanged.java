package com.tarena.tabs.service;

import com.baidu.location.BDLocation;
/***
 * 当地点变换时
 * 
 * @param arg0
 */
public interface OnLocationChanged {
	public void onChanged(BDLocation arg0);
}
