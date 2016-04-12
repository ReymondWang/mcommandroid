package com.purplelight.mcommunity.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.purplelight.mcommunity.R;
import com.purplelight.mcommunity.entity.WebBanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 首页H5功能行
 * Created by wangyn on 16/4/11.
 */
public class HomeFuncRowView extends LinearLayout {

    @InjectView(R.id.funcOne) WebBannerView funcOne;
    @InjectView(R.id.funcTwo) WebBannerView funcTwo;
    @InjectView(R.id.funcThree) WebBannerView funcThree;
    @InjectView(R.id.funcFour) WebBannerView funcFour;

    private List<WebBannerView> mBannerViews = new ArrayList<>();
    private List<WebBanner> mBanners = new ArrayList<>();

    private OnFuncClickListener mFuncClickListener;
    private OnAddMoreClickListener mAddMoreClickListener;

    private WebBannerView mDefaultView;

    public HomeFuncRowView(Context context) {
        this(context, null);
    }

    public HomeFuncRowView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.item_home_func_row, this);
        ButterKnife.inject(this);

        mBannerViews.add(funcOne);
        mBannerViews.add(funcTwo);
        mBannerViews.add(funcThree);
        mBannerViews.add(funcFour);
    }

    public void setBanners(List<WebBanner> banners){
        mBanners = banners;
        initView();
    }

    public void setFuncClickListener(OnFuncClickListener funcClickListener){
        mFuncClickListener = funcClickListener;
        initEvent();
    }

    public void setAddMoreClickListener(OnAddMoreClickListener addMoreClickListener){
        if (mDefaultView != null){
            mAddMoreClickListener = addMoreClickListener;
            mDefaultView.setOnBannerClickEvent(new WebBannerView.OnBannerClickListener() {
                @Override
                public void onBannerClick(WebBanner banner) {
                    mAddMoreClickListener.OnAddMoreClick();
                }
            });
        }
    }

    private void initView(){
        setDefaultView();

        int totalSize = mBanners == null ? 0 : mBanners.size();
        for(int i = 0; i < totalSize; i++){
            if (i < mBannerViews.size()){
                mBannerViews.get(i).setBanner(mBanners.get(i));
            }
        }

        if (totalSize < 4){
            setAddMoreViewAndEvent(mBannerViews.get(totalSize));
        }
    }

    private void initEvent(){
        if (mFuncClickListener != null){
            for(int i = 0; i < mBanners.size(); i++){
                final int index = i;
                if (i < mBannerViews.size()){
                    mBannerViews.get(i).setOnBannerClickEvent(new WebBannerView.OnBannerClickListener() {
                        @Override
                        public void onBannerClick(WebBanner banner) {
                            mFuncClickListener.OnFuncClick(banner, index);
                        }
                    });
                }
            }
        }
    }

    /**
     * 设定默认的图片
     */
    private void setDefaultView(){
        for(WebBannerView bannerView : mBannerViews){
            setImgStyle(bannerView);
            bannerView.changeImgStyle();
            bannerView.getImgBanner().setImageResource(R.drawable.ic_func_default);
        }
    }

    /**
     * 设定默认的图片和事件，即增加新应用的按钮。
     * @param imgView   设定为默认的Image
     */
    private void setAddMoreViewAndEvent(WebBannerView imgView){
        if (imgView != null){
            imgView.getImgBanner().setImageResource(R.drawable.ic_add_fuc);
            mDefaultView = imgView;
        }
    }

    /**
     * 设定WebBannerView的显示样式
     * @param imgView    要设定的WebBannerView
     */
    private void setImgStyle(WebBannerView imgView){
        int parentHeight = getLayoutParams().height;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        params.height = parentHeight;

        imgView.setLayoutParams(params);
        imgView.setImgHeight((int)getResources().getDimension(R.dimen.ui_category_icon_size));
        imgView.setImgWidth((int)getResources().getDimension(R.dimen.ui_category_icon_size));
    }

    public interface OnFuncClickListener{
        void OnFuncClick(WebBanner banners, int index);
    }

    public interface OnAddMoreClickListener{
        void OnAddMoreClick();
    }
}
