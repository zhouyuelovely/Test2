package com.tarena.tabs.entity;
/**
 * 
 * 城市
 * **/
public class City {
	private int id;
	private String cityName;

	private String citySpellName;

	public City(String cityName) {
		this.cityName = cityName; 
	}
	public City(int id, String cityName, int provinceId, String citySpellName) {
		super();
		this.id = id;
		this.cityName = cityName;
	
		this.citySpellName = citySpellName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCitySpellName() {
		return citySpellName;
	}

	public void setCitySpellName(String citySpellName) {
		this.citySpellName = citySpellName;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + cityName + "]";
	}

}
