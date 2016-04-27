package com.purplelight.mcommunity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.purplelight.mcommunity.application.MCommApplication;
import com.purplelight.mcommunity.provider.DomainFactory;
import com.purplelight.mcommunity.provider.entity.LoginInfo;
import com.purplelight.mcommunity.util.Validation;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 启动欢迎界面
 * Created by wangyn on 16/4/7.
 */
public class WelcomeActivity extends Activity {

    /**
     * 老版本的Android在隐藏工具栏的时候需要一点延迟的时间
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private final Handler mHideHandler = new Handler();
    private final Handler mLoginHandler = new Handler();

    // 画面项目
    @InjectView(R.id.imgWelcome) ImageView imgWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        ButterKnife.inject(this);

        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);

        if (Validation.IsActivityNetWork(this)){
            if (getCachedLoginInfo()){
                mLoginHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        WelcomeActivity.this.finish();
                    }
                }, 1000);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(this, getString(R.string.do_not_have_network), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 取得保存登录的信息
     * @return  是否存在
     */
    private boolean getCachedLoginInfo(){
        LoginInfo loginInfo = DomainFactory.createLoginInfo(this).load();
        if (loginInfo != null){
            MCommApplication.setLoginInfo(loginInfo);

            return true;
        }
        return false;
    }

    /**
     * 隐藏系统工具栏
     */
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            imgWelcome.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

}
