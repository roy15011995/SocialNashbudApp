package com.estar.nashbud.settings;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estar.nashbud.R;

public class Activity_Help extends AppCompatActivity {
    TextView text_topic;
    ImageView image_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__help);

        text_topic = (TextView) findViewById(R.id.text_topic);
        image_back = (ImageView) findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_topic.setText("Help");
        Fragment fragment=new SettingsScreen();
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
        if(savedInstanceState == null){
            // Created for the first time
            fragmentTransaction.add(R.id.relative_layout,fragment,"Settings_Fragment");
            fragmentTransaction.commit();
        }else {
            fragment=getFragmentManager().findFragmentByTag("Settings_Fragment");
        }
    }

    public static class SettingsScreen extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_help);

            Preference preference_hide_post=(Preference)findPreference("send_feedback");
            preference_hide_post.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent=new Intent(getContext(), Send_Feedback.class);
                    startActivity(intent);

                    //Toast.makeText(getActivity(),"Comming Soon",Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            Preference preference_who_can_comment=(Preference)findPreference("terms_and_privacy_policy");
            preference_who_can_comment.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent=new Intent(getContext(), TermandconditionsActivity.class);
                    startActivity(intent);

                    //Toast.makeText(getActivity(),"Comming Soon",Toast.LENGTH_SHORT).show();

                    return true;
                }
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);


    }
}
