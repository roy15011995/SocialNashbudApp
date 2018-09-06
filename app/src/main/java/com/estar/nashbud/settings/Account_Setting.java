package com.estar.nashbud.settings;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.estar.nashbud.R;
import com.estar.nashbud.verify_phone.Change_Number;
import com.estar.nashbud.verify_phone.Deactivate_Number;

public class Account_Setting extends AppCompatActivity {
    TextView text_topic;
    ImageView image_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
       /* Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        text_topic = (TextView) findViewById(R.id.text_topic);
        image_back = (ImageView) findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_topic.setText("Account Settings");
       /* PreferenceManager prefMgr = getPreferenceManager();
        prefMgr.setSharedPreferencesName("account_setting_preference");
        prefMgr.setSharedPreferencesMode(MODE_WORLD_READABLE);

        addPreferencesFromResource(R.xml.pref_account_setting);*/


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
    public static class SettingsScreen extends PreferenceFragment{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_account_setting);
            SwitchPreference switchPreference=(SwitchPreference)findPreference("switch_false");
            switchPreference.setDefaultValue(true);

            Preference preference_change_number=(Preference)findPreference("change_number");
            preference_change_number.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onPreferenceClick(Preference preference) {
                   /* Intent intent=new Intent(getContext(), Change_Number.class);
                    startActivity(intent);*/

                    Toast.makeText(getActivity(),"Comming Soon",Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            Preference preference_deactivate_number=(Preference)findPreference("deactivate_account");
            preference_deactivate_number.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    /*Intent intent=new Intent(getContext(), Deactivate_Number.class);
                    startActivity(intent);*/

                    Toast.makeText(getActivity(),"Comming Soon",Toast.LENGTH_SHORT).show();

                    return true;
                }
            });

            Preference preference_block_number=(Preference)findPreference("blocked_account");
            preference_block_number.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent=new Intent(getContext(), Block_Contact.class);
                    startActivity(intent);
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
