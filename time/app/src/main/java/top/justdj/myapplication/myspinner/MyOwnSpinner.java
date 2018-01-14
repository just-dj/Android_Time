package top.justdj.myapplication.myspinner;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;
import top.justdj.myapplication.KindEditActivity;
import top.justdj.myapplication.MainActivity;
import top.justdj.myapplication.R;
import top.justdj.myapplication.adapter.InKindAdapter;
import top.justdj.myapplication.model.Kind;

import java.util.List;

public class MyOwnSpinner extends PopupWindow {
	
	public RecyclerView mspinnerRecyclerView;
	public InKindAdapter inKindAdapter;
	public List<Kind> kindList;
	
	public MyOwnSpinner(final Context context, List<Kind> list) {
		super(context);
		kindList = list;
		View view = LayoutInflater.from(context).inflate(R.layout.spiner_window_layout, null);
		mspinnerRecyclerView = (RecyclerView) view.findViewById(R.id.spinnerRecyclerView);
		setContentView(view);
		setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);
		LinearLayoutManager spinnerLayoutManager  = new LinearLayoutManager(context);
		mspinnerRecyclerView.setLayoutManager(spinnerLayoutManager);
		inKindAdapter = new InKindAdapter(list);
		mspinnerRecyclerView.setAdapter(inKindAdapter);
		
		
		
	}
}