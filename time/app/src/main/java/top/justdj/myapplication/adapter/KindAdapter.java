package top.justdj.myapplication.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import top.justdj.myapplication.R;
import top.justdj.myapplication.model.Kind;

import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class KindAdapter extends RecyclerView.Adapter<KindAdapter.ViewHolder> {
	
	private List<Kind> kindList;
	
	public interface OnItemClickListener{
		void onItemClick(View view, int position);
	}
	
	private OnItemClickListener mOnItemClickListener;
	
	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
		this.mOnItemClickListener = mOnItemClickListener;
	}
	
	
	static class ViewHolder extends RecyclerView.ViewHolder{
		ImageView delKind;
		TextView kindName;
		
		public ViewHolder(View view){
			super(view);
			kindName = (TextView)view.findViewById(R.id.kindName);
			delKind = (ImageView)view.findViewById(R.id.delKind);
		}
	}
	
	public KindAdapter(List<Kind> kindList){
		this.kindList = kindList;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kind_item,parent,false);
		final ViewHolder holder = new ViewHolder(view);
		return holder;
	}
	
	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.kindName.setText(kindList.get(position).getName());
		if(mOnItemClickListener != null){
			//为ItemView设置监听器
			holder.itemView.findViewById(R.id.delKind).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.e(TAG, "onClick: ");
					int position = holder.getLayoutPosition(); // 1
					mOnItemClickListener.onItemClick(holder.itemView.findViewById(R.id.delKind),position); // 2
				}
			});
		}
	}
	
	@Override
	public int getItemCount() {
		return kindList.size();
	}
}
