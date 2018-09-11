package com.tarena.tabs.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


	public class SubscribeActivity extends Activity{
		private CheckBox cb1,cb2,cb3,cb4;//四个复选框
		private Button btn1,btn2;//确定和返回按钮
		private TextView textView1;//机票促销信息
		private TextView textView2;//酒店促销信息
		private TextView textView3;//合作商户促销信息
		private TextView textView4;//航线更改信息
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
			setContentView(R.layout.dingyue);//加载布局文件
			
			cb1 = (CheckBox)findViewById(R.id.p2701_cb);
			cb2 = (CheckBox)findViewById(R.id.p2702_cb);
			cb3 = (CheckBox)findViewById(R.id.p2703_cb);
			cb4 = (CheckBox)findViewById(R.id.p2704_cb);
			btn1 = (Button)findViewById(R.id.p2705_btn);
			btn2 = (Button)findViewById(R.id.p2706_btn);
			
			 textView1=(TextView)findViewById(R.id.p2701_cb_1);
		     textView1.setTextSize(25f);//设置字体大小为25f
		     textView1.setTextColor(Color.RED);//设置字体颜色为红色
		     textView1.setTypeface(Typeface.MONOSPACE,Typeface.BOLD_ITALIC);//设置字体格式为斜体加粗

			 textView2=(TextView)findViewById(R.id.p2702_cb_2);
			 textView2.setTextSize(25f);
			 textView2.setTextColor(Color.YELLOW);
			 textView2.setTypeface(Typeface.MONOSPACE,Typeface.BOLD_ITALIC);
			 
			 textView3=(TextView)findViewById(R.id.p2703_cb_3);
			 textView3.setTextSize(25f);
			 textView3.setTextColor(Color.GREEN);
			 textView3.setTypeface(Typeface.MONOSPACE,Typeface.BOLD_ITALIC);
			 
			 textView4=(TextView)findViewById(R.id.p2704_cb_4);
			 textView4.setTextSize(25f);
			 textView4.setTextColor(Color.BLUE);
			 textView4.setTypeface(Typeface.MONOSPACE,Typeface.BOLD_ITALIC);
			 
			btn2.setOnClickListener(new OnClickListener() {//返回按钮
				// 跳转到主页面
				public void onClick(View arg0) {
					// TODO Auto-generated method stub  		
	    			
					Intent it = new Intent(SubscribeActivity.this, MenuActivity.class);//声明一个意图
					startActivity(it);//启动一个意图

				}
			});
			btn1.setOnClickListener(new OnClickListener() {//确定按钮
				// 跳转到p28页面
				public void onClick(View arg0) {
					// TODO Auto-generated method stub  	
					String s="";
					if(cb1.isChecked())
						s+="机票促销信息：5月26日 北京到上海，1.0折只需110元，限时促销！！！    ";
					if(cb2.isChecked())
						s+="酒店促销信息：7天酒店镇江火车站店，位于镇江市火车站北广场西侧，紧临中山西路，距离镇江火车站约300米，交通便捷，限时促销新用户只需77元即可入住！！！      ";
					if(cb3.isChecked())
						s+="合作商户促销信息：携程旅行网，今秋出行享好礼，活动时间2015年6月2日至7月8日     ";
					if(cb4.isChecked())
						s+="航线更改信息：接总部通知，原定预计5月30日开航的MEX3航线NORTHERN JUBILEE 1410,现将更改为：NORTHERN JUPITER 1402，航期不变      ";
					new AlertDialog.Builder(SubscribeActivity.this)//新声明一个对话框
					.setTitle("订阅信息")//对话框标题栏
					.setMessage("订阅成功")//显示订阅成功的消息
					.setPositiveButton(R.string.ok_str, new DialogInterface.OnClickListener() {//ok_str在strings。xml中为确定
						
						public void onClick(DialogInterface dialog, int p2705_btn) {
							// TODO Auto-generated method stub
							
						}
					}).show();//显示对话框
					Intent intent = new Intent();	   //新建一个意图
		            intent.putExtra("one", s); //Intent传递参数值，one是键，s是值
		            intent.setClass(SubscribeActivity.this, p28.class);//跳转到p28.class的界面
					startActivity(intent);//启动这个意图
					finish();

				}
			});
		}
	}
