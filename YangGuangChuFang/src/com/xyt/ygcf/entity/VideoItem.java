package com.xyt.ygcf.entity;

public class VideoItem {
	public String id;   //视频设备ID
	public String name;   //服务商名称
	public String ip;    //服务器地址
	public String port;   //服务器端口
	public String user;   //对方平台帐号
	public String passward;  //对方平台密码 
	public String sn;      //视频编号
	public String devName;  //视频名称
	public String status;   //设备状态
	public String getIsDigit() {
		return isDigit;
	}
	public void setIsDigit(String isDigit) {
		this.isDigit = isDigit;
	}
	public String getChannelNo() {
		return channelNo;
	}
	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}
	public String supplierId; //供应商ID
	public String isDigit;    //是否绑定通道  0-没绑定；1-绑定
	public String channelNo;  //通道号
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassward() {
		return passward;
	}
	public void setPassward(String passward) {
		this.passward = passward;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getStatus() {
		return status;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
