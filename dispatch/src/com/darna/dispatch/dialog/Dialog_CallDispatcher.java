package com.darna.dispatch.dialog;

import java.util.ArrayList;
import java.util.List;

import com.darna.dispatch.R;
import com.darna.dispatch.aty.Aty_Dispatch;
import com.darna.dispatch.aty.Aty_TodayOrder;
import com.darna.dispatch.bean.DispatcherContact;
import com.darna.dispatch.net.Net_DispatcherContact;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Dialog_CallDispatcher extends Activity {
	RelativeLayout rl_dialogcall;
	LinearLayout ll_dialogcall;
	DispatcherAdapter dispatcherAdapter;
	ListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_call);
		findViewById();
		getData();
	}
	
	private void getData() {
		new Net_DispatcherContact(new Net_DispatcherContact.SuccessCallback() {
			@Override
			public void onSuccess(List<DispatcherContact> dispatcherContacts) {
				dispatcherAdapter.add(dispatcherContacts);
			}
		}, new Net_DispatcherContact.FailCallback() {
			@Override
			public void onFail(int errorCode) {
				Toast.makeText(Dialog_CallDispatcher.this, "请求服务器超时", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void findViewById() {
		rl_dialogcall = (RelativeLayout) findViewById(R.id.rl_dialogcall);
		ll_dialogcall = (LinearLayout) findViewById(R.id.ll_dialogcall);
		mListView = (ListView) findViewById(android.R.id.list);
		dispatcherAdapter = new DispatcherAdapter();
		mListView.setAdapter(dispatcherAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				DispatcherContact dispatcherContact = dispatcherAdapter.getItem(position);
				Intent intent=new Intent("android.intent.action.CALL",Uri.parse("tel:"+dispatcherContact.getTel()));
				startActivity(intent);
				finish();
			}
		});
		
		rl_dialogcall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		ll_dialogcall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}
	
	public class DispatcherAdapter extends BaseAdapter{
		
		List<DispatcherContact> dispatcherContacts = new ArrayList<DispatcherContact>();
		ViewCell viewHolder;

		public void add(List<DispatcherContact> data){
			clear();
			dispatcherContacts = data;
			notifyDataSetChanged();
		}
		
		public void clear(){
			dispatcherContacts.clear();
		}
		
		@Override
		public int getCount() {
			return dispatcherContacts.size();
		}

		@Override
		public DispatcherContact getItem(int position) {
			return dispatcherContacts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_call, parent, false);
				viewHolder = new ViewCell();
				viewHolder.tv_dispatcher = (TextView) convertView.findViewById(R.id.tv_dispatcher);
				viewHolder.tv_dispatcherphone = (TextView) convertView.findViewById(R.id.tv_dispatcherphone);
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewCell) convertView.getTag();
			}
			DispatcherContact dispatcherContact = getItem(position);
			viewHolder.tv_dispatcher.setText(dispatcherContact.getName());
			viewHolder.tv_dispatcherphone.setText(dispatcherContact.getTel());
			return convertView;
		}
		
		public class ViewCell{
			TextView tv_dispatcher, tv_dispatcherphone;
		}
		
	}
}
