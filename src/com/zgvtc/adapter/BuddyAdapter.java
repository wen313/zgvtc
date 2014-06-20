package com.zgvtc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zgvtc.R;

/**
 * 适配器
 * 
 * @author wen
 * 
 */
public class BuddyAdapter extends BaseExpandableListAdapter {

	private List<Map<String, Object>> group;
	private List<List<Map<String, Object>>> buddy;
	private Context context;
	LayoutInflater inflater;

	public BuddyAdapter(Context context, List<Map<String, Object>> group,
			List<List<Map<String, Object>>> buddy) {
		this.context = context;
		this.group = group;
		inflater = LayoutInflater.from(this.context);
		this.buddy = buddy;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return group.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return buddy.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return group.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return buddy.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = inflater
				.inflate(R.layout.buddy_listview_group_item, null);
		TextView groupNameTextView = (TextView) convertView
				.findViewById(R.id.buddy_listview_group_name);
		@SuppressWarnings("unchecked")
		Map<String, Object> content = (Map<String, Object>) getGroup(groupPosition);
		groupNameTextView.setText(content.get("name").toString());
		ImageView iv_icon = (ImageView) convertView
				.findViewById(R.id.buddy_listview_icon);
		iv_icon.setImageResource((int) content.get("img"));
		ImageView image = (ImageView) convertView
				.findViewById(R.id.buddy_listview_image);
		image.setImageResource(R.drawable.down_accessory);
		// 更换展开分组图片
		if (!isExpanded) {
			image.setImageResource(R.drawable.up_accessory);
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = inflater
				.inflate(R.layout.buddy_listview_child_item, null);
		TextView nameTextView = (TextView) convertView
				.findViewById(R.id.buddy_listview_child_name);
		@SuppressWarnings("unchecked")
		Map<String, Object> childcontent = (Map<String, Object>) getChild(
				groupPosition, childPosition);
		nameTextView.setText(childcontent.get("title").toString());
		return convertView;
	}

	/**
	 * 子项是否可以选择
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}