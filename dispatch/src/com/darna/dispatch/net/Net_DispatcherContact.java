package com.darna.dispatch.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.darna.dispatch.Config;
import com.darna.dispatch.bean.DispatcherContact;

public class Net_DispatcherContact {

	public Net_DispatcherContact(final SuccessCallback successCallback, final FailCallback failCallback) {
		new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			@Override
			public void onSuccess(String result) {
				try {
					if (successCallback != null) {
						JSONObject jsonObject = new JSONObject(result);
						List<DispatcherContact> dispatcherContacts = new ArrayList<DispatcherContact>();
						JSONArray dataArray = jsonObject.getJSONArray(Config.KEY_DATA);
						DispatcherContact dispatcherContact;
						JSONObject dispatchObject;
						for (int i = 0; i < dataArray.length(); i++) {
							dispatchObject = dataArray.getJSONObject(i);
							dispatcherContact = new DispatcherContact();
							dispatcherContact.setName(dispatchObject.getString(Config.KEY_NAME));
							dispatcherContact.setTel(dispatchObject.getString(Config.KEY_TEL));
							dispatcherContacts.add(dispatcherContact);
						}
						successCallback.onSuccess(dispatcherContacts);
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
		}, Config.KEY_ACTION, Config.ACTION_DISPATCHERCONTACT);
	}
	
	public static interface SuccessCallback{
		void onSuccess(List<DispatcherContact> dispatcherContacts);
	}
	
	public static interface FailCallback{
		void onFail(int errorCode);
	}
	
}
