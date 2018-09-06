package com.estar.nashbud.settings;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.estar.nashbud.R;

public class TermandconditionsActivity extends AppCompatActivity {
    TextView text_topic;
    ImageView image_back;
    WebView web_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termandconditions);
        text_topic = (TextView) findViewById(R.id.text_topic);
        image_back = (ImageView) findViewById(R.id.image_back);
        web_view = (WebView) findViewById(R.id.web_view);

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_topic.setText("Terms and condition");
        web_view.getSettings().setJavaScriptEnabled(true);

        web_view.loadDataWithBaseURL("", getResources().getString(R.string.term_condition), "text/html", "UTF-8", "");
       // web_view.loadData(getResources().getString(R.string.term_condition), "text/html", "UTF-8");
    }



}
