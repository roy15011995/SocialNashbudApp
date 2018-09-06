package com.estar.nashbud.verify_phone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.estar.nashbud.R;

public class Change_Number extends AppCompatActivity {
Button DoneTick;
ImageView imageBack;
TextView text_topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__number);
        /*DoneTick=(Button)findViewById(R.id.doneTick);
        String checkedMark = "\u2713";
        DoneTick.setText(checkedMark);*/
        imageBack=(ImageView)findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_topic=(TextView)findViewById(R.id.text_topic);
        text_topic.setText("Change Number");
    }
}
