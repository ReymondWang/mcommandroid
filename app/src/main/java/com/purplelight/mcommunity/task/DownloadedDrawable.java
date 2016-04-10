package com.purplelight.mcommunity.task;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.purplelight.mcommunity.R;

import java.lang.ref.WeakReference;

public class DownloadedDrawable extends BitmapDrawable {

	private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;
	
	public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask, Resources resources){
		super(resources, BitmapFactory.decodeResource(resources, R.drawable.cc_bg_default_image));
		bitmapDownloaderTaskReference = new WeakReference<>(bitmapDownloaderTask);
	}
	
	public BitmapDownloaderTask getBitmapDownloaderTask(){
		return bitmapDownloaderTaskReference.get();
	}
}
