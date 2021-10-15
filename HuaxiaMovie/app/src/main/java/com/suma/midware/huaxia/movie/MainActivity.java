package com.suma.midware.huaxia.movie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.suma.midware.huaxia.movie.util.SystemPropertyUtil;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mLayoutPlay;

    private ImageView mPlayFocus;

    private FrameLayout mLayoutSet;

    private ImageView mSetFocus;

    private Handler mHandler = new Handler();

    private boolean mIsFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayoutPlay = findViewById(R.id.ff_play);
        mLayoutSet = findViewById(R.id.ff_set);

        mPlayFocus = findViewById(R.id.iv_play_focus);
        mSetFocus = findViewById(R.id.iv_set_focus);

//        mLayoutPlay=findViewById(R.id.ll_play);
//        mLayoutSet=findViewById(R.id.ll_set);


        initListener();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsFirst) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLayoutPlay.requestFocus();
                    mLayoutPlay.requestFocusFromTouch();
                }
            }, 500);
            mIsFirst = false;
        }
    }

    private void initListener() {
        mLayoutPlay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mPlayFocus.setVisibility(View.VISIBLE);
                } else {
                    mPlayFocus.setVisibility(View.GONE);
                }
            }
        });

        mLayoutSet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mSetFocus.setVisibility(View.VISIBLE);
                } else {
                    mSetFocus.setVisibility(View.GONE);
                }
            }
        });

        mLayoutPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if ("0".equals(SystemPropertyUtil.getSystemPropertie("sys.mounted"))) {
//                    startPlay();
//                } else {
//                    Toast.makeText(MainActivity.this, "移动设备未挂载", Toast.LENGTH_SHORT).show();
//                }
                startPlay();
            }
        });

        mLayoutSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSetting();
            }
        });
    }

    /**
     * 启动播放
     */
    private void startPlay() {
        Intent intent = new Intent(MainActivity.this, MovieActivity.class);
        startActivity(intent);
    }

    /**
     * 启动设置
     */
    private void startSetting() {
        ComponentName toActivity = new ComponentName(
                "com.suma.midware.setting",
                "com.suma.midware.setting.main.SystemMainActivity");
        Intent intent = new Intent();
        intent.setComponent(toActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}