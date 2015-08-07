package com.darna.dispatch.aty;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.darna.dispatch.Config;
import com.darna.dispatch.R;
import com.darna.dispatch.fragment.Frg_Order;

public class Aty_FinishedOrder extends FragmentActivity {
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();  
    private FragmentAdapter mFragmentAdapter;
    private ViewPager mPageVp; 
    TextView tv_date;
    int currentItem = 0;
    
    String date;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_finishedorder);
		
		date = getIntent().getStringExtra(Config.KEY_DATE);
		
		findViewById();
		init();
	}
    
    private void init() {
    	tv_date.setText(date);
		Frg_Order order = new Frg_Order("done", date);
		mFragmentList.add(order);
		mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);  
        mPageVp.setAdapter(mFragmentAdapter);  
        mPageVp.setCurrentItem(currentItem);
	}

	private void findViewById() {
		mPageVp = (ViewPager) this.findViewById(R.id.id_page_vp);
		tv_date = (TextView) this.findViewById(R.id.tv_date);
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
