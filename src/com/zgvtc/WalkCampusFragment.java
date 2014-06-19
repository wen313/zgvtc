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
				switch(position){
				case 0://ѧУ�ſ�
					it = new Intent(getActivity(), NewsClassDetailActivity.class);
					it.putExtra("newsurl", "walkintocampus/campus_overview/");
					it.putExtra("title", "ѧУ�ſ�");
					getActivity().startActivity(it);
					break;
				case 1://ѧУ����˼·
					it = new Intent(getActivity(), NewsClassDetailActivity.class);
					it.putExtra("newsurl", "walkintocampus/campus_workpolicy/");
					it.putExtra("title", "ѧУ����˼·");
					getActivity().startActivity(it);
					break;
				case 2://У԰��ò
					it = new Intent(getActivity(),CampustyleActivity.class);
					it.putExtra("newsurl", "walkintocampus/campus_landscape/");
					it.putExtra("title", "У԰��ò");
					getActivity().startActivity(it);
					break;
				case 3://ѧУ����
					it = new Intent(getActivity(), NewsTitleActivity.class);
					it.putExtra("newsTitleUrl", "walkintocampus/campus_organization/");
					it.putExtra("title", "ѧУ����");
					getActivity().startActivity(it);
					break;
				case 4://ѧУ����
					it = new Intent(getActivity(), NewsTitleActivity.class);
					it.putExtra("newsTitleUrl", "walkintocampus/campus_honours/");
					it.putExtra("title", "ѧУ����");
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
		map.put("title", "ѧУ�ſ�");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.campus_workpolicy);
		map.put("title", "ѧУ����˼·");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.campus_landscape);
		map.put("title", "У԰��ò");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.campus_organization);
		map.put("title", "ѧУ����");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.campus_honours);
		map.put("title", "ѧУ����");
		list.add(map);
		return list;
	}
}
