package top.justdj.myapplication.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.liji.circleimageview.CircleImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import okhttp3.*;
import org.litepal.crud.DataSupport;
import top.justdj.myapplication.*;
import top.justdj.myapplication.model.Article;
import top.justdj.myapplication.model.User;
import top.justdj.myapplication.tool.UserGet;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class MeFragment extends Fragment implements View.OnClickListener{
	
	private View view;
	
	private ImageView myBackgroundImg;
	private CircleImageView myHeadImg;
	private TextView myName;
	private TextView mySign;
	
	private LinearLayout changeInfo;
	private TextView logOut;
	
	private User user;
	private String kind;
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_me_layout,container,false);
		
		initPara();
		
		return view;
	}
	
	private void initPara(){
		//注意这一步 应该是找到当前激活账号
		user = UserGet.getUser();
		
		myBackgroundImg  = (ImageView)view.findViewById(R.id.myBackgroundImg);
			myBackgroundImg.setOnClickListener(this);
			if (user.getBackgroundImgUrl()!= null &&!user.getBackgroundImgUrl().equals("") )
				Picasso.with(getContext()).load(user.getBackgroundImgUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).into(myBackgroundImg);
			else
				myBackgroundImg.setBackgroundColor(Color.parseColor("#81C7D4"));
		
		myHeadImg = (CircleImageView)view.findViewById(R.id.myHeadImg);
			myHeadImg.setOnClickListener(this);
			if (user.getHeadImgUrl()!= null &&!user.getHeadImgUrl().equals("") )
				Picasso.with(getContext()).load(user.getHeadImgUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).into(myHeadImg);
			else
				myHeadImg.setImageResource(R.drawable.defaulthead);
		
		logOut = (TextView)view.findViewById(R.id.logOut);
			logOut.setOnClickListener(this);
			
		myName = (TextView)view.findViewById(R.id.myName);
			myName.setText(user.getName());
		mySign = (TextView)view.findViewById(R.id.mySign);
			mySign.setText(user.getSign());
			
		changeInfo = (LinearLayout)view.findViewById(R.id.change_info);
			changeInfo.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()){
			case R.id.myBackgroundImg:
				intent = new Intent(getContext(), ChooseMyBGActivity.class);
				intent.putExtra("value","更换背景");
				startActivityForResult(intent,90);
				break;
				
			case R.id.myHeadImg:
				intent = new Intent(getContext(), ChooseMyBGActivity.class);
				intent.putExtra("value","更换头像");
				startActivityForResult(intent,89);
				break;
				
			case R.id.change_info:
				intent = new Intent(getContext(), ModifyInfoActivity.class);
				startActivityForResult(intent,88);
			break;
			
			case R.id.logOut:
				DataSupport.deleteAll(User.class);
				intent = new Intent(getContext(),LoginActivity.class);
				startActivity(intent);
				getActivity().finish();
				break;
			
			default:
				break;
		}
	}
	
	
	
	private String newPath = "";
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String path = "";
		User mid;
		switch (requestCode){
			//更换背景
			case 90:
				if (RESULT_OK == resultCode){
					path = data.getStringExtra("path");
					Log.e(TAG, "onActivityResult: 你选了" + path);
					mid = new User();
					newPath = "http://www.justdj.top/android/img/"+ user.getAccount()+"BackgroundImg.jpg";
					mid.setBackgroundImgUrl(newPath);
					mid.updateAll("account=?",UserGet.getUser().getAccount());
					kind = "background";
					new UpdateInfo().execute();
				}else {
					Log.e(TAG, "onActivityResult: 你没选" );
				}
				break;
			//更换头像
			case 89:
				if (RESULT_OK == resultCode){
					path = data.getStringExtra("path");
					Log.e(TAG, "onActivityResult: 你选了" + path);
					mid = new User();
					newPath = "http://www.justdj.top/android/img/"+ user.getAccount()+"HeadImg.jpg";
					mid.setHeadImgUrl(newPath);
					mid.updateAll("account=?",UserGet.getUser().getAccount());
					kind = "head";
					new UpdateInfo().execute();
				}else {
					Log.e(TAG, "onActivityResult: 你没选" );
				}
				break;
			//改名字和签名
			case 88:
				if (RESULT_OK == resultCode){
					List<User> list =  DataSupport.where("isLogin=?","1").find(User.class);
					if (list!= null && !list.isEmpty()){
						user  = list.get(0);
					}
					kind = "name";
					new UpdateInfo().execute();
				}else {
					Log.e(TAG, "onActivityResult: 你没选" );
				}
				break;
			default:
				break;
		}
	}
	
	private Bitmap getBitmap(String path){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(path,options);
		return bitmap;
	}
	
	private class UpdateInfo extends AsyncTask {
		private String responseData;
		
		@Override
		protected void onProgressUpdate(Object[] values) {
			Log.e(TAG, "run 回复的消息: "+responseData );
			user = UserGet.getUser();
			//成功上传以后更新
			if (kind.equals("name")){
				myName.setText(user.getName());
				mySign.setText(user.getSign());
			}else if (kind.equals("head")){
				if (user.getHeadImgUrl()!= null &&!user.getHeadImgUrl().equals("") )
					Picasso.with(getContext()).load(user.getHeadImgUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).into(myHeadImg);
				else
					myHeadImg.setImageResource(R.drawable.defaulthead);
			}else {
				if (user.getBackgroundImgUrl()!= null &&!user.getBackgroundImgUrl().equals("") )
					Picasso.with(getContext()).load(user.getBackgroundImgUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).into(myBackgroundImg);
				else
					myBackgroundImg.setBackgroundColor(Color.parseColor("#81C7D4"));
				kind = "background";
			}
		}
		
		@Override
		protected Object doInBackground(Object[] objects) {
			List<User> list =  DataSupport.where("isLogin=?","1").find(User.class);
			if (list!= null && !list.isEmpty()){
				user  = list.get(0);
			}
			//创建OkHttpClient对象
			OkHttpClient client = new OkHttpClient();
			//通过FormEncodingBuilder对象构造Post请求体
			RequestBody body = null;
			while (kind == null);
			if (kind.equals("name")){
				        body = new FormBody.Builder()
						        .add("kind",kind)
						.add("account",user.getAccount())
						.add("name",user.getName())
						.add("sign",user.getSign())
						.build();
			}else if (kind.equals("head")){
						body = new FormBody.Builder()
						.add("kind",kind)
						.add("headImgUrl",newPath)
						.add("account",user.getAccount())
						.build();
			}else  if (kind.equals("background")){
				Log.e(TAG, "doInBackground: "+"改背景了" + newPath );
						body = new FormBody.Builder()
						.add("kind",kind)
						.add("bgImgUrl",newPath)
						.add("account",user.getAccount())
						.build();
			}
			
			
			
			//通过请求地址和请求体构造Post请求对象Request
			Request request = new Request.Builder()
						.addHeader("Content-Type", "text/html")
						.addHeader("charset", "utf_8")//防止中文乱码
						.url("http://www.justdj.top/android/php/updateInfo.php")
						.post(body)
						.build();

			
			try{
				Response response = client.newCall(request).execute();
				
				responseData = response.body().string();
				
				publishProgress();
				
			}catch (Exception e){
				e.printStackTrace();
			}
			return null;
		}
	}
	
	
}
