package top.justdj.myapplication.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import top.justdj.myapplication.R;

import java.util.LinkedList;
import java.util.List;

public class FontColorAdapter extends RecyclerView.Adapter<FontColorAdapter.ViewHolder> {
	
	private List<String> colorList;
	private List<Boolean> isSelected;
	private TextView fontColorNow;
	
	public interface OnItemClickListener{
		void onItemClick(View view,int position);
	}
	
//	public interface OnItemLongClickListener{
//		void onItemLongClick(View view,int position);
//	}
	
	private OnItemClickListener mOnItemClickListener;
//	private OnItemLongClickListener mOnItemLongClickListener;
	
	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
		this.mOnItemClickListener = mOnItemClickListener;
	}
	
//	public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
//		this.mOnItemLongClickListener = mOnItemLongClickListener;
//	}
//
	
	
	public void select(int index){
		for(int i = 0;i < isSelected.size();++i){
			isSelected.set(i,false);
		}
		isSelected.set(index,true);
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder{
		LinearLayout fontColor;
		ImageView isSelect;
		
		public ViewHolder(View view){
			super(view);
			fontColor = (LinearLayout)view.findViewById(R.id.fontColor);
			isSelect = (ImageView) view.findViewById(R.id.isSelect);
		}
	}
	
	public FontColorAdapter(List<String> colorList, TextView colorNow){
		this.colorList = colorList;
		fontColorNow = colorNow;
		isSelected = new LinkedList <>();
		for (String a:colorList){
			if (!a.equals("#30393E"))
				isSelected.add(false);
			else
				isSelected.add(true);
		}
		
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fontcolor_item,parent,false);
		final ViewHolder holder = new ViewHolder(view);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fontColorNow.setBackgroundColor(Color.parseColor(colorList.get(holder.getAdapterPosition())));
				//Toast.makeText(view.getContext(), "点击了"+holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
			}
		});
		return holder;
	}
	
	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		String color = colorList.get(position);
		holder.fontColor.setBackgroundColor(Color.parseColor(color));
		if (isSelected.get(position))
			holder.isSelect.setVisibility(View.VISIBLE);
		else
			holder.isSelect.setVisibility(View.INVISIBLE);
		
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
		return colorList.size();
	}
}
