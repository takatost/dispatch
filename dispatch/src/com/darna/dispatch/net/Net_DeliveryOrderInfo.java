package com.darna.dispatch.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.darna.dispatch.Config;
import com.darna.dispatch.bean.DishList;
import com.darna.dispatch.bean.OrderInfo;
import com.darna.dispatch.bean.ShopList;

public class Net_DeliveryOrderInfo {
	
	public Net_DeliveryOrderInfo(String sn, final SuccessCallback successCallback, final FailCallback failCallback) {
		new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			@Override
			public void onSuccess(String result) {
				try {
					if (successCallback != null) {
						JSONObject jsonObject = new JSONObject(result);
						JSONObject dataObject = jsonObject.getJSONObject(Config.KEY_DATA);
						OrderInfo orderInfo = new OrderInfo();
						orderInfo.setOrder_sn(dataObject.getString(Config.KEY_ORDERSN));
						orderInfo.setConsignee(dataObject.getString(Config.KEY_CONSIGNEE));
						orderInfo.setStart_time(dataObject.getString(Config.KEY_STARTTIME));
						orderInfo.setRecipient_phone(dataObject.getString(Config.KEY_RECIPIENTPHONE));
						orderInfo.setStatus(dataObject.getString(Config.KEY_STATUS));
						orderInfo.setSum(dataObject.getString(Config.KEY_SUM));
						orderInfo.setAddress(dataObject.getString(Config.KEY_ADDRESS));
						
						List<ShopList> shopLists = new ArrayList<ShopList>();
						ShopList shopList;
						JSONArray shopArray = dataObject.getJSONArray(Config.KEY_SHOPLIST);
						JSONObject shopObject;
						for (int i = 0; i < shopArray.length(); i++) {
							shopObject = shopArray.getJSONObject(i);
							shopList = new ShopList();
							shopList.setShop_name(shopObject.getString(Config.KEY_SHOPNAME));
							shopList.setAddress(shopObject.getString(Config.KEY_ADDRESS));
							shopList.setShop_real_price(shopObject.getString(Config.KEY_SHOPREALPRICE));
							shopList.setNote(shopObject.getString(Config.KEY_NOTE));
							
							List<DishList> dishLists = new ArrayList<DishList>();
							DishList dishList;
							JSONArray dishArray = shopObject.getJSONArray(Config.KEY_DISHLIST);
							JSONObject dishObject;
							for (int j = 0; j < dishArray.length(); j++) {
								dishObject = dishArray.getJSONObject(j);
								dishList = new DishList();
								dishList.setDish_name(dishObject.getString(Config.KEY_DISHNAME));
								dishList.setDish_price(dishObject.getString(Config.KEY_DISHPRICE));
								dishList.setDish_pack_price(dishObject.getString(Config.KEY_DISHPACKPRICE));
								dishList.setNumber(dishObject.getString(Config.KEY_NUMBER));
								dishLists.add(dishList);
							}
							shopList.setDishLists(dishLists);
							shopLists.add(shopList);
						}
						orderInfo.setShopLists(shopLists);
						successCallback.onSuccess(orderInfo);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new NetConnection.FailCallback() {
			@Override
			public void onFail() {
				if (failCallback != null) {
					failCallback.onFail(Config.RESULT_STATUS_FAIL);
				}
			}
		}, Config.KEY_ACTION, Config.ACTION_DELIVERYORDERINFO,
		   Config.KEY_SN, sn);
	}
	
	public static interface SuccessCallback{
		void onSuccess(OrderInfo orderInfo);
	}
	
	public static interface FailCallback{
		void onFail(int errorCode);
	}
	
}
