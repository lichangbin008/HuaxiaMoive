package com.lcb.study.butterknifevip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lcb.study.annotation.BindString;
import com.lcb.study.annotation.BindView;
import com.lcb.study.annotation.OnClick;

public class MainActivity extends AppCompatActivity { //TypeElement类节点

    @BindView(R.id.textView1)
    TextView textView1; //成员变量节点 VariableElement
    @BindString(R.string.tv_text1)
    String text1;


    @Override
    protected void onCreate(Bundle savedInstanceState) { //ExecutableElement 方法节点
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        textView1.setText(text1);

    }

    @OnClick({R.id.textView1, R.id.textView2})
    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.textView1:
                textView1.setText("1111111111");
                break;
            case R.id.textView2:
                break;
        }
    }
}
