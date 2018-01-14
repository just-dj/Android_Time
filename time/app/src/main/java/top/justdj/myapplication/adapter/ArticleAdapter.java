package top.justdj.myapplication.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;
import top.justdj.myapplication.R;
import top.justdj.myapplication.model.Article;

import java.util.List;

import static android.content.ContentValues.TAG;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
	
	private List<Article> mArticleList;
	
	public  interface OnItemClickListener{
		void onItemClick(View view, int position);
	}
	private  ArticleAdapter.OnItemClickListener mOnItemClickListener;
	
	public void setOnItemClickListener( ArticleAdapter.OnItemClickListener mOnItemClickListener){
		this.mOnItemClickListener = mOnItemClickListener;
	}
	
	
	static class ViewHolder extends RecyclerView.ViewHolder{
		TextView date_day;
		TextView date_week;
		TextView date_year;
		TextView article_content;
		ImageView article_img;
		TextView date_hour;
		TextView article_position;
		
		public ViewHolder(View view){
			super(view);
			date_day = (TextView)view.findViewById(R.id.date_day);
			date_week= (TextView)view.findViewById(R.id.date_week);
			date_year = (TextView)view.findViewById(R.id.date_year);
			article_content = (TextView)view.findViewById(R.id.article_content);
			article_img = (ImageView)view.findViewById(R.id.article_img);
			date_hour = (TextView) view.findViewById(R.id.date_hour);
			article_position = (TextView)view.findViewById(R.id.article_position);
		}
	}
	
	public ArticleAdapter(List<Article> articleList){
		mArticleList = articleList;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item,parent,false);
		final ViewHolder holder = new ViewHolder(view);
//		view.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(view.getContext(), "点击了"+holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
////				holder.article_content.setBackgroundColor(Color.RED);
////				v.setBackgroundResource(R.color.transparent);//没有鼠标时背景透明
//			}
//		});
		return holder;
	}
	
	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		Article article = mArticleList.get(position);
		if (article.getImg() == null || article.getImg().equals("") ){
			holder.article_img.setVisibility(View.GONE);
			Log.e(TAG, "onBindViewHolder: "+ "消失" + article.getContent() );
		}else {
			Log.e(TAG, "onBindViewHolder: "+ "出现" + article.getContent() );
			String url = article.getImg().split(" a1b2c3g7as78 ")[0];
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			Bitmap bitmap = BitmapFactory.decodeFile(url,options);
			holder.article_img.setVisibility(View.VISIBLE);
			holder.article_img.setImageBitmap(bitmap);
		}
		String[] time = article.getDate().split(" ");
		holder.date_day.setText(time[1]);
		holder.date_week.setText(time[2]);
		holder.date_year.setText(time[0]);
		holder.article_content.setText(article.getContent());
		holder.date_hour.setText(time[3]);
		holder.article_position.setText(article.getPosition());
		
		if(mOnItemClickListener != null){
			//为ItemView设置监听器
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.e(TAG, "onClick: ");
					int position = holder.getLayoutPosition(); // 1
					mOnItemClickListener.onItemClick(holder.itemView,position); // 2
				}
			});
		}
	}
	
	@Override
	public int getItemCount() {
		return mArticleList.size();
	}
}
