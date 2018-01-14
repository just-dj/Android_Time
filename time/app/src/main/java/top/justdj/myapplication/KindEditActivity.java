package top.justdj.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.litepal.crud.DataSupport;
import top.justdj.myapplication.adapter.KindAdapter;
import top.justdj.myapplication.model.Kind;

import java.util.LinkedList;
import java.util.List;

public class KindEditActivity extends AppCompatActivity implements View.OnClickListener {
	
	private TextView returnKindEdit;
	private TextView complateKindEdit;
	
	private RecyclerView kindListRecyclerView;
	private List<Kind> kindList;
	private KindAdapter kindAdapter;
	
	private EditText addKindNow;
	private ImageView addKind;
	private static final String TAG = "KindEditActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kind_edit);
		
		//5.0推出了Material Design 实现全透明
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |     WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
		}
		
		initPara();
		
		kindAdapter.setOnItemClickListener(new KindAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Log.e(TAG, "onItemClick: " );
				DataSupport.deleteAll(Kind.class,"name=?",kindList.get(position).getName());
				kindList.clear();
				kindList.addAll(DataSupport.findAll(Kind.class));
				kindAdapter.notifyDataSetChanged();
			}
		});
	}
	
	private void initPara(){
		returnKindEdit = (TextView)findViewById(R.id.returnKindEdit);
			returnKindEdit.setOnClickListener(this);
		complateKindEdit = (TextView)findViewById(R.id.complateKindEdit);
			complateKindEdit.setOnClickListener(this);
		
		addKind = (ImageView)findViewById(R.id.kind_add);
			addKind.setOnClickListener(this);
		addKindNow = (EditText)findViewById(R.id.kindEditNow);
		
		kindList = new LinkedList <Kind>();
		kindList = DataSupport.findAll(Kind.class);
		kindListRecyclerView = (RecyclerView)findViewById(R.id.kind_list_recyclerView);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		kindListRecyclerView.setLayoutManager(layoutManager);
		kindAdapter = new KindAdapter(kindList);
		kindListRecyclerView.setAdapter(kindAdapter);
	}
	
	@Override
	public void onClick(View v) {
			Intent intent;
		switch (v.getId()){
			case R.id.returnKindEdit:
				intent = new Intent();
				intent.putExtra("returnData","main");
				setResult(RESULT_CANCELED,intent);
				finish();
				break;
			case R.id.complateKindEdit:
				intent = new Intent();
				intent.putExtra("returnData","main");
				setResult(RESULT_OK,intent);
				finish();
				break;
			case R.id.kind_add:
				if (addKindNow.getText()!=null && !addKindNow.getText().toString().equals("") &&!addKindNow.getText()
						.equals("全部") && !addKindNow.getText().equals("编辑分类")){
					boolean unique = true;
					for (Kind a:kindList){
						Log.e(TAG, "onClick: "+  addKindNow.getText() + 66 + a.getName());
						if (a.getName().equals(addKindNow.getText().toString())){
							Toast.makeText(this, "分类重复！", Toast.LENGTH_SHORT).show();
							unique = false;
						}
					}
					if (unique){
						Log.e(TAG, "onClick: ");
						Kind kind = new Kind(addKindNow.getText().toString());
						kind.save();
						kindList.clear();
						kindList.addAll(DataSupport.findAll(Kind.class));
						kindAdapter.notifyDataSetChanged();
					}
				}
				break;
		}
		
	}
}
