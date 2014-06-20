package com.zgvtc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AdmissionEmpFragment extends Fragment {

	private ListView admissionEmplist;
	List<Map<String, Object>> data;// 数据

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_admissionemp, null);
		setUpView(view);
		return view;
	}

	private void setUpView(View view) {
		admissionEmplist = (ListView) view.findViewById(R.id.lv_admissionEmp);
		// SimpleAdapter<String> adapter = new SimpleAdapter(getActivity(),
		// R.layout.emp_info_list_item,R.id.tv_emp_title, getData());
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), getData(),
				R.layout.news_title_list_item, new String[] { "title" },
				new int[] { R.id.buddy_listview_child_name });
		admissionEmplist.setAdapter(adapter);

		admissionEmplist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Map<String, Object> content = data.get(position);
				content.get("id");
				Intent it = new Intent();
				it = new Intent(getActivity(), NewsTitleActivity.class);
				it.putExtra(
						"newsTitleUrl",
						"/admissionemployment/admission_item_list/"
								+ content.get("id") + "/");
				it.putExtra("title", content.get("title").toString());
				getActivity().startActivity(it);
			}
		});

	}

	private List<Map<String, Object>> getData() {
		data = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<>();
		map.put("title", "招生简介");
		map.put("id", 40);
		data.add(map);

		map = new HashMap<>();
		map.put("title", "专业介绍");
		map.put("id", 41);
		data.add(map);

		map = new HashMap<>();
		map.put("title", "部分就业单位");
		map.put("id", 42);
		data.add(map);

		map = new HashMap<>();
		map.put("title", "优秀毕业生");
		map.put("id", 43);
		data.add(map);

		map = new HashMap<>();
		map.put("title", "就业信息");
		map.put("id", 46);
		data.add(map);

		map = new HashMap<>();
		map.put("title", "就业政策");
		map.put("id", 47);
		data.add(map);

		return data;
	}
}
