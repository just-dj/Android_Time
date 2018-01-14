package top.justdj.myapplication.tool;

import org.litepal.crud.DataSupport;
import top.justdj.myapplication.model.User;

import java.util.List;

public class UserGet {
	private static User user;
	
	public static User getUser() {
		//注意这一步 应该是找到本机当前登录账号
		List<User> list =  DataSupport.where("isLogin=?","1").find(User.class);
		if (list!= null && !list.isEmpty()){
			user  = list.get(0);
		}
		return user;
	}
	

}
