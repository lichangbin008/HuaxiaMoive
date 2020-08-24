package com.lcb.study.vipcoursemainshitest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lcb.study.vipcoursemainshitest.ui.UIActivity;

public class MainActivity extends AppCompatActivity {

    private Button btUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btUi=findViewById(R.id.bt_ui);

        initListener();
    }

    private void initListener() {
        btUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UIActivity.class);
                startActivity(intent);
            }
        });
    }
}
