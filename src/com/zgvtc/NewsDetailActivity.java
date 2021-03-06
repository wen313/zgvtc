package com.zgvtc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zgvtc.nets.HttpUtil;
import com.zgvtc.util.Constant;
import com.zgvtc.util.DownloadFileAsyncTask;

public class NewsDetailActivity extends Activity {
	private String TAG = "NewsDetailActivity";
	private WebView webview;
	private ProgressBar pb;
	private ImageView iv_return;
	private String date;// 新闻时间发布时间
	private String title;// 新闻标题
	private DownloadFileAsyncTask downLoadFileAsyncTask;
	private Toast mToast;

	// private ArrayList<>
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_newsdetail);
		findView();
		setUpEvent();
		Intent it = getIntent();
		date = it.getStringExtra("date");
		title = it.getStringExtra("title");
		String url = it.getStringExtra("newsurl");
		getData(url);
		// url="file:///android_asset/view.html";

	}

	public void findView() {
		webview = (WebView) this.findViewById(R.id.webview);
		pb = (ProgressBar) this.findViewById(R.id.progressBar1);
		iv_return = (ImageView) findViewById(R.id.iv_return);
	}

	private void setUpEvent() {
		this.webview.setDownloadListener(new DownloadListener() {// 下载监听器

					@Override
					public void onDownloadStart(final String url,
							String userAgent, String contentDisposition,
							String mimetype, long contentLength) {
						// TODO Auto-generated method stub
						Log.d(TAG, "下载事件");

						new AlertDialog.Builder(NewsDetailActivity.this)
								.setTitle("提示")
								.setMessage("你确认要下载文件？")
								.setPositiveButton("确认",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface paramDialogInterface,
													int paramInt) {// 下载文件
												DownLoadFile(url);
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface paramDialogInterface,
													int paramInt) {
												paramDialogInterface.dismiss();
											}
										}).show();
					}
				});

		this.pb.setVisibility(ProgressBar.VISIBLE);
		this.pb.setMax(100);
		iv_return.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (webview.canGoBack()) {
					webview.goBack();
				} else {
					NewsDetailActivity.this.finish();
				}
			}
		});
	}

	private void InitWebView() {
		// TODO Auto-generated method stub

		WebSettings localWebSettings = webview.getSettings();
		localWebSettings.setJavaScriptEnabled(true);
		localWebSettings.setSupportZoom(true);// 可以缩放
		localWebSettings.setBuiltInZoomControls(true);
		// localWebSettings.setUseWideViewPort(true);//将图片调整到适合WebView大小，设置该属性可通过双击或手指移动实现缩放
		localWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		this.webview.setScrollBarStyle(0);
		this.webview.setInitialScale(100);// 初始显示比例100%

		this.webview.setWebViewClient(new myWebViewClient());
		this.webview.setWebChromeClient(new myWebChromeClient());
	}

	void getData(String url) {
		Log.d(TAG, "url地址：" + url);
		HttpUtil.get(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Throwable error,
					String content) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, error, content);
				showToast(Constant.WARNING);
				Log.d(TAG, "错误码：" + statusCode + ",错误：" + error.getMessage()
						+ ",内容：" + content);
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, response);
				try {
					String content = URLDecoder.decode(response
							.getString("newstext"));
					String writer = URLDecoder.decode(response
							.getString("writer"));// 作者
					String befrom = URLDecoder.decode(response
							.getString("befrom"));// 来源
					String viewhtml = getFromAssets(Constant.VIEWHTML);
					Log.d(TAG, "html内容：" + content);

					// 格式替换
					content = content
							.replaceAll("<p>&nbsp;</p>", "</br>")
							.replaceAll("<div>&nbsp;",
									"<div>&nbsp;&nbsp;&nbsp;")
							.replaceAll("\\\\", "");

					// 替换内容 时间 作者 来源
					viewhtml = viewhtml.replace("Loading...", content)
							.replace("**[time]**", date)
							.replace("**[author]**", writer)
							.replace("**[source]**", befrom)
							.replace("**[title]**", title);

					InitWebView();
					NewsDetailActivity.this.webview.loadData(viewhtml,
							"text/html", "utf-8");
					Log.d(TAG, "替换后：" + viewhtml);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	class myWebViewClient extends WebViewClient {
		private myWebViewClient() {
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			if (NewsDetailActivity.this.pb.getVisibility() != ProgressBar.VISIBLE)
				return;
			NewsDetailActivity.this.pb.setVisibility(ProgressBar.GONE);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			NewsDetailActivity.this.pb.setVisibility(ProgressBar.VISIBLE);
			NewsDetailActivity.this.pb.setMax(100);
			return false;
		}

	}

	/**
	 * 显示Toast消息
	 * 
	 * @param msg
	 */
	private void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	/**
	 * 获取Assets下的文件
	 * 
	 * @param fileName
	 * @return
	 */
	private String getFromAssets(String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(
					getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			return "error";
		}
	}

	private void DownLoadFile(String url) {
		Log.d(TAG, "url:" + url);
		downLoadFileAsyncTask = new DownloadFileAsyncTask(NewsDetailActivity.this);
		downLoadFileAsyncTask.execute(url);
	}


	class myWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			// CustomDialog.this.setProgress(newProgress * 1000);
			NewsDetailActivity.this.pb.setProgress(newProgress);
			if (newProgress == 100)
				NewsDetailActivity.this.pb.setVisibility(ProgressBar.GONE);
			NewsDetailActivity.this.pb.postInvalidate();
			super.onProgressChanged(view, newProgress);
		}
	}

}
