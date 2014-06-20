package com.zgvtc.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.zgvtc.nets.NetworkUtils;

public class BaseBroadcastReceiver extends BroadcastReceiver{//监听网络打开
	@Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            BaseApplication.mNetWorkState = NetworkUtils.getNetworkState(context);
        }

    }
}
