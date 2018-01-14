package top.justdj.myapplication.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baoyz.widget.PullRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.litepal.crud.DataSupport;
import top.justdj.myapplication.PublicArticleDetailActivity;
import top.justdj.myapplication.R;
import top.justdj.myapplication.adapter.ArticleAdapter;
import top.justdj.myapplication.adapter.PublicArticleAdapter;
import top.justdj.myapplication.model.Article;
import top.justdj.myapplication.model.PublicArticle;
import top.justdj.myapplication.model.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class PassFragment extends Fragment {
	
	private View view;
	private Gson gson;
	private OkHttpClient client;
	private String responseData = "";
	
	private PullRefreshLayout freshLayout;
	
	
	private List<PublicArticle> articleList ;
	
	private RecyclerView publicArticleRecycler;
		private PublicArticleAdapter publicArticleAdapter;
		
	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_pass_layout,container,false);
		
		initPara();
		
		freshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new ArticleTask().execute();
			}
		});
		
		publicArticleAdapter.setOnItemClickListener(new PublicArticleAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(getContext(), PublicArticleDetailActivity.class);
				intent.putExtra("articleId",articleList.get(position).getArticleId()+"");
				startActivityForResult(intent,60);
				
			}
		});
		
		return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode){
			//详情界面返回
			case 60:
				if (RESULT_OK == resultCode){
					articleList.clear();
					articleList.addAll(DataSupport.findAll(PublicArticle.class));
					publicArticleAdapter.notifyDataSetChanged();
				}else {
				
				}
				break;
			default:
				break;
		}
	}
	
	private void initPara(){
		
		articleList = DataSupport.order("articleId desc").find(PublicArticle.class);
		if (null == articleList){
			articleList = new LinkedList <>();
		}
		
		freshLayout = (PullRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
		
		gson  = new Gson();
		client = new OkHttpClient();
		
		publicArticleRecycler = (RecyclerView)view.findViewById(R.id.publicArticle_recyclerView);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		publicArticleRecycler.setLayoutManager(layoutManager);
		
		
		//articleList = new LinkedList <>();
		publicArticleAdapter = new PublicArticleAdapter(articleList,getContext());
		publicArticleRecycler.setAdapter(publicArticleAdapter);
	}
	
	
	public class ArticleTask extends AsyncTask<Void,Integer,Boolean> {
		
		public List<PublicArticle> list;
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			if ( list!=null ){
				DataSupport.deleteAll(PublicArticle.class);
				Log.e(TAG, "onProgressUpdate: 6666: +1"+ responseData);
				for (PublicArticle publicArticle:list){
					Log.e(TAG, "onProgressUpdate: 6666:       "+ publicArticle.getLook() );
						publicArticle.save();
				}
				
				if (list!=null&& !list.isEmpty())
					Log.e(TAG, "getList: 现在在线程内部 "+ list.get(0).getAccount() );
				articleList.clear();
				articleList.addAll(list);
				publicArticleAdapter.notifyDataSetChanged();
				// refresh complete
				freshLayout.setRefreshing(false);
			}
		}
		
		@Override
		protected Boolean doInBackground(Void... voids) {
			Log.e(TAG, "getList: 进来" );
			Request request = new Request.Builder()
					.url("http://www.justdj.top/android/php/getArticleList.php")
					.build();
			try{
				Response response = client.newCall(request).execute();
				responseData = response.body().string();
				
				list = gson.fromJson(responseData,new TypeToken<List<PublicArticle>>(){}.getType());
				publishProgress(0);
				
			}catch (Exception e){
				e.printStackTrace();
			}
			Log.e(TAG, "getList: 出来" );
			
			return true;
		}
	}
}
