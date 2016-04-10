package com.purplelight.mcommunity.task;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.purplelight.mcommunity.result.WebResult;
import com.purplelight.mcommunity.util.HttpUtil;

import java.lang.ref.WeakReference;

/**
 * AsyncTask for downloading data from url.
 * Created by wangyn on 15/7/12.
 */
public class UrlDownloaderTask extends AsyncTask<UrlDownloaderEntity, Integer, WebResult> {

    private Class<? extends WebResult> resultClassName;

    private WeakReference<UrlDownloadedCallBack> callBackWeakReference;

    public UrlDownloaderTask(UrlDownloadedCallBack callBack, Class<? extends WebResult> resultClassName){
        callBackWeakReference = new WeakReference<>(callBack);
        this.resultClassName = resultClassName;
    }

    protected WebResult doInBackground(UrlDownloaderEntity... param){
        WebResult result = new WebResult();

        if (param != null && param.length > 0) {
            UrlDownloaderEntity entity = param[0];

            Gson gson = new Gson();
            String strResult = HttpUtil.GetDataFromNet(entity.getUrl(), entity.getUrlParams(), entity.getHttpType());
            if (!"".equals(strResult)) {
                result = gson.fromJson(strResult, WebResult.class);
                if ("0".equals(result.getCode()) && resultClassName != WebResult.class) {
                    result = gson.fromJson(strResult, resultClassName);
                }
            } else {
                result.setCode("-1");
            }
        }
        return result;
    }

    protected void onPostExecute(WebResult result) {
        if (isCancelled()){
            return;
        }
        if (callBackWeakReference.get() != null){
            callBackWeakReference.get().downloaded(result);
        }
    }

}
