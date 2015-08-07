package com.darna.dispatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.darna.dispatch.aty.Aty_Dispatch;
import com.darna.dispatch.bean.Dispatcher;
import com.darna.dispatch.net.Net_DeliveryManList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends InstrumentedActivity {
	
	ListView mListView;
	DispatcherAdapter dispatcherAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getJobId();
    } 

	private void init() {
		JPushInterface.setDebugMode(true);
		JPushInterface.init(getApplicationContext());
		JPushInterface.setAlias(this, Config.getCachedJobId(this), new TagAliasCallback() {
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				Log.d("TAG", "set alias result is :" + arg0);
			}
		});
	}

	private void getJobId() {
		String job_id = Config.getCachedJobId(this);
		if (job_id == null) {
			//本地没有job_id的缓存，需获取， 初始化ListView，填充数据
			initView();
		}else {
			//有缓存，直接登录到主界面
			goDispatch();
		}
	}
	
	private void initView() {
		setContentView(R.layout.activity_main);
		mListView = (ListView) findViewById(android.R.id.list);
		dispatcherAdapter = new DispatcherAdapter();
		mListView.setAdapter(dispatcherAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Dispatcher dispatcher = dispatcherAdapter.getItem(position);
				Config.cacheJobId(MainActivity.this, dispatcher.getJob_id());
				goDispatch();
			}
		});
		
		getData();
		
	}
	
	private void goDispatch() {
		init();
		System.out.println(Config.getCachedJobId(this));
		startActivity(new Intent(MainActivity.this, Aty_Dispatch.class));
		finish();
	}

	private void getData() {
		new Net_DeliveryManList(new Net_DeliveryManList.SuccessCallback() {
			@Override
			public void onSuccess(List<Dispatcher> dispatchers) {
				dispatcherAdapter.add(dispatchers);
			}
		}, new Net_DeliveryManList.FailCallback() {
			@Override
			public void onFail(int errorCode) {
				Toast.makeText(MainActivity.this, "请求服务器失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public class DispatcherAdapter extends BaseAdapter{
		
		List<Dispatcher> mDispatchers = new ArrayList<Dispatcher>();
		Dispatchercell dispatchercell;
		
		public void add(List<Dispatcher> dispatchers){
			clear();
			mDispatchers = dispatchers;
			notifyDataSetChanged();
		}
		
		public void clear(){
			mDispatchers.clear();
		}

		@Override
		public int getCount() {
			return mDispatchers.size();
		}

		@Override
		public Dispatcher getItem(int position) {
			return mDispatchers.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_dispatcher, parent, false);
				dispatchercell = new Dispatchercell();
				dispatchercell.tv_dispatcher = (TextView) convertView.findViewById(R.id.tv_dispatcher);
				convertView.setTag(dispatchercell);
			}else {
				dispatchercell = (Dispatchercell) convertView.getTag();
			}
			Dispatcher dispatcher = getItem(position);
			dispatchercell.tv_dispatcher.setText(dispatcher.getName());
			return convertView;
		}
		
		public class Dispatchercell{
			TextView tv_dispatcher;
		}
		
	}
	
}
