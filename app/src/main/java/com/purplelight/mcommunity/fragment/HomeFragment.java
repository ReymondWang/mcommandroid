package com.purplelight.mcommunity.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.purplelight.mcommunity.R;
import com.purplelight.mcommunity.WebViewActivity;
import com.purplelight.mcommunity.component.view.HomeFuncRowView;
import com.purplelight.mcommunity.component.view.WebBannerView;
import com.purplelight.mcommunity.component.widget.AutoScrollViewPager;
import com.purplelight.mcommunity.component.widget.CirclePageIndicator;
import com.purplelight.mcommunity.constant.Configuration;
import com.purplelight.mcommunity.constant.WebAPI;
import com.purplelight.mcommunity.entity.WebBanner;
import com.purplelight.mcommunity.util.HttpUtil;
import com.purplelight.mcommunity.util.Validation;
import com.purplelight.mcommunity.web.parameter.AppFuncParameter;
import com.purplelight.mcommunity.web.result.AppFunctionResult;
import com.purplelight.mcommunity.web.result.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 首页的展示内容,本页在设计上主要展示H5的内容。
 * 包括广告轮播,H5轻应用以及系统公告等内容。
 * Created by wangyn on 16/4/9.
 */
public class HomeFragment extends Fragment {

    private final static int FUNC_NUM_EACH_ROW = 4;   // 每行显示的功能个数
    private final static int TOP_ADV_SWIPE_SPEED = 4; // 顶部广告栏的动画间隔
    private final static int NOTICE_NUM_EACH_ROW = 2; // 每行显示的通知个数

    @InjectView(R.id.vpHomeTop) AutoScrollViewPager vpHomeTop;
    @InjectView(R.id.homeTopIndicator) CirclePageIndicator homeTopIndicator;
    @InjectView(R.id.lytFuncContent) LinearLayout lytFuncContent;
    @InjectView(R.id.lytNoticeContent) GridLayout lytNoticeContent;
    @InjectView(R.id.lytContent) ScrollView lytContent;
    @InjectView(R.id.loadingProgress) LinearLayout loadingProgress;


    private List<WebBannerView> mBannerViews;
    private List<WebBanner> mBanners, mFunctions, mNotices;

    private Point mScreenSize = new Point();

    public static HomeFragment Create(){
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.inject(this, rootView);

        // 取得手机屏幕的宽度，并将顶部轮播广告位的比例设置为2:1
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(mScreenSize);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                , FrameLayout.LayoutParams.MATCH_PARENT);
        params.height = mScreenSize.x / 2;
        vpHomeTop.setLayoutParams(params);
        vpHomeTop.setCycle(true);

        AppFuncParameter parameter = new AppFuncParameter();
        parameter.setFragment(Configuration.Fragment.HOME);

        LoadingDataTask task = new LoadingDataTask();
        task.execute(new Gson().toJson(parameter));
        showProgress(true);

