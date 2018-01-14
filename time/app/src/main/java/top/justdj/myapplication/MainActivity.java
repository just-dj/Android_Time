package top.justdj.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.litepal.crud.DataSupport;
import top.justdj.myapplication.fragment.MainFragment;
import top.justdj.myapplication.fragment.MeFragment;
import top.justdj.myapplication.fragment.PassFragment;
import top.justdj.myapplication.model.User;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
	
	private LinearLayout mainContainer;
	private int mcPaddingTop;
	private LinearLayout jour;
	private LinearLayout pass;
	private LinearLayout me;
	private static final String TAG = "MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//5.0推出了Material Design 实现全透明
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |     WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
		}
		
		//初始化参数
		if (isSign()){
			initPara();
			replaceFragment(new MainFragment(),"main");
		}

	}
	
	private boolean isSign(){
		List<User> list =  DataSupport.where("isLogin=?","1").find(User.class);
		if (list == null || list.isEmpty()){
			Intent intent = new Intent(MainActivity.this,LoginActivity.class);
			startActivityForResult(intent,70);
			finish();
//			弹出注册界面
			User user = new User();
			user.setAccount("123456");
			user.setName("石石");
			user.setSign("昨天剪了剪头发");
			user.setBackgroundImgUrl("");
			user.setHeadImgUrl("");
			user.save();
		}
		return true;
	}
	
	private int getStatusBarHeight(){
		int statusBarHeight = -1;
		//获取status_bar_height资源的ID
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			//根据资源ID获取响应的尺寸值
			statusBarHeight = getResources().getDimensionPixelSize(resourceId);
		}
		return statusBarHeight;
	}
	
	private int getNavigationBarHeight() {
		Resources resources = this.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
		int height = resources.getDimensionPixelSize(resourceId);
		Log.v("dbw", "Navi height:" + height);
		return height;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.jour:
				replaceFragment(new MainFragment(),"main");
				break;
			case R.id.passing:
				replaceFragment(new PassFragment(),"pass");
				break;
			case R.id.me:
				replaceFragment(new MeFragment(),"me");
				break;
			default:
				break;
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null){
			String fragmentKind = data.getStringExtra("returnData");
			FragmentManager fragmentManager = getSupportFragmentManager();
        /*在这里，我们通过碎片管理器中的Tag，就是每个碎片的名称，来获取对应的fragment*/
			Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentKind);
        /*然后在碎片中调用重写的onActivityResult方法*/
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	private void initPara(){
		
		mainContainer = (LinearLayout)findViewById(R.id.main_container);
		jour = (LinearLayout)findViewById(R.id.jour);
		jour.setOnClickListener(this);
		pass = (LinearLayout)findViewById(R.id.passing);
		pass.setOnClickListener(this);
		me = (LinearLayout)findViewById(R.id.me);
		me.setOnClickListener(this);
		mcPaddingTop = getStatusBarHeight();
	}
	
	private void replaceFragment(Fragment fragment,String kind){
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		//可以适当设置动画
		//transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		switch (kind){
			case "main":
				transaction.add(R.id.main_layout,fragment, "main");
				if (isNavigationBarShow())
					mainContainer.setPadding(0,mcPaddingTop,0,getNavigationBarHeight());
				else
					mainContainer.setPadding(0,mcPaddingTop,0,0);
				break;
			case "pass":
				transaction.add(R.id.main_layout,fragment, "pass");
				if (isNavigationBarShow())
					mainContainer.setPadding(0,mcPaddingTop,0,getNavigationBarHeight());
				else
					mainContainer.setPadding(0,mcPaddingTop,0,0);
				break;
			case "me":
				transaction.add(R.id.main_layout,fragment, "me");
				if (isNavigationBarShow())
					mainContainer.setPadding(0,0,0,getNavigationBarHeight());
				else
					mainContainer.setPadding(0,0,0,0);
				break;
			default:
				break;
		}
		transaction.replace(R.id.main_layout,fragment);
		transaction.commit();
	}
	
	//判断虚拟按键是否弹出
	
	private boolean isNavigationBarShow(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			Display display = this.getWindowManager().getDefaultDisplay();
			Point size = new Point();
			Point realSize = new Point();
			display.getSize(size);
			display.getRealSize(realSize);
			boolean  result  = realSize.y!=size.y;
			return realSize.y!=size.y;
		}else {
			boolean menu = ViewConfiguration.get(this).hasPermanentMenuKey();
			boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
			if(menu || back) {
				return false;
			}else {
				return true;
			}
		}
	}
}
