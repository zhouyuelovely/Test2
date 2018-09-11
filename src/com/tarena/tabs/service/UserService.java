package com.tarena.tabs.service;

import com.tarena.tabs.dbutils.DBUtils;
import com.tarena.tabs.entity.User;

public interface UserService {

	/**
	 * 修改密码
	 * 
	 * @param oldpwd 原来的密码
	 * @param newpwd  新密码
	 
	 * @throws Exception
	 */
	String modifyPwd(String who, String oldpwd, String newpwd, DBUtils db) throws Exception;

	/**
	 * 登陆业务
	 * 
	 * @param name 姓名
	 * @param pwd  密码
	
	 * @throws Exception
	 */
	String login(String name, String pwd, DBUtils db) throws Exception;

	/**
	 * 注册业务
	 * 
	 * @param user
	 
	 * @throws Exception
	 */
	String regist(User user, DBUtils db) throws Exception;

}
