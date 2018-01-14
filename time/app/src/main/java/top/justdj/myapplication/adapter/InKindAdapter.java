package top.justdj.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import top.justdj.myapplication.R;
import top.justdj.myapplication.model.Kind;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class InKindAdapter extends RecyclerView.Adapter< InKindAdapter.ViewHolder> {
	
	private List<Kind> kindList;
	public List<Boolean> isSeleced;
	
	public   interface OnItemClickListener{
		void onItemClick(View view, int position);
	}
	private  InKindAdapter.OnItemClickListener mOnItemClickListener;
	
	public void setOnItemClickListener( InKindAdapter.OnItemClickListener mOnItemClickListener){
		this.mOnItemClickListener = mOnItemClickListener;
	}
	
	public void check(int index){
		for (int i = 0;i < isSeleced.size();++i)
			isSeleced.set(i,false);
		isSeleced.set(index,true);
	}
	
	
	static class ViewHolder extends RecyclerView.ViewHolder{
		TextView itemName;
		CheckBox itemImg;
		
		public ViewHolder(View view){
			super(view);
			itemName = (TextView) view.findViewById(R.id.item_name);
			itemImg = (CheckBox) view.findViewById(R.id.item_img);
		}
	}
	
	public InKindAdapter(List<Kind> kindList){
		this.kindList = kindList;
		isSeleced = new LinkedList <Boolean>();
		for (int i = 0;i<50;++i)
			isSeleced.add(false);
//		Log.e(TAG, "InKindAdapter: " + this.kindList.get(2).getName() );
	}
	
	@Override
	public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Log.e(TAG, "onCreateViewHolder: "+ 6666);
		 View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spiner_item_layout,parent,false);
		ViewHolder holder = new ViewHolder(view);
		return holder;
	}
	
	@Override
	public void onBindViewHolder(final  InKindAdapter.ViewHolder holder, int position) {
		Log.e(TAG, "onBindViewHolder: "+ "好奇怪" + position );
		holder.itemName.setText(kindList.get(position).getName());
		if (isSeleced.get(position)){
			holder.itemImg.setChecked(true);
		}else {
			holder.itemImg.setChecked(false);
		}
		if (kindList.get(position).getName().equals("编辑分类")){
			holder.itemImg.setVisibility(View.GONE);
			holder.itemName.setGravity(Gravity.CENTER);
		}else {
			holder.itemImg.setVisibility(View.VISIBLE);
			holder.itemName.setGravity(Gravity.LEFT);
		}
		
		
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
		return kindList.size();
	}
}
