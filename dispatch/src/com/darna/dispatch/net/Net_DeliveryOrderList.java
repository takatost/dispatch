package com.darna.dispatch.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.darna.dispatch.Config;
import com.darna.dispatch.bean.Addr;
import com.darna.dispatch.bean.Order;


/**
 * @author xiashuai
 *
 */
public class Net_DeliveryOrderList {
	public Net_DeliveryOrderList(String job_id, String mode, String date, String cancel, final SuccessCallback successCallback, final FailCallback failCallback) {
		new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (successCallback != null) {
						List<Order> orders = new ArrayList<Order>();
						if (!jsonObject.isNull(Config.KEY_DATA)) {
							JSONArray jsonArray = jsonObject.getJSONArray(Config.KEY_DATA);
							Order order;
							JSONObject orderObject;
							for (int i = 0; i < jsonArray.length(); i++) {
								orderObject = jsonArray.getJSONObject(i);
								order = new Order();
								order.setSn(orderObject.getString(Config.KEY_SN));
								order.setStart_time(orderObject.getString(Config.KEY_STARTTIME));
								order.setStatus(orderObject.getString(Config.KEY_STATUS));
								order.setConsignee(orderObject.getString(Config.KEY_CONSIGNEE));
								order.setRecipient_phone(orderObject.getString(Config.KEY_RECIPIENTPHONE));
								order.setAmount(orderObject.getString(Config.KEY_AMOUNT));
								order.setExtra(orderObject.getString(Config.KEY_EXTRA));
								order.setSum(orderObject.getString(Config.KEY_SUM));
								Addr userAddr = new Addr();
								JSONObject userAddrObject = orderObject.getJSONObject(Config.KEY_USERADDR);
								userAddr.setStreet(userAddrObject.getString(Config.KEY_STREET));
								userAddr.setTitle(userAddrObject.getString(Config.KEY_TITLE));
								order.setUserAddr(userAddr);
								List<Addr> addrs = new ArrayList<Addr>();
								Addr addr;
								JSONArray addrJsonArray = orderObject.getJSONArray(Config.KEY_SHOPSADDR);
								JSONObject addrObject;
								for (int j = 0; j < addrJsonArray.length(); j++) {
									addrObject = addrJsonArray.getJSONObject(j);
									addr = new Addr();
									addr.setTitle(addrObject.getString(Config.KEY_TITLE));
									addr.setStreet(addrObject.getString(Config.KEY_STREET));
									addrs.add(addr);
								}
								order.setAddrs(addrs);
								orders.add(order);
 							}
						}
						
						successCallback.onSuccess(orders);
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if (failCallback != null) {
						failCallback.onFail(Config.RESULT_STATUS_FAIL);
					}
				}
			}
		}, new NetConnection.FailCallback() {
			@Override
			public void onFail() {
				if (failCallback != null) {
					failCallback.onFail(Config.RESULT_STATUS_FAIL);
				}
			}
		}, Config.KEY_ACTION, Config.ACTION_DELIVERYORDERLIST,
		   Config.KEY_JOB_ID, job_id,
		   Config.KEY_MODE, mode,
		   Config.KEY_DATE, date,
		   Config.KEY_CANCEL, cancel);
	}
	
	
	public static interface SuccessCallback{
		void onSuccess(List<Order> orders);
	}
	
	public static interface FailCallback{
		void onFail(int errorCode);
	}
	
}
