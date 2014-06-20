package com.zgvtc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zgvtc.bean.NewsTitleBean;
import com.zgvtc.nets.HttpUtil;
import com.zgvtc.util.Constant;
import com.zgvtc.view.XListView;
import com.zgvtc.view.XListView.IXListViewListener;

public class NewsTitleActivity extends Activity implements IXListViewListener,
		OnClickListener {

	private String TAG = "NewsTitleActivity";
	private Toast mToast;
	private XListView newsList;
	private TextView tv_title;
	private ImageView iv_return;// 返回按钮
	private ListAdapter contentAdapter;
	private List<NewsTitleBean> contents = new ArrayList<NewsTitleBean>();// 新闻标题
	private int index = 1;// 页数
	private String BaseUrl;// url地址
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent it = getIntent();
		BaseUrl = it.getStringExtra("newsTitleUrl");
		title = it.getStringExtra("title");
		setContentView(R.layout.activity_newstitle);
		getData(BaseUrl, index);
		setUpView();
	}

	private void setUpView() {
		newsList = (XListView) this.findViewById(R.id.xlv_news);
		newsList.setPullLoadEnable(true);
		newsList.setXListViewListener(this);
		tv_title = (TextView) this.findViewById(R.id.tab5_rl_tv);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		contentAdapter = new ListAdapter(this, contents);
		newsList.setAdapter(contentAdapter);
		newsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				NewsTitleBean ntb = contents.get(position - 1);
				Intent it = new Intent(NewsTitleActivity.this,
						NewsDetailActivity.class);
				it.putExtra("newsurl", "news/news_detail/" + ntb.getId());
				it.putExtra("date", ntb.getNewspath());
				it.putExtra("title", ntb.getTitle());
				NewsTitleActivity.this.startActivity(it);
			}
		});

		// 返回退出
		iv_return.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NewsTitleActivity.this.finish();
			}
		});

		tv_title.setText(title);

	}

	void getData(String url, int page) {
		HttpUtil.get(url + page, null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Throwable error,
					String content) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, error, content);
				showToast(Constant.WARNING);
				onLoad();
				Log.d(TAG, "错误码：" + statusCode + ",错误：" + error.getMessage()
						+ ",内容：" + content);
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				List<NewsTitleBean> newstitlelist = JSONArray.parseArray(
						content, NewsTitleBean.class);
				if (newstitlelist == null || newstitlelist.size() == 0) {
					newsList.setPullLoadEnable(false);
					showToast("已经到达最后一页");
				} else {
					contents.addAll(newstitlelist);
					contentAdapter.notifyDataSetChanged();
				}
				onLoad();

			}

		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		index = 1;
		contents.clear();
		newsList.setPullLoadEnable(true);// 刷新后允许加载更多
		getData(BaseUrl, index);

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		getData(BaseUrl, ++index);
	}

	private void onLoad() {
		newsList.stopRefresh();
		newsList.stopLoadMore();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		newsList.setRefreshTime(date);
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

	public class ListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private List<NewsTitleBean> contents;

		public ListAdapter(Context context) {
			super();
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(context);
		}

		public ListAdapter(Context context, List<NewsTitleBean> contents) {
			super();
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(context);
			this.contents = contents;
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
				convertView = mInflater.inflate(R.layout.news_info_list_item,
						null);
				holder = new ViewHolder();
				holder.tv_news_date = (TextView) convertView
						.findViewById(R.id.tv_news_date);
				holder.tv_news_title = (TextView) convertView
						.findViewById(R.id.tv_news_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position % 2 == 0) {
				convertView.setBackgroundColor(getResources().getColor(
						R.color.info_panel_bg));
			} else {
				convertView.setBackgroundColor(getResources().getColor(
						android.R.color.white));
			}
			NewsTitleBean ntb = contents.get(position);
			holder.tv_news_date.setText(ntb.getNewspath());
			holder.tv_news_title.setText(ntb.getTitle());
			return convertView;
		}

	}

	public class ViewHolder {
		public TextView tv_news_date;
		public TextView tv_news_title;

	}

}
