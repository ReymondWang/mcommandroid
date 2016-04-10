package com.purplelight.mcommunity.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.purplelight.mcommunity.constant.WebServiceAPI;
import com.purplelight.mcommunity.task.BitmapDownloaderTask;
import com.purplelight.mcommunity.task.DownloadedDrawable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageHelper {
	private static final String TAG = "ImageHelper";
	private static final int DISK_MAX_SIZE = 32 * 1024 * 1024;// SD 32MB
	public static final String CACHE_PATH = Environment.getExternalStorageDirectory().toString() + "/mcommunity/";

	private final static LruCache<String, Bitmap> mMemoryCache;
	private final static SimpleDiskLruCache mDiskCache;
	static{
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap bitmap){
				return bitmap.getByteCount() / 1024;
			}
		};

		File cacheDir = new File(CACHE_PATH);
		mDiskCache = SimpleDiskLruCache.openCache(cacheDir, DISK_MAX_SIZE);
	}

	public static void clear(){
		mDiskCache.clearCache();
	}

	public static void addBitmapToCache(String key, Bitmap bitmap){
		addBitmapToCache(key, bitmap, false);
	}

	public static void addBitmapToCache(String key, Bitmap bitmap, boolean forceReplaced){
		if (bitmap != null){
			synchronized(mMemoryCache){
				if(forceReplaced){
					mMemoryCache.remove(key);
				}
				Bitmap bmp = mMemoryCache.get(key);
				if (bmp == null || bmp.isRecycled()) {
					Log.i(TAG, "Set bitmap into key:" + key);
					mMemoryCache.put(key, bitmap);
				}
			}
			synchronized (mDiskCache) {
				if (!mDiskCache.containsKey(key) || forceReplaced) {
					Log.i(TAG, "put bitmap into SD key = " + key);
					mDiskCache.put(key, bitmap);
				}
			}
		}
	}

	public static Bitmap getBitmapFromCache(String key){
		if (key == null){
			return null;
		}

		synchronized (mMemoryCache) {
			Bitmap bitmap = mMemoryCache.get(key);
			if (bitmap != null && !bitmap.isRecycled()) {
				Log.i(TAG, "Get bitmap from key:" + key);
				return bitmap;
			}
		}

		synchronized (mDiskCache){
			Bitmap bitmap = mDiskCache.get(key);
			if (bitmap != null){
				Log.i(TAG, "Get bitmap from SD key:" + key);
				return bitmap;
			}
		}

		return null;
	}

	public static String GetImageUrl(String fileName){
//		IServerInfoModel serverInfoModel = new ServerInfoModel();
//		return serverInfoModel.getUrl() + "HeadImage/" + fileName + ".jpg";
		return fileName;
	}

	public static String UploadImage(String fileName, Context context){
		String retMsg = "";

		try {
			FileInputStream fStream = new FileInputStream(mDiskCache.getFile(GetImageUrl(fileName)));
			int bufferSize = 100 * 1024;
			byte[] buffer =new byte[bufferSize];
			int count = 0;
			int i = 0;
			while((count = fStream.read(buffer)) >= 0){
				String uploadBuffer=new String(Base64.encode(buffer, 0, count, Base64.DEFAULT));
				uploadService(uploadBuffer, fileName, i, context);
				i++;
			}
			fStream.close();

		} catch (IOException e) {
			retMsg = e.getMessage();
			Log.e(TAG, e.getMessage());
		}

		return retMsg;
	}

	private static void uploadService(String image, String fileName, int tag, Context context){
		SoapObject request = new SoapObject(WebServiceAPI.NAMESPACE, WebServiceAPI.getMethodName(WebServiceAPI.UPLOAD_IMAGE) );
		request.addProperty("fileName", fileName);
		request.addProperty("image", image);
		request.addProperty("tag", tag);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = envelope;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		try {
			HttpTransportSE ht = new HttpTransportSE(WebServiceAPI.getServiceUrl(WebServiceAPI.UPLOAD_IMAGE, context));
			ht.call(WebServiceAPI.getFullMethodName(WebServiceAPI.UPLOAD_IMAGE), envelope);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqHeight){
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqHeight){
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapResource(Resources res, int resId, int reqWidth, int reqHeight){
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static Bitmap loadBitmapFromNet(String imageUrlStr) {
		final HttpClient client = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(imageUrlStr);
		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w(TAG, "Error " + statusCode + " while retrieving bitmap from " + imageUrlStr);
				return null;
			}
			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					return BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			getRequest.abort();
			Log.w(TAG, "Error while retrieving bitmap from " + imageUrlStr + "; " + e.toString());
		}
		return null;
	}

	public static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView){
		if (imageView != null){
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable){
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	public static boolean cancelPotentialDownload(String url, ImageView imageView){
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
		if (bitmapDownloaderTask != null){
			String bitmapUrl = bitmapDownloaderTask.getUrl();
			if (Validation.IsNullOrEmpty(bitmapUrl)){
				bitmapDownloaderTask.cancel(true);
			}else{
				return false;
			}
		}
		return true;
	}

	public static Bitmap CompressImageToSize(Bitmap image, int height, int width) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if( baos.toByteArray().length / 1024 > 1024) {
			baos.reset();
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		int be = 1;
		if (w > h && w > width) {
			be = newOpts.outWidth / width;
		} else if (w < h && h > height) {
			be = newOpts.outHeight / height;
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return CompressImage(bitmap);
	}

	public static Bitmap CompressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100 && options <= 0) {
			int i = baos.toByteArray().length / 1024;
			baos.reset();
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
			options -= 10;
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		return BitmapFactory.decodeStream(isBm, null, null);
	}

	public static byte[] GetBytesFromBitmap(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
}
