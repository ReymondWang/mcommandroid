package com.purplelight.mcommunity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

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
    private static final String TAG = "MainActivity";

    // 当用户在一定时间内连续点击菜单上的返回键时，才会真正退出，防止用户的误操作。
    private static final int EXIT_TIME_LENGTH = 2000;

    // 第一次点击按下返回键的时间
    private long firstPressBackTime = 0;

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            long currentTime = System.currentTimeMillis();
            if (currentTime - firstPressBackTime > EXIT_TIME_LENGTH){
                firstPressBackTime = currentTime;
                Toast.makeText(this, getString(R.string.press_back_noce_more), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                firstPressBackTime = 0;
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
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
