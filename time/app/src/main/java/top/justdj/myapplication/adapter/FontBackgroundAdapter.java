package top.justdj.myapplication.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import top.justdj.myapplication.R;

import java.util.LinkedList;
import java.util.List;

public class FontBackgroundAdapter  extends RecyclerView.Adapter<FontBackgroundAdapter.ViewHolder> {
	
	private List<Integer> backgroundImgList;
	
	public interface OnItemClickListener{
		void onItemClick(View view, int position);
	}
	
	
	private OnItemClickListener mOnItemClickListener;
	
	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
		this.mOnItemClickListener = mOnItemClickListener;
	}
	
	
	static class ViewHolder extends RecyclerView.ViewHolder{
		ImageView backgroundImg;
		
		public ViewHolder(View view){
			super(view);
			backgroundImg = (ImageView)view.findViewById(R.id.backgroundImg);
		}
	}
	
	public FontBackgroundAdapter(List<Integer> colorList){
		this.backgroundImgList = colorList;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fontbackground_item,parent,false);
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
		int id = backgroundImgList.get(position);
		holder.backgroundImg.setBackgroundResource(id);
		
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
//		if(mOnItemLongClickListener != null){
//			holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//				@Override
//				public boolean onLongClick(View v) {
//					int position = holder.getLayoutPosition();
//					mOnItemLongClickListener.onItemLongClick(holder.itemView,position);
//					//返回true 表示消耗了事件 事件不会继续传递
//					return true;
//				}
//			});
//		}
	}
	
	@Override
	public int getItemCount() {
		return backgroundImgList.size();
	}
}
