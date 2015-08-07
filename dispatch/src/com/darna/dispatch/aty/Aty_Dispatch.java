package com.darna.dispatch.aty;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.darna.dispatch.Config;
import com.darna.dispatch.R;
import com.darna.dispatch.bean.TodayOverView;
import com.darna.dispatch.net.Net_DeliveryTodayOverview;

public class Aty_Dispatch extends Activity {
	
	ListView mListView;
	OverViewAdapter overViewAdapter;
	TextView tv_deliveryer, tv_ordernum, tv_ordermoney;
	
	//上次按下返回键的系统时间  
    private long lastBackTime = 0;  
    //当前按下返回键的系统时间  
    private long currentBackTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_dispatch);
		
		mListView = (ListView) findViewById(android.R.id.list);
		overViewAdapter = new OverViewAdapter();
		mListView.setAdapter(overViewAdapter);
		
		tv_deliveryer = (TextView) findViewById(R.id.tv_deliveryer);
		tv_ordernum = (TextView) findViewById(R.id.tv_ordernum);
		tv_ordermoney = (TextView) findViewById(R.id.tv_ordermoney);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String overview = overViewAdapter.getItem(position);
				if (overview.equals("今日订单")) {
					startActivity(new Intent(Aty_Dispatch.this, Aty_TodayOrder.class));
					overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
				}else {
					startActivity(new Intent(Aty_Dispatch.this, Aty_MonthOverView.class));
					overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
				}
			}
		});
		
		init();
		
		getData();
		
	}
	
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        //捕获返回键按下的事件  
        if(keyCode == KeyEvent.KEYCODE_BACK){  
            //获取当前系统时间的毫秒数  
            currentBackTime = System.currentTimeMillis();  
            //比较上次按下返回键和当前按下返回键的时间差，如果大于2秒，则提示再按一次退出  
            if(currentBackTime - lastBackTime > 2 * 1000){  
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();  
                lastBackTime = currentBackTime;  
            }else{ //如果两次按下的时间差小于2秒，则退出程序  
                finish();  
            }  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }
	
	@Override
	protected void onStart() {
		super.onStart();
		init();
	}
	
	private void init() {
		new Net_DeliveryTodayOverview(Config.getCachedJobId(this), new Net_DeliveryTodayOverview.SuccessCallback() {
			@Override
			public void onSuccess(TodayOverView todayOverView) {
				inflate(todayOverView);
			}
		}, new Net_DeliveryTodayOverview.FailCallback() {
			@Override
			public void onFail(int errorCode) {
				Toast.makeText(Aty_Dispatch.this, "请求服务器超时", Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void inflate(TodayOverView data) {
		tv_deliveryer.setText(data.getName());
		tv_ordernum.setText(data.getOrder_cnt());
		tv_ordermoney.setText(data.getPayment());
	}

	private void getData() {
		String[] overView = getResources().getStringArray(R.array.overview);
		List<String> overviewList = new ArrayList<String>();
		for (int i = 0; i < overView.length; i++) {
			overviewList.add(overView[i]);
		}
		overViewAdapter.add(overviewList);
	}

	public class OverViewAdapter extends BaseAdapter{
		
		OverViewCell overViewCell;
		List<String> mOverView = new ArrayList<String>();
		
		public void add(List<String> overviews){
			clear();
			mOverView = overviews;
			notifyDataSetChanged();
		}
		
		public void clear(){
			mOverView.clear();
		}

		@Override
		public int getCount() {
			return mOverView.size();
		}

		@Override
		public String getItem(int position) {
			return mOverView.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_over_view, parent, false);
				overViewCell = new OverViewCell();
				overViewCell.iv_ordericon = (ImageView) convertView.findViewById(R.id.iv_ordericon);
				overViewCell.tv_order = (TextView) convertView.findViewById(R.id.tv_order);
				convertView.setTag(overViewCell);
			}else {
				overViewCell = (OverViewCell) convertView.getTag();
			}
			String overview = getItem(position);
			overViewCell.tv_order.setText(overview);
			if (overview.equals("今日订单")) {
				overViewCell.iv_ordericon.setImageResource(R.drawable.order);
			}else {
				overViewCell.iv_ordericon.setImageResource(R.drawable.historyorder);
			}
			
			return convertView;
		}
		
		public class OverViewCell{
			ImageView iv_ordericon;
			TextView tv_order;
		}
		
	}
	
}
