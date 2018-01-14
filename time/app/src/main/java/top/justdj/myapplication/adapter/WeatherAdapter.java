package top.justdj.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import top.justdj.myapplication.R;

import java.util.LinkedList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
	
	private List<Integer> weatherList;
	
	public interface OnItemClickListener{
		void onItemClick(View view, int position);
	}
	
	private OnItemClickListener mOnItemClickListener;
//	private OnItemLongClickListener mOnItemLongClickListener;
	
	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
		this.mOnItemClickListener = mOnItemClickListener;
	}
	
	
	static class ViewHolder extends RecyclerView.ViewHolder{
		LinearLayout weather;
		
		public ViewHolder(View view){
			super(view);
			weather = (LinearLayout)view.findViewById(R.id.fontColor);
		}
	}
	
	public WeatherAdapter(List<Integer> weatherList){
		this.weatherList = weatherList;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item,parent,false);
		final ViewHolder holder = new ViewHolder(view);
		return holder;
	}
	
	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		int id = weatherList.get(position);
		holder.weather.setBackgroundResource(id);
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
		return weatherList.size();
	}
}
