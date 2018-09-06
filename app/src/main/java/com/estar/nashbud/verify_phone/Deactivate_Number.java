package com.estar.nashbud.verify_phone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.estar.nashbud.R;

public class Deactivate_Number extends AppCompatActivity {
TextView text_topic;
ImageView imageBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate__number);
        text_topic=(TextView)findViewById(R.id.text_topic);
        text_topic.setText("Deactivate Number");
        imageBack=(ImageView)findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
