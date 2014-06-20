package com.zgvtc.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import com.zgvtc.base.BaseApplication;
import com.zgvtc.nets.NetworkUtils;

public class AppApplication extends BaseApplication {
	public static final String DOMAIN = "domain";
	public static final String DOMAIN_URL = "url";
	public static String mDomain = Constant.WEBVIE_WBASEURL;
	//public static String mBakeDomain = "http://1.kaiyuanxiangmu.sinaapp.com/";

	private static final String DB_NAME = Constant.APPNAME+".db";

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
		mDownloadPath = "/"+Constant.APPNAME+"/download";
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/"+Constant.APPNAME+"/config/");
			if (!file.exists()) {
				if (file.mkdirs()) {
					mSdcardDataDir = file.getAbsolutePath();
				}
			} else {
				mSdcardDataDir = file.getAbsolutePath();
			}
		}

		mNetWorkState = NetworkUtils.getNetworkState(this);

	}

	@Override
	public void exitApp(Context context) {
		// TODO Auto-generated method stub

	}
}
