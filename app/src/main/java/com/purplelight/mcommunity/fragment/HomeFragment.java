package com.purplelight.mcommunity.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.purplelight.mcommunity.R;
import com.purplelight.mcommunity.component.view.HomeFuncRowView;
import com.purplelight.mcommunity.component.view.WebBannerView;
import com.purplelight.mcommunity.component.widget.AutoScrollViewPager;
import com.purplelight.mcommunity.component.widget.CirclePageIndicator;
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

    private final static int FUNC_NUM_EACH_ROW = 4;   // 每行显示的功能个数
    private final static int TOP_ADV_SWIPE_SPEED = 4; // 顶部广告栏的动画间隔
    private final static int NOTICE_NUM_EACH_ROW = 2; // 每行显示的通知个数

    @InjectView(R.id.vpHomeTop) AutoScrollViewPager vpHomeTop;
    @InjectView(R.id.homeTopIndicator) CirclePageIndicator homeTopIndicator;
    @InjectView(R.id.lytFuncContent) LinearLayout lytFuncContent;
    @InjectView(R.id.lytNoticeContent) GridLayout lytNoticeContent;



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

        loadingDataHandler.post(loadingDataRunnable);

        return rootView;
    }

    private final Handler loadingDataHandler = new Handler();
    private final Runnable loadingDataRunnable = new Runnable() {
        @Override
        public void run() {
            test();
            paintingViewHandle.post(paintingViewRunnable);
        }
    };

    private final Handler paintingViewHandle = new Handler();
    private final Runnable paintingViewRunnable = new Runnable() {
        @Override
        public void run() {
            initTopAdvView();
            initFuncView();
            initNoticeView();
        }
    };

    /**
     * 测试数据
     */
    private void test(){

        String[] images = new String[]{
                "http://picyun.90sheji.com/design/00/02/16/65/s_1024_54eee87bbe660.jpg",
                "http://c.hiphotos.bdimg.com/album/whcrop%3D657%2C282%3Bq%3D90/sign=f7b9153773f082022dc7c77d248bc6db/e824b899a9014c0834f4d9b10a7b02087af4f4d3.jpg",
                "http://picyun.90sheji.com/design/00/07/81/58/s_1024_552725395ecc7.jpg",
                "http://img.ui.cn/data/file/1/9/9/136991.jpg"
        };

        mBanners = new ArrayList<>();
        for(String img : images){
            WebBanner banner = new WebBanner();
            banner.setImage(img);
            banner.setUrl("http://hanyu.iciba.com/wiki/26565.shtml");

            mBanners.add(banner);
        }

        String[] funcImgs = new String[]{
                "http://www.yooyoo360.com/photo/2009-1-1/20090112114253792.jpg",
                "http://www.yooyoo360.com/photo/2009-1-1/20090112112311190.jpg",
                "http://www.iconpng.com/png/beautiful_flat_one/power.png",
                "http://www.yooyoo360.com/photo/2009-1-1/20090112111251531.jpg"
        };
        String[] funcLbls = new String[]{"图标一", "图标二", "图标三", "图标四"};

        mFunctions = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            WebBanner banner = new WebBanner();
            banner.setImage(funcImgs[i]);
            banner.setLabel(funcLbls[i]);

            mFunctions.add(banner);
        }

        String[] noticeImgs = new String[]{
                "http://pic54.nipic.com/file/20141128/17062090_115021324621_2.jpg",
                "http://a2.att.hudong.com/50/64/21300541930997136453649062547.jpg",
                "http://zb1.img.680.com/Task/2012-3/1/2186424_20123173445.jpg",
                "http://m2.quanjing.com/2m/wavebreak009/wavebreak173954.jpg",
                "http://e.hiphotos.baidu.com/zhidao/pic/item/0823dd54564e9258c5d33c2b9d82d158cdbf4e0b.jpg",
                "http://e.hiphotos.baidu.com/zhidao/pic/item/4034970a304e251f31adc265a786c9177e3e53d0.jpg"
        };

        mNotices = new ArrayList<>();
        for(String img : noticeImgs){
            WebBanner banner = new WebBanner();
            banner.setImage(img);

            mNotices.add(banner);
        }
    }

    /**
     * 初始化顶部滚动广告栏
     */
    private void initTopAdvView(){
        mBannerViews = new ArrayList<>();
        for(WebBanner item : AutoScrollViewPager.GetCircleModePagerSource(mBanners)){
            WebBannerView bannerView = new WebBannerView(getContext());
            bannerView.setBanner(item);
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
