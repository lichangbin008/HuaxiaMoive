package com.suma.midware.huaxia.movie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.suma.midware.huaxia.movie.util.SystemPropertyUtil;

public class StorageReceiver extends BroadcastReceiver {

    private static final String TAG = "StorageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive, action=" + action);
        if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
//            Toast.makeText(context, "ACTION_MEDIA_MOUNTED", Toast.LENGTH_SHORT).show();
        } else if (Intent.ACTION_MEDIA_UNMOUNTED.equals(action)) {
//            Toast.makeText(context, "ACTION_MEDIA_UNMOUNTED", Toast.LENGTH_SHORT).show();
//            SystemPropertyUtil.setSystemPropertie("sys.mounted","1");
        } else if (Intent.ACTION_MEDIA_REMOVED.equals(action)
                || Intent.ACTION_MEDIA_BAD_REMOVAL.equals(action)) {
//            Toast.makeText(context, "ACTION_MEDIA_REMOVED", Toast.LENGTH_SHORT).show();
        }else if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
//            Toast.makeText(context, "ACTION_MEDIA_SCANNER_FINISHED", Toast.LENGTH_SHORT).show();
//            SystemPropertyUtil.setSystemPropertie("sys.mounted","0");
        }
    }
}