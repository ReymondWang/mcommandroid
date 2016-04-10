package com.purplelight.mcommunity.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purplelight.mcommunity.R;
import com.purplelight.mcommunity.component.view.ToggleSwipeLayout;
import com.purplelight.mcommunity.component.view.WebBannerView;
import com.purplelight.mcommunity.component.widget.AutoScrollViewPager;
import com.purplelight.mcommunity.component.widget.CirclePageIndicator;
import com.purplelight.mcommunity.component.widget.PageIndicator;
import com.purplelight.mcommunity.entity.WebBanner;

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

    @InjectView(R.id.swipeRefreshLayout) ToggleSwipeLayout swipeRefreshLayout;
    @InjectView(R.id.vpHomeTop) AutoScrollViewPager vpHomeTop;
    @InjectView(R.id.homeTopIndicator) CirclePageIndicator homeTopIndicator;

    private List<WebBannerView> mBannerViews;

    public static HomeFragment Create(){
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.inject(this, rootView);

        // 取得手机屏幕的宽度，并将顶部轮播广告位的比例设置为2:1
        Point outSize = new Point();
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(outSize);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                , FrameLayout.LayoutParams.MATCH_PARENT);
        params.height = outSize.x / 2;
        vpHomeTop.setLayoutParams(params);
        vpHomeTop.setCycle(true);

        // 当手指滑动区域在顶部轮播广告位时，不触发刷新事件。
        swipeRefreshLayout.setHeight(params.height);

        test();

        BannerAdapter adapter = new BannerAdapter();
        vpHomeTop.setAdapter(adapter);
        if (mBannerViews.size() > 1) {
            homeTopIndicator.setSnap(true);
            homeTopIndicator.setViewPager(vpHomeTop);
        }
        vpHomeTop.setCurrentItem(1);

        // 设定手动切换的速度
        vpHomeTop.setSwipeScrollDurationFactor(4);
        // 设定自动切换的速度
        vpHomeTop.setAutoScrollDurationFactor(4);
        // 设定自动切换的模式为轮转模式
        vpHomeTop.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);

        vpHomeTop.startAutoScroll();

        return rootView;
    }


    private void test(){

        String[] images = new String[]{
                "http://d.hiphotos.baidu.com/image/pic/item/32fa828ba61ea8d35e634fb8960a304e241f58cc.jpg",
                "http://g.hiphotos.baidu.com/image/pic/item/d31b0ef41bd5ad6e2b0f8ff583cb39dbb6fd3c3f.jpg",
                "http://d.hiphotos.baidu.com/image/pic/item/c2fdfc039245d688d7844ea2a6c27d1ed21b2435.jpg"
        };

        List<WebBanner> banners = new ArrayList<>();
        for(String img : images){
            WebBanner banner = new WebBanner();
            banner.setImage(img);
            banner.setUrl("http://hanyu.iciba.com/wiki/26565.shtml");

            banners.add(banner);
        }

        mBannerViews = new ArrayList<>();
        for(WebBanner item : AutoScrollViewPager.GetCircleModePagerSource(banners)){
            WebBannerView bannerView = new WebBannerView(getContext());
            bannerView.setBanner(item);
            mBannerViews.add(bannerView);
        }
    }

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
