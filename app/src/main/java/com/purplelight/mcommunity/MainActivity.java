package com.purplelight.mcommunity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.Window;
import android.view.WindowManager;

import com.purplelight.mcommunity.component.widget.TabGroup;
import com.purplelight.mcommunity.component.widget.ToggleViewPager;
import com.purplelight.mcommunity.fragment.HomeFragment;
import com.purplelight.mcommunity.util.SystemBarTintManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 应用主界面
 * Created by wangyn on 16/4/7.
 */
public class MainActivity extends FragmentActivity {

    private final static int PAGE_COUNT = 4;

    @InjectView(R.id.tabGroup) TabGroup mTabGroup;
    @InjectView(R.id.mPager) ToggleViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.color_primary);
        }

        initView();
        initEvent();

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void initView(){
        mPager.setCanScroll(false);
        mPager.setAdapter(new MainViewAdapter(getSupportFragmentManager()));
        mPager.setCurrentItem(0);
    }

    private void initEvent(){
        mTabGroup.setOnTabChangeListener(new TabGroup.TabSelectListener() {
            @Override
            public boolean onTabSelected(int index) {
                mPager.setCurrentItem(index, true);
                return true;
            }
        });
    }

    /**
     * 画面主要框架的绑定适配器
     */
    private class MainViewAdapter extends FragmentStatePagerAdapter {
        public MainViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return HomeFragment.Create(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }
}
