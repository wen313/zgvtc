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

public class WalkCampusFragment extends Fragment {

	private ListView walkcampuslist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_walkcampus, null);
		setUpView(view);
		return view;
	}

	private void setUpView(View view) {
		walkcampuslist = (ListView) view.findViewById(R.id.lv_walkcampus);
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), getData(),
				R.layout.walkcampus_list_item, new String[] { "img", "title" },
				new int[] { R.id.iv_icon, R.id.tv_title });

		walkcampuslist.setAdapter(adapter);
		walkcampuslist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent it;
				switch (position) {
				case 0:// 学校概况
					it = new Intent(getActivity(),
							NewsClassDetailActivity.class);
					it.putExtra("newsurl", "walkintocampus/campus_overview/");
					it.putExtra("title", "学校概况");
					getActivity().startActivity(it);
					break;
				case 1:// 学校工作思路
					it = new Intent(getActivity(),
							NewsClassDetailActivity.class);
					it.putExtra("newsurl", "walkintocampus/campus_workpolicy/");
					it.putExtra("title", "学校工作思路");
					getActivity().startActivity(it);
					break;
				case 2:// 校园风貌
					it = new Intent(getActivity(), CampustyleActivity.class);
					it.putExtra("newsurl", "walkintocampus/campus_landscape/");
					it.putExtra("title", "校园风貌");
					getActivity().startActivity(it);
					break;
				case 3:// 学校机构
					it = new Intent(getActivity(), NewsTitleActivity.class);
					it.putExtra("newsTitleUrl",
							"walkintocampus/campus_organization/");
					it.putExtra("title", "学校机构");
					getActivity().startActivity(it);
					break;
				case 4:// 学校荣誉
					it = new Intent(getActivity(), NewsTitleActivity.class);
					it.putExtra("newsTitleUrl",
							"walkintocampus/campus_honours/");
					it.putExtra("title", "学校荣誉");
					getActivity().startActivity(it);
					break;
				}
			}
		});

	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img", R.drawable.campus_overview);
		map.put("title", "学校概况");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.campus_workpolicy);
		map.put("title", "学校工作思路");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.campus_landscape);
		map.put("title", "校园风貌");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.campus_organization);
		map.put("title", "学校机构");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.campus_honours);
		map.put("title", "学校荣誉");
		list.add(map);
		return list;
	}
}
