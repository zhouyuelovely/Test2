package com.tarena.tabs.entity;

import java.io.Serializable;

public class Passanger implements Serializable{
	public static final String NAME  ="name";//乘客姓名
	public static final String IDTYPE  ="psgCertifType";//证件类型
	public static final String IDCARD  ="CertifNum";//身份证号码
	public static final String EMAIL  ="email";//邮箱
	public static final String PHONE  ="phone";//电话
	public static final String ID  ="_id";
	public static final String TABLENAME  ="Passanger";//表名
	//声明变量
	private String psgId;//乘机人id
	private String psgName;//乘机人姓名
	private String psgCertifType;//证件类型
	private String CertifNum;//证件号码
	private String psgEmail;//邮箱
	private String psgPhone;//电话

	public Passanger() {
	}

	public Passanger(String psgId, String psgName, String psgCertifType,
			String certifNum, String psgEmail, String psgPhone) {
		super();//表示调用父类的构造函数。
		this.psgId = psgId;
		this.psgName = psgName;
		this.psgCertifType = psgCertifType;
		CertifNum = certifNum;
		this.psgEmail = psgEmail;
		this.psgPhone = psgPhone;
	}

	public String getPsgId() {
		return psgId;
	}

	public void setPsgId(String psgId) {
		this.psgId = psgId;
	}

	public String getPsgName() {
		return psgName;
	}

	public void setPsgName(String psgName) {
		this.psgName = psgName;
	}

	public String getPsgCertifType() {
		return psgCertifType;
	}

	public void setPsgCertifType(String psgCertifType) {
		this.psgCertifType = psgCertifType;
	}

	public String getCertifNum() {
		return CertifNum;
	}

	public void setCertifNum(String certifNum) {
		CertifNum = certifNum;
	}

	public String getPsgEmail() {
		return psgEmail;
	}

	public void setPsgEmail(String psgEmail) {
		this.psgEmail = psgEmail;
	}

	public String getPsgPhone() {
		return psgPhone;
	}

	public void setPsgPhone(String psgPhone) {
		this.psgPhone = psgPhone;
	}

	public String toString() {
		return this.psgName + "     " + this.psgCertifType + "     "
				+ this.CertifNum;
	}
}
