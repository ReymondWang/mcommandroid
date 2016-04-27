package com.purplelight.mcommunity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.purplelight.mcommunity.application.MCommApplication;
import com.purplelight.mcommunity.constant.WebAPI;
import com.purplelight.mcommunity.provider.DomainFactory;
import com.purplelight.mcommunity.provider.dao.ILoginInfoDao;
import com.purplelight.mcommunity.util.HttpUtil;
import com.purplelight.mcommunity.util.Validation;
import com.purplelight.mcommunity.web.parameter.LoginParameter;
import com.purplelight.mcommunity.web.result.LoginResult;
import com.purplelight.mcommunity.web.result.Result;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 登录界面
 * Created by wangyn on 16/4/26.
 */
public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";

    // 画面项目
    @InjectView(R.id.txtLoginId) EditText txtLoginId;
    @InjectView(R.id.txtPassword) EditText txtPassword;
    @InjectView(R.id.btnLogin) Button btnLogin;
    @InjectView(R.id.loginProgress) LinearLayout loginProgress;
    @InjectView(R.id.lytLoginFrom) RelativeLayout lytLoginFrom;

    private ILoginInfoDao loginInfoDao = DomainFactory.createLoginInfo(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        initEvents();
    }

    private void initEvents(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginId = txtLoginId.getText().toString();
                String password = txtPassword.getText().toString();

                LoginTask loginTask = new LoginTask();
                loginTask.execute(loginId, password);
                showProgressBar(true);
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            LoginParameter parameter = new LoginParameter();
            parameter.setLoginId(params[0]);
            parameter.setPassword(params[1]);
            try{
                return HttpUtil.PostJosn(WebAPI.getWebAPI(WebAPI.LOGIN), new Gson().toJson(parameter));
            } catch (UnsupportedEncodingException ex){
                Log.e(TAG, ex.getMessage());
                return "";
            } catch (IOException ex){
                Log.e(TAG, ex.getMessage());
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            showProgressBar(false);

            if (!Validation.IsNullOrEmpty(s)){
                LoginResult result = new Gson().fromJson(s, LoginResult.class);
                if (Result.ERROR.equals(result.getSuccess())){
                    Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    loginInfoDao.save(result.getUser());
                    MCommApplication.setLoginInfo(result.getUser());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();

                    Toast.makeText(LoginActivity.this, getString(R.string.login_successed), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showProgressBar(boolean show){
        if (show){
            loginProgress.setVisibility(View.VISIBLE);
            lytLoginFrom.setVisibility(View.GONE);
        } else {
            loginProgress.setVisibility(View.GONE);
            lytLoginFrom.setVisibility(View.VISIBLE);
        }
    }
}
