package com.darna.dispatch.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.darna.dispatch.Config;
import com.darna.dispatch.R;
import com.darna.dispatch.aty.Aty_OrderInfo;
import com.darna.dispatch.bean.Addr;
import com.darna.dispatch.bean.Order;
import com.darna.dispatch.net.Net_DeliveryOrderList;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Frg_Order extends Fragment {
	
	PullToRefreshListView mListView;
	OrderAdapter orderAdapter;
	String myMode, date;
	
	public Frg_Order(String mode, String date) {
		myMode = mode;
		this.date = date;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_order, container, false);
		findViewById(view);
		init();
		return view;
	}

	private void findViewById(View view) {
		mListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		orderAdapter = new OrderAdapter();
		mListView.setAdapter(orderAdapter);
		
		//View foot = LayoutInflater.from(getActivity()).inflate(R.layout.view_foot, null, false);
		//mListView.addFooterView(foot);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Order order = orderAdapter.getItem(position - 1);
				Intent intent = new Intent(getActivity(), Aty_OrderInfo.class);
				intent.putExtra(Config.KEY_SN, order.getSn());
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			}
		});
		
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
	
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
	
				// Do work to refresh the list here.
				init();
			}
		});
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		init();
	}
	
	private void init() {
		new Net_DeliveryOrderList(Config.getCachedJobId(getActivity()), myMode, date, "0", new Net_DeliveryOrderList.SuccessCallback() {
			@Override
			public void onSuccess(List<Order> orders) {
				orderAdapter.add(orders);
				mListView.onRefreshComplete(); 
			}
		}, new Net_DeliveryOrderList.FailCallback() {
			@Override
			public void onFail(int errorCode) {
				Toast.makeText(getActivity(), "服务器连接超时", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	public class OrderAdapter extends BaseAdapter{
		List<Order> mOrders = new ArrayList<Order>();
		OrderCell orderCell;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( 
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); 
		
		public void add(List<Order> orders){
			clear();
			mOrders = orders;
			notifyDataSetChanged();
		}
		
		public void clear(){
			mOrders.clear();
		}

		@Override
		public int getCount() {
			return mOrders.size();
		}

		@Override
		public Order getItem(int position) {
			return mOrders.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.item_order, parent, false);
				orderCell = new OrderCell();
				orderCell.tv_ordersn = (TextView) convertView.findViewById(R.id.tv_ordersn);
				orderCell.tv_ordertime = (TextView) convertView.findViewById(R.id.tv_ordertime);
				orderCell.iv_righticon = (ImageView) convertView.findViewById(R.id.iv_righticon);
				orderCell.tv_destination = (TextView) convertView.findViewById(R.id.tv_destination);
				orderCell.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
				orderCell.tv_phonenum = (TextView) convertView.findViewById(R.id.tv_phonenum);
				orderCell.customprice = (TextView) convertView.findViewById(R.id.customprice);
				orderCell.ll_shop = (LinearLayout) convertView.findViewById(R.id.ll_shop);
				orderCell.iv_righticon = (ImageView) convertView.findViewById(R.id.iv_righticon);
				convertView.setTag(orderCell);
			}else {
				orderCell = (OrderCell) convertView.getTag();
			}
			final Order order = getItem(position);
			orderCell.tv_ordersn.setText(order.getSn().substring(13));
			orderCell.tv_ordertime.setText(order.getStart_time().substring(8, 10) + ":" + order.getStart_time().substring(10, 12));
			if (order.getStatus().equals("undo")) {
				orderCell.iv_righticon.setVisibility(View.GONE);
			}else if (order.getStatus().equals("doing")) {
				orderCell.iv_righticon.setVisibility(View.VISIBLE);
				orderCell.iv_righticon.setBackgroundResource(R.drawable.dispatching);
			}else if (order.getStatus().equals("done")) {
				orderCell.iv_righticon.setVisibility(View.VISIBLE);
				orderCell.iv_righticon.setBackgroundResource(R.drawable.finish);
			}
			orderCell.tv_phone.setText(order.getConsignee());
			orderCell.tv_phonenum.setText(order.getRecipient_phone());
			orderCell.tv_phonenum.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
			orderCell.tv_phonenum.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent=new Intent("android.intent.action.CALL",Uri.parse("tel:"+order.getRecipient_phone()));
					startActivity(intent);
				}
			});
			orderCell.customprice.setText(order.getSum() + "元");
			Addr userAddr = order.getUserAddr();
			orderCell.tv_destination.setText(userAddr.getTitle());	
			List<Addr> shopAddrs = order.getAddrs();
			orderCell.ll_shop.removeAllViews();
			Addr addr;
			TextView tv_shop;
			for (int i = 0; i < shopAddrs.size(); i++) {
				addr = shopAddrs.get(i);
				tv_shop = new TextView(getActivity());
				tv_shop.setLayoutParams(lp);
				tv_shop.setText(addr.getTitle());// +"(" + addr.getStreet().replace("\t", "").replace("\n", "") +")"
				tv_shop.setTextSize(16);
				tv_shop.setTextColor(getResources().getColor(R.color.black));
				orderCell.ll_shop.addView(tv_shop);
			}
			
			return convertView;
		}
		
		public class OrderCell{
			TextView tv_ordersn, tv_ordertime, tv_destination, tv_phone, tv_phonenum, customprice;
			ImageView iv_righticon;
			LinearLayout ll_shop;
		}
		
	}
	
}
