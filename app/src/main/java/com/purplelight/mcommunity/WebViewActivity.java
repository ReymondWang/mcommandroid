package com.purplelight.mcommunity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.DownloadListener;

import com.purplelight.mcommunity.component.view.ProgressWebView;
import com.purplelight.mcommunity.component.widget.HeadBar;
import com.purplelight.mcommunity.util.Validation;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 加载网页到浏览器
 * Created by wangyn on 16/4/28.
 */
public class WebViewActivity extends BaseActivity {
    private static final String TAG = "WebViewActivity";

    @InjectView(R.id.headBar) HeadBar headBar;
    @InjectView(R.id.webView) ProgressWebView webView;

    private String title;
    private String urlStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getIntent().getStringExtra("title");
        urlStr = getIntent().getStringExtra("url");

        setContentView(R.layout.activity_webview);
        ButterKnife.inject(this);

        initView();
        initEvents();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if(webView.canGoBack()){
                webView.goBack();
            } else {
                WebViewActivity.this.finish();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void initView(){
        if (!Validation.IsNullOrEmpty(title)){
            headBar.setCenterTitle(title);
        }
        if (!Validation.IsNullOrEmpty(urlStr)){
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    if (!Validation.IsNullOrEmpty(url) && url.startsWith("http://")){
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                }
            });
            webView.loadUrl(urlStr);
        }
    }

    private void initEvents(){
        headBar.setRightHeadButtonEvent(new HeadBar.HeadButtonClickedListener() {
            @Override
            public void onHeadButtonClicked() {
                WebViewActivity.this.finish();
            }
        });
        headBar.setLeftHeadButtonEvent(new HeadBar.HeadButtonClickedListener() {
            @Override
            public void onHeadButtonClicked() {
                if(webView.canGoBack()){
                    webView.goBack();
                } else {
                    WebViewActivity.this.finish();
                }
            }
        });
    }
}
