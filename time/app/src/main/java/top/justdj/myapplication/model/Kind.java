package top.justdj.myapplication.model;

import org.litepal.crud.DataSupport;

public class Kind extends DataSupport {
	
	private int kindId;
	
	private String name;
	
	public Kind(String name) {
		this.name = name;
	}
	
	public int getKindId() {
		return kindId;
	}
	
	public void setKindId(int kindId) {
		this.kindId = kindId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
