package com.darna.dispatch.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.darna.dispatch.Config;
import com.darna.dispatch.bean.MonthOverview;

public class Net_DeliveryMonthOverview {
	
	public Net_DeliveryMonthOverview(String job_id, final SuccessCallback successCallback, final FailCallback failCallback) {
		new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (!jsonObject.isNull(Config.KEY_DATA)) {
						if (successCallback != null) {
							MonthOverview monthOverview = new MonthOverview();
							JSONObject dataObject = jsonObject.getJSONObject(Config.KEY_DATA);
							monthOverview.setAvg_cost_time(dataObject.getString(Config.KEY_AVG_COST_TIME));
							monthOverview.setBooking_cnt(dataObject.getString(Config.KEY_BOOKING_CNT));
							monthOverview.setCanceled_cnt(dataObject.getString(Config.KEY_CANCELED_CNT));
							monthOverview.setGreater60_cnt(dataObject.getString(Config.KEY_GREATERSIXTY));
							monthOverview.setNormal_cnt(dataObject.getString(Config.KEY_NORMAL_CNT));
							monthOverview.setOrder_cnt(dataObject.getString(Config.KEY_ORDERCNT));
							monthOverview.setOver10_cnt(dataObject.getString(Config.KEY_OVERTEN));
							monthOverview.setPayment(dataObject.getString(Config.KEY_PAYMENT));
							monthOverview.setRanking(dataObject.getString(Config.KEY_RANKING));
							successCallback.onSuccess(monthOverview);
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
		}, Config.KEY_ACTION, Config.ACTION_DELIVERYMONTHOVERVIEW,
		   Config.KEY_JOB_ID, job_id);
	}
	
	public static interface SuccessCallback{
		void onSuccess(MonthOverview monthOverview);
	}
	
	public static interface FailCallback{
		void onFail(int errorCode);
	}
	
}
