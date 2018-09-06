package com.estar.nashbud.bottombarpages;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.thread.ThreadActivity;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.Constants;
import com.estar.nashbud.utils.SharedPreference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Requests extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase,UserReference,FriendsRequestReference,FriendsReference;
    Context context;
    User user;
    String getUid;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    SharedPreference sharedPreference;
    View view;
    Toolbar toolbar;
    String OnlineUserId;
    String sender_user_id,CurrentDate;
    ImageView imageback;
    TextView textTopic,no_of_request;
    LinearLayout linear_request,linear_no_request;
    Button done;
    long count=0;
    public Add_Requests() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add__requests, container, false);
        // Inflate the layout for this fragment
        recyclerView=(RecyclerView)view.findViewById(R.id.requests_people_RecyclerView);
        linear_no_request = (LinearLayout)view.findViewById(R.id.linear_no_contact_requests);
        context = getActivity();
        sharedPreference = new SharedPreference();
        mAuth = FirebaseAuth.getInstance();
        OnlineUserId = mAuth.getCurrentUser().getUid();
       // Log.e("OnlineUserId",""+OnlineUserId);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_Requests").child(OnlineUserId);
        UserReference = FirebaseDatabase.getInstance().getReference().child("users");
        FriendsReference = FirebaseDatabase.getInstance().getReference().child("Friends");
        FriendsRequestReference = FirebaseDatabase.getInstance().getReference().child("Friend_Requests");



        /*toolbar=(Toolbar)view.findViewById(R.id.toolbar_discover);

        toolbar.setTitle("Add Requests");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));


        if(toolbar!=null){
            getActivity().setActionBar(toolbar);
        }*/

        imageback = (ImageView)view.findViewById(R.id.imageBack);
        imageback.setVisibility(View.GONE);

        done = (Button)view.findViewById(R.id.doneVisible);
        done.setVisibility(View.GONE);

        textTopic = (TextView)view.findViewById(R.id.textTopic);
        textTopic.setText("Add Requests");

        //no_of_request = (TextView)view.findViewById(R.id.no_of_people_request);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


         final FirebaseRecyclerAdapter<User,RequestViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<User, RequestViewHolder>
                        (
                                User.class,
                                R.layout.add_requests_row,
                                Add_Requests.RequestViewHolder.class,
                                mDatabase
                        ) {
                    @Override
                    protected void populateViewHolder(final RequestViewHolder viewHolder, User model, int position) {

                        final String list_user_id = getRef(position).getKey();
                        DatabaseReference get_ref_type = getRef(position).child("request_type").getRef();

                        Log.e("RequestType",""+get_ref_type.equals("receive"));

                        get_ref_type.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists()){
                                    String Request_type = dataSnapshot.getValue().toString();


                                    if(Request_type.equals("receive"))
                                    {
                                        UserReference.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                count =dataSnapshot.getChildrenCount();
                                                //no_of_request.setText(count+" people");

                                                final String user_name = dataSnapshot.child("displayName").getValue().toString();
                                                final String user_pic = dataSnapshot.child("photoUrl").getValue().toString();
                                                final String user_contact = dataSnapshot.child("mobileNo").getValue().toString();
                                                viewHolder.people_requests_name.setText(user_name);

                                                Glide.with(context)
                                                        .load(user_pic)
                                                        .placeholder(R.drawable.user_profile_pic)
                                                        .centerCrop()
                                                        .dontAnimate()
                                                        .bitmapTransform(new CropCircleTransformation(context))
                                                        .into(viewHolder.people_requests_pic);

                                                viewHolder.people_requests_contact.setText(user_contact);
                                                viewHolder.btn_add_request.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        Calendar calendarDate = Calendar.getInstance();
                                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
                                                        CurrentDate = simpleDateFormat.format(calendarDate.getTime());

                                                        FriendsReference.child(OnlineUserId).child(list_user_id)
                                                                .setValue(CurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                FriendsReference.child(list_user_id).child(OnlineUserId)
                                                                        .setValue(CurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        FriendsRequestReference.child(OnlineUserId).child(list_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                if (task.isSuccessful())
                                                                                {
                                                                                    FriendsRequestReference.child(list_user_id).child(OnlineUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                            if(task.isSuccessful())
                                                                                            {

                                                                                                Toast.makeText(getActivity(),"Friend Request Accepted",Toast.LENGTH_SHORT).show();

                                                                                            }

                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                });

                                                            }
                                                        });

                                                    }
                                                });

                                                viewHolder.Request_Remove.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        FriendsRequestReference.child(OnlineUserId).child(list_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful())
                                                                {
                                                                    FriendsRequestReference.child(list_user_id).child(OnlineUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            if(task.isSuccessful())
                                                                            {

                                                                                Toast.makeText(getActivity(),"Cancelled Request",Toast.LENGTH_SHORT).show();

                                                                            }

                                                                        }
                                                                    });
                                                                }

                                                            }
                                                        });
                                                    }
                                                });



                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else if(Request_type.equals("sent"))
                                    {
                                        viewHolder.linearLayout.setVisibility(View.GONE);
                                        viewHolder.view.setVisibility(View.GONE);
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                };

        FriendsRequestReference.child(OnlineUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    recyclerView.setAdapter(firebaseRecyclerAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    linear_no_request.setVisibility(View.GONE);
                    Log.e("IfCall ","Yes");
                }
                else
                {
                    recyclerView.setVisibility(View.GONE);
                    linear_no_request.setVisibility(View.VISIBLE);
                    Log.e("ElseCall ","Yes");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder{


        public RequestViewHolder(View itemView) {
            super(itemView);
        }

        ImageView people_requests_pic = (ImageView)itemView.findViewById(R.id.add_requests_pic);
        TextView people_requests_name = (TextView)itemView.findViewById(R.id.people_requests_name);
        TextView people_requests_contact = (TextView)itemView.findViewById(R.id.people_requests_mobile);
        LinearLayout linearLayout = (LinearLayout)itemView.findViewById(R.id.linear_row_user);
        View view = (View)itemView.findViewById(R.id.view_add_request);
        Button btn_add_request = (Button)itemView.findViewById(R.id.button_add_people_requests);
        TextView Request_Remove = (TextView)itemView.findViewById(R.id.people_requests_remove);
        //TextView no_friend_request = (TextView)itemView.findViewById(R.id.no_friend_request);


    }



}
