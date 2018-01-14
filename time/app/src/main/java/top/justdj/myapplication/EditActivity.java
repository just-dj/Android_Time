package top.justdj.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.baidu.location.*;
import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import okhttp3.*;
import org.litepal.crud.DataSupport;
import top.justdj.myapplication.adapter.*;
import top.justdj.myapplication.model.*;
import top.justdj.myapplication.myspinner.MyOwnSpinner;
import top.justdj.myapplication.tool.UserGet;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.net.Proxy.Type.HTTP;

public class EditActivity extends AppCompatActivity implements View.OnClickListener,TakePhoto.TakeResultListener,InvokeListener {
	
	//拍照 以及选取图片
	private TakePhoto takePhoto;
	private InvokeParam invokeParam;
	
	private User user;
	
	//定位用到的变量
	public LocationClient mLocationClient = null;
	private MyLocationListener myLocationListener = new MyLocationListener();
		//相关配置
		private LocationClientOption option = new LocationClientOption();
	
	private Toolbar toolbar;
	//取消按钮
	private TextView cancel;
	//完成按钮
	private ImageView complate;
	//用textView实现的spinner
	private TextView spinnerView;
	//点击分类链表显示的下拉框
	private MyOwnSpinner mySpinner;
	
	
	//分类链表
	private List<Kind> kindList;
	
	private LinearLayout container;
	//最重要的
	private EditText articleContent;
	private Article article;
	
	//编辑控件
		private LinearLayout editChoice;
		private LinearLayout retractContain;
		private ImageView retract;//收回编辑菜单
		
		//几个编辑菜单名
			private ImageView font;
			private ImageView background;
			private ImageView img;
			private ImageView position;
			private ImageView weather;
			//几个ViewGroup
				//字体
				private LinearLayout fontContain;
					private SeekBar fontSize;
					private TextView fontSizeValue;
					private TextView fontColorNow;
					private RecyclerView fontRecycleView;
						private FontColorAdapter fontColorAdapter;
				//背景
				private LinearLayout backgroundContain;
					private RecyclerView fontBgImgRecycleView;
						private FontBackgroundAdapter fontBackgroundAdapter;
					
				//选取图片
				private List<String> articleInsertImgList;
				private LinearLayout imgContain;
					private RecyclerView choosedImgRecycleView;
						private ArticleInsertImgAdapter articleInsertImgAdapter;
						
					private TextView takePhotoMy;
					private TextView getImg;
					
				//定位
				private LinearLayout positionContain;
					private CheckBox isPublishCheckBox;
					private TextView positionResultTextView;
				
				//天气
				private LinearLayout weatherContain;
					private ImageView weatherChooseNow;
					private RecyclerView weatherRecycleView;
						private WeatherAdapter weatherAdapter;
						private int weatherNow = 0;
						
				private static final String TAG = "EditActivity";
				
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getTakePhoto().onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		
		Intent intent = getIntent();
		//String id = intent.getStringExtra("articlieId");
		//这里从数据库获取相应的Article
		
