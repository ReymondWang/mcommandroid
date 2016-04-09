package com.purplelight.mcommunity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.purplelight.mcommunity.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 首页的展示内容
 * Created by wangyn on 16/4/9.
 */
public class HomeFragment extends Fragment {


    public static HomeFragment Create(int index){
        HomeFragment fragment = new HomeFragment();

        Bundle args = new Bundle();
        args.putInt("Index", index);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.inject(this, rootView);

        int index = getArguments().getInt("Index");

        return rootView;
    }
}
