package com.darna.dispatch.aty;

import java.util.ArrayList;
import java.util.List;

import com.darna.dispatch.Config;
import com.darna.dispatch.R;
import com.darna.dispatch.bean.Addr;
import com.darna.dispatch.bean.DishList;
import com.darna.dispatch.bean.OrderInfo;
import com.darna.dispatch.bean.ShopList;
import com.darna.dispatch.dialog.Dialog_CallDispatcher;
import com.darna.dispatch.net.Net_DeliveryComplete;
import com.darna.dispatch.net.Net_DeliveryConfirm;
import com.darna.dispatch.net.Net_DeliveryOrderInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Aty_OrderInfo extends Activity {
	
	TextView tv_ordersn, tv_ordertime, tv_destination, tv_phone, tv_phonenum, customprice;
	ImageView iv_righticon;
	LinearLayout ll_shop;
	Button btn_call, btn_deal;
	ExpandableListView elistview;
	String sn;
	OrderInfoAdapter orderInfoAdapter;
	OrderInfo mInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_orderinfo);
		
		sn = getIntent().getStringExtra(Config.KEY_SN);
		
		findViewById();
		getData();
	}

	private void getData() {
		new Net_DeliveryOrderInfo(sn, new Net_DeliveryOrderInfo.SuccessCallback() {
			@Override
			public void onSuccess(OrderInfo orderInfo) {
				btn_deal.setEnabled(true);
				mInfo = orderInfo;
				init(orderInfo);
			}
		}, new Net_DeliveryOrderInfo.FailCallback() {
			@Override
			public void onFail(int errorCode) {
				Toast.makeText(Aty_OrderInfo.this, "服务器请求超时", Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void init(OrderInfo orderInfo) {
		orderInfoAdapter.add(orderInfo.getShopLists());
		for(int i = 0; i < orderInfoAdapter.getGroupCount(); i++){  
			elistview.expandGroup(i);  
		}
		tv_ordersn.setText(orderInfo.getOrder_sn());
		tv_ordertime.setText(orderInfo.getStart_time().substring(8, 10) + ":" + orderInfo.getStart_time().substring(10, 12));
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( 
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); 
		
		List<ShopList> shopLists = orderInfo.getShopLists();
		String shopName;
		TextView tv_shop;
		ll_shop.removeAllViews();
		for (int i = 0; i < shopLists.size(); i++) {
			shopName = shopLists.get(i).getShop_name();
			tv_shop = new TextView(Aty_OrderInfo.this);
			tv_shop.setLayoutParams(lp);
			tv_shop.setText(shopName);
			tv_shop.setTextSize(16);
			tv_shop.setTextColor(getResources().getColor(R.color.black));
			ll_shop.addView(tv_shop);
		}
		
		tv_destination.setText(orderInfo.getAddress());
		tv_phone.setText(orderInfo.getConsignee());
		tv_phonenum.setText(orderInfo.getRecipient_phone());
		tv_phonenum.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
		callphone();
		
		customprice.setText(orderInfo.getSum() + "元");
		if (orderInfo.getStatus().equals("undo")) {
			btn_deal.setText("接单");
			dealOrder();
		}else if (orderInfo.getStatus().equals("doing")) {
			btn_deal.setText("正在配送");
			orderFinish();
		}else if (orderInfo.getStatus().equals("done")) {
			btn_deal.setText("已完成");
			btn_deal.setEnabled(false);
		}
		
	}
	
	private void orderFinish() {
		btn_deal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 AlertDialog.Builder builder = new Builder(Aty_OrderInfo.this);
				 builder.setMessage("确认已完成订单吗？");
				 builder.setTitle("完成订单确认");
				 builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						btn_deal.setEnabled(false);
						DeliveryComplete();
					}
				});
				 builder.setNegativeButton("取消", null);
				 builder.create().show();
			}
		});
	}
	
	public void DeliveryComplete(){
		new Net_DeliveryComplete(mInfo.getOrder_sn(), Config.getCachedJobId(Aty_OrderInfo.this), new Net_DeliveryComplete.SuccessCallback() {
			@Override
			public void onSuccess() {
				Toast.makeText(Aty_OrderInfo.this, "成功完成订单", Toast.LENGTH_SHORT).show();
				getData();
			}
		}, new Net_DeliveryComplete.FailCallback() {
			
			@Override
			public void onFail(int errorCode) {
				switch (errorCode) {
				case Config.RESULT_STATUS_FAIL:
					Toast.makeText(Aty_OrderInfo.this, "操作失败，请联系调度员", Toast.LENGTH_SHORT).show();
					break;

				case Config.RESULT_STATUS_NOTOWNER:
					Toast.makeText(Aty_OrderInfo.this, "订单已有变动", Toast.LENGTH_SHORT).show();
					finish();
					break;
				}
			}
		});
	}

	private void dealOrder() {
		btn_deal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(Aty_OrderInfo.this);
			    builder.setMessage("确认接单吗？");
				builder.setTitle("接单确认");
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						btn_deal.setEnabled(false);
						DeliveryConfirm();
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});
		
		
	}
	
	public void DeliveryConfirm(){
		new Net_DeliveryConfirm(mInfo.getOrder_sn(), Config.getCachedJobId(Aty_OrderInfo.this), new Net_DeliveryConfirm.SuccessCallback() {
			@Override
			public void onSuccess() {
				Toast.makeText(Aty_OrderInfo.this, "接单操作成功", Toast.LENGTH_SHORT).show();
				getData();
			}
		}, new Net_DeliveryConfirm.FailCallback() {
			@Override
			public void onFail(int errorCode) {
				switch (errorCode) {
				case Config.RESULT_STATUS_FAIL:
					Toast.makeText(Aty_OrderInfo.this, "操作失败，请联系调度员", Toast.LENGTH_SHORT).show();
					break;

				case Config.RESULT_STATUS_NOTOWNER:
					Toast.makeText(Aty_OrderInfo.this, "订单已有变动", Toast.LENGTH_SHORT).show();
					finish();
					break;
				}
			}
		});
	}

	private void callphone() {
		tv_phonenum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent("android.intent.action.CALL",Uri.parse("tel:"+tv_phonenum.getText().toString()));
				startActivity(intent);
			}
		});
	}

	private void findViewById() {
		
		tv_ordersn = (TextView) findViewById(R.id.tv_ordersn);
		tv_ordertime = (TextView) findViewById(R.id.tv_ordertime);
		tv_destination = (TextView) findViewById(R.id.tv_destination);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_phonenum = (TextView) findViewById(R.id.tv_phonenum);
		customprice = (TextView) findViewById(R.id.customprice);
		iv_righticon = (ImageView) findViewById(R.id.iv_righticon);
		ll_shop = (LinearLayout) findViewById(R.id.ll_shop);
		btn_call = (Button) findViewById(R.id.btn_call);
		btn_deal = (Button) findViewById(R.id.btn_deal);
		elistview = (ExpandableListView) findViewById(R.id.elistview);
		orderInfoAdapter = new OrderInfoAdapter();
		elistview.setAdapter(orderInfoAdapter);
		
		/*elistview.setOnGroupClickListener(new OnGroupClickListener() {  
            
            @Override  
            public boolean onGroupClick(ExpandableListView parent, View v,  
                    int groupPosition, long id) {  
                return true;  
            }  
        });*/
		
		btn_call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Aty_OrderInfo.this, Dialog_CallDispatcher.class));
			}
		});
		
	}
	
	public class OrderInfoAdapter extends BaseExpandableListAdapter{
		List<ShopList> shopLists = new ArrayList<ShopList>();
		GroupHolder groupHolder;
		ChildHolder childHolder;
		
		public void add(List<ShopList> shops){
			clear();
			shopLists = shops;
			notifyDataSetChanged();
		}
		
		public void clear(){
			shopLists.clear();
		}

		@Override
		public int getGroupCount() {
			return shopLists.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return shopLists.get(groupPosition).getDishLists().size();
		}

		@Override
		public ShopList getGroup(int groupPosition) {
			return shopLists.get(groupPosition);
		}

		@Override
		public DishList getChild(int groupPosition, int childPosition) {
			return shopLists.get(groupPosition).getDishLists().get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.head_orderinfo, parent, false);
				groupHolder = new GroupHolder();
				groupHolder.tv_shopname = (TextView) convertView.findViewById(R.id.tv_shopname);
				groupHolder.totalprice = (TextView) convertView.findViewById(R.id.totalprice);
				groupHolder.tv_note = (TextView) convertView.findViewById(R.id.tv_note);
				convertView.setTag(groupHolder);
			}else {
				groupHolder = (GroupHolder) convertView.getTag();
			}
			ShopList shopList = getGroup(groupPosition);
			groupHolder.tv_shopname.setText(shopList.getShop_name());
			groupHolder.totalprice.setText(shopList.getShop_real_price() + "元");
			if (shopList.getNote().equals("")) {
				groupHolder.tv_note.setVisibility(View.GONE);
			}else {
				groupHolder.tv_note.setVisibility(View.VISIBLE);
				groupHolder.tv_note.setText("备注： " +  shopList.getNote());
			}
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.child_orderinfo, parent, false);
				childHolder = new ChildHolder();
				childHolder.tv_dishname = (TextView) convertView.findViewById(R.id.tv_dishname);
				childHolder.tv_dishnum = (TextView) convertView.findViewById(R.id.tv_dishnum);
				childHolder.tv_dishprice = (TextView) convertView.findViewById(R.id.tv_dishprice);
				convertView.setTag(childHolder);
			}else {
				childHolder = (ChildHolder) convertView.getTag();
			}
			DishList dishList = getChild(groupPosition, childPosition);
			childHolder.tv_dishname.setText(dishList.getDish_name());
			childHolder.tv_dishnum.setText(dishList.getNumber());
			childHolder.tv_dishprice.setText(dishList.getDish_price() + " (" + "+" + dishList.getDish_pack_price() + ") 元");
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
		
		public class GroupHolder{
			TextView tv_shopname, totalprice, tv_note;
		}
		
		public class ChildHolder{
			TextView tv_dishname, tv_dishnum, tv_dishprice;
		}
		
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
	}
	
}