		//初始化参数
		initPara();
		//设置toolbar
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		//5.0推出了Material Design 实现全透明
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |     WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
		}
		
		//用于处理字体选择事件
		fontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				
				fontSizeValue.setText((progress+10) + "");
				articleContent.setTextSize(progress + 10);
				article.setFontSize(progress + 10);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		fontColorAdapter.setOnItemClickListener(new FontColorAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				fontColorNow.setBackgroundColor(Color.parseColor(FontColor.fontColor[position]));
				fontColorAdapter.select(position);
				fontColorAdapter.notifyDataSetChanged();
				articleContent.setTextColor(Color.parseColor(FontColor.fontColor[position]));
				article.setFontColor(FontColor.fontColor[position]);
				//Toast.makeText(EditActivity.this, "click " + FontColor.fontColor[position], Toast.LENGTH_SHORT)
				//		.show();
			}
		});
		
		fontBackgroundAdapter.setOnItemClickListener(new FontBackgroundAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				container.setBackgroundResource(FontBackgroundImg.backgroundImg[position]);
				article.setBackground(FontBackgroundImg.backgroundImg[position]);
			}
		});
		
		articleInsertImgAdapter.setOnItemClickListener(new ArticleInsertImgAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				//Toast.makeText(EditActivity.this, position, Toast.LENGTH_SHORT).show();
				articleInsertImgList.remove(position);
				articleInsertImgAdapter.notifyDataSetChanged();
			}
		});
		
		weatherAdapter.setOnItemClickListener(new WeatherAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				weatherChooseNow.setBackgroundResource(Weather.weatherImg[position]);
				article.setWeather(Weather.weatherImg[position]);
				weatherNow = Weather.weatherImg[position];
			}
		});
		
		mySpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				setTextImage(R.drawable.downblack);
			}
		});
		
		mySpinner.inKindAdapter.setOnItemClickListener(new InKindAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				mySpinner.dismiss();
				if (kindList.get(position).getName().equals("编辑分类") && position == kindList.size()-1){
					Intent intent = new Intent(EditActivity.this,KindEditActivity.class);
					startActivityForResult(intent,88);
					//跳转到编辑界面
				}
				else	{
					//数据库查询类型
					//mySpinner.check(position);
					mySpinner.inKindAdapter.check(position);
					mySpinner.inKindAdapter.notifyDataSetChanged();
					spinnerView.setText(kindList.get(position).getName());
					article.setKind(kindList.get(position).getName());
					Toast.makeText(EditActivity.this, "点击了:" + kindList.get(position).getName(),Toast.LENGTH_LONG).show();
				}
			}
		});
		
		checkPermission();
	}
	
	
	private void initPara(){
		
		
		
		
		//应该是当前激活账号
		user =UserGet.getUser();
		
		
		//声明LocationClient类
		mLocationClient = new LocationClient(getApplicationContext());
		//注册监听函数
		mLocationClient.registerLocationListener(myLocationListener);
		//配置定位选项
		configureLocation();
		
		
		//首先判断是不是二次编辑
		if (article == null){
			article = new Article();
			article.setDate("");
			article.setKind("未分类");
			article.setFontSize(17);
			article.setFontColor("#30393E");
			article.setBackground(R.drawable.bg23);
			article.setPosition("");
			article.setPublish(false);
			article.setImg("");
			article.setWeather(R.drawable.weather1);
			//设置background属性
		}
		
		//控件
		container = (LinearLayout)findViewById(R.id.container);
		container.setBackgroundResource(article.getBackground());
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		//初始化分类链表
		kindList = new ArrayList<Kind>();
		kindList.add(new Kind("未分类"));
		kindList.addAll(DataSupport.order("name asc").find(Kind.class));
		kindList.add(new Kind("编辑分类"));
		
		//toolbar上的三个按钮
		cancel = (TextView)findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		complate = (ImageView)findViewById(R.id.complate) ;
		complate.setOnClickListener(this);
		spinnerView = (TextView) findViewById(R.id.myspinner);
		spinnerView.setText(article.getKind());
		spinnerView.setOnClickListener(this);
		
		//点击分类链表显示的下拉框
		mySpinner = new MyOwnSpinner(this,kindList);
			//将article对应的类别选中
			setTextImage(R.drawable.downblack);
			mySpinner.inKindAdapter.check(0);
			mySpinner.inKindAdapter.notifyDataSetChanged();
			spinnerView.setText(kindList.get(0).getName());
			
		//文本编辑框{
		articleContent = (EditText)findViewById(R.id.articleContent);
		articleContent.setOnClickListener(this);
		articleContent.setTextColor(Color.parseColor(article.getFontColor()));
		articleContent.setTextSize(article.getFontSize());
		
		
		//编辑菜单选选项
			editChoice = (LinearLayout)findViewById(R.id.editchoice);
			retractContain = (LinearLayout)findViewById(R.id.retractContain);
			retract = (ImageView)findViewById(R.id.retract);
			retract.setOnClickListener(this);
			//具体的菜单
				//字体
				font = (ImageView)findViewById(R.id.font);
				font.setOnClickListener(this);
					fontSizeValue = (TextView)findViewById(R.id.fontSizeValue);
						fontSizeValue.setText(article.getFontSize() + "");
					fontSize = (SeekBar)findViewById(R.id.fontSize);
						fontSize.setProgress(article.getFontSize() - 10);
					fontColorNow = (TextView)findViewById(R.id.fontColorNow);
						fontColorNow.setBackgroundColor(Color.parseColor(article.getFontColor()));
					fontRecycleView = (RecyclerView)findViewById(R.id.fontRecycleView);
						StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
								StaggeredGridLayoutManager.HORIZONTAL);
						fontRecycleView.setLayoutManager(layoutManager);
						fontColorAdapter = new FontColorAdapter(Arrays.asList(FontColor.fontColor),fontColorNow);
						fontRecycleView.setAdapter(fontColorAdapter);
						fontColorAdapter.select(FontColor.getColorPosition(article.getFontColor()));
						fontColorAdapter.notifyDataSetChanged();
				//背景
				background = (ImageView)findViewById(R.id.background);
				background.setOnClickListener(this);
					fontBgImgRecycleView = (RecyclerView)findViewById(R.id.fontBgImgRecycleView);
					StaggeredGridLayoutManager bgLayoutManager = new StaggeredGridLayoutManager(1,
							StaggeredGridLayoutManager.HORIZONTAL);
					fontBgImgRecycleView.setLayoutManager(bgLayoutManager);
					fontBackgroundAdapter = new FontBackgroundAdapter(Arrays.asList(FontBackgroundImg.backgroundImg));
					fontBgImgRecycleView.setAdapter(fontBackgroundAdapter);
					
				//图片
				articleInsertImgList = new LinkedList <String>();
								//初始化 要看是不是二次编辑
					
				img = (ImageView)findViewById(R.id.img);
				img.setOnClickListener(this);
					takePhotoMy = (TextView)findViewById(R.id.takePhoto);
					    takePhotoMy.setOnClickListener(this);
					getImg = (TextView)findViewById(R.id.getImg);
						getImg.setOnClickListener(this);
					choosedImgRecycleView = (RecyclerView)findViewById(R.id.choosedImgRecycleView);
						StaggeredGridLayoutManager insertImgLayoutManager = new StaggeredGridLayoutManager(1,
								StaggeredGridLayoutManager.HORIZONTAL);
						choosedImgRecycleView.setLayoutManager(insertImgLayoutManager);
						articleInsertImgAdapter = new ArticleInsertImgAdapter(articleInsertImgList);
						choosedImgRecycleView.setAdapter(articleInsertImgAdapter);
					
					
				//定位和同步
				position = (ImageView)findViewById(R.id.position);
				position.setOnClickListener(this);
					isPublishCheckBox = (CheckBox)findViewById(R.id.isPublishCheckBox);
						isPublishCheckBox.setChecked(article.isPublish());
						isPublishCheckBox.setOnClickListener(this);
					positionResultTextView = (TextView)findViewById(R.id.positionResultTextView);
						positionResultTextView.setText(article.getPosition());
				
				
				//天气
				weather = (ImageView)findViewById(R.id.weather);
				weather.setOnClickListener(this);
					weatherChooseNow = (ImageView)findViewById(R.id.weatherChooseNow);
						weatherChooseNow.setBackgroundResource(article.getWeather());
					weatherRecycleView = (RecyclerView)findViewById(R.id.weatherRecycleView);
						StaggeredGridLayoutManager weatherLayoutManager = new StaggeredGridLayoutManager(2,
								StaggeredGridLayoutManager.HORIZONTAL);
						weatherRecycleView.setLayoutManager(weatherLayoutManager);
						weatherAdapter = new WeatherAdapter(Arrays.asList(Weather.weatherImg));
						weatherRecycleView.setAdapter(weatherAdapter);
					
					
					
				//几个ViewGroup
					fontContain = (LinearLayout)findViewById(R.id.fontContain);
					backgroundContain = (LinearLayout)findViewById(R.id.backgroundContain);
					positionContain = (LinearLayout)findViewById(R.id.positionContain);
					imgContain = (LinearLayout)findViewById(R.id.imgContain);
					weatherContain = (LinearLayout)findViewById(R.id.weatherContain);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()){
			case R.id.cancel:
				//取消编辑
				Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
				intent = new Intent();
				intent.putExtra("returnData","main");
				setResult(RESULT_CANCELED,intent);
				finish();
				break;
			case R.id.complate:
				//进行数据存储管理 把数据存入数据库
				if (articleContent.getText().toString().equals("")){
					Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
				}else{
					//开始写入数据库
					if (!articleInsertImgList.isEmpty()){
						for (String a:articleInsertImgList)
							article.setImg(article.getImg() +a +" a1b2c3g7as78 ");
					}else {
						article.setImg("");
					}
					if (weatherNow!=0){
						article.setWeather(weatherNow);
					}
					article.setContent(articleContent.getText().toString());
					article.setDate(stringData());
					article.setUserId(user.getAccount());
					article.save();
					Toast.makeText(this, "complate", Toast.LENGTH_SHORT).show();
					//检测是否选择发布到公共板块
					if(isPublishCheckBox.isChecked()){
						uploadArticle();
					}
					
					intent = new Intent();
					intent.putExtra("returnData","main");
					setResult(RESULT_OK,intent);
					finish();
				}
				break;
			//显示PopupWindow 就是分类链表显示的下拉框 完成
			case R.id.myspinner:
				//选取分类
				mySpinner.setWidth(spinnerView.getWidth());
				mySpinner.showAsDropDown(spinnerView);
				setTextImage(R.drawable.upblack);
				break;
			//点击文本编辑框
			case R.id.articleContent:
				//隐藏菜单 同时弹出键盘
				editChoice.setVisibility(View.GONE);
				retractContain.setVisibility(View.VISIBLE);
				break;
			//收回编辑菜单
			case R.id.retract:
				showMenu(-1);
				break;
			//五个功能按键
			case R.id.font:
				showMenu(0);
				break;
//背景
			case R.id.background:
				showMenu(1);
				editChoice.setVisibility(View.VISIBLE);
				retractContain.setVisibility(View.VISIBLE);
				break;
//插入图片
			case R.id.img:
				showMenu(2);
				break;
			case R.id.takePhoto:
				/**
				 * 从相机获取图片(不裁剪)
				 * @param outPutUri 图片保存的路径
				 */
				String filename =""+System.currentTimeMillis() ;
				File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
				File outputImage = new File(path,filename+".jpg");
				try {
					if(outputImage.exists()) {
						outputImage.delete();
					}
					outputImage.createNewFile();
				} catch(IOException e) {
					e.printStackTrace();
				}
				Uri imgUri;
				if(Build.VERSION.SDK_INT >= 24){
					imgUri = FileProvider.getUriForFile(EditActivity.this,"top.justdj.myapplication.fileprovider",
							outputImage);
				}else {
					imgUri = Uri.fromFile(outputImage);
				}
				CompressConfig compressConfig1=new CompressConfig.Builder().setMaxPixel(100).create();
				takePhoto.onEnableCompress(compressConfig1,true);
				takePhoto.onPickFromCapture(imgUri);
//
				break;
			case R.id.getImg:
				CompressConfig compressConfig=new CompressConfig.Builder().setMaxPixel(100).create();
				takePhoto.onEnableCompress(compressConfig,true);
				takePhoto.onPickFromDocuments();
				break;
//定位
			case R.id.position:
				showMenu(3);
				checkPermission();
				break;
				case R.id.isPublishCheckBox:
					article.setPublish(isPublishCheckBox.isChecked());
					break;
					
//天气
			case R.id.weather:
				showMenu(4);
				break;
			default:
				break;
		}
	}
	
	String account;
	String bgUrl;
	String headUrl ;
	String date ;
	long time;
	String content ;
	String userName ;
	
	private void uploadArticle(){
//		private String likeList;
//		private String storeList;
//		private int look;
		account = user.getAccount();
		
		
		user = UserGet.getUser();
		String head = "";
		String background = "";
		head = user.getHeadImgUrl();
		background = user.getBackgroundImgUrl();
		
		bgUrl = background;
		headUrl = head;
		date = article.getDate();
		time = new Date().getTime();
		content = articleContent.getText().toString();
		userName = user.getName();
		
		new UploadArticle().execute();
	}
	
	
	class UploadArticle extends AsyncTask {
		private String responseData;
		
		@Override
		protected void onProgressUpdate(Object[] values) {
			//Log.e(TAG, "run: 你刚刚发送的数据"+responseData );
		}
		
		@Override
		protected Object doInBackground(Object[] objects) {
			//创建OkHttpClient对象
			OkHttpClient client = new OkHttpClient();
			//通过FormEncodingBuilder对象构造Post请求体
			RequestBody body = new FormBody.Builder()
					.add("account",account)
					.add("userName",userName)
					.add("bgUrl",bgUrl)
					.add("headUrl",headUrl)
					.add("date",date)
					.add("time",time+"")
					.add("content",content)
					.build();
			//通过请求地址和请求体构造Post请求对象Request
			Request request = new Request.Builder()
					.addHeader("Content-Type", "text/html")
					.addHeader("charset", "utf-8")//防止中文乱码
					.url("http://www.justdj.top/android/php/uploadArticle.php")
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
	
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location){
			//此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
			//以下只列举部分获取地址相关的结果信息
			//更多结果信息获取说明，请参照类参考中BDLocation类中的说明
			
			String addr = location.getAddrStr();    //获取详细地址信息
//			String country = location.getCountry();    //获取国家
//			String province = location.getProvince();    //获取省份
//			String city = location.getCity();    //获取城市
//			String district = location.getDistrict();    //获取区县
//			String street = location.getStreet();    //获取街道信息
			positionResultTextView.setText(addr);
			article.setPosition(addr);
			
			Log.e(TAG, "onReceiveLocation: " + addr );
		}
	}
	
	
	private void checkPermission(){
		List<String> permissionList = new ArrayList<>();
		if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
				PackageManager
				.PERMISSION_GRANTED) {
			permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
		}
		if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			permissionList.add(Manifest.permission.READ_PHONE_STATE);
		}
		if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}
		if (!permissionList.isEmpty()) {
			String [] permissions = permissionList.toArray(new String[permissionList.size()]);
			ActivityCompat.requestPermissions(EditActivity.this, permissions, 1);
		} else {
			mLocationClient.restart();
		}
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
		switch (requestCode){
			//编辑kind分类返回
			case 88:
				freshenKindList();
				break;
			default:
				break;
		}
	}
	private void freshenKindList(){
		kindList.clear();
		kindList.add(new Kind("未分类"));
		kindList.addAll(DataSupport.order("name asc").find(Kind.class));
		kindList.add(new Kind("编辑分类"));
		spinnerView.setText(kindList.get(0).getName());
		mySpinner.inKindAdapter.notifyDataSetChanged();
	}
	
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		//图片读取权限
		PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
		PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
		
		//百度地图权限
		switch (requestCode) {
			case 1:
				if (grantResults.length > 0) {
					for (int result : grantResults) {
						if (result != PackageManager.PERMISSION_GRANTED) {
							Toast.makeText(this, "拒绝权限可能导致程序无法运行", Toast.LENGTH_SHORT).show();
							return;
						}
					}
					mLocationClient.restart();
				} else {
					Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
					finish();
				}
				break;
			default:
		}
	}
	
	private void configureLocation(){
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		//可选，设置定位模式，默认高精度
		//LocationMode.Hight_Accuracy：高精度；
		//LocationMode. Battery_Saving：低功耗；
		//LocationMode. Device_Sensors：仅使用设备；
		
		option.setCoorType("bd09ll");
		//可选，设置返回经纬度坐标类型，默认gcj02
		//gcj02：国测局坐标；
		//bd09ll：百度经纬度坐标；
		//bd09：百度墨卡托坐标；
		//海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
		
		option.setScanSpan( 10 * 60 * 1000);
		//可选，设置发起定位请求的间隔，int类型，单位ms
		//如果设置为0，则代表单次定位，即仅定位一次，默认为0
		//如果设置非0，需设置1000ms以上才有效
		
		option.setOpenGps(true);
		//可选，设置是否使用gps，默认false
		//使用高精度和仅用设备两种定位模式的，参数必须设置为true
		
		option.setLocationNotify(false);
		//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
		
		option.setIgnoreKillProcess(false);
		//可选，定位SDK内部是一个service，并放到了独立进程。
		//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
		
		option.SetIgnoreCacheException(false);
		//可选，设置是否收集Crash信息，默认收集，即参数为false
		
		option.setWifiCacheTimeOut(5*60*1000);
		//可选，7.2版本新增能力
		//如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
		
		option.setEnableSimulateGps(false);
		//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
		
		mLocationClient.setLocOption(option);
		//mLocationClient为第二步初始化过的LocationClient对象
		//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
		//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
		
		option.setIsNeedAddress(true);
		//可选，是否需要地址信息，默认为不需要，即参数为false
		//如果开发者需要获得当前点的地址信息，此处必须为true
		
		mLocationClient.setLocOption(option);
		//mLocationClient为第二步初始化过的LocationClient对象
		//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
		//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationClient.stop();
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
		String iconPath = result.getImage().getOriginalPath();
		articleInsertImgList.add(iconPath);
		articleInsertImgAdapter.notifyDataSetChanged();
		
		//Toast显示图片路径
		Log.e(TAG, "takeSuccess: "+ "imagePath:" + iconPath);
		//Google Glide库 用于加载图片资源
	}
	
	@Override
	public void takeFail(TResult result,String msg) {
		Log.i(TAG, "takeFail:" + msg);
	}
	
	@Override
	public void takeCancel() {
		Log.i(TAG, getResources().getString(R.string.msg_operation_canceled));
	}
	
	@Override
	public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
		PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
		if(PermissionManager.TPermissionType.WAIT.equals(type)){
			this.invokeParam=invokeParam;
		}
		return type;
	}
	
	private void showMenu(int position){
		hideMenu();
		//先收回软键盘
		View viewFocus = this.getCurrentFocus();
		if (viewFocus != null) {
			InputMethodManager imManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imManager.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
		}
		switch (position){
			case -1:
				//Toast.makeText(this, "收回", Toast.LENGTH_SHORT).show();
				editChoice.setVisibility(View.GONE);
				retractContain.setVisibility(View.GONE);
				break;
			case 0:
				editChoice.setVisibility(View.VISIBLE);
				retractContain.setVisibility(View.VISIBLE);
				hideMenu();
				fontContain.setVisibility(View.VISIBLE);
				//显示字体编辑菜单
				break;
			case 1:
				editChoice.setVisibility(View.VISIBLE);
				retractContain.setVisibility(View.VISIBLE);
				hideMenu();
				backgroundContain.setVisibility(View.VISIBLE);
				//显示背景选择菜单
				break;
			case 2:
				editChoice.setVisibility(View.VISIBLE);
				retractContain.setVisibility(View.VISIBLE);
				hideMenu();
				imgContain.setVisibility(View.VISIBLE);
				//显示图片选择菜单
				break;
			case 3:
				editChoice.setVisibility(View.VISIBLE);
				retractContain.setVisibility(View.VISIBLE);
				hideMenu();
				positionContain.setVisibility(View.VISIBLE);
				//显示定位选择菜单
				break;
			case 4:
				editChoice.setVisibility(View.VISIBLE);
				retractContain.setVisibility(View.VISIBLE);
				hideMenu();
				weatherContain.setVisibility(View.VISIBLE);
				//天气选择菜单
				break;
			default:
				break;
		}
	}
	
	//切换具体的菜单项
	private void hideMenu(){
		//隐藏所有菜单
		fontContain.setVisibility(View.GONE);
		backgroundContain.setVisibility(View.GONE);
		imgContain.setVisibility(View.GONE);
		positionContain.setVisibility(View.GONE);
		weatherContain.setVisibility(View.GONE);
	}
	
	//用于处理toolbar  //监听popupwindow取消 就是分类链表显示的下拉框 完成
	private PopupWindow.OnDismissListener dismissListener=new PopupWindow.OnDismissListener() {
		@Override
		public void onDismiss() {
			setTextImage(R.drawable.downblack);
		}
	};
	
	
	//用于处理toolbar  //给TextView右边设置图片 完成
	private void setTextImage(int imgId) {
		Drawable drawable = getResources().getDrawable(imgId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
		spinnerView.setCompoundDrawables(null, null, drawable, null);
	}
	
	//格式化时间
	private String stringData(){
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if("1".equals(mWay)){
			mWay ="周日";
		}else if("2".equals(mWay)){
			mWay ="周一";
		}else if("3".equals(mWay)){
			mWay ="周二";
		}else if("4".equals(mWay)){
			mWay ="周三";
		}else if("5".equals(mWay)){
			mWay ="周四";
		}else if("6".equals(mWay)){
			mWay ="周五";
		}else if("7".equals(mWay)){
			mWay ="周六";
		}
		String hour=c.get(Calendar.HOUR_OF_DAY)+"";
		String minute=c.get(Calendar.MINUTE)+"";
		return mYear + "." + mMonth + " " + mDay+" "+mWay +" "+ hour+":"+minute ;
	}
}
