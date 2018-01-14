package top.justdj.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.*;
import org.litepal.crud.DataSupport;
import top.justdj.myapplication.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
	
	private EditText register_account;
	private EditText register_email;
	private EditText register_password;
	
	private TextView register_btn;
	
	private TextView register_login;
	
	private String account;
	private String email;
	private String password;
	
	
	private static final String TAG = "RegisterActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		initPara();
	}
	
	private void initPara(){
		register_account = (EditText)findViewById(R.id.register_account);
		
		register_email = (EditText)findViewById(R.id.register_email);
		
		register_password = (EditText)findViewById(R.id.register_password);
		
		register_btn = (TextView)findViewById(R.id.register_btn);
			register_btn.setOnClickListener(this);
			
		register_login = (TextView)findViewById(R.id.register_login);
			register_login.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()){
			case R.id.register_btn:
				//首先检查格式
					if (checkString()){
						new CheckAccountTask().execute();
					}
				break;
			case R.id.register_login:
				intent = new Intent(RegisterActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
				break;
			default:
				break;
		}
	}
	
	private boolean checkString(){
		boolean condition = true;
		account = register_account.getText().toString();
		if (account == null || account.length() <= 6){
			condition = false;
			Toast.makeText(this, "账号为7到10位数字", Toast.LENGTH_SHORT).show();
			return condition;
		}
		email = register_email.getText().toString();
		if (email == null || email.length()<5){
			condition = false;
			Toast.makeText(this, "email格式不正确", Toast.LENGTH_SHORT).show();
			
		}else {
			condition = isEmail(email);
			if (condition == false)
				Toast.makeText(this, "email格式不正确", Toast.LENGTH_SHORT).show();
		}
		
		if (condition == false)
			return condition;
		
		password = register_password.getText().toString();
		if (password == null || password.length() < 6){
			condition = false;
			Toast.makeText(this, "密码为6到16位字符串", Toast.LENGTH_SHORT).show();
			return condition;
		}
		return condition;
	}
	
	// 判断email格式是否正确
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}
	
	private class CheckAccountTask extends AsyncTask {
		private String responseData;
		
		@Override
		protected void onProgressUpdate(Object[] values) {
			Log.e(TAG, "run 回复的消息: "+responseData );
			if (responseData.equals("success")){
				//跳转到mainActivity
				User mid = new User();
				mid.setLogin(false);
				mid.updateAll();
				
				User user = new User();
				user.setAccount(account);
				user.setName("石头");
				user.setSign("");
				user.setLogin(true);
				user.setBackgroundImgUrl("");
				user.setHeadImgUrl("");
				user.save();
				
				Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}else {
				Toast.makeText(RegisterActivity.this, responseData, Toast.LENGTH_SHORT).show();
			}
		}
		
		@Override
		protected Object doInBackground(Object[] objects) {
			//创建OkHttpClient对象
			OkHttpClient client = new OkHttpClient();
			//通过FormEncodingBuilder对象构造Post请求体
			RequestBody body = new FormBody.Builder()
					.add("account",account)
					.add("email",email)
					.add("password",password)
					.build();
			//通过请求地址和请求体构造Post请求对象Request
			Request request = new Request.Builder()
					.addHeader("Content-Type", "text/html")
					.addHeader("charset", "utf_8")//防止中文乱码
					.url("http://www.justdj.top/android/php/register.php")
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
}
