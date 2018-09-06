package com.estar.nashbud.settings;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.estar.nashbud.R;
import com.estar.nashbud.verify_phone.Change_Number;
import com.estar.nashbud.verify_phone.Deactivate_Number;

import java.util.ArrayList;

public class DataStorage_andSync extends AppCompatActivity {
    TextView text_topic;
    ImageView image_back;
    public static String [] text_title={"On Mobile Data","On WiFi","On Roaming"};
    public static String [] text_summery={"All Media","All Media","All Media"};
    ListView listView;
    Context context;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer>mUserItems=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_storage_and_sync);
        text_topic = (TextView) findViewById(R.id.text_topic);
        image_back = (ImageView) findViewById(R.id.image_back);
        listView=(ListView)findViewById(R.id.list_view);
        context=this;
        listView.setAdapter(new Data_sycn_Adapter(this, text_title,text_summery));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        DialogInterface1();
                        break;
                    case 1:
                        DialogInterface2();
                        break;

                    case 2:
                        DialogInterface3();
                        break;
                }
            }
        });
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_topic.setText("Data Storage and Sync");
        listItems=getResources().getStringArray(R.array.pref_sync_frequency_titles);
        checkedItems=new boolean[listItems.length];

    }

    public void DialogInterface1(){
        AlertDialog.Builder builder=new AlertDialog.Builder(DataStorage_andSync.this);
        builder.setTitle("When using mobile data");
        builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {

                 if(isChecked){
                     if(!mUserItems.contains(position))
                     {
                         mUserItems.add(position);
                     }
                     else
                     {
                         mUserItems.remove(position);
                     }
                 }
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String item="";
                for(int i=0;i<mUserItems.size();i++){
                    item=item+listItems[mUserItems.get(i)];
                    if(i!=mUserItems.size()-1){
                    item=item+",";
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    public void DialogInterface2(){
        AlertDialog.Builder builder=new AlertDialog.Builder(DataStorage_andSync.this);
        builder.setTitle("When connected on Wi-Fi");
        builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {

                if(isChecked){
                    if(!mUserItems.contains(position))
                    {
                        mUserItems.add(position);
                    }
                    else
                    {
                        mUserItems.remove(position);
                    }
                }
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String item="";
                for(int i=0;i<mUserItems.size();i++){
                    item=item+listItems[mUserItems.get(i)];
                    if(i!=mUserItems.size()-1){
                        item=item+",";
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    public void DialogInterface3(){
        AlertDialog.Builder builder=new AlertDialog.Builder(DataStorage_andSync.this);
        builder.setTitle("When roaming");
        builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {

                if(isChecked){
                    if(!mUserItems.contains(position))
                    {
                        mUserItems.add(position);
                    }
                    else
                    {
                        mUserItems.remove(position);
                    }
                }
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String item="";
                for(int i=0;i<mUserItems.size();i++){
                    item=item+listItems[mUserItems.get(i)];
                    if(i!=mUserItems.size()-1){
                        item=item+",";
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
