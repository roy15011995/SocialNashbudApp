package com.estar.nashbud.chatscreenpages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.profile.FriendsProfileActivity;
import com.estar.nashbud.profile.MyProfilePage;
import com.estar.nashbud.upload_photo.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class CustomAdapterLike extends BaseAdapter {
    Context c;
    ArrayList<User> user;
    FirebaseAuth mAuth;
    String CurrentUser;

    public CustomAdapterLike(Context c, ArrayList<User> user) {
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
            convertView= LayoutInflater.from(c).inflate(R.layout.like_people_row,parent,false);
        }

        TextView nameTxt= (TextView) convertView.findViewById(R.id.like_profile_name);
        ImageView ProfileImage= (ImageView) convertView.findViewById(R.id.like_profile_pic);
        Button add_Btn = (Button) convertView.findViewById(R.id.Like_btn);
        TextView descTxt= (TextView) convertView.findViewById(R.id.status);
        LinearLayout userLinearLayout = (LinearLayout) convertView.findViewById(R.id.user_linear_layout);

        final User s= (User) this.getItem(position);

        if(CurrentUser.contains(s.getUid()))
        {
            add_Btn.setVisibility(View.GONE);

        }

        ProfileImage.setOnClickListener(new View.OnClickListener() {
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

        nameTxt.setOnClickListener(new View.OnClickListener() {
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

        userLinearLayout.setOnClickListener(new View.OnClickListener() {
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

        /*if(CurrentUser.contains(s.getUid()))
        {
            nameTxt.setText("You");
        }
        else
        {*/
            nameTxt.setText(s.getDisplayName());
       // }

        Glide.with(c)
                .load(s.getPhotoUrl())
                .placeholder(R.drawable.user_profile_pic)
                .centerCrop()
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(c))
                .into(ProfileImage);
        descTxt.setText("@"+s.getUserName());



        return convertView;
    }
}
