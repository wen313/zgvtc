package com.zgvtc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zgvtc.adapter.BuddyAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DemoSchoolFragment extends Fragment {

	private String TAG = "DemoSchoolFragment";
	private ExpandableListView elv_demoschool;
	private List<Map<String, Object>> group = null;
	private List<List<Map<String, Object>>> buddy = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_demoschool, null);
		InitData();
		setUpView(view);
		return view;
	}

	private void InitData() {
		group = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img", R.drawable.key_majors);
		map.put("name", "重点专业建设");
		group.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.featured_projects);
		map.put("name", "特色项目建设");
		group.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.achievements);
		map.put("name", "成果展示");
		group.add(map);

		buddy = new ArrayList<List<Map<String,Object>>>();
		List<Map<String,Object>> child = new ArrayList<Map<String, Object>>();

		//栏目一
		Map<String, Object> childmap = new HashMap<String, Object>();
		childmap.put("title", "机械加工技术");
		childmap.put("id", 85);
		child.add(childmap);

		childmap = new HashMap<String, Object>();
		childmap.put("title", "汽车运用于维修");
		childmap.put("id", 86);
		child.add(childmap);
		
		childmap = new HashMap<String, Object>();
		childmap.put("title", "畜牧兽医");
		childmap.put("id", 87);
		child.add(childmap);
		
		buddy.add(child);
		
		//栏目二
		child = new ArrayList<Map<String, Object>>();
		childmap = new HashMap<String, Object>();
		childmap.put("title", "项目介绍");
		childmap.put("id", 118);
		child.add(childmap);

		childmap = new HashMap<String, Object>();
		childmap.put("title", "实施方案");
		childmap.put("id", 119);
		child.add(childmap);
		
		childmap = new HashMap<String, Object>();
		childmap.put("title", "建设动态");
		childmap.put("id",120);
		child.add(childmap);
		
		childmap = new HashMap<String, Object>();
		childmap.put("title", "成果展示");
		childmap.put("id", 121);
		child.add(childmap);
		
		buddy.add(child);
		
		//栏目三
		child = new ArrayList<Map<String, Object>>();
		childmap = new HashMap<String, Object>();
		childmap.put("title", "物化成果");
		childmap.put("id", 118);
		child.add(childmap);

		childmap = new HashMap<String, Object>();
		childmap.put("title", "非物化成果");
		childmap.put("id", 119);
		child.add(childmap);
		
		childmap = new HashMap<String, Object>();
		childmap.put("title", "阶段成果");
		childmap.put("id",120);
		child.add(childmap);
		
		buddy.add(child);
		
		
//		buddy = new String[][] { { "机械加工技术", "汽车运用于维修", "畜牧兽医" },
//				{ "项目介绍", "实施方案", "建设动态", "成果展示" }, { "物化成果", "非物化成果", "阶段成果" } };
	}

	private void setUpView(View view) {
		elv_demoschool = (ExpandableListView) view
				.findViewById(R.id.elv_demoschool);
		ExpandableListAdapter adapter = new BuddyAdapter(getActivity(), group,
				buddy);
		elv_demoschool.setAdapter(adapter);
//		elv_demoschool.setChildDivider(getResources().getDrawable(
//				R.drawable.iteminfo_divider));
		elv_demoschool.setGroupIndicator(null);// 将控件默认的左边箭头去掉

		// 子项单击
		elv_demoschool.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Log.d(TAG, "子项被点击");
				Log.d(TAG, "父项:"+groupPosition+",子项："+childPosition);
				Map<String,Object> childitem = buddy.get(groupPosition).get(childPosition);
				Intent it = null;
				if(groupPosition!=0){//不是第一项
					it = new Intent(getActivity(), NewsTitleActivity.class);
					it.putExtra("newsTitleUrl", "demonstrationschool/demonstration_item_list/"+childitem.get("id")+"/");
					it.putExtra("title", childitem.get("title").toString());
					getActivity().startActivity(it);
				}else{
					it = new Intent(getActivity(), BuildDetailActivity.class);
					it.putExtra("title", childitem.get("title").toString());
					it.putExtra("classid", childitem.get("id").toString());
					getActivity().startActivity(it);
				}
				return false;
			}
		});
	}

}
