package com.zgvtc.nets;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

public class AsyncImageLoader {

	private static final String LOG_TAG = "AsyncImageLoader";
	private HashMap<String, SoftReference<Bitmap>> imageCache;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	public Bitmap loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			Bitmap bitmap = softReference.get();
			if (bitmap != null) {
				return bitmap;
			}
		}
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(imageUrl, null, new BinaryHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, byte[] binaryData) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, binaryData);
				Bitmap bitmap =null;
				if (binaryData.length != 0) {
					bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
		        } else {
		            
		        }
				imageCallback.imageLoaded(bitmap, imageUrl);
			}
		});
		return null;
	}

	

	public interface ImageCallback {
		public void imageLoaded(Bitmap imageBitmap, String imageUrl);

	}
}
