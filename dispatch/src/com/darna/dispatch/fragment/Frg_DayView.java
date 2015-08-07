package com.darna.dispatch.fragment;

import com.darna.dispatch.Config;
import com.darna.dispatch.R;
import com.darna.dispatch.aty.Aty_FinishedOrder;
import com.darna.dispatch.bean.DayOverView;
import com.darna.dispatch.bean.DayOverViewInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Frg_DayView extends Fragment {
	
	DayOverView dayOverView;
	TextView tv_ordercnt, tv_cancelorder, tv_normalorder, tv_greatersixty, tv_appointmentorder, tv_greaterten, tv_date;
	RelativeLayout rl_ordercnt;
	
	public Frg_DayView(DayOverView data) {
		dayOverView = data;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_dayview, container, false);
		findViewById(view);
		init();
		return view;
	}

	private void init() {
		DayOverViewInfo info = dayOverView.getInfos();
		tv_date.setText(dayOverView.getDate());
		tv_ordercnt.setText(info.getOrder_cnt());
		tv_cancelorder.setText(info.getCanceled_cnt());
		tv_normalorder.setText(info.getNormal_cnt());
		tv_greatersixty.setText(info.getGreater60_cnt());
		tv_appointmentorder.setText(info.getBooking_cnt());
		tv_greaterten.setText(info.getOver10_cnt());
	}

	private void findViewById(View view) {
		tv_date = (TextView) view.findViewById(R.id.tv_date);
		tv_ordercnt = (TextView) view.findViewById(R.id.tv_ordercnt);
		tv_cancelorder = (TextView) view.findViewById(R.id.tv_cancelorder);
		tv_normalorder = (TextView) view.findViewById(R.id.tv_normalorder);
		tv_greatersixty = (TextView) view.findViewById(R.id.tv_greatersixty);
		tv_appointmentorder = (TextView) view.findViewById(R.id.tv_appointmentorder);
		tv_greaterten = (TextView) view.findViewById(R.id.tv_greaterten);
		rl_ordercnt = (RelativeLayout) view.findViewById(R.id.rl_ordercnt);
		
		rl_ordercnt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), Aty_FinishedOrder.class);
				intent.putExtra(Config.KEY_DATE, dayOverView.getDate());
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			}
		});
		
	}
}
