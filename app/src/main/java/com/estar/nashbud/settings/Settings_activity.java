package com.estar.nashbud.settings;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.estar.nashbud.R;
import com.estar.nashbud.chatscreenpages.ChatScreenOne;
import com.estar.nashbud.profile.EditProfilePage;
import com.estar.nashbud.profile.MyProfilePage;

import java.util.ArrayList;

public class Settings_activity extends AppCompatActivity {
    ListView listView;
    Context context;
    TextView text_topic;
    ImageView imageback;

    ArrayList prgmName;
    public static int [] prgmImages={R.drawable.user_settings,R.drawable.info};
   // public static int [] prgmImages={R.drawable.user_settings,R.drawable.television,R.drawable.synchronization_arrows,R.drawable.notifications_setting,R.drawable.info};
   // public static String [] prgmNameList={"Account Settings","Activity Settings","Data Storage and Sync","Notifications","Help"};
   public static String [] prgmNameList={"Edit Profile" ,"Help"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        listView=(ListView)findViewById(R.id.ListView);
        context=this;
        listView.setAdapter(new Setting_Adapter(this, prgmNameList,prgmImages));
        text_topic=(TextView)findViewById(R.id.text_topic);
        imageback=(ImageView)findViewById(R.id.image_back);
        text_topic.setText("Settings");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(context,"you are in position="+position,Toast.LENGTH_SHORT).show();*/
                switch (position){
                    case 0:
                        Intent intent = new Intent(Settings_activity.this, EditProfilePage.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent1=new Intent(Settings_activity.this,Activity_Help.class);
                        startActivity(intent1);
                        //Toast.makeText(getApplicationContext(),"Comming Soon",Toast.LENGTH_SHORT).show();
                        break;
//                    case 0:
//                        Intent intent=new Intent(Settings_activity.this,Account_Setting.class);
//                        startActivity(intent);
//                        break;
//
//                    case 1:
//                        Intent intent1=new Intent(Settings_activity.this,Activity_sitting.class);
//                        startActivity(intent1);
//                        //Toast.makeText(getApplicationContext(),"Comming Soon",Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case 2:
//                      /*  Intent intent1=new Intent(Settings_activity.this,DataStorage_andSync.class);
//                        startActivity(intent1);*/
//
//                        Toast.makeText(getApplicationContext(),"Comming Soon",Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case 3:
//                        Intent intent2=new Intent(Settings_activity.this,Notifications.class);
//                        startActivity(intent2);
//                        break;
//
//                    case 4:
//
//                        Intent intent3 = new Intent(Settings_activity.this,Activity_Help.class);
//                        startActivity(intent3);
//                        break;
                }
            }
        });
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
