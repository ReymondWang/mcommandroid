package com.purplelight.mcommunity.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
        mBannerClickListener.onBannerClick(mBanner);
    }

    public void setOnBannerClickEvent(OnBannerClickListener bannerClickListener){
        if (mBannerClickListener != null){
            mBannerClickListener = bannerClickListener;
        }
    }

    public interface OnBannerClickListener{
        void onBannerClick(WebBanner banner);
    }

    private void initView(){
        if (mBanner != null){
            if (!Validation.IsNullOrEmpty(mBanner.getImage())){
                BitmapDownloaderTask task = new BitmapDownloaderTask(imgBanner);
                DownloadedDrawable drawable = new DownloadedDrawable(task, getResources());
                imgBanner.setImageDrawable(drawable);
                task.execute(ImageHelper.GetImageUrl(mBanner.getImage()));
            }

            if (!Validation.IsNullOrEmpty(mBanner.getLabel())){
                txtBanner.setText(mBanner.getLabel());
            }

            imgBanner.setOnClickListener(this);
            setOnClickListener(this);
        }
    }
}
