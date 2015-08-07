package com.darna.dispatch;

import android.content.Context;
import android.content.SharedPreferences.Editor;

/**
 * @author xiashuai 2015-07-30
 *
 */
public class Config {
	public static String APP_ID = "com.darna.wmxfx";
	public static String CHARSET = "utf-8";
	
	public static final String SERVER_URL = "http://www.wmxfx.com/api/client/api.php";
	//public static final String SERVER_URL = "http://192.168.1.106/darna/api/client/api.php";
	public static final String KEY_ACTION = "Action";
	public static final String KEY_DATA = "data";
	
	public static final int RESULT_STATUS_FAIL = 0;
	public static final int RESULT_STATUS_NOTOWNER = 1;
	
	public static final String KEY_JOB_ID = "job_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_MODE = "mode";
	public static final String KEY_DATE = "date";
	public static final String KEY_CANCEL = "cancel";
	public static final String KEY_SN = "sn";
	public static final String KEY_STARTTIME = "start_time";
	public static final String KEY_STATUS = "status";
	public static final String KEY_CONSIGNEE = "consignee";
	public static final String KEY_RECIPIENTPHONE = "recipient_phone";
	public static final String KEY_AMOUNT = "amount";
	public static final String KEY_EXTRA = "extra";
	public static final String KEY_SUM = "sum";
	public static final String KEY_TITLE = "title";
	public static final String KEY_STREET = "street";
	public static final String KEY_SHOPSADDR = "shops_addr";
	public static final String KEY_USERADDR = "user_addr";
	public static final String KEY_SHOPLIST = "shop_list";
	public static final String KEY_SHOPNAME = "shop_name";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_SHOPREALPRICE = "shop_real_price";
	public static final String KEY_NOTE = "note";
	public static final String KEY_DISHLIST = "dish_list";
	public static final String KEY_DISHNAME = "dish_name";
	public static final String KEY_DISHPRICE = "dish_price";
	public static final String KEY_DISHPACKPRICE = "dish_pack_price";
	public static final String KEY_NUMBER = "number";
	public static final String KEY_ORDERSN = "order_sn";
	public static final String KEY_TEL = "tel";	
	public static final String KEY_ORDER_CNT = "order_cnt";
	public static final String KEY_PAYMENT = "payment";
	public static final String KEY_AVG_COST_TIME = "avg_cost_time";
	public static final String KEY_BOOKING_CNT = "booking_cnt";
	public static final String KEY_CANCELED_CNT = "canceled_cnt";
	public static final String KEY_GREATERSIXTY = "greater60_cnt";
	public static final String KEY_NORMAL_CNT = "normal_cnt";
	public static final String KEY_ORDERCNT = "order_cnt";
	public static final String KEY_OVERTEN = "over10_cnt";
	public static final String KEY_RANKING = "ranking";
	public static final String KEY_DATE_TO = "date_to";
	public static final String KEY_INFO = "info";
	public static final String KEY_CODE = "code";
	public static final Object KEY_NOTOWNER = "not_owner";
	
	public static final String ACTION_DELIVERYMANLIST = "DeliveryManList";
	public static final String ACTION_DELIVERYORDERLIST = "DeliveryOrderList";
	public static final String ACTION_DELIVERYORDERINFO = "DeliveryOrderInfo";
	public static final String ACTION_DISPATCHERCONTACT = "DispatcherContact";
	public static final String ACTION_DELIVERYCONFIRM = "DeliveryConfirm";
	public static final String ACTION_DELIVERYCOMPLETE = "DeliveryComplete";
	public static final String ACTION_DELIVERYTODAYOVERVIEW = "DeliveryTodayOverview";
	public static final String ACTION_DELIVERYMONTHOVERVIEW = "DeliveryMonthOverview";
	public static final String ACTION_DELIVERYDAYOVERVIEW = "DeliveryDayOverview";
	/**
	 * 得到缓存的job_id
	 * @param context
	 * @return
	 */
	public static String getCachedJobId(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_JOB_ID, null);
	}
	
	/**
	 * 缓存配送员ID
	 * @param context
	 * @param job_id
	 */
	public static void cacheJobId(Context context, String job_id ){
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
		e.putString(KEY_JOB_ID, job_id);
		e.commit();
	}
}
