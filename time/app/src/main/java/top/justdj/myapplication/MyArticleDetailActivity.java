package top.justdj.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import org.litepal.crud.DataSupport;
import top.justdj.myapplication.adapter.ArticleInsertImgAdapter;
import top.justdj.myapplication.adapter.ArtileDetailInsertImgAdapter;
import top.justdj.myapplication.model.Article;
import top.justdj.myapplication.model.User;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MyArticleDetailActivity extends AppCompatActivity implements View.OnClickListener{
	
	private LinearLayout container;
	private Toolbar toolbar;
	private LinearLayout articleDetail_return;
	private TextView articleDetail_kind;
	
	private ImageView articleDetail_del;
	private ImageView articleDetail_edit;
	private ImageView article_go_left;
	private ImageView article_go_right;
	
	private TextView articleDetail_year;
	private TextView articleDetail_hour;
	private TextView articleDetail_week;
	
	private ImageView articleDetail_weather;
	
	private TextView articleDetail_position;
	
	private  TextView articleDetail_content;
	private RecyclerView articleDetail_insertImgRecyclerView;
	private ArtileDetailInsertImgAdapter articleInsertImgAdapter;
		private List<String> insertImgUrlList;
	
	private Article article;
	
	private User user;
	
	private static final String TAG = "MyArticleDetailActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_article_detail);
		
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
		int id = intent.getIntExtra("articleId",0);
		List<Article> midList = DataSupport.findAll(Article.class,id);
		//初始化参数
		article = midList.get(0);
		initPara();
		
		//设置toolbar
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		articleInsertImgAdapter.setOnItemClickListener(new ArtileDetailInsertImgAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Toast.makeText(MyArticleDetailActivity.this, "点击了图片", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	private void initPara(){
		List<User> list =  DataSupport.where("isLogin=?","1").find(User.class);
		if (list!= null && !list.isEmpty()){
			user  = list.get(0);
		}
		
//控件
		article_go_left = (ImageView)findViewById(R.id.article_go_left);
			article_go_left.setOnClickListener(this);
		article_go_right = (ImageView)findViewById(R.id.article_go_right) ;
			article_go_right.setOnClickListener(this);
		//背景
		container = (LinearLayout)findViewById(R.id.container);
			//container.setBackgroundResource(article.getBackground());
		//标题栏
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		articleDetail_return = (LinearLayout) findViewById(R.id.articleDetail_return);
			articleDetail_return.setOnClickListener(this);
		articleDetail_del = (ImageView)findViewById(R.id.articleDetail_del);
			articleDetail_del.setOnClickListener(this);
		
		articleDetail_kind = (TextView)findViewById(R.id.articleDetail_kind);
		articleDetail_year = (TextView)findViewById(R.id.articleDetail_year);
		articleDetail_hour = (TextView)findViewById(R.id.articleDetail_hour);
		articleDetail_week = (TextView)findViewById(R.id.articleDetail_week);
		articleDetail_weather = (ImageView) findViewById(R.id.articleDetail_weather);
		articleDetail_position = (TextView) findViewById(R.id.articleDetail_position);
		articleDetail_content = (TextView)findViewById(R.id.articleDetail_content);
			articleDetail_content.setMovementMethod(new ScrollingMovementMethod());
		articleDetail_insertImgRecyclerView = (RecyclerView)findViewById(R.id.articleDetail_insertImg);
		StaggeredGridLayoutManager insertImgLayoutManager = new StaggeredGridLayoutManager(2,
				StaggeredGridLayoutManager.VERTICAL);
		articleDetail_insertImgRecyclerView.setLayoutManager(insertImgLayoutManager);
		insertImgUrlList = new LinkedList <>(Arrays.asList(article.getImg().split(" a1b2c3g7as78 ")));
		articleInsertImgAdapter = new ArtileDetailInsertImgAdapter(insertImgUrlList);
		articleDetail_insertImgRecyclerView.setAdapter(articleInsertImgAdapter);
		
		frushData();
	}
	
	private void frushData(){
		insertImgUrlList.clear();
		insertImgUrlList.addAll(Arrays.asList(article.getImg().split(" a1b2c3g7as78 ")));
		articleInsertImgAdapter.notifyDataSetChanged();
		
		container.setBackgroundResource(article.getBackground());
		articleDetail_kind.setText(article.getKind());
		String[] time = article.getDate().split(" ");
		String day = time[0] +"."+ time[1];
		String week = time[2];
		String hour = time[3];
		articleDetail_year.setText(day);
		articleDetail_week.setText(week);
		articleDetail_hour.setText(hour);
		
		articleDetail_weather.setBackgroundResource(article.getWeather());
		articleDetail_position.setText(article.getPosition());
		articleDetail_content.setText(article.getContent());
		articleDetail_content.scrollTo(0,0);
		Log.e(TAG, "frushData: "+article.getContent() );
		articleDetail_content.setTextSize(article.getFontSize());
		articleDetail_content.setTextColor(Color.parseColor(article.getFontColor()));
		
		if (insertImgUrlList.get(0)!=null){
			if (insertImgUrlList.get(0).equals("")){
				articleDetail_insertImgRecyclerView.setVisibility(View.GONE);
			}else {
				articleDetail_insertImgRecyclerView.setVisibility(View.VISIBLE);
			}
		}else {
			articleDetail_insertImgRecyclerView.setVisibility(View.GONE);
		}
	}
	
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		List<Article> midList = null;
		switch (v.getId()){
			case R.id.articleDetail_del:
				DataSupport.deleteAll(Article.class,"id=?",article.getId()+"");
				intent = new Intent();
				intent.putExtra("returnData","main");
				setResult(RESULT_OK,intent);
				finish();
				break;
			case R.id.article_go_left:
				midList = DataSupport.where("userId = ? and id < ?",user.getAccount(),article.getId()+"").find(Article.class);
				if (midList!=null && !midList.isEmpty()){
					article = midList.get(midList.size() -1);
					frushData();
				}else {
					Toast.makeText(this, "已经到底了", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.article_go_right:
				midList = DataSupport.where("id>?",article.getId()+"").find(Article.class);
				if (midList!=null && !midList.isEmpty()){
					article = midList.get(0);
					frushData();
				}else {
					Toast.makeText(this, "已经到底了", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.articleDetail_return:
				intent = new Intent();
				intent.putExtra("returnData","main");
				setResult(RESULT_CANCELED,intent);
				finish();
				finish();
				break;
			default:
				break;
				
		}
	}
}
