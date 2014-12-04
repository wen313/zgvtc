package com.zgvtc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import javax.xml.transform.Templates;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

public class DownloadFileAsyncTask extends
		AsyncTask<String, Integer, String> {
	private Context context;
	private ProgressDialog mProgressDialog; // 用来显示进度的对话框
	private String filePath;
	private String fileExt = "apk"; // 这里可以修改成自己需要的文件类型
	private String filePrefix = "fmf"; // 这里可以修改成自己想要的文件名前缀

	public DownloadFileAsyncTask(Context context) {
		this.setContext(context);
	}

	public void setContext(Context context) {
		this.context = context;
		initProgressDialog();
	}

	// 初始化进度对话框
	private void initProgressDialog() {
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage("Downloading...");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	}

	// 关闭进度对话框
	public void closeProgressDialog() {
		mProgressDialog.cancel();
		mProgressDialog.dismiss();
	}

	@Override
	protected String doInBackground(String... params) {
		// 第一个参数是要下载的文件名。如http://www.test.com/download/1.apk
		// 第二个参数是要下载的文件的大小。如1024
		String strUrl = params[0];

		try {
			URL url = new URL(strUrl);
			
			URLConnection conn = url.openConnection();
			conn.connect();
			// 如果getContentLength能取到文件长度的话，其实params的第二个参数是没有需要的。
			// 可以改成String...而不需要用List<String>...
			int nFileLength = conn.getContentLength();
			if (nFileLength <= 0)
				nFileLength = 1;

			InputStream is = conn.getInputStream();
			if (is == null) {
				throw new RuntimeException("stream is null");
			}
			String filename = strUrl.substring(strUrl.lastIndexOf('/') + 1);
			if(filename==null || "".equals(filename.trim())){//如果获取不到文件名称
				filename = UUID.randomUUID()+ ".tmp";//默认取一个文件名
			}
			File tempFile = new File(AppApplication.mDownloadPath+"/"+filename);
			filePath = tempFile.getAbsolutePath();
			System.out.println("路径:"+filePath);
			int nTotal = 0;
			FileOutputStream output = new FileOutputStream(tempFile);
			byte data[] = new byte[1024];
			do {
				int nRead = is.read(data);
				if (nRead <= 0) {
					break;
				}
				output.write(data, 0, nRead);
				nTotal += nRead;
				publishProgress((int) (nTotal * 100 / nFileLength));
			} while (true);
			try {
				is.close();
			} catch (Exception e) {
			}
		} catch (Exception e) {

		}
		return filePath;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog.show();
		mProgressDialog.setProgress(0);
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		mProgressDialog.setProgress(progress[0]);
	}

	@Override
	protected void onPostExecute(String strPath) {
		super.onPostExecute(strPath);
		closeProgressDialog(); // 重点在这句，关闭进度对话框
		if(mProgressDialog.getProgress()==mProgressDialog.getMax()){
			ToastUtil.showShortToast(context, "下载成功");
		}else{
			ToastUtil.showShortToast(context, "下载失败,请重试");
		}
		
		//File f = new File(strPath);
//		if (f.exists())
//			PublicOP.openFile(f, (Activity) this.context); // 打开这个文件
	}
}
