package com.purplelight.mcommunity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.purplelight.mcommunity.component.widget.TabGroup;
import com.purplelight.mcommunity.fragment.HomeFragment;
import com.purplelight.mcommunity.fragment.ProfileFragment;
import com.purplelight.mcommunity.fragment.WorkFragment;
import com.purplelight.mcommunity.util.SystemBarTintManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 应用主界面
 * Created by wangyn on 16/4/7.
 */
public class MainActivity extends FragmentActivity {

    @InjectView(R.id.tabGroup) TabGroup mTabGroup;

    private HomeFragment homeFragment;
    private WorkFragment workFragment;
    private ProfileFragment profileFragment;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        setTintBarBgColor(R.color.user_head_bg);

        fragmentManager = getSupportFragmentManager();

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
        homeFragment = HomeFragment.Create();
        workFragment = WorkFragment.Create();
        profileFragment = ProfileFragment.Create();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.lytContent, homeFragment);
        transaction.add(R.id.lytContent, workFragment);
        transaction.add(R.id.lytContent, profileFragment);
        transaction.commit();

        changeTab(0);
    }

    private void initEvent(){
        mTabGroup.setOnTabChangeListener(new TabGroup.TabSelectListener() {
            @Override
            public boolean onTabSelected(int index) {
                changeTab(index);
                return true;
            }
        });
    }

    private void setTintBarBgColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);
        }
    }

    private void changeTab(int index){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (index){
            case 0:
                transaction.show(homeFragment);
                transaction.hide(workFragment);
                transaction.hide(profileFragment);
                break;
            case 1:
                transaction.hide(homeFragment);
                transaction.show(workFragment);
                transaction.hide(profileFragment);
                break;
            case 2:
                transaction.hide(homeFragment);
                transaction.hide(workFragment);
                transaction.show(profileFragment);

                break;
        }
        transaction.commit();
    }

}
