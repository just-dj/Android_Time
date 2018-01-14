package top.justdj.myapplication.model;

import org.litepal.crud.DataSupport;

public class User extends DataSupport {
	//账号 密码 头像地址 昵称 背景图片地址 签名
	
	private String account;
	
	private String passWord;
	
	private  String name;
	
	private  String sign;
	
	private String eamil;
	
	private  String headImgUrl;
	
	private String backgroundImgUrl;
	
	private boolean isLogin = false;
	
	private String likeList;
	
	private String storeList;
	
	public String getAccount() {
		return account;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getPassWord() {
		return passWord;
	}
	
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSign() {
		return sign;
	}
	
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getEamil() {
		return eamil;
	}
	
	public void setEamil(String eamil) {
		this.eamil = eamil;
	}
	
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	
	public String getBackgroundImgUrl() {
		return backgroundImgUrl;
	}
	
	public void setBackgroundImgUrl(String backgroundImgUrl) {
		this.backgroundImgUrl = backgroundImgUrl;
	}
	
	public boolean isLogin() {
		return isLogin;
	}
	
	public void setLogin(boolean login) {
		isLogin = login;
	}
	
	public String getLikeList() {
		return likeList;
	}
	
	public void setLikeList(String likeList) {
		this.likeList = likeList;
	}
	
	public String getStoreList() {
		return storeList;
	}
	
	public void setStoreList(String storeList) {
		this.storeList = storeList;
	}
}
