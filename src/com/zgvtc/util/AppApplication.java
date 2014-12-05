package com.zgvtc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;

import com.zgvtc.base.BaseApplication;
import com.zgvtc.nets.NetworkUtils;

public class AppApplication extends BaseApplication {
	public static final String DOMAIN = "domain";
	public static final String DOMAIN_URL = "url";
	public static String mDomain = Constant.WEBVIE_WBASEURL;
	// public static String mBakeDomain =
	// "http://1.kaiyuanxiangmu.sinaapp.com/";

	private static final String DB_NAME = Constant.APPNAME + ".db";

	public static String mSdcardDataDir;
	public static String mApkDownloadUrl = null;

	@Override
	public void fillTabs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initDb() {
		// TODO Auto-generated method stub
		mSQLiteHelper = new AppSQLiteHelper(getApplicationContext(), DB_NAME, 1);
	}

	@Override
	public void initEnv() {
		// TODO Auto-generated method stub
		mAppName = Constant.APPNAME;
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/" + Constant.APPNAME + "/config/");
			if (!file.exists()) {
				if (file.mkdirs()) {
					mSdcardDataDir = file.getAbsolutePath();
				}
			} else {
				mSdcardDataDir = file.getAbsolutePath();
			}

			// 下载路径
			File downloadfile = new File(Environment
					.getExternalStorageDirectory().getPath()
					+ "/"
					+ Constant.APPNAME + "/download/");
			if (!downloadfile.exists()) {
				if (downloadfile.mkdirs()) {
					mDownloadPath = downloadfile.getAbsolutePath();
				}
			} else {
				mDownloadPath = downloadfile.getAbsolutePath();
			}

		}

		mNetWorkState = NetworkUtils.getNetworkState(this);

	}

	@Override
	public void exitApp(Context context) {
		// TODO Auto-generated method stub

	}

	// 复制欢迎文件
	@Override
	public void copyWelFile() {
		// TODO Auto-generated method stub
		File f = new File(mDownloadPath + "/欢迎您的使用.pdf");
		if (!f.exists()) {// 如果文件存在
			InputStream is;
			try {
				is = getResources().getAssets().open("welcome.pdf");
				FileOutputStream fos = new FileOutputStream(f);
				// 下面这一段代码不是很理解，有待研究

				byte[] buffer = new byte[8192];
				int count = 0;
				// 开始复制db文件
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
