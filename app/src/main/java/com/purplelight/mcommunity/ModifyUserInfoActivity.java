package com.purplelight.mcommunity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.purplelight.mcommunity.application.MCommApplication;
import com.purplelight.mcommunity.component.widget.HeadBar;
import com.purplelight.mcommunity.constant.WebAPI;
import com.purplelight.mcommunity.provider.DomainFactory;
import com.purplelight.mcommunity.provider.dao.ILoginInfoDao;
import com.purplelight.mcommunity.provider.entity.LoginInfo;
import com.purplelight.mcommunity.util.HttpUtil;
import com.purplelight.mcommunity.util.Validation;
import com.purplelight.mcommunity.web.parameter.UpdateUserInfoParameter;
import com.purplelight.mcommunity.web.result.Result;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 修改用户信息画面
 * Created by wangyn on 16/4/27.
 */
public class ModifyUserInfoActivity extends BaseActivity {
    private static final String TAG = "ModifyUserInfoActivity";

    public static final int USER_NAME = 1;
    public static final int EMAIL = 2;
    public static final int PHONE = 3;
    public static final int SEX = 4;

    @InjectView(R.id.headBar) HeadBar headBar;
    @InjectView(R.id.txtModifyInfo) EditText txtModifyInfo;
    @InjectView(R.id.lytUserSex) RelativeLayout lytUserSex;
    @InjectView(R.id.radMale) RadioButton radMale;
    @InjectView(R.id.radFemale) RadioButton radFemale;
    @InjectView(R.id.loginProgress) LinearLayout loginProgress;

    private int type;
    private LoginInfo user = new LoginInfo();

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);
        ButterKnife.inject(this);

        LoginInfo loginInfo = MCommApplication.getLoginInfo();
        user.setId(loginInfo.getId());

        type = getIntent().getIntExtra("type", USER_NAME);
        initView(type);
        initEvents();
    }

    private void initView(int type){
        switch (type){
            case USER_NAME:
                lytUserSex.setVisibility(View.GONE);
                txtModifyInfo.setVisibility(View.VISIBLE);
                txtModifyInfo.setHint(R.string.txt_user_name_hint);
                headBar.setCenterTitle(getString(R.string.txt_user_name_title));
                break;
            case EMAIL:
                lytUserSex.setVisibility(View.GONE);
                txtModifyInfo.setVisibility(View.VISIBLE);
                txtModifyInfo.setHint(R.string.txt_user_email_hint);
                txtModifyInfo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                headBar.setCenterTitle(getString(R.string.txt_user_email_title));
                break;
            case PHONE:
                lytUserSex.setVisibility(View.GONE);
                txtModifyInfo.setVisibility(View.VISIBLE);
                txtModifyInfo.setHint(R.string.txt_user_phone_hint);
                txtModifyInfo.setInputType(InputType.TYPE_CLASS_PHONE);
                headBar.setCenterTitle(getString(R.string.txt_user_phone_title));
                break;
            case SEX:
                lytUserSex.setVisibility(View.VISIBLE);
                txtModifyInfo.setVisibility(View.GONE);
                headBar.setCenterTitle(getString(R.string.txt_user_sex_title));
                break;
        }
    }

    private void initEvents(){
        headBar.setLeftHeadButtonEvent(new HeadBar.HeadButtonClickedListener() {
            @Override
            public void onHeadButtonClicked() {
                ModifyUserInfoActivity.this.finish();
            }
        });

        headBar.setRightHeadButtonEvent(new HeadBar.HeadButtonClickedListener() {
            @Override
            public void onHeadButtonClicked() {
                if (checkInput()){
                    showProgress(true);
                    UpdateTask task = new UpdateTask();
                    UpdateUserInfoParameter parameter = new UpdateUserInfoParameter();
                    parameter.setUser(user);
                    task.execute(gson.toJson(parameter));
                }
            }
        });
    }

    private class UpdateTask extends AsyncTask<String, Void, Result>{
        @Override
        protected Result doInBackground(String... params) {
            String updateJson = params[0];
            Result result = new Result();
            if (Validation.IsActivityNetWork(ModifyUserInfoActivity.this)){
                try{
                    String resultJson = HttpUtil.PostJosn(WebAPI.getWebAPI(WebAPI.UPDATE_USER), updateJson);
                    result = gson.fromJson(resultJson, Result.class);
                } catch (IOException ex){
                    Log.e(TAG, ex.getMessage());
                    result.setSuccess(Result.ERROR);
                    result.setMessage(ex.getMessage());
                }
            } else {
                result.setSuccess(Result.ERROR);
                result.setMessage(getString(R.string.do_not_have_network));
            }

            return result;
        }

        @Override
        protected void onPostExecute(Result result) {
            showProgress(false);
            if (Result.SUCCESS.equals(result.getSuccess())){
                LoginInfo loginInfo = MCommApplication.getLoginInfo();
                switch (type){
                    case USER_NAME:
                        loginInfo.setUserName(user.getUserName());
                        break;
                    case EMAIL:
                        loginInfo.setEmail(user.getEmail());
                        break;
                    case PHONE:
                        loginInfo.setPhone(user.getPhone());
                        break;
                    case SEX:
                        if (radFemale.isChecked()){
                            loginInfo.setSex("2");
                        } else if (radMale.isChecked()){
                            loginInfo.setSex("1");
                        }
                        break;
                }
                saveLgoinInfo(loginInfo);
                ModifyUserInfoActivity.this.finish();
            }
            Toast.makeText(ModifyUserInfoActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLgoinInfo(LoginInfo loginInfo){
        MCommApplication.setLoginInfo(loginInfo);
        ILoginInfoDao loginInfoDao = DomainFactory.createLoginInfo(this);
        loginInfoDao.save(loginInfo);
    }

    private boolean checkInput(){
        switch (type){
            case USER_NAME:
                if (Validation.IsNullOrEmpty(txtModifyInfo.getText().toString())){
                    Toast.makeText(this, getString(R.string.user_name_must_input), Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    user.setUserName(txtModifyInfo.getText().toString());
                    return true;
                }
            case EMAIL:
                if (Validation.IsNullOrEmpty(txtModifyInfo.getText().toString())){
                    Toast.makeText(this, getString(R.string.user_email_must_input), Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    user.setEmail(txtModifyInfo.getText().toString());
                    return true;
                }
            case PHONE:
                if (Validation.IsNullOrEmpty(txtModifyInfo.getText().toString())){
                    Toast.makeText(this, getString(R.string.user_phone_must_input), Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    user.setPhone(txtModifyInfo.getText().toString());
                    return true;
                }
            case SEX:
                if (!radFemale.isChecked() && !radMale.isChecked()){
                    Toast.makeText(this, getString(R.string.sex_selected_sex), Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    if (radMale.isChecked()){
                        user.setSex("1");
                    } else if (radFemale.isChecked()){
                        user.setSex("2");
                    }
                    return true;
                }
        }
        return false;
    }

    private void showProgress(boolean show){
        if (show){
            loginProgress.setVisibility(View.VISIBLE);
        } else {
            loginProgress.setVisibility(View.GONE);
        }
    }
}
