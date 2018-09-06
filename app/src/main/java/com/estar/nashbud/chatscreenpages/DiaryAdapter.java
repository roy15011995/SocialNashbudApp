package com.estar.nashbud.chatscreenpages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.profile.FriendsProfileActivity;
import com.estar.nashbud.profile.MyProfilePage;
import com.estar.nashbud.upload_photo.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class DiaryAdapter extends BaseAdapter {
    Context c;
    ArrayList<User> user;
    FirebaseAuth mAuth;
    String CurrentUser;

    public DiaryAdapter(Context c, ArrayList<User> user) {
        this.c = c;
        this.user = user;
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
    }

    @Override
    public int getCount() {
        return user.size();
    }

    @Override
    public Object getItem(int position) {
        return user.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v= convertView;
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.activity_row_layout,parent,false);
        }
        ImageView profilePic=(ImageView)convertView.findViewById(R.id.post_user_pic);
        //PostPic =(ImageView)v.findViewById(R.id.post_pic);
        TextView ProfileName=(TextView)convertView.findViewById(R.id.post_user_name);
        TextView Description=(TextView)convertView.findViewById(R.id.post_description);
        TextView post_data = (TextView)convertView.findViewById(R.id.post_date);
        TextView post_time =(TextView)convertView.findViewById(R.id.post_time);
        TextView UserNameLocation = (TextView)convertView.findViewById(R.id.name_location);
        TextView Place = (TextView)convertView.findViewById(R.id.post_place);

        final User s= (User) this.getItem(position);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentUser.contains(s.getUid()))
                {
                    v.getContext().startActivity(new Intent(c,MyProfilePage.class));

                }
                else if(s.getUid()!=null){
                    Intent intent = new Intent(c,FriendsProfileActivity.class);
                    intent.putExtra("user_id",s.getUid());
                    v.getContext().startActivity(intent);
                }



            }
        });

        ProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentUser.contains(s.getUid()))
                {
                    v.getContext().startActivity(new Intent(c,MyProfilePage.class));

                }
                else if(s.getUid()!=null){
                    Intent intent = new Intent(c,FriendsProfileActivity.class);
                    intent.putExtra("user_id",s.getUid());
                    v.getContext().startActivity(intent);
                }
            }
        });

        ProfileName.setText(s.getDisplayName());

        Glide.with(c)
                .load(s.getPhotoUrl())
                .placeholder(R.drawable.user_profile_pic)
                .centerCrop()
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(c))
                .into(profilePic);



        return convertView;
    }
}
