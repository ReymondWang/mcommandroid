package com.purplelight.mcommunity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.purplelight.mcommunity.R;

/**
 * 我的展示内容,主要用于个人信息的设置。
 * Created by wangyn on 16/4/9.
 */
public class ProfileFragment extends Fragment {

    public static ProfileFragment Create(){
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        return rootView;
    }
}
