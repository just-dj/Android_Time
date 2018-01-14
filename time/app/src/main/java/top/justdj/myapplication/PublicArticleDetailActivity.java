package top.justdj.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.reflect.TypeToken;
import com.liji.circleimageview.CircleImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import okhttp3.*;
import org.litepal.crud.DataSupport;
import top.justdj.myapplication.model.PublicArticle;
import top.justdj.myapplication.model.User;
import top.justdj.myapplication.tool.UserGet;

import java.util.List;

import static org.litepal.LitePalApplication.getContext;

public class PublicArticleDetailActivity extends AppCompatActivity implements View.OnClickListener{
	
	private LinearLayout mainContainer;
	
	private ImageView background;
	private CircleImageView headImg;
	
	private TextView time;
	private TextView likeNum;
	private TextView lookNum;
	private TextView content;
	
	private ImageView goBack;
	private ImageView like;
		private boolean isLiked = false;
//	private ImageView store;
	
	private String articleId;
	private static final String TAG = "PublicArticleDetailActi";
	private PublicArticle article;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_article_detail);
		
		//5.0推出了Material Design 实现全透明
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
		}
		
		
		initPara();
	}
	
	@Override
	protected void onStart() {
		if (isNavigationBarShow())
			mainContainer.setPadding(0,0,0,getNavigationBarHeight());
		else
			mainContainer.setPadding(0,0,0,0);
		super.onStart();
	}
	
	private String kind = "";
	
	private void initPara(){
		Intent intent = getIntent();
		articleId = intent.getStringExtra("articleId");
		
		List<PublicArticle> list = DataSupport.where("articleId = ?",articleId+"").find(PublicArticle.class);
		if (list == null||list.isEmpty()){
			Log.e(TAG, "loadDate: 出错了2" );
			return;
		}
		article = list.get(0);
		Log.e(TAG, "initPara: 7777  "+ articleId + article.getArticleId());
		//查看记录加一
		PublicArticle mid =new  PublicArticle();
		mid.setLook(article.getLook() +1);
		mid.updateAll("articleId= ?",articleId+"");
		
		kind = "look";
		//服务器浏览数量加一
		new UpdateArticleInfo().execute();
		
		
		
		mainContainer = (LinearLayout)findViewById(R.id.publicArticleInfo_container);
		
		background = (ImageView)findViewById(R.id.publicArticleInfo_bg);
		headImg = (CircleImageView)findViewById(R.id.publicArticleInfo_headImg);
		time = (TextView)findViewById(R.id.publicArticleInfo_time);
		likeNum = (TextView)findViewById(R.id.publicArticleInfo_likeNum);
		lookNum = (TextView)findViewById(R.id.publicArticleInfo_lookNum);
		content = (TextView)findViewById(R.id.publicArticleInfo_content);
			content.setMovementMethod(new ScrollingMovementMethod());
		
		
		goBack = (ImageView)findViewById(R.id.publicArticleInfo_return);
			goBack.setOnClickListener(this);
		like = (ImageView)findViewById(R.id.publicArticleInfo_like);
			if (isContain(UserGet.getUser().getLikeList(),article.getArticleId()+"")){
				like.setBackgroundResource(R.drawable.liked);
				isLiked = true;
			}
			like.setOnClickListener(this);
		
			
		loadDate();
//		new GetArticleInfo().execute();
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()){
			case R.id.publicArticleInfo_return:
				intent = new Intent();
				intent.putExtra("returnData","pass");
				setResult(RESULT_OK,intent);
				finish();
				break;
			case R.id.publicArticleInfo_like:
				if (article!=null){
					User user  = UserGet.getUser();
					if (!isContain(user.getLikeList(),article.getArticleId()+"")&&!isLiked){
						isLiked = true;
						like.setBackgroundResource(R.drawable.liked);
						//增加账户喜欢列表 将文章的喜欢和那个赞数量加一
						String likeList = user.getLikeList();
						if (likeList == null)
							likeList = "";
						likeList += " " +articleId;
						User user1 = new User();
						user1.setLikeList(likeList);
						user1.updateAll("account = ?",user.getAccount());
						
						likeNum.setText(article.getLikeNum()+1+"");
						
						PublicArticle mid = new PublicArticle();
						mid.setLikeNum(article.getLikeNum()+1);
						mid.updateAll("articleId= ?",article.getArticleId()+"");
						
						kind  = "like";
						//服务器喜欢数量加一
						new UpdateArticleInfo().execute();
					}
					
				}
				break;
			default:
				break;
		}
	}
	
	private boolean isContain(String likeList,String id){
		if (likeList == null)
			return false;
		for (String mid:likeList.split(" ")){
			if (mid.equals(id))
				return true;
		}
		return false;
	}
	
	private void loadDate(){
		
		
		
		User user = UserGet.getUser();
		
		if (article.getBgUrl()!= null &&!article.getBgUrl().equals("") )
			Picasso.with(getContext()).load(article.getBgUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).into(background);
		else
			background.setBackgroundColor(Color.parseColor("#81C7D4"));
		
		if (article.getHeadUrl()!= null &&!article.getHeadUrl().equals("") )
			Picasso.with(getContext()).load(article.getHeadUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).into(headImg);
		else
			headImg.setImageResource(R.drawable.defaulthead);
		
		
		time.setText(article.getDate());
		
		likeNum.setText(article.getLikeNum()+ "");
		lookNum.setText((article.getLook()+ 1)+"");
		content.setText(article.getContent());
		
	}
	
	private class UpdateArticleInfo extends AsyncTask {
		private String responseData;
		private List<User> list;

		@Override
		protected void onProgressUpdate(Object[] values) {
			Log.e(TAG, "run 回复的消息: "+responseData );

		}

		@Override
		protected Object doInBackground(Object[] objects) {
			String requestKind = kind;
			//创建OkHttpClient对象
			OkHttpClient client = new OkHttpClient();
			//通过FormEncodingBuilder对象构造Post请求体
			RequestBody body;
			if (kind.equals("look")){
						body = new FormBody.Builder()
						.add("kind",requestKind)
						.add("id",article.getArticleId()+"")
						.build();
			}else {
				body = new FormBody.Builder()
						.add("kind",requestKind)
						.add("id",article.getArticleId()+"")
						.add("account",UserGet.getUser().getAccount())
						.add("likeList",UserGet.getUser().getLikeList())
						.build();
			}
			
			//通过请求地址和请求体构造Post请求对象Request
			Request request = new Request.Builder()
					.addHeader("Content-Type", "text/html")
					.addHeader("charset", "utf-8")//防止中文乱码
					.url("http://www.justdj.top/android/php/updateArticle.php")
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
	
	private int getNavigationBarHeight() {
		Resources resources = this.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
		int height = resources.getDimensionPixelSize(resourceId);
		Log.v("dbw", "Navi height:" + height);
		return height;
	}
	
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

