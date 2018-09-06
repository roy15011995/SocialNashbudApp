package com.estar.nashbud.settings;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
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

public class Activity_sitting extends AppCompatActivity {
    TextView text_topic;
    ImageView image_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitting);

        text_topic = (TextView) findViewById(R.id.text_topic);
        image_back = (ImageView) findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_topic.setText("Activity Settings");

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
            addPreferencesFromResource(R.xml.pref_activity_setting);

            Preference preference_hide_post=(Preference)findPreference("hide_account");
            preference_hide_post.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent=new Intent(getContext(), Hide_Post_From.class);
                    startActivity(intent);

                    /*Toast.makeText(getActivity(),"Comming Soon",Toast.LENGTH_SHORT).show();*/
                    return true;
                }
            });

            Preference preference_who_can_comment=(Preference)findPreference("who_can_comment");
            preference_who_can_comment.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    /*Intent intent=new Intent(getContext(), Deactivate_Number.class);
                    startActivity(intent);*/

                    Toast.makeText(getActivity(),"Comming Soon",Toast.LENGTH_SHORT).show();

                    return true;
                }
            });

            Preference preference_who_can_repost=(Preference)findPreference("who_can_repost");
            preference_who_can_repost.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onPreferenceClick(Preference preference) {
                   /* Intent intent=new Intent(getContext(), Block_Contact.class);
                    startActivity(intent);*/
                    Toast.makeText(getActivity(),"Comming Soon",Toast.LENGTH_SHORT).show();
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
