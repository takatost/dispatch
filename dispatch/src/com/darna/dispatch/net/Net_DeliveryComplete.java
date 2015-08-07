package com.darna.dispatch.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.darna.dispatch.Config;

public class Net_DeliveryComplete {
	public Net_DeliveryComplete(String sn, String job_id, final SuccessCallback successCallback, final FailCallback failCallback) {
		new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean(Config.KEY_STATUS)) {
						if (successCallback != null) {
							successCallback.onSuccess();
						}
					}else {
						if (failCallback != null) {
							if (jsonObject.getString(Config.KEY_CODE).equals(Config.KEY_NOTOWNER)) {
								failCallback.onFail(Config.RESULT_STATUS_NOTOWNER);
							}else {
								failCallback.onFail(Config.RESULT_STATUS_FAIL);
							}
							
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
		}, Config.KEY_ACTION, Config.ACTION_DELIVERYCOMPLETE,
		   Config.KEY_SN, sn,
		   Config.KEY_JOB_ID, job_id);
	}
	
	public static interface SuccessCallback{
		void onSuccess();
	}
	
	public static interface FailCallback{
		void onFail(int errorCode);
	}
}
