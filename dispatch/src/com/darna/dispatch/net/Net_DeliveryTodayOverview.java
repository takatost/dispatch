package com.darna.dispatch.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.darna.dispatch.Config;
import com.darna.dispatch.bean.TodayOverView;


public class Net_DeliveryTodayOverview {
	
	public Net_DeliveryTodayOverview(String job_id, final SuccessCallback successCallback, final FailCallback failCallback) {
		new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (!jsonObject.isNull(Config.KEY_DATA)) {
						if (successCallback != null) {
							TodayOverView todayOverView = new TodayOverView();
							JSONObject dataObject = jsonObject.getJSONObject(Config.KEY_DATA);
							todayOverView.setName(dataObject.getString(Config.KEY_NAME));
							todayOverView.setOrder_cnt(dataObject.getString(Config.KEY_ORDER_CNT));
							todayOverView.setPayment(dataObject.getString(Config.KEY_PAYMENT));
							successCallback.onSuccess(todayOverView);
						}
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
		}, Config.KEY_ACTION, Config.ACTION_DELIVERYTODAYOVERVIEW,
		   Config.KEY_JOB_ID, job_id);
	}
	
	public static interface SuccessCallback{
		void onSuccess(TodayOverView todayOverView);
	}
	
	public static interface FailCallback{
		void onFail(int errorCode);
	}
	
}
