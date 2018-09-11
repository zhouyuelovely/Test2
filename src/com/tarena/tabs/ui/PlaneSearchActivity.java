package com.tarena.tabs.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;


public class PlaneSearchActivity extends Activity {

	private String[] type = {"B747-300", "B747-300", "B747-300", "B747-300"};//机型号
	private String[] carbin = {"头等舱/公务舱/经济舱", "头等舱/公务舱/经济舱", "头等舱/公务舱/经济舱", "头等舱/公务舱/经济舱"};//机舱等级
	private int[] voyage = {11000, 11000, 11000, 11000};//最大航程树
	private String[] seats = {"12/24/188", "12/24/188", "12/24/188", "12/24/188"};
	private ListView lv;
	ImageView iv; //返回

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        setContentView(R.layout.flighttypefind);//加载布局文件
        lv = (ListView) this.findViewById(R.id.p19_lv);
        iv = (ImageView)findViewById(R.id.imageView2);
	    iv.setOnClickListener(new OnClickListener(){
			public void onClick(View v) { 
					  finish();//这个是关键 
					  } 
			     });  
   
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();//是定义一个List类型的变量，list里面存放的是一个Map，而Map的key是一个String类型，Map的value是Object类型
        for (int i = 0; i < type.length; i++) {//机型号的长度
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("tIndex", "机型：" + type[i]);
        	map.put("cIndex", carbin[i]);
        	map.put("vIndex", "最大航程：" + voyage[i]);
        	map.put("sIndex", seats[i]);
        	data.add(map);//数据源
        }
        //简单适配器
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.layout_19_1,	
        		new String[]{"tIndex", "cIndex", "vIndex", "sIndex"}, 
        		new int[]{R.id.p1901_tv, R.id.p1902_tv, R.id.p1903_tv, R.id.p1904_tv});
        
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener(){//点击列表触发事件

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent it=new Intent(PlaneSearchActivity.this,PlaneDetailActivity.class);//跳转到PlaneDetailActivity.class界面上去
				startActivity(it);
			}});
        
        
        
    }
}