package com.zgvtc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zgvtc.util.AppApplication;
import com.zgvtc.util.OpenFiles;
import com.zgvtc.util.ToastUtil;

public class DownLoadManageActivity extends Activity {
	private ImageView iv_return;
	private ListView lv_download;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_download_manage);
		setUpView();
	}

	private void setUpView() {
		iv_return = (ImageView) this.findViewById(R.id.iv_return);
		lv_download = (ListView) this.findViewById(R.id.lv_download);
		iv_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DownLoadManageActivity.this.finish();
			}
		});

		lv_download.setAdapter(new DownloadListAdapter(this,
				getAllFiles(AppApplication.mDownloadPath)));
	}

	private List<Map<String, Object>> getAllFiles(String Path) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		File[] files = new File(Path).listFiles();
		for (int i = 0; i < files.length; ++i) {
			File f = files[i];
			if (f.isFile())// 是文件
			{
				Map<String, Object> map = new HashMap<String, Object>();
				String extension = "";
				String filename = f.getName();
				int dot = filename.lastIndexOf('.');
				if ((dot > -1) && (dot < (filename.length() - 1))) {
					extension = filename.substring(dot + 1); // 后缀名
				}
				map.put("filename", filename);
				map.put("extension", extension);
				map.put("file_time", new SimpleDateFormat("MM月dd日")
						.format(new Date(f.lastModified())));
				data.add(map);
			}
		}
		return data;
	}

	private boolean checkExtInStringArray(String checkItsExt, String[] fileExts) {
		for (String aExt : fileExts) {
			if (checkItsExt.endsWith(aExt))
				return true;
		}
		return false;
	}

	private void openFiles(String filename) {
		File currentPath = new File(AppApplication.mDownloadPath + "/"
				+ filename);
		if (currentPath != null && currentPath.isFile()) {
			String fileName = currentPath.toString();
			Intent intent;
			if (checkExtInStringArray(fileName,
					getResources().getStringArray(R.array.fileEndingImage))) {
				intent = OpenFiles.getImageFileIntent(currentPath);
				startActivity(intent);
			} else if (checkExtInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingWebText))) {
				intent = OpenFiles.getHtmlFileIntent(currentPath);
				startActivity(intent);
				// } else if (checkExtInStringArray(fileName, getResources()
				// .getStringArray(R.array.fileEndingPackage))) {
				// intent = OpenFiles.getApkFileIntent(currentPath);
				// startActivity(intent);

			} else if (checkExtInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingAudio))) {
				intent = OpenFiles.getAudioFileIntent(currentPath);
				startActivity(intent);
			} else if (checkExtInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingVideo))) {
				intent = OpenFiles.getVideoFileIntent(currentPath);
				startActivity(intent);
			} else if (checkExtInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingText))) {
				intent = OpenFiles.getTextFileIntent(currentPath);
				startActivity(intent);
			} else if (checkExtInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingPdf))) {
				intent = OpenFiles.getPdfFileIntent(currentPath);
				startActivity(intent);
			} else if (checkExtInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingWord))) {
				intent = OpenFiles.getWordFileIntent(currentPath);
				startActivity(intent);
			} else if (checkExtInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingExcel))) {
				intent = OpenFiles.getExcelFileIntent(currentPath);
				startActivity(intent);
			} else if (checkExtInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingPPT))) {
				intent = OpenFiles.getPPTFileIntent(currentPath);
				startActivity(intent);
			} else {
				ToastUtil.showShortToast(DownLoadManageActivity.this,
						"无法打开，请安装相应的软件！");
			}
		} else {
			ToastUtil.showShortToast(DownLoadManageActivity.this, "这不是文件");
		}
	}

	class DownloadListAdapter extends BaseAdapter {
		List<Map<String, Object>> data;
		private Context context;
		private LayoutInflater mInflater;

		public DownloadListAdapter(Context context,
				List<Map<String, Object>> data) {
			mInflater = LayoutInflater.from(context);
			this.data = data;
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
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
				holder = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.download_manage_list_item, null);
				holder.btn_open = (Button) convertView
						.findViewById(R.id.btn_open);
				holder.tv_file_time = (TextView) convertView
						.findViewById(R.id.tv_file_time);
				holder.tv_filename = (TextView) convertView
						.findViewById(R.id.tv_filename);
				holder.iv_filetype = (ImageView) convertView
						.findViewById(R.id.iv_filetype);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final HashMap<String, Object> item = (HashMap<String, Object>) data
					.get(position);
			holder.tv_filename.setText(item.get("filename").toString());
			holder.tv_file_time.setText(item.get("file_time").toString());
			holder.iv_filetype.setBackgroundResource(getFileType(item.get(
					"extension").toString()));
			holder.btn_open.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					openFiles(item.get("filename").toString());
				}
			});
			// 设置文件类型图片
			return convertView;
		}

		private int getFileType(String extesion) {
			int resourid = R.drawable.file_pic;
			switch (extesion) {
			case "jpg":
			case "png":
			case "gif":
			case "bmp":// 图片文件
				resourid = R.drawable.file_pic;
				break;
			case "rar":
				resourid = R.drawable.file_rar;
				break;
			case "zip":
				resourid = R.drawable.file_zip;
				break;
			case "doc":
			case "docx":
				resourid = R.drawable.file_word;
				break;
			default:
				break;
			}
			return resourid;//
		}

		public class ViewHolder {
			public TextView tv_filename;
			public ImageView iv_filetype;
			public TextView tv_file_time;
			public Button btn_open;
		}

	}

}
