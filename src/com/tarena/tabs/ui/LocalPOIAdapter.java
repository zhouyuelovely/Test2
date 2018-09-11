package com.tarena.tabs.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tarena.tabs.entity.User;
import com.tarena.tabs.service.OnClickLoadMore;
import com.tarena.tabs.util.ViewHolder;

/***
 * 附近药店，附近医院
 * 
 * @author Kevin
 * 
 */
public class LocalPOIAdapter extends BaseAdapter {//BaseAdapter是基础适配器，适配器的作用主要是用来给诸如 (Spinner,ListView,GridView)来填充数据的。
	private Context context;//声明上下文对象
	// private ArrayList<PoiInfo> data;
	List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	private OnClickLoadMore mOnClickLoadMore;//声明点击加载更多的对象

	public void setOnClickLoadMoreListener(OnClickLoadMore mOnClickLoadMore) {
		this.mOnClickLoadMore = mOnClickLoadMore;
	}

	public LocalPOIAdapter(Context context, ArrayList<HashMap<String, String>> data) {
		super();
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {

		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//这个方法就是用来获得指定位置要显示的View，方法中有个convertView，这个是Android在为我们而做的缓存机制。
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(
				R.layout.item_poi_lbs, null);//创建一个布局
		HashMap<String, String> map = data.get(position);//获取位置的数据
		//用ViewHolder，主要是进行一些性能优化，减少一些不必要的重复操作
		TextView name = ViewHolder.get(convertView, R.id.name);//名称
		name.setText(map.get("name"));
		TextView addr = ViewHolder.get(convertView, R.id.addr);//地址
		addr.setText(map.get("addr"));
		TextView dis = ViewHolder.get(convertView, R.id.distance);//距离

		if (Double.valueOf(map.get("distance")) < 1000.0) {
			dis.setText(String.format("%.1f", map.get("distance"))
					+ "m");
		} else {
			dis.setText(String.format("%.1f", Double.valueOf(map.get("distance"))/1000f)
					+ "km");
		}

		TextView more = ViewHolder.get(convertView, R.id.more);//ViewHolder是在工具类中
		LinearLayout line = ViewHolder.get(convertView, R.id.line);//
		if (position == data.size() - 1) {
			if (data.size() % 10 == 0) {//
				line.setVisibility(View.VISIBLE);
				if (mOnClickLoadMore != null) {
					mOnClickLoadMore.onClickMore(data.size() / 10);
				}
			} else {
				Toast.makeText(context,"没有更多数据了", 1).show();
			}
			
		}
		return convertView;
	}
}
