package com.tarena.tabs.service;

import com.tarena.tabs.dbutils.DBUtils;
import com.tarena.tabs.entity.User;

public interface UserService {

	/**
	 * �޸�����
	 * 
	 * @param oldpwd ԭ��������
	 * @param newpwd  ������
	 
	 * @throws Exception
	 */
	String modifyPwd(String who, String oldpwd, String newpwd, DBUtils db) throws Exception;

	/**
	 * ��½ҵ��
	 * 
	 * @param name ����
	 * @param pwd  ����
	
	 * @throws Exception
	 */
	String login(String name, String pwd, DBUtils db) throws Exception;

	/**
	 * ע��ҵ��
	 * 
	 * @param user
	 
	 * @throws Exception
	 */
	String regist(User user, DBUtils db) throws Exception;

}
