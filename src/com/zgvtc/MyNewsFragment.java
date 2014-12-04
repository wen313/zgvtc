package com.zgvtc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zgvtc.bean.NewsTitleBean;
import com.zgvtc.nets.HttpUtil;
import com.zgvtc.nets.NetworkUtils;
import com.zgvtc.util.AppApplication;
import com.zgvtc.util.ConfigCache;
import com.zgvtc.util.Constant;
import com.zgvtc.view.SegmentedGroup;
import com.zgvtc.view.XListView;
import com.zgvtc.view.XListView.IXListViewListener;

public class MyNewsFragment extends Fragment implements IXListViewListener,
		OnClickListener {

	private String TAG = "MyNewsFragment";
	private Toast mToast;
	private XListView newsList;
	private ListAdapter contentAdapter;
	private SimpleAdapter sa;
	private List<NewsTitleBean> contents = new ArrayList<NewsTitleBean>();// 新闻标题
	private int index = 1;// 索引
	private String BaseUrl = "news/campus_news/";// url地址
	private SegmentedGroup segmented;
	private RelativeLayout relativelayout_top;

	// 查询
	private AlertDialog dialog;
	private View dialogView;
	// 搜索
	private RelativeLayout rl_condition_choices;
	// 查询框
	private AutoCompleteTextView act_keyword;// 搜索
	private Button btn_clear;

	private int width;
	private int height;
	WindowManager wm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_mynews, null);
		wm = getActivity().getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		setUpView(view);
		setNewsList();
		return view;
	}

	private void setUpView(View view) {
		newsList = (XListView) view.findViewById(R.id.xlv_news);
		newsList.setPullLoadEnable(true);
		newsList.setXListViewListener(this);
		segmented = (SegmentedGroup) view.findViewById(R.id.segmented);
		contentAdapter = new ListAdapter(getActivity(), contents);
		newsList.setAdapter(contentAdapter);
		rl_condition_choices = (RelativeLayout) view
				.findViewById(R.id.rl_condition_choices);
		relativelayout_top = (RelativeLayout) view
				.findViewById(R.id.relativelayout_top);
		rl_condition_choices.setOnClickListener(this);

		newsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				NewsTitleBean ntb = contents.get(position - 1);
				Intent it = new Intent(getActivity(), NewsDetailActivity.class);
				it.putExtra("newsurl", "news/news_detail/" + ntb.getId());
				it.putExtra("date", ntb.getNewstime());
				it.putExtra("title", ntb.getTitle());
				getActivity().startActivity(it);
			}
		});

		// 切换事件
		segmented.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int radioButtonId = group.getCheckedRadioButtonId();
				switch (radioButtonId) {
				case R.id.rb_message:// ͨ通知
					Log.d(TAG, "ͨ通知");
					BaseUrl = "news/notifications/";
					onRefresh();
					break;
				case R.id.rb_newscenter:// 消息中心
					Log.d(TAG, "消息中心");
					BaseUrl = "news/campus_news/";
					onRefresh();
					break;
				default:
					break;
				}
			}
		});

		// 输入搜索框
		dialog = new AlertDialog.Builder(getActivity()).create();
		// dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {// 窗口消失事件
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				relativelayout_top.setVisibility(View.VISIBLE);// 重新显示
			}
		});
		dialogView = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_search, null);

		// 关键字
		act_keyword = (AutoCompleteTextView) dialogView
				.findViewById(R.id.act_keyword);
		btn_clear = (Button) dialogView.findViewById(R.id.btn_clear);

		dialog.getWindow().setGravity(Gravity.TOP | Gravity.LEFT);
		// dialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT,
		// WindowManager.LayoutParams.FILL_PARENT);
		dialog.setView(dialogView, 0, 0, 0, 0);
		btn_clear.setOnClickListener(this);

	}

	List<Map<String, Object>> getData() {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (NewsTitleBean ntb : contents) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("date", ntb.getNewstime());
			map.put("title", ntb.getTitle());
			map.put("id", ntb.getId());
			data.add(map);
		}
		return data;
	}

	private void setNewsList() {
		final String url = BaseUrl + index;
		String cacheConfigString = ConfigCache.getUrlCache(url);
		if (cacheConfigString != null) {
			showNewsList(cacheConfigString);
		} else {
			HttpUtil.get(url, null, new AsyncHttpResponseHandler() {
				@Override
				public void onStart() {
				}

				@Override
				public void onSuccess(String result) {
					ConfigCache.setUrlCache(result, url);
					showNewsList(result);
				}

				@Override
				public void onFailure(Throwable arg0) {
					showToast(Constant.WARNING);
				}

			});
		}
	}

	private void setMoreNewsList() {
		final String url = BaseUrl + index;
		HttpUtil.get(url, null, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
			}

			@Override
			public void onSuccess(String result) {
				showNewsList(result);
			}

			@Override
			public void onFailure(Throwable arg0) {
				showToast(Constant.WARNING);
				onLoad();
			}

		});
	}

	// 解析json
	private void showNewsList(String result) {
		List<NewsTitleBean> newstitlelist = JSONArray.parseArray(result,
				NewsTitleBean.class);
		if (newstitlelist == null || newstitlelist.size() == 0) {
			newsList.setPullLoadEnable(false);
			showToast("已经到达最后一页");
		} else {
			contents.addAll(newstitlelist);
			contentAdapter.notifyDataSetChanged();
		}
		onLoad();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_condition_choices:
			relativelayout_top.setVisibility(View.GONE);// 隐藏最上面的
			initAutoComplete();// 初始化自动填充框
			dialog.show();
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.width = width; // 设置宽度
			dialog.getWindow().setAttributes(lp);
			break;
		case R.id.btn_clear:
			act_keyword.setText("");
			break;

		}
	}

	private void initAutoComplete() {
		sa = new SimpleAdapter(getActivity(), getData(),
				R.layout.news_info_list_item, new String[] { "date", "title",
						"id" }, new int[] { R.id.tv_news_date,
						R.id.tv_news_title, R.id.tv_news_id });
		act_keyword.setAdapter(sa);
		act_keyword.setDropDownHeight(height / 3);
		act_keyword.setDropDownWidth(width - 10);
		act_keyword.setThreshold(1);// 设置输入1个字符串后就出现提示
		act_keyword.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				AutoCompleteTextView view = (AutoCompleteTextView) v;
				if (hasFocus) {
					view.showDropDown();
				}
			}
		});
		act_keyword.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView tv_title = (TextView) view
						.findViewById(R.id.tv_news_title);
				String title = tv_title.getText().toString();
				String date = ((TextView) view.findViewById(R.id.tv_news_date))
						.getText().toString();
				String newsid = ((TextView) view.findViewById(R.id.tv_news_id))
						.getText().toString();

				Intent it = new Intent(getActivity(), NewsDetailActivity.class);
				it.putExtra("newsurl", "news/news_detail/" + newsid);
				it.putExtra("date", date);
				it.putExtra("title", title);
				act_keyword.setText("");
				dialog.cancel();
				getActivity().startActivity(it);
			}
		});

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		index = 1;
		newsList.setPullLoadEnable(true);// 刷新后允许加载更多
		final String url = BaseUrl + index;
		try {
			HttpUtil.get(url, null, new AsyncHttpResponseHandler() {
				@Override
				public void onStart() {
				}

				@Override
				public void onSuccess(String result) {
					ConfigCache.setUrlCache(result, url);
					contents.clear();
					showNewsList(result);
					showToast(Constant.SUCCESS);
				}

				@Override
				public void onFailure(Throwable arg0) {
					showToast(Constant.WARNING);
					onLoad();
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		// getData(BaseUrl, ++index);
		if (AppApplication.mNetWorkState == NetworkUtils.NETWORN_NONE) {
			showToast(Constant.NONETWARNING);
			onLoad();
		} else {
			++index;
			setMoreNewsList();
		}
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
			mToast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
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
			holder.tv_news_date.setText(ntb.getNewstime());
			holder.tv_news_title.setText(ntb.getTitle());
			return convertView;
		}

	}

	public class ViewHolder {
		public TextView tv_news_date;
		public TextView tv_news_title;

	}

}
