package com.zgvtc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zgvtc.adapter.BuddyAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class ShorttermTrainFragment extends Fragment {

	private String TAG = "ShorttermTrainFragment";
	private ExpandableListView elv_shorttermtrain;
	private List<Map<String, Object>> group = null;
	// ����Ŀ
	private List<List<Map<String, Object>>> buddy = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_shorttemtrain, null);

		InitData();
		setUpView(view);
		return view;
	}

	private void InitData() {
		group = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img", R.drawable.work_collection);
		map.put("name", "��������");
		map.put("id", 35);
		group.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.admission_counsel);
		map.put("name", "����������ѯ");
		group.add(map);

		buddy = new ArrayList<List<Map<String,Object>>>();
		//��Ŀһ
		List<Map<String,Object>> child = new ArrayList<Map<String, Object>>();
		buddy.add(child);
		
		//��Ŀ��
		child = new ArrayList<Map<String, Object>>();
		Map<String, Object> childmap = new HashMap<String, Object>();
		childmap.put("title", "ѧ������");
		childmap.put("id", 29);
		child.add(childmap);

		childmap = new HashMap<String, Object>();
		childmap.put("title", "��ѧ������");
		childmap.put("id", 30);
		child.add(childmap);
		
		childmap = new HashMap<String, Object>();
		childmap.put("title", "����רҵ");
		childmap.put("id", 31);
		child.add(childmap);
		
		buddy.add(child);
		
//		buddy = new String[][] { {}, { "ѧ������", "��ѧ������", "����רҵ" } };
	}

	private void setUpView(View view) {
		elv_shorttermtrain = (ExpandableListView) view
				.findViewById(R.id.elv_shorttermtrain);
		ExpandableListAdapter adapter = new BuddyAdapter(getActivity(), group,
				buddy);
		elv_shorttermtrain.setAdapter(adapter);
//		elv_shorttermtrain.setChildDivider(getResources().getDrawable(
//				R.drawable.iteminfo_divider));
		elv_shorttermtrain.setGroupIndicator(null);// ���ؼ�Ĭ�ϵ���߼�ͷȥ��

		// �����
		elv_shorttermtrain.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Log.d(TAG, "������");
				Log.d(TAG, "����:"+groupPosition+",���"+childPosition);
				Intent it = null;
				if(groupPosition!=0){//���ǵ�һ��
					Map<String,Object> childitem = buddy.get(groupPosition).get(childPosition);
					it = new Intent(getActivity(), NewsTitleActivity.class);
					it.putExtra("newsTitleUrl", "adultshorttermtraining/shorttrain_item_list/"+childitem.get("id")+"/");
					it.putExtra("title", childitem.get("title").toString());
					getActivity().startActivity(it);
				}
				return false;
			}
		});
		
		//�����¼�
		elv_shorttermtrain.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				Log.d(TAG, "group:"+groupPosition);
				if(groupPosition==0){//��һ�����͵���ͼƬҳ��
					Map<String,Object> groupItem = (Map<String, Object>) group.get(groupPosition);
					Intent it = new Intent(getActivity(), NewsTitleActivity.class);
					it.putExtra("newsTitleUrl", "adultshorttermtraining/shorttrain_item_list/"+groupItem.get("id")+"/");
					it.putExtra("title", groupItem.get("name").toString());
					getActivity().startActivity(it);
				}
				return false;
			}
		});
	}
}
