package com.dqr.www.flowlayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FlowLayout mFlowLayout;
    private String []mTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlowLayout = (FlowLayout) findViewById(R.id.fl_content);
        ImageView ivAdd = (ImageView) findViewById(R.id.iv_add);
        mTags=getResources().getStringArray(R.array.girls);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFLView();
            }
        });
    }

    private void addFLView(){
        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.tv_bg_shape);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(6,6,6,6);
        Random random = new Random();
        textView.setText(mTags[random.nextInt(mTags.length)]);

        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        params.setMargins(20,20,20,20);
        mFlowLayout.addView(textView,params);
    }
}
