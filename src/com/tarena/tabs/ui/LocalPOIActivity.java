package com.tarena.tabs.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.tarena.tabs.entity.User;
import com.tarena.tabs.service.OnClickLoadMore;

public class LocalPOIActivity extends Activity implements
		OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
	// 定位相关
	LocationClient mLocClient;//功能：定位SDK的核心类

	// poi符合条件的点
	private PoiSearch mPoiSearch = null;//POI检索接口
	private SuggestionSearch mSuggestionSearch = null;//建议查询接口


	private String kewords;//关键词

	private ListView listView;//列表
	private LocalPOIAdapter poiAdapter;

	ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
	LatLng currentlocation;//当前位置

	Context mContext;//
	int distance = 100000;//半径
	ProgressDialog pd;//
	LinearLayout pro;//线型布局

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		setContentView(R.layout.activity_localhosp);//加载布局文件
		pro = (LinearLayout)findViewById(R.id.pro);
	//	kewords = getIntent().getStringExtra("keywords");
		kewords ="机票";//关键词是机票 ，会自动搜索与机票相关的信息
		
		currentlocation = new LatLng(getIntent().getDoubleExtra("la", 0),
				getIntent().getDoubleExtra("lo", 0));//通过获纬度和经度确定当前位置
		
		if (android.os.Build.VERSION.SDK_INT >= 11) {//手机版本
			mContext = new ContextThemeWrapper(this,
					android.R.style.Theme_Holo_Light);
		} else {
			mContext = this;
		}

		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(new OnItemClickListener() {//点击列表触发事件 可以点相应的地点查看地图

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(LocalPOIActivity.this,
						POIOnMapActivity.class);//声明一个意图 跳转到查看地图的界面上
				
				i.putExtra("la", listData.get(position).get("la"));
				i.putExtra("lo", listData.get(position).get("lo"));
				i.putExtra("name", listData.get(position).get("name"));
				startActivity(i);

			}
		});
		// pd = initProgressDialog();
		poiAdapter = new LocalPOIAdapter(this, listData);
		poiAdapter.setOnClickLoadMoreListener(new OnClickLoadMore() {//往下拉 加载更多数据

			@Override
			public void onClickMore(int page) {//重写onClickLoadMore。class中的onClickMore(int page)方法
				// TODO Auto-generated method stub
				searchPOI(page, kewords, distance);//调用下面的方法
			}
		});
		listView.setAdapter(poiAdapter);
		
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(LocalPOIActivity.this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch
				.setOnGetSuggestionResultListener(LocalPOIActivity.this);
		searchPOI(0, kewords, distance);
		
	}
//距离设置
	private void showRight() {
		// TODO Auto-generated method stub
		final String[] Items = getResources().getStringArray(R.array.diatance);//获取strings.xml中的distance数组
		AlertDialog.Builder ab1 = new AlertDialog.Builder(mContext);
		ab1.setTitle("距离设置");
		ab1.setItems(Items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface d, int which) {
				// TODO Auto-generated method stub
				distance = Integer.valueOf((Items[which]).substring(0,
						(Items[which]).lastIndexOf("k")));
				searchPOI(0, kewords, distance * 1000);

			}
		});

		ab1.create().show();
	}

	/**
	 * 根据关键字页数检索
	 * 
	 * @param page
	 * @param kewords
	 */
	private void searchPOI(int page, String kewords, int distance) {
		//PoiNearbySearch
		PoiNearbySearchOption option = new PoiNearbySearchOption();
		// option.city(getIntent().getStringExtra("city") == null ? "南京"
		// : getIntent().getStringExtra("city"));
		option.keyword(kewords);//检索关键字
		option.pageCapacity(10);//设置每页容量，默认为每页10条
		option.pageNum(page);//分页编号
		option.location(currentlocation);//检索位置
		option.radius(distance);//设置检索的半径范围 
		
		mPoiSearch.searchNearby(option);//

	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		// TODO Auto-generated method stub
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(LocalPOIActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(LocalPOIActivity.this,
					result.getName() + ": " + result.getAddress(),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		// TODO Auto-generated method stub
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(LocalPOIActivity.this, "未找到结果", Toast.LENGTH_LONG)
					.show();
			pro.setVisibility(View.GONE);
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			System.out.println("999999" + result.getAllPoi().size());
			for (PoiInfo info : result.getAllPoi()) {
				// map = new HashMap<String, User>();
				DecimalFormat df = (new DecimalFormat("#.0"));
				HashMap<String, String> row = new HashMap<String, String>();
				row.put("name", info.name);
				row.put("la", String.valueOf(info.location.latitude));
				row.put("lo", String.valueOf(info.location.longitude));
				row.put("distance",
						Double.valueOf(df.format(DistanceUtil.getDistance(
								info.location, currentlocation))) + "");
				row.put("addr", info.address);
				listData.add(row);

			}
			Collections.sort(listData,
					new Comparator<HashMap<String, String>>() {

						@Override
						public int compare(HashMap<String, String> lhs,
								HashMap<String, String> rhs) {
							// TODO Auto-generated method stub
							double d1 = Double.valueOf(lhs.get("distance"));
							double d2 = Double.valueOf(rhs.get("distance"));
							return (int) Math.round(d1 - d2);
						}
					});
			if (poiAdapter != null) {
				poiAdapter.notifyDataSetChanged();
			}
			pro.setVisibility(View.GONE);
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "找到结果";
			Toast.makeText(LocalPOIActivity.this, strInfo, Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public void onGetSuggestionResult(SuggestionResult arg0) {
		// TODO Auto-generated method stub

	}

	public ProgressDialog initProgressDialog() {
		ProgressDialog progressDialog = ProgressDialog.show(this, null, null,
				true);
		progressDialog.setCancelable(true);

		LayoutInflater inflater = LayoutInflater.from(LocalPOIActivity.this);
		View v = inflater.inflate(R.layout.waiting, null);//加载waiting.xml的布局文件
		// TextView tvMsg = (TextView) v.findViewById(R.id.tvTitle);
		//
		// if (tvMsg != null) {
		// tvMsg.setText(getString(R.string.data_getting));
		// }
		progressDialog.setContentView(v);
		progressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub

					}
				});

		return progressDialog;
	}
}
