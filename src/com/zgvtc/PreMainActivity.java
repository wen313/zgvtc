package com.zgvtc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class PreMainActivity extends Activity {
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			PreMainActivity.this.startActivity(new Intent(PreMainActivity.this,
					MainActivity.class));
			finish();

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_premain);
		this.mHandler.sendEmptyMessageDelayed(0, 2000L);// 延迟2s之后在跳转
	}

}
