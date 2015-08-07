package com.darna.dispatch.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.darna.dispatch.Config;
import com.darna.dispatch.bean.Dispatcher;

/**
 * @author xiashuai
 *
 */
public class Net_DeliveryManList {
	public Net_DeliveryManList(final SuccessCallback successCallback, final FailCallback failCallback) {
		new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (!jsonObject.isNull(Config.KEY_DATA)) {
						if (successCallback != null) {
							JSONArray jsonArray = jsonObject.getJSONArray(Config.KEY_DATA);
							List<Dispatcher> dispatchers = new ArrayList<Dispatcher>();
							Dispatcher dispatcher;
							JSONObject dispatcherObject;
							for (int i = 0; i < jsonArray.length(); i++) {
								dispatcherObject = jsonArray.getJSONObject(i);
								dispatcher = new Dispatcher();
								dispatcher.setJob_id(dispatcherObject.getString(Config.KEY_JOB_ID));
								dispatcher.setName(dispatcherObject.getString(Config.KEY_NAME));
								dispatchers.add(dispatcher);
							}
							successCallback.onSuccess(dispatchers);
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
		}, Config.KEY_ACTION, Config.ACTION_DELIVERYMANLIST);
	}
	
	public static interface SuccessCallback{
		void onSuccess(List<Dispatcher> dispatchers);
	}
	
	public static interface FailCallback{
		void onFail(int errorCode);
	}
}
