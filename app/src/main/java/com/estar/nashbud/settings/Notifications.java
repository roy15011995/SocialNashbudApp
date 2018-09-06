package com.estar.nashbud.settings;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.estar.nashbud.R;
import com.estar.nashbud.verify_phone.Change_Number;
import com.estar.nashbud.verify_phone.Deactivate_Number;

public class Notifications extends AppCompatActivity {
ImageView imageBack;
TextView text_topic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        imageBack=(ImageView)findViewById(R.id.image_back);
        text_topic=(TextView)findViewById(R.id.text_topic);
        text_topic.setText("Notifications");
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
            addPreferencesFromResource(R.xml.pref_notification);
            SwitchPreference switchPreference_mynoti=(SwitchPreference)findPreference("notifications_new_message");
            switchPreference_mynoti.setDefaultValue(true);

            SwitchPreference switchPreference_vibrate=(SwitchPreference)findPreference("notifications_new_message_vibrate");
            switchPreference_vibrate.setDefaultValue(false);

            RingtonePreference ringtonePreference=(RingtonePreference)findPreference("notifications_new_message_ringtone");
            ringtonePreference.setDefaultValue(true);

        }
    }
}
