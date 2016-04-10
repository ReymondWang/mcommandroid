package com.purplelight.mcommunity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.purplelight.mcommunity.R;

/**
 * 联系展示内容,主要展示个人的社交圈信息。
 * Created by wangyn on 16/4/9.
 */
public class ContactFragment extends Fragment {

    public static ContactFragment Create(){
        ContactFragment fragment = new ContactFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        return rootView;
    }
}
