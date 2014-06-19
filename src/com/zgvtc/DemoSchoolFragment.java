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
		map.put("name", "�ص�רҵ����");
		group.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.featured_projects);
		map.put("name", "��ɫ��Ŀ����");
		group.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.achievements);
		map.put("name", "�ɹ�չʾ");
		group.add(map);

		buddy = new ArrayList<List<Map<String,Object>>>();
		List<Map<String,Object>> child = new ArrayList<Map<String, Object>>();

		//��Ŀһ
		Map<String, Object> childmap = new HashMap<String, Object>();
		childmap.put("title", "��е�ӹ�����");
		childmap.put("id", 85);
		child.add(childmap);

		childmap = new HashMap<String, Object>();
		childmap.put("title", "����������ά��");
		childmap.put("id", 86);
		child.add(childmap);
		
		childmap = new HashMap<String, Object>();
		childmap.put("title", "������ҽ");
		childmap.put("id", 87);
		child.add(childmap);
		
		buddy.add(child);
		
		//��Ŀ��
		child = new ArrayList<Map<String, Object>>();
		childmap = new HashMap<String, Object>();
		childmap.put("title", "��Ŀ����");
		childmap.put("id", 118);
		child.add(childmap);

		childmap = new HashMap<String, Object>();
		childmap.put("title", "ʵʩ����");
		childmap.put("id", 119);
		child.add(childmap);
		
		childmap = new HashMap<String, Object>();
		childmap.put("title", "���趯̬");
		childmap.put("id",120);
		child.add(childmap);
		
		childmap = new HashMap<String, Object>();
		childmap.put("title", "�ɹ�չʾ");
		childmap.put("id", 121);
		child.add(childmap);
		
		buddy.add(child);
		
		//��Ŀ��
		child = new ArrayList<Map<String, Object>>();
		childmap = new HashMap<String, Object>();
		childmap.put("title", "�ﻯ�ɹ�");
		childmap.put("id", 118);
		child.add(childmap);

		childmap = new HashMap<String, Object>();
		childmap.put("title", "���ﻯ�ɹ�");
		childmap.put("id", 119);
		child.add(childmap);
		
		childmap = new HashMap<String, Object>();
		childmap.put("title", "�׶γɹ�");
		childmap.put("id",120);
		child.add(childmap);
		
		buddy.add(child);
		
		
//		buddy = new String[][] { { "��е�ӹ�����", "����������ά��", "������ҽ" },
//				{ "��Ŀ����", "ʵʩ����", "���趯̬", "�ɹ�չʾ" }, { "�ﻯ�ɹ�", "���ﻯ�ɹ�", "�׶γɹ�" } };
	}

	private void setUpView(View view) {
		elv_demoschool = (ExpandableListView) view
				.findViewById(R.id.elv_demoschool);
		ExpandableListAdapter adapter = new BuddyAdapter(getActivity(), group,
				buddy);
		elv_demoschool.setAdapter(adapter);
//		elv_demoschool.setChildDivider(getResources().getDrawable(
//				R.drawable.iteminfo_divider));
		elv_demoschool.setGroupIndicator(null);// ���ؼ�Ĭ�ϵ���߼�ͷȥ��

		// �����
		elv_demoschool.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Log.d(TAG, "������");
				Log.d(TAG, "����:"+groupPosition+",���"+childPosition);
				Map<String,Object> childitem = buddy.get(groupPosition).get(childPosition);
				Intent it = null;
				if(groupPosition!=0){//���ǵ�һ��
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
