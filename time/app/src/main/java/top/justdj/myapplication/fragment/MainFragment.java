package top.justdj.myapplication.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import top.justdj.myapplication.*;
import top.justdj.myapplication.adapter.ArticleAdapter;
import top.justdj.myapplication.adapter.InKindAdapter;
import top.justdj.myapplication.model.Article;
import top.justdj.myapplication.model.Kind;
import top.justdj.myapplication.model.User;
import top.justdj.myapplication.myspinner.MyOwnSpinner;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment implements View.OnClickListener{
	
	private User user;
	
	private View view;
	
	private Toolbar toolbar;
	//搜索按钮
	private ImageView search;
	//编辑按钮
	private ImageView edit;
	//用textView实现的spinner
	private TextView spinnerView;
	
	//点击分类链表显示的下拉框
	private MyOwnSpinner mySpinner;
	
	//分类链表
	private List<Kind> kindList;
	//文章列表
	private RecyclerView recyclerView;
	private List<Article> articleList;
	private ArticleAdapter articleAdapter;
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_main_layout,container,false);
		
		LitePal.getDatabase();
		//初始化参数
		initPara();

		//分类列表点击事件

		mySpinner.inKindAdapter.setOnItemClickListener(new InKindAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				mySpinner.dismiss();
				if (kindList.get(position).getName().equals("编辑分类")){
					Intent intent = new Intent(getContext(),KindEditActivity.class);
					startActivityForResult(intent,99);
					//跳转到编辑界面
				}else {
					//数据库查询类型
					spinnerView.setText(kindList.get(position).getName());
					mySpinner.inKindAdapter.check(position);
					mySpinner.inKindAdapter.notifyDataSetChanged();
					String kind = kindList.get(position).getName();

					articleList.clear();
					if(kind.equals("全部")){
						articleList.addAll(DataSupport.order("id desc").find(Article.class));
					}else{
						articleList.addAll(DataSupport.where("kind=?",kind).order("id desc").find(Article.class));
					}
					articleAdapter.notifyDataSetChanged();

					Toast.makeText(getContext(), "点击了:" + kindList.get(position).getName(),Toast.LENGTH_LONG).show();
				}
			}
		});
		//用于处理toolbar //监听popupwindow取消 就是分类链表显示的下拉框 完成
		mySpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				setTextImage(R.drawable.down);
			}
		});

		articleAdapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent intent   = new Intent(getContext(),MyArticleDetailActivity.class);
				intent.putExtra("articleId",articleList.get(position).getId());
				startActivityForResult(intent,98);
				Toast.makeText(getContext(), articleList.get(position).getId()+"", Toast.LENGTH_SHORT).show();
			}
		});
		
//		inKindAdapter.notifyDataSetChanged();

//		for (int i = 0;i< 10;++i){
//			Article article = new Article();
//			article.setContent(i + "昨天剪了剪头发");
//			article.setUserId("123456");
//			article.setAuthorId("123456");
//			article.setDate(stringData());
//			article.setToDefault("look");
//			article.setToDefault("like");
//			article.setToDefault("store");
//			article.setPosition("浙江省杭州市余杭区");
//			article.setToDefault("kind");
//			article.setFontColor("#30393E");
//			article.setFontSize(17);
//			article.save();
//		}
//	DataSupport.deleteAll(Article.class,"like=?","0");
//		DataSupport.deleteAll(Kind.class,"id>=?","0");
		
		
		
		
		
		
		return view;
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()){
			case R.id.search:
				Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
				break;
			case R.id.edit:
				Toast.makeText(getActivity(), "edit", Toast.LENGTH_SHORT).show();
				intent = new Intent(getActivity(),EditActivity.class);
				startActivityForResult(intent,100);
				break;
			//显示PopupWindow 就是分类链表显示的下拉框 完成
			case R.id.myspinner:
				mySpinner.setWidth(spinnerView.getWidth());
				mySpinner.showAsDropDown(spinnerView);
				setTextImage(R.drawable.up);
				break;
			default:
				break;
		}
	}

	//	初始化变量
	private void initPara() {
		List<User> list =  DataSupport.where("isLogin=?","1").find(User.class);
		if (list!= null && !list.isEmpty()){
			user  = list.get(0);
		}
		toolbar = (Toolbar) view.findViewById(R.id.toolbar);
		//初始化分类链表
		kindList = new ArrayList<Kind>();
		kindList.add(new Kind("全部"));
		kindList.add(new Kind("未分类"));
		kindList.addAll(DataSupport.order("name asc").find(Kind.class));
		kindList.add(new Kind("编辑分类"));

		//toolbar上的三个按钮
		search = (ImageView)view.findViewById(R.id.search);
		search.setOnClickListener(this);
		edit = (ImageView)view.findViewById(R.id.edit) ;
		edit.setOnClickListener(this);
		spinnerView = (TextView)view.findViewById(R.id.myspinner);
		spinnerView.setOnClickListener(this);

		//点击分类链表显示的下拉框
		mySpinner = new MyOwnSpinner (getContext(),kindList);
		spinnerView.setText(kindList.get(0).getName());
		mySpinner.inKindAdapter.check(0);
		setTextImage(R.drawable.down);

		recyclerView = (RecyclerView)view.findViewById(R.id.article_list);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);
		articleList = DataSupport.where("userId=?",user.getAccount()).order("id desc").find(Article.class);
		articleAdapter = new ArticleAdapter(articleList);
		recyclerView.setAdapter(articleAdapter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode){
			//编辑文章完成
			case 100:
				if (RESULT_OK == resultCode){
					articleList.clear();
					articleList.addAll(DataSupport.where("userId=?",user.getAccount()).order("id desc").find(Article.class));
					articleAdapter.notifyDataSetChanged();
					freshenKindList();
					spinnerView.setText(kindList.get(0).getName());
					mySpinner.inKindAdapter.check(0);
				}else {
					freshenKindList();
				}
				break;
			//编辑kind分类返回
			case 99:
				freshenKindList();
				break;

			//查看文章详细信息页面返回
			case 98:
				articleList.clear();
				articleList.addAll(DataSupport.where("userId=?",user.getAccount()).order("id desc").find(Article.class));
				articleAdapter.notifyDataSetChanged();
				freshenKindList();
				break;
			default:
				break;
		}
	}

	private void freshenKindList(){
		kindList.clear();
		kindList.add(new Kind("全部"));
		kindList.add(new Kind("未分类"));
		kindList.addAll(DataSupport.order("name asc").find(Kind.class));
		kindList.add(new Kind("编辑分类"));
//		spinnerView.setText(kindList.get(0).getName());
//		mySpinner.inKindAdapter.check(0);
		mySpinner.inKindAdapter.notifyDataSetChanged();
	}

	//用于处理toolbar  //给TextView右边设置图片 完成
	private void setTextImage(int imgId) {
		Drawable drawable = getResources().getDrawable(imgId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
		spinnerView.setCompoundDrawables(null, null, drawable, null);
	}
	
}
