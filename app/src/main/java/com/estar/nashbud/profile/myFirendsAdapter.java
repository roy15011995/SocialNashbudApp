package com.estar.nashbud.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.post.Post_Model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class myFirendsAdapter extends BaseAdapter {
    Context c;
    ArrayList<Post_Model> user;
    FirebaseAuth mAuth;
    String CurrentUser, getUid, UserId;
    ;
    SharedPreferences sharedPreferences;

    DatabaseReference databaseReference;

    public myFirendsAdapter(Context c, ArrayList<Post_Model> user) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(R.layout.my_profile_row, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.my_profile_imageView);

        final Post_Model s = (Post_Model) this.getItem(position);

        //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

       /* sharedPreferences = c.getSharedPreferences("PREF_KEY_FRIENDSUSERID", Context.MODE_PRIVATE);
        UserId = sharedPreferences.getString("friends_user_id", "");*/

        Glide.with(c)
                .load(s.getImagePath())
                .placeholder(R.drawable.gallery)
                .centerCrop()
                .dontAnimate()
                .into(imageView);

                return convertView;

            }

          /*   databaseReference.orderByChild("uid").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot ds: dataSnapshot.getChildren())
            {

                s = ds.getValue(Post_Model.class);
                String UserUid = s.getUid().toString();
                Log.e("UserId",""+UserUid);

                if (UserId != null) {

                    Log.e("UserIdFriends", "" + UserId);

                    if (UserId.contains(s.getUid())) {

                        Glide.with(c)
                                .load(s.getImagePath())
                                .placeholder(R.drawable.gallery)
                                .centerCrop()
                                .dontAnimate()
                                .into(imageView);

                    }


                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });*/
        }


