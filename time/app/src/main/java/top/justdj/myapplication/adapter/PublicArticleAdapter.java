package top.justdj.myapplication.adapter;

import android.content.Context;
import android.print.PrinterId;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import top.justdj.myapplication.R;
import top.justdj.myapplication.fragment.PassFragment;
import top.justdj.myapplication.model.PublicArticle;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class PublicArticleAdapter extends RecyclerView.Adapter<PublicArticleAdapter.ViewHolder> {
	private Context context;
	
	private final int[] BG = new int[]
			{R.drawable.publicarticleitemborder1,
			R.drawable.publicarticleitemborder2,
			R.drawable.publicarticleitemborder3,
			R.drawable.publicarticleitemborder4,
			R.drawable.publicarticleitemborder5,
			R.drawable.publicarticleitemborder6,
			R.drawable.publicarticleitemborder7,
			R.drawable.publicarticleitemborder8,};
	
	private List<PublicArticle> articleList;
	
	public interface OnItemClickListener{
		void onItemClick(View view, int position);
	}
	
	private OnItemClickListener mOnItemClickListener;
	
	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
		this.mOnItemClickListener = mOnItemClickListener;
	}
	
	
	static class ViewHolder extends RecyclerView.ViewHolder{
		ImageView headImg;
		TextView name;
		TextView content;
		TextView time;
		TextView likeNum;
		ImageView likeImg;
		
		public ViewHolder(View view){
			super(view);
			headImg = (ImageView)view.findViewById(R.id.publicArticle_headImg);
			name = (TextView)view.findViewById(R.id.publicArticle_name);
			content = (TextView)view.findViewById(R.id.publicArticle_content);
			time = (TextView)view.findViewById(R.id.publicArticle_time);
			likeNum = (TextView)view.findViewById(R.id.publicArticle_likeNum);
			likeImg = (ImageView)view.findViewById(R.id.publicArticle_like);
		}
	}
	
	public PublicArticleAdapter(List<PublicArticle> publicArticleList, Context context){
		this.articleList = publicArticleList;
		this.context = context;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.publicarticle_item,parent,false);
		final ViewHolder holder = new ViewHolder(view);
		return holder;
	}
	
	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		PublicArticle publicArticle = articleList.get(position);
		Log.e(TAG, "onBindViewHolder: 666"+ publicArticle.getAccount() );
		
//		headImg = (ImageView)view.findViewById(R.id.publicArticle_headImg);
//
//		time = (TextView)view.findViewById(R.id.publicArticle_time);
//		likeImg = (ImageView)view.findViewById(R.id.publicArticle_like);
		
		
		holder.name.setText(publicArticle.getUserName());
		holder.content.setText(publicArticle.getContent());
		holder.likeNum.setText(publicArticle.getLikeNum()+ "");
		holder.itemView.setBackgroundResource(getRandomBg());
		holder.time.setText(getTimeInfo(publicArticle));
		if (publicArticle.getHeadUrl()!= null &&!publicArticle.getHeadUrl().equals("") )
			Picasso.with(context).load(publicArticle.getHeadUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.headImg);
		else
			holder.headImg.setImageResource(R.drawable.defaulthead);
		
//		if (publicArticle.getBgUrl()!=null&& !publicArticle.getBgUrl().equals("")){
//			Picasso.with(context).load(publicArticle.getBgUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.);
//		}else {
//			holder.headImg.setImageResource(R.drawable.defaulthead);
//		}
		
		holder.itemView.setPadding(getPx(context,14),getPx(context,10) ,getPx(context,3),getPx(context,6));
		
		
		if(mOnItemClickListener != null){
			//为ItemView设置监听器
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = holder.getLayoutPosition(); // 1
					mOnItemClickListener.onItemClick(holder.itemView,position); // 2
				}
			});
		}
		
		
	}
	
	public static int getPx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	private int getRandomBg(){
		Random rand = new Random();
		return BG[rand.nextInt(8)];
	}
	
	private String getTimeInfo(PublicArticle article){
		String result = "";
		long timeNow = new Date().getTime();
		long mid = timeNow - article.getTime();
		if (mid < 60 * 1000)
			result = "刚刚";
		else if(mid < 60 * 60 * 1000)
			result = mid / (60 * 1000) +"分钟前";
		else if(mid < 24 * 60 * 60 * 1000)
			result = mid /  (60 * 60 * 1000) + "小时前";
		else
			result = mid / (24 * 60 * 60 * 1000) + "天前";
		return result;
	}
	
	@Override
	public int getItemCount() {
		return articleList.size();
	}
}
