package com.estar.nashbud.profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.post.Post_Model;
import com.estar.nashbud.upload_photo.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class myProfileAdapter extends BaseAdapter{
    Context c;
    ArrayList<Post_Model> user;
    FirebaseAuth mAuth;
    String CurrentUser;
    public myProfileAdapter(Context c, ArrayList<Post_Model> user) {
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
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.my_profile_row,parent,false);
        }

        ImageView imageView= (ImageView) convertView.findViewById(R.id.my_profile_imageView);

        final Post_Model s= (Post_Model) this.getItem(position);
        Log.e("GetImagePathInAdapter ",""+s.getImagePath());

        Glide.with(c)
                .load(s.getImagePath())
                .placeholder(R.drawable.gallery)
                .centerCrop()
                .dontAnimate()
                .into(imageView);

        return convertView;

    }
}
