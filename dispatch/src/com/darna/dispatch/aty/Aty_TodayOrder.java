package com.darna.dispatch.aty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.darna.dispatch.R;
import com.darna.dispatch.fragment.Frg_Order;

public class Aty_TodayOrder extends FragmentActivity {
	
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();  
    private FragmentAdapter mFragmentAdapter;
    private ViewPager mPageVp; 
    
    private Frg_Order orderDealed, orderNew;
    
    Button new_order, dealed_order;
    int currentItem = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_todayorder);
		
		findViewById();
		init();
	}
	

	private void findViewById() {
		mPageVp = (ViewPager) this.findViewById(R.id.id_page_vp);
		new_order = (Button) findViewById(R.id.new_order);
		dealed_order = (Button) findViewById(R.id.dealed_order);
		new_order.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentItem = 0;
				mPageVp.setCurrentItem(currentItem);
				setButton();
			}
		});
		
		dealed_order.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentItem = 1;
				mPageVp.setCurrentItem(currentItem);
				setButton();
			}
		});
		
	}
	
	private void init() {
		setButton();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());       
		String date = sDateFormat.format(new java.util.Date());
		orderNew = new Frg_Order("undo", date);
		orderDealed = new Frg_Order("doing", date);
		mFragmentList.add(orderNew);
		mFragmentList.add(orderDealed);
		mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);  
        mPageVp.setAdapter(mFragmentAdapter);  
        mPageVp.setCurrentItem(currentItem);
        
        mPageVp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					currentItem = 0;
					break;

				case 1:
					currentItem = 1;
					break;
				}
				setButton();
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
        
	}
	
	public void setButton(){
		switch (currentItem) {
		case 0:
			new_order.setBackgroundResource(R.drawable.backwhite);
			new_order.setTextColor(getResources().getColor(R.color.brown));
			dealed_order.setBackgroundResource(R.drawable.backbrown);
			dealed_order.setTextColor(getResources().getColor(R.color.white));
			break;

		case 1:
			new_order.setBackgroundResource(R.drawable.backbrown);
			new_order.setTextColor(getResources().getColor(R.color.white));
			dealed_order.setBackgroundResource(R.drawable.backwhite);
			dealed_order.setTextColor(getResources().getColor(R.color.brown));
			break;
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
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
	
}
