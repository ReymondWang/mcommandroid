package com.purplelight.mcommunity.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purplelight.mcommunity.R;
import com.purplelight.mcommunity.entity.WebBanner;
import com.purplelight.mcommunity.task.BitmapDownloaderTask;
import com.purplelight.mcommunity.task.DownloadedDrawable;
import com.purplelight.mcommunity.util.ImageHelper;
import com.purplelight.mcommunity.util.Validation;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 用于显示从服务器上下载先来的图片信息。
 * Created by wangyn on 16/4/10.
 */
public class WebBannerView extends LinearLayout implements View.OnClickListener {

    @InjectView(R.id.imgBanner) ImageView imgBanner;
    @InjectView(R.id.txtBanner) TextView txtBanner;

    private WebBanner mBanner;
    private OnBannerClickListener mBannerClickListener;

    private int mImgHeight, mImgWidth;

    private boolean hasSetStyle = false;     // 是否已经设定过样式

    public WebBannerView(Context context) {
        this(context, null);
    }

    public WebBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.item_web_banner_view, this);
        ButterKnife.inject(this);
    }

    public void setBanner(WebBanner banner){
        mBanner = banner;
        initView();
    }

    @Override
    public void onClick(View v) {
        if (mBannerClickListener != null){
            mBannerClickListener.onBannerClick(mBanner);
        }
    }

    public void setOnBannerClickEvent(OnBannerClickListener bannerClickListener){
        mBannerClickListener = bannerClickListener;
        if (mBannerClickListener != null){
            setOnClickListener(this);
            imgBanner.setOnClickListener(this);
        }
    }

    public void setImgScaleStyle(ImageView.ScaleType type){
        imgBanner.setScaleType(type);
    }

    public void setImgClickable(boolean clickable){
        imgBanner.setClickable(clickable);
    }

    public ImageView getImgBanner(){
        return imgBanner;
    }

    public void setImgHeight(int height){
        mImgHeight = height;
    }

    public void setImgWidth(int width){
        imgBanner.setMaxWidth(width);
        mImgWidth = width;
    }

    /**
     * 将setImgHeight和setImgWidth设定的高度最终设定到imgBanner上。
     */
    public void changeImgStyle(){
        if (mImgHeight != 0 || mImgWidth != 0){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (mImgHeight != 0){
                params.height = mImgHeight;
            }
            if (mImgWidth != 0){
                params.width = mImgWidth;
            }
            imgBanner.setLayoutParams(params);

            hasSetStyle = true;
        }
    }

    public interface OnBannerClickListener{
        void onBannerClick(WebBanner banner);
    }

    private void initView(){
        // 如果没有手动设定过样式，则在绑定数据源的是否自动设定样式。
        if (!hasSetStyle){
            changeImgStyle();
        }

        if (mBanner != null){
            if (!Validation.IsNullOrEmpty(mBanner.getImage())){
                BitmapDownloaderTask task = new BitmapDownloaderTask(imgBanner);
                DownloadedDrawable drawable = new DownloadedDrawable(task, getResources());
                imgBanner.setImageDrawable(drawable);
                task.execute(ImageHelper.GetImageUrl(mBanner.getImage()));
            }

            if (!Validation.IsNullOrEmpty(mBanner.getLabel())){
                txtBanner.setVisibility(View.VISIBLE);
                txtBanner.setText(mBanner.getLabel());
            } else {
                txtBanner.setVisibility(View.GONE);
            }
        }
    }

}
