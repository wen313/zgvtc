package com.zgvtc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BuildDetailActivity extends Activity {

	private ListView list;
	private ImageView iv_return;
	List<Map<String, Object>> data;// 数据
	private String title;
	private String classid;
	private TextView tv_condition;
	private String[] itemids;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_builddetail);
		Intent it = getIntent();
		title = it.getStringExtra("title");
		classid = it.getStringExtra("classid");
		setUpView();
	}

	private void setUpView() {
		list = (ListView) findViewById(R.id.lv_admissionEmp);
		tv_condition = (TextView) findViewById(R.id.tv_condition);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		tv_condition.setText(title);
		// SimpleAdapter<String> adapter = new SimpleAdapter(getActivity(),
		// R.layout.emp_info_list_item,R.id.tv_emp_title, getData());
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.news_title_list_item, new String[] { "title" },
				new int[] { R.id.buddy_listview_child_name });
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Map<String, Object> content = data.get(position);
				content.get("id");
				Intent it = new Intent();
				it = new Intent(BuildDetailActivity.this,
						NewsTitleActivity.class);
				it.putExtra("newsTitleUrl",
						"/demonstrationschool/demonstration_item_list/"
								+ itemids[position] + "/");
				it.putExtra("title", content.get("title").toString());
				BuildDetailActivity.this.startActivity(it);
			}
		});

		iv_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BuildDetailActivity.this.finish();
			}
		});

	}

	private List<Map<String, Object>> getData() {
		data = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<>();
		map.put("title", "专业介绍");
		data.add(map);

		map = new HashMap<>();
		map.put("title", "实施方案");
		data.add(map);

		map = new HashMap<>();
		map.put("title", "人才培养与课程体系改革");
		data.add(map);

		map = new HashMap<>();
		map.put("title", "师资队伍建设");
		data.add(map);

		map = new HashMap<>();
		map.put("title", "校企合作工学结合运行机制建设");
		map.put("id", 45);
		data.add(map);

		map = new HashMap<>();
		map.put("title", "成果展示");
		data.add(map);

		switch (classid) {
		case "85":
			itemids = new String[] { "108", "109", "88", "89", "90", "110" };
			break;
		case "86":
			itemids = new String[] { "111", "112", "91", "92", "93", "113" };
			break;
		case "87":
			itemids = new String[] { "114", "115", "94", "95", "96", "116" };
			break;
		default:
			break;
		}

		return data;
	}
}
