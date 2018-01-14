package top.justdj.myapplication.model;

import org.litepal.crud.DataSupport;

public class Article extends DataSupport{
	private int id;
	private String userId;
	private String content;
	private String date;
	private String fontColor;
	private int fontSize;
	private int background;
	private String position;
	private String kind;
	private String img;
	private int like;
	private int store;
	private int look;
	private int weather;
	//是否发布
	private boolean isPublish;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getFontColor() {
		return fontColor;
	}
	
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public int getBackground() {
		return background;
	}
	
	public void setBackground(int background) {
		this.background = background;
	}
	
	public String getPosition() {
		return position;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getKind() {
		return kind;
	}
	
	public void setKind(String kind) {
		this.kind = kind;
	}
	
	public String getImg() {
		return img;
	}
	
	public void setImg(String img) {
		this.img = img;
	}
	
	public int getLike() {
		return like;
	}
	
	public void setLike(int like) {
		this.like = like;
	}
	
	public int getStore() {
		return store;
	}
	
	public void setStore(int store) {
		this.store = store;
	}
	
	public int getLook() {
		return look;
	}
	
	public void setLook(int look) {
		this.look = look;
	}
	
	public int getWeather() {
		return weather;
	}
	
	public void setWeather(int weather) {
		this.weather = weather;
	}
	
	public boolean isPublish() {
		return isPublish;
	}
	
	public void setPublish(boolean publish) {
		isPublish = publish;
	}
}
