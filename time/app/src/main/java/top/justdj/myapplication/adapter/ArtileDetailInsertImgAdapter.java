package top.justdj.myapplication.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import top.justdj.myapplication.R;

import java.util.List;

public class ArtileDetailInsertImgAdapter extends RecyclerView.Adapter<ArtileDetailInsertImgAdapter.ViewHolder> {
	
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
		
		public ViewHolder(View view){
			super(view);
			selectImg = (ImageView) view.findViewById(R.id.articleDetailSelectImg);
		}
	}
	
	public ArtileDetailInsertImgAdapter(List<String> colorList){
		this.imgPathList = colorList;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artiledetailinsetimg_item,parent,false);
		final ViewHolder holder = new ViewHolder(view);
		return holder;
	}
	
	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		String url = imgPathList.get(position);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap bitmap = BitmapFactory.decodeFile(url,options);
		holder.selectImg.setImageBitmap(bitmap);
		
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
	
	@Override
	public int getItemCount() {
		return imgPathList.size();
	}
}
