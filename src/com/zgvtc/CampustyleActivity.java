package com.zgvtc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.zgvtc.NewsTitleActivity.ViewHolder;
import com.zgvtc.bean.NewsTitleBean;
import com.zgvtc.bean.NewsTitlePicBean;
import com.zgvtc.nets.AsyncImageLoader;
import com.zgvtc.nets.AsyncImageLoader.ImageCallback;
import com.zgvtc.nets.HttpUtil;
import com.zgvtc.nets.ImageFileCache;
import com.zgvtc.nets.ImageGetFromHttp;
import com.zgvtc.nets.ImageMemoryCache;
import com.zgvtc.util.Constant;

public class CampustyleActivity extends Activity {
	private static final String LOG_TAG = "CampustyleActivity";
	private GridView gridview;
	private ImageView iv_return;
	private String url;
	private String title;
	GridViewAdapter adapter;
	private List<NewsTitlePicBean> contents = new ArrayList<NewsTitlePicBean>();// 新闻标题

	private Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_campustyle);
		Intent it = getIntent();
		url = it.getStringExtra("newsurl");
		title = it.getStringExtra("title");
		getData();
		setUpView();
	}

	private void setUpView() {
		gridview = (GridView) findViewById(R.id.gridview);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		adapter = new GridViewAdapter(this, contents, gridview);
		gridview.setAdapter(adapter);
		iv_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CampustyleActivity.this.finish();
			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				NewsTitlePicBean ntpb = contents.get(position);
//				String imageUrl = ntpb.getNewstext();
//				Intent it = new Intent(CampustyleActivity.this,
//						CampusStyleImgDetailActivity.class);
//				it.putExtra("title", ntpb.getTitle());
//				it.putExtra("ImgUrl", ntpb.getNewstext());
				Intent it = new Intent(CampustyleActivity.this,
						CampusStyleImgDetailActivity.class);
				List<Map<String,String>> list1=new ArrayList<Map<String,String>>();
				for (NewsTitlePicBean ntpb: contents) {
					Map<String,String> map = new HashMap<>();
					map.put("title", ntpb.getTitle());
					map.put("ImageUrl", ntpb.getNewstext());
					list1.add(map);
				}
				Bundle bundle=new Bundle();
				bundle.putParcelableArrayList("contents", (ArrayList)list1);
                it.putExtras(bundle);
				it.putExtra("index", position);
				CampustyleActivity.this.startActivity(it);
			}
		});
	}

	private void getData() {
		HttpUtil.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Throwable error,
					String content) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, error, content);
				showToast(Constant.WARNING);
				Log.d(LOG_TAG, "错误码：" + statusCode + ",错误：" + error.getMessage()
						+ ",内容：" + content);
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				contents.clear();
				List<NewsTitlePicBean> newstitlelist = JSONArray.parseArray(
						content, NewsTitlePicBean.class);
				contents.addAll(newstitlelist);
				adapter.notifyDataSetChanged();
			}
		});

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

	public class GridViewAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private List<NewsTitlePicBean> contents;
		private Context context;
		// 从内存缓存中获取图片
		ImageMemoryCache memoryCache;
		ImageFileCache fileCache;
		private AsyncImageLoader asyncImageLoader;
		private GridView gridView;

		public GridViewAdapter(Context context) {
			super();
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(context);
			this.context = context;
			memoryCache = new ImageMemoryCache(context);
			fileCache = new ImageFileCache();
			asyncImageLoader = new AsyncImageLoader();
		}

		public GridViewAdapter(Context context,
				List<NewsTitlePicBean> contents, GridView gridView1) {
			super();
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(context);
			this.contents = contents;
			this.context = context;
			memoryCache = new ImageMemoryCache(context);
			fileCache = new ImageFileCache();
			this.gridView = gridView1;
			asyncImageLoader = new AsyncImageLoader();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contents.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return contents.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.campustyle_info_list_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				holder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			NewsTitlePicBean ntpb = contents.get(position);
			String imageUrl = ntpb.getTitlepic();
			// 内存加载
			Bitmap result = memoryCache.getBitmapFromCache(imageUrl);
			holder.iv_icon.setTag(imageUrl);
			if (result == null) {
				// 文件缓存中获取
				result = fileCache.getImage(imageUrl);
				if (result == null) {// 网络获取
					result = asyncImageLoader.loadDrawable(imageUrl,
							new ImageCallback() {
								public void imageLoaded(Bitmap imageBitmap,
										String imageUrl) {
									memoryCache.addBitmapToCache(imageUrl,
											imageBitmap);
									fileCache.saveBitmap(imageBitmap, imageUrl);
									ImageView imageViewByTag = (ImageView) gridView
											.findViewWithTag(imageUrl);
									if (imageViewByTag != null) {
										imageViewByTag
												.setImageBitmap(imageBitmap);
										;
									}
								}
							});
				}
			} else {
				// 添加到内存缓存
				memoryCache.addBitmapToCache(imageUrl, result);
			}
			if (result == null) {
				holder.iv_icon.setImageResource(R.drawable.loading_image);// 替换成加载的中的图片
			} else {
				holder.iv_icon.setImageBitmap(result);
			}
			// Set the text on the TextView
			// 加载图片
			holder.tv_title.setText(ntpb.getTitle());
			return convertView;
		}

	}

	public class ViewHolder {
		public ImageView iv_icon;
		public TextView tv_title;

	}

}
