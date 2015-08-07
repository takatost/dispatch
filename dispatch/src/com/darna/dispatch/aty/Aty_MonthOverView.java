package com.darna.dispatch.aty;

import com.darna.dispatch.Config;
import com.darna.dispatch.R;
import com.darna.dispatch.bean.MonthOverview;
import com.darna.dispatch.net.Net_DeliveryMonthOverview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Aty_MonthOverView extends Activity {
	
	TextView tv_ordercnt, tv_payment, tv_costtime, tv_grade, tv_normalorder, tv_greatersixty, tv_appointmentorder,
	     tv_greaterten;
	RelativeLayout rl_ordercnt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_monthoverview);
		findViewById();
		getData();
	}
	
	private void getData() {
		new Net_DeliveryMonthOverview(Config.getCachedJobId(this), new Net_DeliveryMonthOverview.SuccessCallback() {
			@Override
			public void onSuccess(MonthOverview monthOverview) {
				init(monthOverview);
			}
		}, new Net_DeliveryMonthOverview.FailCallback() {
			@Override
			public void onFail(int errorCode) {
				Toast.makeText(Aty_MonthOverView.this, "服务器连接超时", Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void init(MonthOverview monthOverview) {
		tv_ordercnt.setText(monthOverview.getOrder_cnt());
		tv_payment.setText(monthOverview.getPayment());
		tv_costtime.setText(monthOverview.getAvg_cost_time());
		tv_grade.setText(monthOverview.getRanking());
		tv_normalorder.setText(monthOverview.getNormal_cnt());
		tv_greatersixty.setText(monthOverview.getGreater60_cnt());
		tv_appointmentorder.setText(monthOverview.getBooking_cnt());
		tv_greaterten.setText(monthOverview.getOver10_cnt());
	}

	private void findViewById() {
		tv_ordercnt = (TextView) findViewById(R.id.tv_ordercnt);
		tv_payment = (TextView) findViewById(R.id.tv_payment);
		tv_costtime = (TextView) findViewById(R.id.tv_costtime);
		tv_grade = (TextView) findViewById(R.id.tv_grade);
		tv_normalorder = (TextView) findViewById(R.id.tv_normalorder);
		tv_greatersixty = (TextView) findViewById(R.id.tv_greatersixty);
		tv_appointmentorder = (TextView) findViewById(R.id.tv_appointmentorder);
		tv_greaterten = (TextView) findViewById(R.id.tv_greaterten);
		rl_ordercnt = (RelativeLayout) findViewById(R.id.rl_ordercnt);
		
		rl_ordercnt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Aty_MonthOverView.this, Aty_DayOverView.class));
				overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			}
		});
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
	}
}
