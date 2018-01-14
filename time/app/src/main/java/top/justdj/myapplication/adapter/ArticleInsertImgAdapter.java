package top.justdj.myapplication.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import top.justdj.myapplication.R;

import java.util.List;

import static android.content.ContentValues.TAG;

public class ArticleInsertImgAdapter  extends RecyclerView.Adapter<ArticleInsertImgAdapter.ViewHolder> {
	
	private List<String> imgPathList;
	
	public interface OnItemClickListener{
		void onItemClick(View view, int position);
	}
	
	
	private OnItemClickListener mOnItemClickListener;
	
	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
		this.mOnItemClickListener = mOnItemClickListener;
	}
	
	
	static class ViewHolder extends RecyclerView.ViewHolder{
		ImageView selectImg;
		ImageView delSelectImg;
		
		public ViewHolder(View view){
			super(view);
			selectImg = (ImageView) view.findViewById(R.id.selectImg);
			delSelectImg = (ImageView)view.findViewById(R.id.delSelectImg);
		}
	}
	
	public ArticleInsertImgAdapter(List<String> colorList){
		this.imgPathList = colorList;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articleinsertimg_item,parent,false);
		final ViewHolder holder = new ViewHolder(view);
//		view.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				fontColorNow.setBackgroundColor(Color.parseColor(colorList.get(holder.getAdapterPosition())));
//				//Toast.makeText(view.getContext(), "点击了"+holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
//			}
//		});
		return holder;
	}
	
	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		Log.e(TAG, "onBindViewHolder: " + imgPathList.get(position));
		String url = imgPathList.get(position);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 6;
		Bitmap bitmap = BitmapFactory.decodeFile(url,options);
		holder.selectImg.setImageBitmap(bitmap);
//		holder.delSelectImg.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//			}
//		});
		
		if(mOnItemClickListener != null){
			//为ItemView设置监听器
			holder.itemView.findViewById(R.id.delSelectImg).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = holder.getLayoutPosition(); // 1
					mOnItemClickListener.onItemClick(holder.itemView.findViewById(R.id.delSelectImg),position); // 2
				}
			});
		}
	}
	
	@Override
	public int getItemCount() {
		return imgPathList.size();
	}
}
