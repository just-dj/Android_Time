package top.justdj.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.litepal.crud.DataSupport;
import top.justdj.myapplication.model.User;

import java.util.List;


public class ModifyInfoActivity extends AppCompatActivity implements View.OnClickListener{
	
	private TextView modifyInfoReturn;
	private TextView modifyInfoSave;
	private EditText modifyInfoName;
	private EditText modifyInfoSign;
	
	private User userNow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_info);
		
		initPara();
		
		
	}
	
	
	private void initPara(){
		List<User> list =  DataSupport.where("isLogin=?","1").find(User.class);
		if (list!= null && !list.isEmpty()){
			userNow  = list.get(0);
		}
		//支持5.0之上状态栏全透明
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |     WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
		}
		
		modifyInfoReturn = (TextView)findViewById(R.id.modifyInfo_return);
			modifyInfoReturn.setOnClickListener(this);
		modifyInfoSave = (TextView)findViewById(R.id.modifyInfo_save);
			modifyInfoSave.setOnClickListener(this);
		modifyInfoName = (EditText)findViewById(R.id.modifyInfo_name);
		modifyInfoSign  = (EditText)findViewById(R.id.modifyInfo_sign);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		String name = modifyInfoName.getText().toString();
		String sign = modifyInfoSign.getText().toString();
		intent.putExtra("returnData","me");
		
			switch (v.getId()){
				case R.id.modifyInfo_return:
						setResult(RESULT_CANCELED,intent);
						finish();
					break;
					
				case R.id.modifyInfo_save:
					User user = new User();
					if ((name == null || name.equals("") )&& (sign == null || sign.equals(""))){
						Toast.makeText(this, "信息不能为空", Toast.LENGTH_SHORT).show();
					}else {
						if (name != null && !name.equals(""))
							user.setName(name);
						if (sign!=null && !sign.equals(""))
							user.setSign(sign);
						user.updateAll("account = ?",userNow.getAccount());
						setResult(RESULT_OK,intent);
						finish();
					}
					break;
				default:
					break;
			}
			
	}
}
