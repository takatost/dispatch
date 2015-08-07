package com.darna.dispatch.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.darna.dispatch.Config;
import com.darna.dispatch.bean.DayOverView;
import com.darna.dispatch.bean.DayOverViewInfo;

public class Net_DeliveryDayOverview {
	
	public Net_DeliveryDayOverview(String job_id, String date_to, final SuccessCallback successCallback, final FailCallback failCallback) {
		new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (!jsonObject.isNull(Config.KEY_DATA)) {
						if (successCallback != null) {
							List<DayOverView> dayOverViews = new ArrayList<DayOverView>();
							JSONArray dataArray = jsonObject.getJSONArray(Config.KEY_DATA);
							JSONObject dayObject;
							DayOverView dayOverView;
							for (int i = 0; i < dataArray.length(); i++) {
								dayObject = dataArray.getJSONObject(i);
								dayOverView = new DayOverView();
								dayOverView.setDate(dayObject.getString(Config.KEY_DATE));
								
								JSONObject infoObject = dayObject.getJSONObject(Config.KEY_INFO);
								DayOverViewInfo dayOverViewInfo = new DayOverViewInfo();
								dayOverViewInfo.setBooking_cnt(infoObject.getString(Config.KEY_BOOKING_CNT));
								dayOverViewInfo.setCanceled_cnt(infoObject.getString(Config.KEY_CANCELED_CNT));
								dayOverViewInfo.setGreater60_cnt(infoObject.getString(Config.KEY_GREATERSIXTY));
								dayOverViewInfo.setNormal_cnt(infoObject.getString(Config.KEY_NORMAL_CNT));
								dayOverViewInfo.setOrder_cnt(infoObject.getString(Config.KEY_ORDER_CNT));
								dayOverViewInfo.setOver10_cnt(infoObject.getString(Config.KEY_OVERTEN));
								
								dayOverView.setInfos(dayOverViewInfo);
								dayOverViews.add(dayOverView);
								
							}
							
							successCallback.onSuccess(dayOverViews);
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
		}, Config.KEY_ACTION, Config.ACTION_DELIVERYDAYOVERVIEW,
		   Config.KEY_JOB_ID, job_id,
		   Config.KEY_DATE_TO, date_to);
	}
	
	public static interface SuccessCallback{
		void onSuccess(List<DayOverView> dayOverViews);
	}
	
	public static interface FailCallback{
		void onFail(int errorCode);
	}
	
}
