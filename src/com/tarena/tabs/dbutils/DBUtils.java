package com.tarena.tabs.dbutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tarena.tabs.dbutils.DBHelper.TABLEFLY;
import com.tarena.tabs.dbutils.DBHelper.TABLEUSER;
import com.tarena.tabs.entity.Flight;
import com.tarena.tabs.entity.Passanger;
import com.tarena.tabs.entity.TicketOrder;
import com.tarena.tabs.ui.R;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DBUtils {
	DBHelper dbHelper;
	Context context;//上下文对象

	public DBUtils(Context context) {
		super();
		dbHelper = DBHelper.getInstance(context);
		this.context = context;
	}
	/***
	 * 
	 * @param username
	 * @return 已存在返回true
	 */
	//检查用户是否已经存在了，若是已经注册过的用户再次用相同的用户名注册时，会提示该用户名已经注册过了
	public boolean checkUserExist(String username){
		SQLiteDatabase database = dbHelper.getWritableDatabase();//对数据库进行写操作
		Cursor cur = database.rawQuery("select "+TABLEUSER.USERNAME+" from "+TABLEUSER.TABLENAME+" where "+TABLEUSER.USERNAME+"=? ", new String[]{username});
		if(!cur.moveToFirst()){//指向查询结果的第一个位置。查询出来的cursor的初始位置是指向第一条记录的前一个位置的
			 
			cur.close();
			return false;
		}else{
			cur.close();
			return true;
		}
	}
//插入用户注册信息
	public boolean insertUser(String email, String phone, String idcard,
			String idtype, String pwd, String realname, String username) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TABLEUSER.PHONE, phone);
		values.put(TABLEUSER.EMAIL, email);
		values.put(TABLEUSER.IDCARD, idcard);
		values.put(TABLEUSER.IDTYPE, idtype);
		values.put(TABLEUSER.PWD, pwd);
		values.put(TABLEUSER.REALNAME, realname);
		values.put(TABLEUSER.USERNAME, username);
		try {
			Cursor cur = database.rawQuery("select "+TABLEUSER.USERNAME+" from "+TABLEUSER.TABLENAME+" where "+TABLEUSER.USERNAME+"=? ", new String[]{username});
			if(!cur.moveToFirst()){
				long t = database.insert(TABLEUSER.TABLENAME, null, values);
				cur.close();
				return true;
			}else{
				cur.close();
				return false;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
	
		return false;//布尔型返回false
	}

	// 初始化航班
	public void initFlay(String[] city, String[] company) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();//对数据库进行写操作
		database.beginTransaction();//开启一个事务
		int price = (4000 + ((int) (Math.random() * 1000)));
		for (int i = 0; i < city.length; i++) {
			for (int j = 0; j < city.length; j++) {
				for (int t = 0; t < company.length; t++) {
					if (!city[i].equals(city[j])) {//两个城市不同时
						insertFlay(
								company[t],
								((int) (Math.random() * 12)) + ":"
										+ ((int)( Math.random() * 60)),//起飞时间
								city[i],
								"15",
								"" + price,
								"30",
								"" + ((int) price * 0.9f),
								"25",
								"" + ((int) price * 0.6f),
								((int) (Math.random() * 12)+12) + ":"//到达时间
										+ ((int) (Math.random() * 60)), city[j]);
					}
				}
			}
		}
		database.setTransactionSuccessful();
		database.endTransaction();//结束一个事务
	}

	// 新增航班
	public void insertFlay(String c, String e, String f, String hn, String hp,
			String ln, String lp, String mn, String mp, String s, String t) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TABLEFLY.Company, c);
		values.put(TABLEFLY.Etime, e);//到达时间
		values.put(TABLEFLY.FROM, f);//起飞城市
		values.put(TABLEFLY.LOWPRICE, lp);//机票价格
		values.put(TABLEFLY.Stime, s);//起飞时间
		values.put(TABLEFLY.TO, t);//到达城市
		values.put(TABLEFLY.Plane, "波音");
		values.put(//随机航班号
				TABLEFLY.FlightId,
				((char) ('A' + (int) (Math.random() * 26))) + ""
						+ ((char) ('A' + (int) (Math.random() * 26)))
						+ Math.round(Math.random() * 10000));//，可基本均衡获取0到10000的随机整数
		try {
			long t2 = database.insert(TABLEFLY.TABLENAME, null, values);//数据库的加入
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}

	}
	//提交订单
	public void submitOrder(TicketOrder order){
		SQLiteDatabase database = dbHelper.getWritableDatabase();//对数据库进行写操作
		ContentValues values = new ContentValues();
		values.put(TicketOrder.FlightId, order.getFlightId());//航班号
		values.put(TicketOrder.ORDERID, order.getOrderId());//订单号？
		values.put(TicketOrder.OrderDate, order.getOrderDate());//订单日期
		values.put(TicketOrder.OrderMoney, order.getOrderMoney());//订单价格
		
		values.put(TicketOrder.UserIdCard, order.getUserId());//用户身份证号
		values.put(TicketOrder.FLAY_DAY, order.fly_day);//日期
	 
		try {
			long t2 = database.insert(TicketOrder.TABLENAME, null, values);//数据库的加入
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}
	}
	//找回密码 不用输入原密码 即可进行修改密码 再登入系统
	public boolean checkID(String idcardtype,String idcard){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		String searchSql="select * from tb_user where idtype = '"+ idcardtype+"' and idcard='"+idcard+"'";//选择证件类型为身份证号并且输入身份证号可以修改密码
		Cursor cursor=database.rawQuery(searchSql, null);//行查询
		if(cursor.moveToNext()){//如果有下一条记录的话
			return true;
		}else{
			return false;
		}
	}
	//删除订单
	public void delOrder(String oid){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.delete(TicketOrder.TABLENAME, " "+TicketOrder.ORDERID+" =? ", new String[]{oid});//根据订单项号删除订单信息
		
	}
	//获得所有订单
	public ArrayList<HashMap<String, String>> getOrder(){
		ArrayList<HashMap<String, String>> arr = new ArrayList<HashMap<String,String>>();//ArrayList是个数据列表
		SQLiteDatabase database = dbHelper.getWritableDatabase();//对数据库进行写操作
		Cursor cursor = database.rawQuery("select * from "+TicketOrder.TABLENAME+" ", null);//行查询
		while(cursor.moveToNext()){
			HashMap<String, String> map = new HashMap<String, String>();//HashMap你可以理解成是一对对数据的集合
			map.put(TicketOrder.FlightId, cursor.getString(cursor.getColumnIndex(TicketOrder.FlightId)));
			map.put(TicketOrder.ORDERID, cursor.getString(cursor.getColumnIndex(TicketOrder.ORDERID)));
			map.put(TicketOrder.OrderDate, cursor.getString(cursor.getColumnIndex(TicketOrder.OrderDate)));
			map.put(TicketOrder.OrderMoney, cursor.getString(cursor.getColumnIndex(TicketOrder.OrderMoney)));
			map.put(TicketOrder.OrderState, cursor.getString(cursor.getColumnIndex(TicketOrder.OrderState)));
			map.put(TicketOrder.UserIdCard, cursor.getString(cursor.getColumnIndex(TicketOrder.UserIdCard)));
			map.put(TicketOrder.FLAY_DAY, cursor.getString(cursor.getColumnIndex(TicketOrder.FLAY_DAY)));
			arr.add(map);
		}
		cursor.close();//关闭游标
		for(HashMap<String, String> map :arr){
			cursor = database.rawQuery("select * from "+TABLEFLY.TABLENAME+" where "+TABLEFLY.FlightId+" =? ", new String[]{map.get(TicketOrder.FlightId)});
			while(cursor.moveToNext()){
				map.put("arr", cursor.getString(cursor.getColumnIndex(TABLEFLY.FROM))+"-"+ cursor.getString(cursor.getColumnIndex(TABLEFLY.TO)));//把飞城市和到达城市传入到arr键中
				//flay-day是指起飞日期。etime是指起飞时间。etime是指到达时间，把flay-day，stime，etime存入到duration中去
				map.put("duration", map.get(TicketOrder.FLAY_DAY)+" "+cursor.getString(cursor.getColumnIndex(TABLEFLY.Etime))+"->"+ map.get(TicketOrder.FLAY_DAY)+" "+cursor.getString(cursor.getColumnIndex(TABLEFLY.Stime)));
			}
			cursor.close();
			//可以生成多个订单
			cursor = database.rawQuery("select * from "+Passanger.TABLENAME+" where "+Passanger.ID+"=? ", new String[]{map.get(TicketOrder.UserIdCard)});//行查询乘机人的姓名
			if(cursor.moveToNext()){
				map.put("uname", cursor.getString(cursor.getColumnIndex(Passanger.NAME)));//把乘机人的姓名存入到uname键中
				map.put("idcard", cursor.getString(cursor.getColumnIndex(Passanger.IDCARD)));
			}
			cursor.close();
			
		}
		return arr;//返回
	}
	/***
	 * 
	 * @param from
	 * @param to
	 * @param date
	 * @return
	 */
	//根据航班的起飞城市和到达城市进行查询
	public  List<Flight> searchFlight(String from, String to,String date) {
		List<Flight> lists = new ArrayList<Flight>();//声明一个list
		SQLiteDatabase database = dbHelper.getWritableDatabase();//对数据库进行写操作
		Cursor cursor = database.rawQuery(
				"select * from " + TABLEFLY.TABLENAME + " where "
						+ TABLEFLY.FROM + " =? and " + TABLEFLY.TO + " =? ",
				new String[] { from, to });
		while(cursor.moveToNext()){
			//// 只能根据列的索引来获得相应的字段值
			Flight f = new Flight(cursor.getString(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex(TABLEFLY.FlightId)), date, date, cursor.getString(cursor.getColumnIndex(TABLEFLY.Stime)), cursor.getString(cursor.getColumnIndex(TABLEFLY.Etime)), cursor.getString(cursor.getColumnIndex(TABLEFLY.Plane)), cursor.getString(cursor.getColumnIndex(TABLEFLY.LOWPRICE)), cursor.getString(cursor.getColumnIndex(TABLEFLY.FROM))+"机场", cursor.getString(cursor.getColumnIndex(TABLEFLY.TO))+"机场", "0", cursor.getString(cursor.getColumnIndex(TABLEFLY.FROM)), cursor.getString(cursor.getColumnIndex(TABLEFLY.TO)));
			lists.add(f);//把所有符合条件的航flight对象存到list里面
		}
		return lists;
	}

	// 新增乘客
	public void insertPassanger(Passanger passanger) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Passanger.EMAIL, passanger.getPsgEmail());//邮箱
		values.put(Passanger.IDCARD, passanger.getCertifNum());//身份证号
		values.put(Passanger.IDTYPE, passanger.getPsgCertifType());//证件类型
		values.put(Passanger.NAME, passanger.getPsgName());//乘客姓名
		values.put(Passanger.PHONE, passanger.getPsgPhone());//乘客电话
		try {
			long t = database.insert(Passanger.TABLENAME, null, values);//数据库的加入
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	// 删除乘客
	public boolean delPassanger(String id) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();//对数据库进行写操作
		 
		try {
			//t所影响的行数
			long t = database.delete(Passanger.TABLENAME, Passanger.ID+"=? ", new String[]{id});//根据乘机人id好进行删除乘机人信息
			if(t>0){//指乘机人信息存在，影响行数到了
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
       return false;//布尔型返回false
	}
	// 修改乘客
	public boolean moPassanger(Passanger passanger) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Passanger.EMAIL, passanger.getPsgEmail());//邮箱
		values.put(Passanger.IDCARD, passanger.getCertifNum());//身份证号码
		values.put(Passanger.IDTYPE, passanger.getPsgCertifType());//证件类型
		values.put(Passanger.NAME, passanger.getPsgName());//乘客姓名
		values.put(Passanger.PHONE, passanger.getPsgPhone());//电话
		try {
			long t = database.update(Passanger.TABLENAME, values, Passanger.ID+"=? ", new String[]{passanger.getPsgId()+""});//数据库的更新
			if(t>0){//指乘机人信息存在
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
return false;//布尔型返回false
	}

	// 查找所有乘客
	public ArrayList<Passanger> getPassangers() {
		SQLiteDatabase database = dbHelper.getWritableDatabase();//对数据库进行写操作
		Cursor cursor = database.rawQuery("select * from "
				+ Passanger.TABLENAME, null);
		ArrayList<Passanger> ps = new ArrayList<Passanger>();//声明一个ArrayList
		while (cursor.moveToNext()) {
			Passanger p = new Passanger(cursor.getString(cursor
					.getColumnIndex(Passanger.ID)), cursor.getString(cursor
					.getColumnIndex(Passanger.NAME)), cursor.getString(cursor
					.getColumnIndex(Passanger.IDTYPE)), cursor.getString(cursor
					.getColumnIndex(Passanger.IDCARD)), cursor.getString(cursor
					.getColumnIndex(Passanger.EMAIL)), cursor.getString(cursor
					.getColumnIndex(Passanger.PHONE)));
			ps.add(p);
		}
		return ps;
	}
	//验证登录的信息
		public boolean checkLogin(String name, String pwd) {
			SQLiteDatabase database = dbHelper.getWritableDatabase();
			Cursor cursor = database.rawQuery(//行查询任何查询语句
					"select " + TABLEUSER.PWD + " from " + TABLEUSER.TABLENAME
							+ " where " + TABLEUSER.USERNAME + " =? ",
					new String[] { name });
			if (cursor.moveToNext()) {//如果有下一条记录的话
				if (cursor.getString(cursor.getColumnIndex(TABLEUSER.PWD)).equals(
						pwd)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
//修改用户密码
	public boolean mPWD(String name, String pwd, String opwd) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TABLEUSER.PWD, pwd);//存储密码
		int row = database.update(TABLEUSER.TABLENAME, values, TABLEUSER.PWD
				+ " =? ", new String[] { opwd });
		if (row >= 1) {
			return true;
		} else {
			return false;//布尔型返回false
		}
	}
	//重置密码用到了checkID和reSetPWD方法
	public boolean reSetPWD(String pwd,String card) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TABLEUSER.PWD, pwd);
		int row = database.update(TABLEUSER.TABLENAME, values, TABLEUSER.IDCARD
				+ " =? ", new String[] { card });
		if (row >= 1) {
			return true;
		} else {
			return false;
		}
	}
	
	//修改用户注册的信息
	public void setUser(String id,String type,String phone,String rname,String email,String uname){
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(TABLEUSER.IDCARD, id);//身份证号
		values.put(TABLEUSER.IDTYPE, type);//证件类型
		values.put(TABLEUSER.PHONE, phone);//电话
		values.put(TABLEUSER.REALNAME, rname);//真实姓名
		values.put(TABLEUSER.EMAIL, email); //邮箱
		database.update(TABLEUSER.TABLENAME, values, " "+TABLEUSER.USERNAME+"=? ", new String[]{uname});//数据库的更新操作
		
	}
	//查询或得所有用户信息
	public ArrayList<HashMap<String, Object>> getUser() {
		SQLiteDatabase database = dbHelper.getReadableDatabase();//对数据库进行读操作
		Cursor cursor = database.rawQuery("select * from tb_user ", null);
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();//HashMap你可以理解成是一对对数据的集合，先声明一个list集合
		while (cursor.moveToNext()) {
			HashMap<String, Object> map = new HashMap<String, Object>();//
			map.put(TABLEUSER.PHONE, cursor.getString(cursor.getColumnIndex(TABLEUSER.PHONE)));
			map.put(TABLEUSER.EMAIL, cursor.getString(cursor.getColumnIndex(TABLEUSER.EMAIL)));
			map.put(TABLEUSER.IDCARD, cursor.getString(cursor.getColumnIndex(TABLEUSER.IDCARD)));
			map.put(TABLEUSER.IDTYPE, cursor.getString(cursor.getColumnIndex(TABLEUSER.IDTYPE)));
			map.put(TABLEUSER.REALNAME, cursor.getString(cursor.getColumnIndex(TABLEUSER.REALNAME)));
			map.put(TABLEUSER.PWD, cursor.getString(cursor.getColumnIndex(TABLEUSER.PWD)));
			map.put(TABLEUSER.USERNAME, cursor.getString(cursor.getColumnIndex(TABLEUSER.USERNAME)));
			 
			list.add(map);
			
			 
		}

		return list;
	}

 
 
 
 

	/***
	 * 创建等待对话框
	 * 
	 * @param context
	 * @param cancelable
	 *            是否可以取消
	 * @param msg
	 *            显示文字 不显示则传null
	 * @param listener
	 *            对话框取消事件
	 * @return
	 */
	public static ProgressDialog initProgressDialog(Context context,//对话框
			boolean cancelable, String msg,
			DialogInterface.OnCancelListener listener) {
		ProgressDialog progressDialog = ProgressDialog.show(context, null,
				null, true);
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.waiting, null);//加载waiting。xml的布局文件
		TextView tvMsg = (TextView) v.findViewById(R.id.tvTitle);
		if (tvMsg != null && msg != null) {
			tvMsg.setVisibility(View.VISIBLE);
			tvMsg.setText(msg);
		} else {
			//tvMsg.setVisibility(View.GONE);
		}
		progressDialog.setContentView(v);
		progressDialog.setCancelable(cancelable);
		progressDialog.setOnCancelListener(listener);

		return progressDialog;
	}
}
