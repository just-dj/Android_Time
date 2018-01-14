package top.justdj.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import okhttp3.*;
import org.litepal.crud.DataSupport;
import top.justdj.myapplication.model.User;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChooseMyBGActivity extends AppCompatActivity implements View.OnClickListener,TakePhoto
		.TakeResultListener,InvokeListener {
	
	private TakePhoto takePhoto;
	private InvokeParam invokeParam;
	
	private User user;
	private static final String TAG = "ChooseMyBGActivity";
	private LinearLayout btnContainer;
	private TextView myTitle;
	private TextView myTakePhoto;
	private TextView myPickPhoto;
	private TextView myReturn;
	private String kind;
	
	
	private File file;
	private String fileName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getTakePhoto().onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_my_bg);
		
		//5.0推出了Material Design 实现全透明
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |     WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
		}
		Intent intent = getIntent();
		kind = intent.getStringExtra("value");
		
		initPara();
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		getTakePhoto().onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		getTakePhoto().onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 *  获取TakePhoto实例
	 * @return
	 */
	public TakePhoto getTakePhoto(){
		if (takePhoto==null){
			takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
		}
		return takePhoto;
	}
	
	@Override
	public void takeSuccess(TResult result) {
		Log.i(TAG,"takeSuccess：" + result.getImage().getCompressPath());
		upImage();
		Intent intent = new Intent();
		intent.putExtra("path",path);
		intent.putExtra("returnData","me");
		setResult(RESULT_OK,intent);
		finish();
	}
	@Override
	public void takeFail(TResult result,String msg) {
		Log.i(TAG, "图片获取失败,请稍后尝试" + msg);
		Intent intent = new Intent();
		intent.putExtra("returnData","me");
		setResult(RESULT_CANCELED,intent);
		finish();
	}
	@Override
	public void takeCancel() {
		Log.i(TAG, getResources().getString(R.string.msg_operation_canceled));
		Intent intent = new Intent();
		intent.putExtra("returnData","me");
		setResult(RESULT_CANCELED,intent);
		finish();
	}
	
	@Override
	public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
		PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
		if(PermissionManager.TPermissionType.WAIT.equals(type)){
			this.invokeParam=invokeParam;
		}
		return type;
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		//以下代码为处理Android6.0、7.0动态权限所需
		PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
		PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
	}
	
	
	private void initPara(){
		
		//实际上也是应该找到当前登录账号
		List<User> list =  DataSupport.where("isLogin=?","1").find(User.class);
		if (list!= null && !list.isEmpty()){
			user  = list.get(0);
		}
		
		btnContainer = (LinearLayout)findViewById(R.id.btn_container);
			btnContainer.setOnClickListener(this);
		myTitle = (TextView)findViewById(R.id.my_title);
			myTitle.setText(kind);
		myTakePhoto = (TextView)findViewById(R.id.my_takePhoto);
			myTakePhoto.setOnClickListener(this);
		myPickPhoto = (TextView)findViewById(R.id.my_pickPhoto);
			myPickPhoto.setOnClickListener(this);
		myReturn = (TextView)findViewById(R.id.my_return);
			myReturn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Uri imgUri = getUri();
//这里先不压缩了
//		CompressConfig compressConfig1=new CompressConfig.Builder().setMaxPixel(100).create();
		CropOptions cropOptions=new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
//		takePhoto.onEnableCompress(compressConfig1,true);
		switch (v.getId()){
			case R.id.my_takePhoto:
				takePhoto.onPickFromCaptureWithCrop(imgUri,cropOptions);
				break;
			case R.id.my_pickPhoto:
				takePhoto.onPickFromGalleryWithCrop(imgUri,cropOptions);
				break;
			case R.id.my_return:
				Intent intent = new Intent();
				intent.putExtra("returnData","me");
				setResult(RESULT_CANCELED,intent);
				finish();
				break;
			default:
				break;
		}
		
	}
	
	private String path;
	private Uri getUri(){
		File dcimPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		if (kind.equals("更换头像"))
			fileName = user.getAccount()+"HeadImg.jpg";
		else
			fileName = user.getAccount() + "BackgroundImg.jpg";
		
		file = new File(dcimPath,fileName);
		
		path = file.getAbsolutePath();
		try {
			if(file.exists()) {
				file.delete();
			}
			file.createNewFile();
		} catch(IOException e) {
			e.printStackTrace();
		}
		Uri imgUri;
		if(Build.VERSION.SDK_INT >= 24){
			imgUri = FileProvider.getUriForFile(this,"top.justdj.myapplication.fileprovider",
					file);
		}else {
			imgUri = Uri.fromFile(file);
		}
		return imgUri;
	}
	
	// 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event) {  //点击外面退出这activity
		Intent intent = new Intent();
		intent.putExtra("returnData","me");
		setResult(RESULT_CANCELED,intent);
		finish();
		return true;
	}
	
	private void upImage() {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				OkHttpClient mOkHttpClient = new OkHttpClient();
				
				//多文件表单上传构造器
				MultipartBody.Builder builder = new MultipartBody.Builder()
						.setType(MultipartBody.FORM)
						.addFormDataPart("imgName", fileName,
								RequestBody.create(MediaType.parse("image/png"), file));
				
				RequestBody requestBody = builder.build();
				
				Request request = new Request.Builder()
						.url("http://www.justdj.top/android/php/uploadImg.php")
						.post(requestBody)
						.build();
				
				Call call = mOkHttpClient.newCall(request);
				
				call.enqueue(new Callback() {
					@Override
					public void onFailure(Call call, IOException e) {
						Log.e(TAG, "onFailure: "+e );
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Log.e(TAG, "run: 图片上传失败" );
								//Toast.makeText(ChooseMyBGActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
							}
						});
					}
					
					@Override
					public void onResponse(Call call, Response response) throws IOException {
						Log.e(TAG, "回复信息："+response.body().string());
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Log.e(TAG, "run: 图片上传成功" );
								//Toast.makeText(ChooseMyBGActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
							}
						});
					}
				});
				
				
			}
		}).start();
	}
}
