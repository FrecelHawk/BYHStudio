package com.xyt.ygcf.entity;

public class VideoSupplier {
	public String id;      //供应商ID
	public String name;   //服务商名称
	public String ip;    //服务器地址
	public String port;   //服务器端口
	public String user;   //对方平台帐号
	public String passward;  //对方平台密码 
	
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
}
