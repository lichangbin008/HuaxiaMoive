package com.lcb.notchscreentest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements OnNotchCallBack{

    /**
     * 刘海屏工具
     */
    private NotchTools notchTools = null;

    private ImageView mBackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBackView = findViewById(R.id.img_back);

        notchTools = new NotchTools();
        notchTools.fullScreenUseNotch(this,this);
    }

    @Override
    public void onNotchPropertyCallback(NotchProperty notchProperty) {
//        移动我们控件  底层 +  不想去使用 刘海屏   全面
        int marginTop = notchProperty.getNotchHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mBackView.getLayoutParams();
        layoutParams.topMargin += marginTop;
        mBackView.setLayoutParams(layoutParams);
    }
}
