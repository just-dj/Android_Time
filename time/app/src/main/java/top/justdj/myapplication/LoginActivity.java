package top.justdj.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.litepal.crud.DataSupport;
import top.justdj.myapplication.model.PublicArticle;
import top.justdj.myapplication.model.User;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
	
	private EditText loginAccount;
	private EditText loginPassword;
	private TextView loginBtn;
	private Gson gson;
	
	private TextView jumpToRegister;
	
	private String account;
	
	private String passWord;
	private static final String TAG = "LoginActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//5.0推出了Material Design 实现全透明
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |     WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
		}
		
		initPara();
		
		
	}
	
	private void initPara(){
		
		gson  = new Gson();
		
		loginAccount = (EditText)findViewById(R.id.login_account);
		
		loginPassword = (EditText)findViewById(R.id.login_password);
		
		loginBtn = (TextView) findViewById(R.id.login_btn);
			loginBtn.setOnClickListener(this);
		
		jumpToRegister = (TextView)findViewById(R.id.jumptoregister);
			jumpToRegister.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.login_btn:
				//账号有效性
				if (checkString()){
					checkAccount();
				}
				
				
				
				break;
				
			case R.id.jumptoregister:
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
				finish();
				break;
				
				default:
					break;
		}
	}
	
	
	private boolean checkString(){
		boolean condition = false;
		account = loginAccount.getText().toString();
		if (account == null || account.length() <= 6){
			condition = false;
			Toast.makeText(this, "账号为7到10位数字", Toast.LENGTH_SHORT).show();
			return condition;
		}
		passWord = loginPassword.getText().toString();
		if (passWord == null || passWord.length() < 6){
			condition = false;
			Toast.makeText(this, "密码为6到16位字符串", Toast.LENGTH_SHORT).show();
			return condition;
		}
		
		condition = true;
		return condition;
	}
	
	private boolean isTrue = false;
	private boolean checkAccount(){
		
		new CheckAccountTask().execute();
		
		return false;
	}
	
	private class CheckAccountTask extends AsyncTask {
		private String responseData;
		
		@Override
		protected void onProgressUpdate(Object[] values) {
			Log.e(TAG, "run 回复的消息: "+responseData );
			if (responseData.equals("loginSuccess")){
				User user = new User();
				user.setLogin(false);
				user.updateAll();
				
				List<User> userList = DataSupport.where("account=?",account).find(User.class);
				User mid;
				
				new GetUserInfo().execute();
				
			}else {
			
			}
		}
		
		@Override
		protected Object doInBackground(Object[] objects) {
			//创建OkHttpClient对象
			OkHttpClient client = new OkHttpClient();
			//通过FormEncodingBuilder对象构造Post请求体
			RequestBody body = new FormBody.Builder()
					.add("account",account)
					.add("password",passWord)
					.build();
			//通过请求地址和请求体构造Post请求对象Request
			Request request = new Request.Builder()
					.addHeader("Content-Type", "text/html")
					.addHeader("charset", "utf_8")//防止中文乱码
					.url("http://www.justdj.top/android/php/login.php")
					.post(body)
					.build();
			try{
				Response response = client.newCall(request).execute();
				
				responseData = response.body().string();
				
				publishProgress();
				
			}catch (Exception e){
				e.printStackTrace();
			}
			return null;
		}
	}
	
	
	private class GetUserInfo extends AsyncTask {
		private String responseData;
		private List<User> list;
		
		@Override
		protected void onProgressUpdate(Object[] values) {
			Log.e(TAG, "run 回复的消息: "+responseData );
			User user = new User();
			user.setAccount(list.get(0).getAccount());
			user.setName(list.get(0).getName());
			user.setSign(list.get(0).getSign());
			user.setHeadImgUrl(list.get(0).getHeadImgUrl());
			user.setBackgroundImgUrl(list.get(0).getBackgroundImgUrl());
			user.setEamil(list.get(0).getEamil());
			user.setLogin(true);
			user.setLikeList(list.get(0).getLikeList());
			user.setStoreList(list.get(0).getStoreList());
			user.save();
			Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
		
		@Override
		protected Object doInBackground(Object[] objects) {
			//创建OkHttpClient对象
			OkHttpClient client = new OkHttpClient();
			//通过FormEncodingBuilder对象构造Post请求体
			RequestBody body = new FormBody.Builder()
					.add("account",account)
					.build();
			//通过请求地址和请求体构造Post请求对象Request
			Request request = new Request.Builder()
					.addHeader("Content-Type", "text/html")
					.addHeader("charset", "utf_8")//防止中文乱码
					.url("http://www.justdj.top/android/php/getUser.php")
					.post(body)
					.build();
			try{
				Response response = client.newCall(request).execute();
				
				responseData = response.body().string();
				
				list = gson.fromJson(responseData,new TypeToken<List<User>>(){}.getType());
				
				publishProgress();
				
			}catch (Exception e){
				e.printStackTrace();
			}
			return null;
		}
	}
}
