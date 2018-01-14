package top.justdj.myapplication.model;

import org.litepal.crud.DataSupport;

public class PublicArticle extends DataSupport{
	
	private int articleId;
	private String account;
	private String userName;
	private String bgUrl;
	private String headUrl;
	private String date;
	private long time;
	private String content;
	private int likeNum;
	private int look;
	
	
	public int getArticleId() {
		return articleId;
	}
	
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	
	public String getAccount() {
		return account;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getBgUrl() {
		return bgUrl;
	}
	
	public void setBgUrl(String bgUrl) {
		this.bgUrl = bgUrl;
	}
	
	public String getHeadUrl() {
		return headUrl;
	}
	
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public int getLikeNum() {
		return likeNum;
	}
	
	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}
	
	public int getLook() {
		return look;
	}
	
	public void setLook(int look) {
		this.look = look;
	}
}
