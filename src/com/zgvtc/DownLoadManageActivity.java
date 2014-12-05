package com.zgvtc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zgvtc.bean.SlideViewItem;
import com.zgvtc.util.AppApplication;
import com.zgvtc.util.OpenFiles;
import com.zgvtc.util.ToastUtil;
import com.zgvtc.view.ListViewCompat;
import com.zgvtc.view.SlideView;
import com.zgvtc.view.SlideView.OnSlideListener;

public class DownLoadManageActivity extends Activity implements OnSlideListener {
	private static final String TAG = "DownLoadManageActivity";
	private ImageView iv_return;
	private ListViewCompat lvc_download;
	private SlideView mLastSlideViewWithStatusOn;
	private List<SlideViewItem> mSlideViewItems = new ArrayList<SlideViewItem>();

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
		lvc_download = (ListViewCompat) this.findViewById(R.id.lvc_download);
		iv_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DownLoadManageActivity.this.finish();
			}
		});
		getAllFiles(AppApplication.mDownloadPath);
		lvc_download.setAdapter(new DownloadListAdapter());
	}

	private void getAllFiles(String Path) {
		mSlideViewItems = new ArrayList<SlideViewItem>();
		File[] files = new File(Path).listFiles();
		for (int i = 0; i < files.length; ++i) {
			File f = files[i];
			if (f.isFile())// 是文件
			{
				SlideViewItem item = new SlideViewItem();
				String extension = "";
				String filename = f.getName();
				int dot = filename.lastIndexOf('.');
				if ((dot > -1) && (dot < (filename.length() - 1))) {
					extension = filename.substring(dot + 1); // 后缀名
				}
				item.filename = filename;
				item.extension = extension;
				item.file_time = new SimpleDateFormat("MM月dd日")
						.format(new Date(f.lastModified()));
				mSlideViewItems.add(item);
			}
		}
	}

	private boolean checkExtInStringArray(String checkItsExt, String[] fileExts) {
		for (String aExt : fileExts) {
			if (checkItsExt.endsWith(aExt))
				return true;
		}
		return false;
	}
	
	/**
	 * 删除文件
	 * @param filename
	 */
	private boolean delFile(String filename){
		boolean flag = false;  
	    File file = new File(AppApplication.mDownloadPath + "/"
				+ filename);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag; 
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
		private LayoutInflater mInflater;

		public DownloadListAdapter() {
			mInflater = getLayoutInflater();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mSlideViewItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mSlideViewItems.get(position);
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
			SlideView slideView = (SlideView) convertView;
			if (slideView == null) {
				
				View itemView = mInflater.inflate(
						R.layout.download_manage_list_item, null);
				
				slideView = new SlideView(DownLoadManageActivity.this);
				slideView.setContentView(itemView);

				holder = new ViewHolder(slideView);
				slideView.setOnSlideListener(DownLoadManageActivity.this);
				slideView.setTag(holder);
			} else {
				holder = (ViewHolder) slideView.getTag();
			}
			
			SlideViewItem item = mSlideViewItems.get(position);
			item.slideView = slideView;
			item.slideView.shrink();
			final String filename = item.filename;
			final int p = position;
			holder.tv_filename.setText(filename);
			holder.tv_file_time.setText(item.file_time);
			holder.iv_filetype
					.setBackgroundResource(getFileType(item.extension));
			holder.btn_open.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					openFiles(filename);
				}
			});
			
			holder.deleteHolder.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d(TAG, "删除按钮点击:"+p);
					//删除文件
					if(delFile(filename)){
						mSlideViewItems.remove(p);
						notifyDataSetChanged();
						ToastUtil.showShortToast(DownLoadManageActivity.this, "删除成功");
					}else{
						ToastUtil.showShortToast(DownLoadManageActivity.this, "删除失败");
					}
					
				}
			});
			// 设置文件类型图片
			return slideView;
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
			case "pdf":
				resourid = R.drawable.file_pdf;
				break;
			default:
				resourid = R.drawable.file_unknown;
				break;
			}
			return resourid;//
		}

		public class ViewHolder {
			ViewHolder(View view) {
				btn_open = (Button) view
						.findViewById(R.id.btn_open);
				tv_file_time = (TextView) view
						.findViewById(R.id.tv_file_time);
				tv_filename = (TextView) view
						.findViewById(R.id.tv_filename);
				iv_filetype = (ImageView) view
						.findViewById(R.id.iv_filetype);
				deleteHolder = (ViewGroup) view
						.findViewById(R.id.holder);
	        }
			public TextView tv_filename;
			public ImageView iv_filetype;
			public TextView tv_file_time;
			public Button btn_open;
			public ViewGroup deleteHolder;
		}

	}

	@Override
	public void onSlide(View view, int status) {
		// TODO Auto-generated method stub
		if (mLastSlideViewWithStatusOn != null
				&& mLastSlideViewWithStatusOn != view) {
			mLastSlideViewWithStatusOn.shrink();
		}

		if (status == SLIDE_STATUS_ON) {
			mLastSlideViewWithStatusOn = (SlideView) view;
		}
	}

}
