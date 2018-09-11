package com.tarena.tabs.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *机型类型详情页面
 */
public class PlaneDetailActivity extends Activity{
	private TextView flightType;//航班号
	private ImageView flightImage;//图片
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		setContentView(R.layout.flighttypedetail);//加载布局文件
	}
}
