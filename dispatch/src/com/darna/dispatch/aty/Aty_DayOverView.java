package com.darna.dispatch.aty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.darna.dispatch.Config;
import com.darna.dispatch.R;
import com.darna.dispatch.aty.Aty_TodayOrder.FragmentAdapter;
import com.darna.dispatch.bean.DayOverView;
import com.darna.dispatch.fragment.Frg_DayView;
import com.darna.dispatch.net.Net_DeliveryDayOverview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

public class Aty_DayOverView extends FragmentActivity {
	
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();  
    private FragmentAdapter mFragmentAdapter;
    private ViewPager mPageVp; 
    
    int currentItem = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_dayoverview);
		findViewById();
		getData();
	}
	
	private void getData() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());       
		String date_to = sDateFormat.format(new java.util.Date());
		new Net_DeliveryDayOverview(Config.getCachedJobId(this), date_to, new Net_DeliveryDayOverview.SuccessCallback() {
			@Override
			public void onSuccess(List<DayOverView> dayOverViews) {
				init(dayOverViews);
			}
		}, new Net_DeliveryDayOverview.FailCallback() {
			@Override
			public void onFail(int errorCode) {
				Toast.makeText(Aty_DayOverView.this, "请求服务器超时", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void init(List<DayOverView> dayOverViews) {
		Frg_DayView dayView;
		for (int i = 0; i < dayOverViews.size(); i++) {
			dayView = new Frg_DayView(dayOverViews.get(i));
			mFragmentList.add(dayView);
		}
		mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
		mPageVp.setAdapter(mFragmentAdapter);  
        mPageVp.setCurrentItem(currentItem);
        
	}

	private void findViewById() {
		mPageVp = (ViewPager) this.findViewById(R.id.id_page_vp);
	}

	public class FragmentAdapter extends FragmentPagerAdapter {  
		  
	    List<Fragment> fragmentList = new ArrayList<Fragment>();  
	    public FragmentAdapter(FragmentManager fm,List<Fragment> fragmentList) {  
	        super(fm);  
	        this.fragmentList = fragmentList;  
	    }  
	  
	    @Override  
	    public Fragment getItem(int position) {  
	        return fragmentList.get(position);  
	    }  
	  
	    @Override  
	    public int getCount() {  
	        return fragmentList.size();  
	    }  
	  
	} 
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
	}
	
}
