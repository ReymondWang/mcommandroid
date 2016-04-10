package com.purplelight.mcommunity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.purplelight.mcommunity.R;

/**
 * 办公区展示内容,本页主要展示的是系统原生应用,每个原生应用都需要下载并安装到用户手机本地。
 * 主要用于一些和系统本地频繁交互,对性能和离线等要求比较高的内容。
 * Created by wangyn on 16/4/9.
 */
public class WorkFragment extends Fragment {

    public static WorkFragment Create(){
        WorkFragment fragment = new WorkFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_work, container, false);

        return rootView;
    }
}