        return rootView;
    }

    private class LoadingDataTask extends AsyncTask<String, Void, AppFunctionResult>{
        @Override
        protected AppFunctionResult doInBackground(String... params) {
            AppFunctionResult result = new AppFunctionResult();
            if (Validation.IsActivityNetWork(getActivity())){
                String reqJson = params[0];
                try{
                    String resJson = HttpUtil.PostJosn(WebAPI.getWebAPI(WebAPI.APP_FUNCTION), reqJson);
                    result = new Gson().fromJson(resJson, AppFunctionResult.class);
                } catch (Exception ex){
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
        protected void onPostExecute(AppFunctionResult appFunctionResult) {
            showProgress(false);
            if (Result.SUCCESS.equals(appFunctionResult.getSuccess())){
                mBanners = appFunctionResult.getTopList();
                mFunctions = appFunctionResult.getBodyList();
                mNotices = appFunctionResult.getFootList();

                initTopAdvView();
                initFuncView();
                initNoticeView();
            } else {
                Toast.makeText(getActivity(), appFunctionResult.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 初始化顶部滚动广告栏
     */
    private void initTopAdvView(){
        mBannerViews = new ArrayList<>();
        for(final WebBanner item : AutoScrollViewPager.GetCircleModePagerSource(mBanners)){
            WebBannerView bannerView = new WebBannerView(getContext());
            bannerView.setBanner(item);
            bannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("title", item.getLabel());
                    intent.putExtra("url", item.getUrl());
                    startActivity(intent);
                }
            });

            mBannerViews.add(bannerView);
        }

        BannerAdapter adapter = new BannerAdapter();
        vpHomeTop.setAdapter(adapter);
        if (mBannerViews.size() > 1) {
            homeTopIndicator.setSnap(true);
            homeTopIndicator.setViewPager(vpHomeTop);
        }
        vpHomeTop.setCurrentItem(1);

        // 设定手动切换的速度
        vpHomeTop.setSwipeScrollDurationFactor(TOP_ADV_SWIPE_SPEED);
        // 设定自动切换的速度
        vpHomeTop.setAutoScrollDurationFactor(TOP_ADV_SWIPE_SPEED);
        // 设定自动切换的模式为轮转模式
        vpHomeTop.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);

        vpHomeTop.startAutoScroll();
    }

    /**
     * 初始化首页功能部分，每个功能行最多放置4个功能按钮，并且在最后增加一个新的功能。
     */
    private void initFuncView(){
        lytFuncContent.removeAllViews();

        int row = 0;                                      // 定义当前是第几行
        int addedFunc = 0;                                // 定义已经添加了多少功能
        boolean needLastRow = true;                       // 定义是否需要最后一行来显示增加更多的按钮，如果是正好整除则需要新增一行，否则不需要。
        List<WebBanner> funcRowSrc = new ArrayList<>();   // 每个功能行的数据源
        while (addedFunc < mFunctions.size()){
            for (int i = 0; i < FUNC_NUM_EACH_ROW; i++){
                int curIndex = row * FUNC_NUM_EACH_ROW + i;
                if (mFunctions.size() > curIndex){
                    funcRowSrc.add(mFunctions.get(curIndex));
                    addedFunc++;
                } else {
                    needLastRow = false;
                }
            }

            HomeFuncRowView funcRowView = new HomeFuncRowView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.height = (int)getResources().getDimension(R.dimen.ui_home_function_height);
            funcRowView.setLayoutParams(params);

            funcRowView.setBanners(funcRowSrc);

            // 功能按钮点击的触发的事件
            funcRowView.setFuncClickListener(new HomeFuncRowView.OnFuncClickListener() {
                @Override
                public void OnFuncClick(WebBanner banners, int index) {
                    Toast.makeText(getContext(), index + "", Toast.LENGTH_SHORT).show();
                }
            });

            // 如果不需要增加最后一行（非整除的情况），则将增加更多的按钮加到改行的下一个空余位置。
            if (!needLastRow){
                // 新增更多的事件
                funcRowView.setAddMoreClickListener(new HomeFuncRowView.OnAddMoreClickListener() {
                    @Override
                    public void OnAddMoreClick() {
                        Toast.makeText(getContext(), "default", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            lytFuncContent.addView(funcRowView);

            funcRowSrc = new ArrayList<>();
            row++;
        }

        // 如果需要增加最后一行（整除的情况），则新增一行用来显示新增更多的按钮。
        if (needLastRow){
            HomeFuncRowView funcRowView = new HomeFuncRowView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.height = (int)getResources().getDimension(R.dimen.ui_home_function_height);
            funcRowView.setLayoutParams(params);

            funcRowView.setBanners(funcRowSrc);
            // 新增更多的事件
            funcRowView.setAddMoreClickListener(new HomeFuncRowView.OnAddMoreClickListener() {
                @Override
                public void OnAddMoreClick() {
                    Toast.makeText(getContext(), "default", Toast.LENGTH_SHORT).show();
                }
            });

            lytFuncContent.addView(funcRowView);
        }
    }

    /**
     * 初始化首页通知功能部分
     */
    private void initNoticeView(){

        lytNoticeContent.setColumnCount(NOTICE_NUM_EACH_ROW);
        int rowCnt = mNotices.size() / NOTICE_NUM_EACH_ROW;
        if (mNotices.size() % NOTICE_NUM_EACH_ROW != 0){
            rowCnt++;
        }
        lytNoticeContent.setRowCount(rowCnt);

        int spacing = (int)getResources().getDimension(R.dimen.common_spacing_xsmall);
        for (int i = 0; i < mNotices.size(); i++){
            WebBannerView bannerView = new WebBannerView(getContext());

            GridLayout.Spec rowSpec = GridLayout.spec(i / NOTICE_NUM_EACH_ROW, 1);
            GridLayout.Spec columnSpec = GridLayout.spec(i % NOTICE_NUM_EACH_ROW, 1);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            params.width = (mScreenSize.x - spacing * (NOTICE_NUM_EACH_ROW - 1)) / NOTICE_NUM_EACH_ROW;
            params.height = params.width / 2;

            if (i % NOTICE_NUM_EACH_ROW == 0){
                params.setMargins(0, spacing, 0, 0);
            } else {
                params.setMargins(spacing, spacing, 0, 0);
            }
            bannerView.setLayoutParams(params);

            bannerView.setBanner(mNotices.get(i));

            lytNoticeContent.addView(bannerView);
        }
    }

    private void showProgress(boolean show){
        if (show){
            lytContent.setVisibility(View.GONE);
            loadingProgress.setVisibility(View.VISIBLE);
        } else {
            lytContent.setVisibility(View.VISIBLE);
            loadingProgress.setVisibility(View.GONE);
        }
    }

    /**
     * 顶部Banner的适配器
     */
    private class BannerAdapter extends PagerAdapter {

        public int getCount() {
            return mBannerViews.size();
        }

        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mBannerViews.get(position));
            return mBannerViews.get(position);
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
