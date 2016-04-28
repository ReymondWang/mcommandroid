package com.purplelight.mcommunity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.purplelight.mcommunity.LoginActivity;
import com.purplelight.mcommunity.PersonalInfoActivity;
import com.purplelight.mcommunity.R;
import com.purplelight.mcommunity.application.MCommApplication;
import com.purplelight.mcommunity.component.view.CircleImageView;
import com.purplelight.mcommunity.constant.WebAPI;
import com.purplelight.mcommunity.provider.DomainFactory;
import com.purplelight.mcommunity.provider.dao.ILoginInfoDao;
import com.purplelight.mcommunity.provider.entity.LoginInfo;
import com.purplelight.mcommunity.task.BitmapDownloaderTask;
import com.purplelight.mcommunity.task.DownloadedDrawable;
import com.purplelight.mcommunity.util.ImageHelper;
import com.purplelight.mcommunity.util.Validation;
import com.purplelight.mcommunity.component.view.ConfirmDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 我的展示内容,主要用于个人信息的设置。
 * Created by wangyn on 16/4/9.
 */
public class ProfileFragment extends Fragment {

    // 画面项目
    @InjectView(R.id.imgHeadImage) CircleImageView imgHeadImage;
    @InjectView(R.id.txtUserName) TextView txtUserName;
    @InjectView(R.id.imgUserSex) ImageView imgUserSex;
    @InjectView(R.id.txtUserCode) TextView txtUserCode;
    @InjectView(R.id.lytUserInfo) RelativeLayout lytUserInfo;
    @InjectView(R.id.lytMessage) RelativeLayout lytMessage;
    @InjectView(R.id.lytPassword) RelativeLayout lytPassword;
    @InjectView(R.id.lytBuffer) RelativeLayout lytBuffer;
    @InjectView(R.id.lytAdvice) RelativeLayout lytAdvice;
    @InjectView(R.id.lytAbout) RelativeLayout lytAbout;
    @InjectView(R.id.lytLogout) LinearLayout lytLogout;

    public static ProfileFragment Create(){
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.inject(this, rootView);

        initEvents();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView(){
        LoginInfo loginInfo = MCommApplication.getLoginInfo();
        if (loginInfo != null){
            if (!Validation.IsNullOrEmpty(loginInfo.getHeadImgPath())){
                BitmapDownloaderTask task = new BitmapDownloaderTask(imgHeadImage);
                DownloadedDrawable drawable = new DownloadedDrawable(task, getResources(), R.drawable.default_head_image);
                imgHeadImage.setImageDrawable(drawable);
                task.execute(WebAPI.getFullImagePath(loginInfo.getHeadImgPath()));
            }
            txtUserName.setText(loginInfo.getUserName());
            if ("1".equals(loginInfo.getSex())){
                imgUserSex.setImageResource(R.drawable.ic_mine_male);
            } else {
                imgUserSex.setImageResource(R.drawable.ic_mine_female);
            }
            txtUserCode.setText(loginInfo.getUserCode());
        }
    }

    private void initEvents(){
        lytLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
        lytUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                startActivity(intent);
            }
        });
        lytBuffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClearBufferDialog();
            }
        });
    }

    private void showLogoutDialog(){
        final ConfirmDialog dialog = new ConfirmDialog(getActivity());
        dialog.setTitle(getString(R.string.title_logout_confirm));
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ILoginInfoDao loginInfoDao = DomainFactory.createLoginInfo(getActivity());
                loginInfoDao.clear();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        dialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showClearBufferDialog(){
        final ConfirmDialog dialog = new ConfirmDialog(getActivity());
        dialog.setTitle(getString(R.string.title_clear_buffer_confirm));
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ImageHelper.clear();
                Toast.makeText(getActivity(), getString(R.string.clear_buffer_success), Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
