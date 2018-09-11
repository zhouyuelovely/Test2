package com.tarena.tabs.service;

import java.util.List;

import com.tarena.tabs.dbutils.DBUtils;
import com.tarena.tabs.entity.Passanger;

public interface PassangerService {

	/**
	 * 获取当前用户所添加过的所有的乘机人信息
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Passanger> findMyPassangers(DBUtils db) throws Exception;

	/**
	 * 更新乘机人信息
	 * 
	 * @param passanger
	 * @return
	 * @throws Exception
	 */
	String updatePassanger(Passanger passanger,DBUtils db) throws Exception;

	/**
	 * 通过id列表删除我的乘机人
	 * 
	 * @param ids
	 * @throws Exception
	 */
	String removeAllByIds(int ids,DBUtils db) throws Exception;

	/**
	 * 添加一个乘机人
	 * @param passanger
	 * @return
	 * @throws Exception
	 */
	String addPassanger(Passanger passanger,DBUtils db) throws Exception;

}
