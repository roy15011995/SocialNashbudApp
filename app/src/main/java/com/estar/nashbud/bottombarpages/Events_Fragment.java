package com.estar.nashbud.bottombarpages;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.estar.nashbud.R;

/**
 * Created by Sudipta on 10/24/2017.
 */

public class Events_Fragment extends Fragment {

    ListView lv;
    Context context;
    Toolbar toolbar;
    ImageView imageback;
    TextView textTopic;
    Button done;
    LinearLayout linearLayout;
    public static String [] prgmNameList={"Aman Mishra , Kunal Paul and 5 others like your posts",
            "Aman Mishra , Kunal Paul and 5 others like your posts","Aman Mishra , Kunal Paul and 5 others like your posts",
            "Aman Mishra , Kunal Paul and 5 others like your posts","Aman Mishra , Kunal Paul and 5 others like your posts"};
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.events_fragment, container, false);
        //context=getActivity();

        lv = (ListView)view.findViewById(R.id.listViewNotification);

        /*toolbar=(Toolbar)view.findViewById(R.id.toolbar_discover);

        toolbar.setTitle("Notifications");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));


        if(toolbar!=null){
            getActivity().setActionBar(toolbar);
        }*/


        lv.setAdapter(new eventAdapter( prgmNameList,getActivity()));

        imageback = (ImageView)view.findViewById(R.id.imageBack);
        imageback.setVisibility(View.GONE);

        done = (Button)view.findViewById(R.id.doneVisible);
        done.setVisibility(View.GONE);

        textTopic = (TextView)view.findViewById(R.id.textTopic);
        textTopic.setText("Notifications");


        return view;
    }
}