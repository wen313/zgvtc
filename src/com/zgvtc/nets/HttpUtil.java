package com.zgvtc.nets;

import android.content.Context;
import android.graphics.Bitmap;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zgvtc.util.Constant;

public class HttpUtil {

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		System.out.println("url��ַ��" + getAbsoluteUrl(url));
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return Constant.WEBVIE_WBASEURL + relativeUrl;
	}

	// //=============================��ȡͼƬ========================================
	/*** ���һ��ͼƬ,�������ط���ȡ,�������ڴ滺��,Ȼ�����ļ�����,���������ȡ ***/
	public static Bitmap getBitmap(String url,Context context) {
		// ���ڴ滺���л�ȡͼƬ
		ImageMemoryCache memoryCache = new ImageMemoryCache(context);
		Bitmap result = memoryCache.getBitmapFromCache(url);
		if (result == null) {
			// �ļ������л�ȡ
			ImageFileCache fileCache = new ImageFileCache();
			result = fileCache.getImage(url);
			if (result == null) {
				// �������ȡ
				result = ImageGetFromHttp.downloadBitmap(url);
				if (result != null) {
					fileCache.saveBitmap(result, url);
					memoryCache.addBitmapToCache(url, result);
				}
			} else {
				// ��ӵ��ڴ滺��
				memoryCache.addBitmapToCache(url, result);
			}
		}
		return result;
	}

}
