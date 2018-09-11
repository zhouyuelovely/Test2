package com.tarena.tabs.dbutils;

import com.tarena.tabs.entity.Passanger;
import com.tarena.tabs.entity.TicketOrder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String BLOB_TYPE = " BLOB";
	private static final String COMMA_SEP = ",";
	private static final String REAL_TYPE = " REAL";
	public static final int DATABASE_VERSION = 4;//数据库的版本号
	public static final String DATABASE_NAME = "lq.db";//数据库的名字
//用户表
	public class TABLEUSER {
		public static final String TABLENAME = "tb_user";
		public static final String PHONE = "phone";
		public static final String REALNAME = "realname";
		public static final String IDTYPE = "idtype";
		public static final String IDCARD = "idcard";
		public static final String PWD = "pwd";
		public static final String EMAIL = "email";
		public static final String USERNAME = "username";
	}
//航班表
	public class TABLEFLY {
		public static final String TABLENAME = "tb_fly";
		public static final String FROM = "fromc";
		public static final String TO = "toc";
		public static final String LOWPRICE = "lowprice";
		public static final String Stime = "stime";
		public static final String Etime = "etime";
		public static final String Company = "Company";
		// 机号 型号
		public static final String FlightId = "flightId";
		public static final String Plane = "Plane";
	}

	/***
	 * tb_user phone - 电话 realname - 真实姓名 idtype - 证件类型 idcard - 证件号 pwd - 登录密码
	 * email - 邮箱 username - 用户名
	 */
	//创建用户表TABLEUSER
	private static final String SQL_CREATE_USER = "CREATE TABLE IF NOT EXISTS "
			+ TABLEUSER.TABLENAME + " ( _id INTEGER PRIMARY KEY,"
			+ TABLEUSER.PHONE + TEXT_TYPE + COMMA_SEP + TABLEUSER.REALNAME
			+ TEXT_TYPE + COMMA_SEP + TABLEUSER.IDTYPE + TEXT_TYPE + COMMA_SEP
			+ TABLEUSER.IDCARD + TEXT_TYPE + COMMA_SEP + TABLEUSER.PWD
			+ TEXT_TYPE + COMMA_SEP + TABLEUSER.EMAIL + TEXT_TYPE + COMMA_SEP
			+ TABLEUSER.USERNAME + TEXT_TYPE + " )";
	//创建航班表
	/***
	 * tb_fly etime - 到达时间   stime - 开始时间     from -出发城市   to - 到达城市  flightid - 航班编号
	 * lowprice - 价格 
	 */
	private static final String SQL_CREATE_FLY = "CREATE TABLE IF NOT EXISTS "
			+ TABLEFLY.TABLENAME + " ( _id INTEGER PRIMARY KEY,"
			+ TABLEFLY.Company + TEXT_TYPE + COMMA_SEP + TABLEFLY.Etime
			+ TEXT_TYPE + COMMA_SEP + TABLEFLY.FROM + TEXT_TYPE + COMMA_SEP
			+TABLEFLY.LOWPRICE + TEXT_TYPE + COMMA_SEP
			+TABLEFLY.Stime + TEXT_TYPE + COMMA_SEP
			+ TABLEFLY.TO + TEXT_TYPE + COMMA_SEP + TABLEFLY.FlightId
			+ TEXT_TYPE + COMMA_SEP + TABLEFLY.Plane + TEXT_TYPE + " )";
//创建乘机人表Passanger
	/***
	 * tb_user phone - 电话     name - 真实姓名 idtype - 证件类型 idcard - 证件号 
	 * email - 邮箱 
	 */
	private static final String SQL_CREATE_PASSANGER = "CREATE TABLE IF NOT EXISTS "
			+ Passanger.TABLENAME
			+ " ( _id INTEGER PRIMARY KEY,"
			+ Passanger.PHONE
			+ TEXT_TYPE
			+ COMMA_SEP
			+ Passanger.EMAIL
			+ TEXT_TYPE
			+ COMMA_SEP
			+ Passanger.IDTYPE
			+ TEXT_TYPE
			+ COMMA_SEP
			+ Passanger.IDCARD
			+ TEXT_TYPE
			+ COMMA_SEP
			+ Passanger.NAME
			+ TEXT_TYPE + " )";
	//创建订单表
	/***
	 * tb_record  FlightId -航班号     name - 真实姓名 idtype - 证件类型 idcard - 证件号 
	 * email - 邮箱 
	 */
	private static final String SQL_CREATE_RECORD = "CREATE TABLE IF NOT EXISTS "
			+ TicketOrder.TABLENAME
			+ " ( _id INTEGER PRIMARY KEY,"
			+ TicketOrder.FlightId
			+ TEXT_TYPE
			+ COMMA_SEP
			+ TicketOrder.OrderDate//下订单的日期
			+ TEXT_TYPE
			+ COMMA_SEP
			+ TicketOrder.FLAY_DAY//订单时间
			+ TEXT_TYPE
			+ COMMA_SEP
			+ TicketOrder.OrderMoney
			+ TEXT_TYPE
			+ COMMA_SEP
			+ TicketOrder.OrderState
			+ TEXT_TYPE
			+ COMMA_SEP
			+ TicketOrder.ORDERID
			+ TEXT_TYPE
			+ COMMA_SEP
			+ TicketOrder.UserIdCard + TEXT_TYPE + " )";

	private static DBHelper mInstance;
//单例 防止创建多个DBHelper对象
	public synchronized static DBHelper getInstance(Context context) {//
		if (mInstance == null) {
			mInstance = new DBHelper(context);
		}
		return mInstance;
	};
//私有化构造方法
	private DBHelper(Context context) {//获取上下文对象
		super(context, DATABASE_NAME, null, DATABASE_VERSION);//上下文对象，数据库名字，数据库版本号
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE_PASSANGER);//建表语句直接使用db.execSQL()方法执行SQL建表语句
		db.execSQL(SQL_CREATE_USER);
		db.execSQL(SQL_CREATE_FLY);
		db.execSQL(SQL_CREATE_RECORD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE_PASSANGER);
		db.execSQL(SQL_CREATE_FLY);
		db.execSQL(SQL_CREATE_RECORD);
	}

}
