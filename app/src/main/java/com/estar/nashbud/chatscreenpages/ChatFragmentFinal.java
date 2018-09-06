package com.estar.nashbud.chatscreenpages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.contacts.ContactList;
import com.estar.nashbud.thread.ThreadActivity;
import com.estar.nashbud.upload_photo.Message;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.Constants;
import com.estar.nashbud.utils.SharedPreference;
import com.estar.nashbud.widgets.EmptyStateRecyclerView;
import com.firebase.client.ServerValue;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.estar.nashbud.utils.SharedPreference;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by User on 06-12-2017.
 */

public class ChatFragmentFinal extends Fragment implements SwipyRefreshLayout.OnRefreshListener {
    //@BindView(R.id.activity_main_users_recycler)
    EmptyStateRecyclerView usersRecycler;
    @BindView(R.id.activity_main_empty_view)
    TextView emptyView;
    FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    Context context;
    User user;
    String getUid, Uid;
    DatabaseReference databaseReference, databaseReferenceMessage, databaseReferencePositionlMessage, databaseReferenceFriends;
    RecyclerView recyclerView;
    SharedPreference sharedPreference;
    CardView card_tick;
    ImageView itemUserImageView, user_profilePic;
    TextView itemFriendNameTextView, itemFriendEmailTextView, UnReadMessageCount;
    LinearLayout userData, List_Selection, image_selection, linear_photo_card, linearToolbar;
    List<User> Userslist;
    private SparseBooleanArray mSelectedItemsIds;
    ListView listView;
    FloatingActionButton fab_Contact;
    SharedPreferences.Editor editor;
    View item_view;
    String userUid;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Message message;
    long messageUnReadCount = 0;
    ArrayList<String> arrayList = null;
    SwipyRefreshLayout swipyRefreshLayout;
    Query query = null;


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chats_final_fragment, container, false);
        ButterKnife.bind(this.getActivity());
        context = getActivity();
        listView = view.findViewById(R.id.activity_main_users_recycler);
        swipyRefreshLayout = (SwipyRefreshLayout)view.findViewById(R.id.swipyrefreshlayoutchat);
        //itemFriendEmailTextView = (TextView) view.findViewById(R.id.item_friend_email_text_view);
        fab_Contact = view.findViewById(R.id.fab_contact);
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        databaseReferenceMessage = FirebaseDatabase.getInstance().getReference().child("messages");
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReferenceFriends = FirebaseDatabase.getInstance().getReference();
        //floatingActionButton.setVisibility(View.VISIBLE);
        sharedPreference = new SharedPreference();
        swipyRefreshLayout.setOnRefreshListener(this);


        fab_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactList.class);
                startActivity(intent);
            }
        });

        initializeFirebaseAuthListener();
        initializeUsersRecycler();


        /* if(firebaseUser!=null)
         {
             firebaseUser = firebaseAuth.getCurrentUser();
             Log.e("CurrentUserId",""+firebaseUser.getUid());
         }*/


        return view;
    }

    private void initializeUsersRecycler() {

        mSelectedItemsIds = new SparseBooleanArray();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String CurrentUser = firebaseUser.getUid();
        databaseReferenceFriends = FirebaseDatabase.getInstance().getReference();
        arrayList = new ArrayList<String>();
       /* databaseReferenceFriends = FirebaseDatabase.getInstance().getReference().child("Friends");
        final UserAdpater userAdpater = new UserAdpater(getActivity(), User.class, R.layout.item_user,
                databaseReferenceFriends.child(CurrentUser).orderByChild("check").equalTo(true));
        //Log.e("mDatabase", "" + mDatabase.child("users").child("online"));

        listView.setAdapter(userAdpater);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                EventBus.getDefault().post(userAdpater.getRef(position));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TextView Time_Update = (TextView) view.findViewById(R.id.time_update);
                        Time_Update.setTextColor(Color.parseColor("#616161"));
                    }
                }, 2000);

               *//*if(isNetworkAvailable(context)==false){
                   CardView online_indication=(CardView)view.findViewById(R.id.online_indication);
                   online_indication.setVisibility(View.GONE);
               }*//*
            }
        });

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
                View view = getLayoutInflater().inflate(R.layout.item_user, null);

                // Capture total checked items
                card_tick = (CardView) view.findViewById(R.id.tick_cardView);
                final int checkedCount = listView.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " ");
                Log.e("CheckItem", String.valueOf(checkedCount));
                // Calls toggleSelection method from ListViewAdapter Class
                userAdpater.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.delete, menu);
                View view = getLayoutInflater().inflate(R.layout.activity_chat_screen_one, null);
                linearToolbar = (LinearLayout) view.findViewById(R.id.linear_toolbar);
                linearToolbar.setVisibility(View.GONE);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return true;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = userAdpater
                                .getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                *//*User selecteditem = userAdpater
                                        .getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                userAdpater.remove(selecteditem);*//*
                                //userAdpater.getRef(i).removeValue();

                            }
                        }
                        // Close CAB
                        linearToolbar.setVisibility(View.VISIBLE);

                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {
                // TODO Auto-generated method stub
                userAdpater.removeSelection();
            }
        });*/

        databaseReferenceFriends.child("Friends").child(CurrentUser).orderByChild("check")
                .equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    //arrayList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //Uid = ds.child("uid").getValue().toString();
                        User user = ds.getValue(User.class);
                        Log.e("GetAllUidFriends ", "" + user.getDisplayName());
                        Uid = user.getUid();
                        arrayList.add(Uid);

                        Log.e("GetAllUidFriends ", "" + user.getDisplayName());
                        mDatabase = FirebaseDatabase.getInstance().getReference();

                        mDatabase.child("users").orderByChild("uid").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String UidSnap = snapshot.child("uid").getValue().toString();
                                        if (arrayList.contains(UidSnap)) {

                                            for (int i = 0; i < arrayList.size(); i++) {
                                                 final String user = arrayList.get(i);
                                                 Log.e("GetAllUserId ",""+user);
                                                 DatabaseReference ref = mDatabase.child("users");
                                                 query = ref.orderByChild("uid").equalTo(user);

                                            }
                                            Log.i("nknoihno" , query.toString());
                                            final UserAdpater userAdpater = new UserAdpater(getActivity(), User.class, R.layout.item_user, query);
                                            //Log.e("mDatabase", "" + mDatabase.child("users").child("online"));
                                            listView.setAdapter(userAdpater);

                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                                                    EventBus.getDefault().post(userAdpater.getRef(position));

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            TextView Time_Update = (TextView) view.findViewById(R.id.time_update);
                                                            Time_Update.setTextColor(Color.parseColor("#616161"));
                                                        }
                                                    }, 2000);
                                                }
                                            });

                                            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                                                @Override
                                                public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
                                                    View view = getLayoutInflater().inflate(R.layout.item_user, null);

                                                    // Capture total checked items
                                                    card_tick = (CardView) view.findViewById(R.id.tick_cardView);
                                                    final int checkedCount = listView.getCheckedItemCount();
                                                    // Set the CAB title according to total checked items
                                                    mode.setTitle(checkedCount + " ");
                                                    Log.e("CheckItem", String.valueOf(checkedCount));
                                                    // Calls toggleSelection method from ListViewAdapter Class
                                                    userAdpater.toggleSelection(position);
                                                }

                                                @Override
                                                public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                                                    mode.getMenuInflater().inflate(R.menu.delete, menu);
                                                    View view = getLayoutInflater().inflate(R.layout.activity_chat_screen_one, null);
                                                    linearToolbar = (LinearLayout) view.findViewById(R.id.linear_toolbar);
                                                    linearToolbar.setVisibility(View.GONE);

                                                    return true;
                                                }

                                                @Override
                                                public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                                                    // TODO Auto-generated method stub
                                                    return true;
                                                }

                                                @Override
                                                public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                                                    switch (item.getItemId()) {
                                                        case R.id.delete:
                                                            // Calls getSelectedIds method from ListViewAdapter Class
                                                            SparseBooleanArray selected = userAdpater
                                                                    .getSelectedIds();
                                                            // Captures all selected ids with a loop
                                                            for (int i = (selected.size() - 1); i >= 0; i--) {
                                                                if (selected.valueAt(i)) {

                                                                }
                                                            }
                                                            // Close CAB
                                                            linearToolbar.setVisibility(View.VISIBLE);

                                                            mode.finish();
                                                            return true;
                                                        default:
                                                            return false;
                                                    }
                                                }

                                                @Override
                                                public void onDestroyActionMode(android.view.ActionMode mode) {
                                                    // TODO Auto-generated method stub
                                                    userAdpater.removeSelection();

                                                }
                                            });

                                        }
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

    }

    private void initializeFirebaseAuthListener() {
        mAuth = FirebaseAuth.getInstance();
        Log.e("Auth", "" + mAuth);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.e("current user", "" + user);
                if (user != null) {
                    //addUserToDatabase(user);
                    getUid = user.getUid();
                    Log.e("current user ID", "" + getUid);

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(getUid);

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREF_KEY_lASTSENDMESSAGE", Context.MODE_PRIVATE);
                            String Message = sharedPreferences.getString("MessageText", "");
                            Log.e("MessageChat", "" + Message);

                            SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("PREF_KEY_TIME", Context.MODE_PRIVATE);
                            Long Time = sharedPreferences1.getLong("TIME", 0);
                            Log.e("Time", "" + Time);
                            /*long timestamp = new Date().getTime();
                            long dayTimestamp = getDayTimestamp(timestamp);*/

                            SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("PREF_KEY_ID", Context.MODE_PRIVATE);
                            String Id = sharedPreferences2.getString("ID", "");
                            Log.e("ID", "" + Id);


                            mDatabase.child("online").onDisconnect().setValue(false);
                            mDatabase.child("time").onDisconnect().setValue(ServerValue.TIMESTAMP);
                            mDatabase.child("online").setValue(true);
                            mDatabase.child("message").setValue(Message);
                            mDatabase.child("timeStamp").setValue(Time);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }


                    });


                    Log.e("@@@@", "home:signed_in:" + user.getUid());
                } else {
                    Log.e("@@@@", "home:signed_out");

                }
            }
        };
    }

    private void addUserToDatabase(FirebaseUser firebaseUser) {
        user = new User();
        user.setDisplayName("saurabh");
        user.setUid(getUid);
        //user.setPhotoUrl(taskSnapshot.getDownloadUrl().toString());

        databaseReference.child("users")
                .child(user.getUid()).setValue(user);

        String instanceId = FirebaseInstanceId.getInstance().getToken();
        if (instanceId != null) {
            databaseReference.child("users")
                    .child(getUid)
                    .child("instanceId")
                    .setValue(instanceId);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserSelected(DatabaseReference selectedRef) {

        User user = new User();
        Intent thread = new Intent(context, ThreadActivity.class);
        thread.putExtra(Constants.USER_ID_EXTRA, selectedRef.getKey());
            /*thread.putExtra("DisplayName",user.getDisplayName());
            thread.putExtra("PhotoUrl",user.getPhotoUrl());*/
        //thread.putExtra()
        Log.e("thread activity", "" + selectedRef.getKey());
        startActivity(thread);


    }

    @Override
    public void onRefresh(final SwipyRefreshLayoutDirection direction) {

        Log.d("MainActivity", "Refresh triggered at "
                + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));

        initializeFirebaseAuthListener();
        initializeUsersRecycler();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Hide the refresh after 2sec
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipyRefreshLayout.setRefreshing(false);
                        swipyRefreshLayout.setDirection(direction);
                    }
                });
            }
        }, 2000);

    }

    public class UserAdpater extends FirebaseListAdapter<User> {

        View mView;
        //boolean status=true;

        public UserAdpater(Activity activity, Class<User> modelClass, int modelLayout, Query ref) {
            super(activity, modelClass, modelLayout, ref);
        }

        @Override
        protected void populateView(View v, final User model, final int position) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            itemUserImageView = (ImageView) v.findViewById(R.id.item_user_image);
            itemFriendNameTextView = (TextView) v.findViewById(R.id.item_friend_name_text_view);
            itemFriendEmailTextView = (TextView) v.findViewById(R.id.item_friend_email_text_view);
            userData = (LinearLayout) v.findViewById(R.id.linear_user_data);
            List_Selection = (LinearLayout) v.findViewById(R.id.list_selection);
            image_selection = (LinearLayout) v.findViewById(R.id.list_selection);
            final CardView online_indication = (CardView) v.findViewById(R.id.online_indication);
            item_view = (View) v.findViewById(R.id.item_view);
            TextView Time_Update = (TextView) v.findViewById(R.id.time_update);
            CardView card_tick = (CardView) v.findViewById(R.id.tick_cardView);
            UnReadMessageCount = (TextView) v.findViewById(R.id.unreadMessageCount);

            card_tick.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);


            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREF_KEY_lASTSENDMESSAGE", Context.MODE_PRIVATE);
            final String Message = sharedPreferences.getString("MessageText", "");
            Log.e("MessageChatPopulate", "" + Message);

            SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("PREF_KEY_TIME", Context.MODE_PRIVATE);
            Long Time = sharedPreferences1.getLong("TIME", 0);
            Log.e("TimePopulate", "" + Time);
                            /*long timestamp = new Date().getTime();
                            long dayTimestamp = getDayTimestamp(timestamp);*/

            SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("PREF_KEY_ID", Context.MODE_PRIVATE);
            final String Id = sharedPreferences2.getString("ID", "");
            Log.e("IDPopulate", "" + Id);


            final String user_login_id = getRef(position).getKey();
            Log.e("LoginId", user_login_id);


            FirebaseUser id = FirebaseAuth.getInstance().getCurrentUser();

            final String current_user_Id = id.getUid();

            Log.e("Current_User_Id", "" + current_user_Id);

            /*databaseReference1=FirebaseDatabase.getInstance().getReference();

            Query lastQuery = databaseReference1.child("messages").child(current_user_Id).child(user_login_id);

            lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        *//*String message = dataSnapshot.child("body").getValue().toString();

                        Log.e("ShowMessage"," "+message);*//*

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        Log.e("MessageShow"," "+data.child("body").getValue().toString());

                        String message = data.child("body").getValue().toString();

                        if(user_login_id.contains(Id)){
                            itemFriendEmailTextView.setText(message);
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


*/


            if (current_user_Id.equals(model.getUid()) && current_user_Id != null) {
                List_Selection.setVisibility(View.GONE);
                item_view.setVisibility(View.GONE);
            }

            itemFriendNameTextView.setText(model.getDisplayName());

            ////////////////25/08/2018//////////////////////////////////////////////

            databaseReference.child("users").orderByChild("uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            User user = ds.getValue(User.class);
                            String AllUid = user.getUid();
                            Log.e("GetAllUidChat ",""+AllUid);

                            databaseReferenceMessage.child(current_user_Id).child(AllUid).orderByKey().limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        for(DataSnapshot ds: dataSnapshot.getChildren())
                                        {
                                            String KeyValue = ds.getKey();
                                            Log.e("GetKeyValue ",""+KeyValue);

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //////////////////////////////////

            itemFriendEmailTextView.setText("@"+model.getUserName());

            if (user_login_id.contains(Id)) {
                //itemFriendEmailTextView.setText(Message);

                if (Time != null) {
                    Time_Update.setText(getTimePretty(Time));
                    Time_Update.setVisibility(View.VISIBLE);
                }
            }

            String GetUidUser = getRef(position).getKey();
            Log.e("GetUserIdPosition ", "" + GetUidUser);

            databaseReferencePositionlMessage = FirebaseDatabase.getInstance().getReference("users");
            databaseReferencePositionlMessage.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String GetAllUid = ds.child("uid").getValue().toString();
                        Log.e("GetAllUidReference ", "" + GetAllUid);

                        /*arrayList = new ArrayList<User>();
                        User user = new User();
                        user.setUid(GetAllUid);
                        arrayList.add(user);

                        for(int i=0;i<arrayList.size();i++)
                        {
                            User user1 = arrayList.get(i);
                            String uid = user1.getUid();

                            databaseReferenceMessage.child(uid).child(current_user_Id).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for(DataSnapshot ds : dataSnapshot.getChildren())
                                    {
                                        String Message = ds.child("body").getValue().toString();
                                        itemFriendEmailTextView.setText(Message);
                                        Log.e("GetMessageBody ",""+Message);
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }*/

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            /*itemFriendEmailTextView.setText(model.getMessage());*/

            Log.e("Display Name :", "" + model.getDisplayName() + " " + "Number :" + model.getMobileNo() + " " + "Status :" + model.getOnline() + " "
                    + "Current Time " + model.getTime() + " " + "Message " + model.getMessage() + " " + "Time" + model.getTimeStamp());

            // Check user Online Status......*******/////


            Boolean status = model.getOnline();

            Log.e("online status", "" + status);
            if (status == null) {
                online_indication.setVisibility(View.GONE);
            } else if (status == true) {
                online_indication.setVisibility(View.VISIBLE);
                Log.e("online status", "" + status);
            } else if (status == false) {
                online_indication.setVisibility(View.GONE);
                Log.e("online status", "" + status);
            }


            //String message=model.getMessage();



           /* if(message==null ){
                itemFriendEmailTextView.setText("Hay There I am Using Nashbud.....");
            }*/


            /*if(message!=null && model.getUid()!=null){
                if(model.getTimeStamp()!=null){
                    Time_Update.setText(getTimePretty(model.getTimeStamp()));
                    Time_Update.setVisibility(View.VISIBLE);
                }
            }
            else {
                Time_Update.setTextColor(Color.parseColor("#616161"));
            }*/


         /* if(isNetworkAvailable(context)){

          }*/
          /*else {
              if(isNetworkAvailable(context)==false){

                  Log.e("Else online status","" + status);
                  if(status == null){
                      online_indication.setVisibility(View.GONE);
                  }
                  else if (status == true){
                      online_indication.setVisibility(View.VISIBLE);
                      Log.e("online status","" + status);
                  }else if (status == false){
                      online_indication.setVisibility(View.GONE);
                      Log.e("online status","" + status);
                  }
              }
          }*/
                /* databaseReferenceMessage.keepSynced(true);


                    databaseReferenceMessage.child(current_user_Id).child(model.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            message = new Message();

                            for(DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                message = ds.getValue(Message.class);
                                String status = message.getStatus();
                                Log.e("GetMessageStatus",""+status);

                                    databaseReferenceMessage.child(current_user_Id).child(model.getUid()).orderByChild(status).equalTo("read").addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            messageUnReadCount = dataSnapshot.getChildrenCount();
                                            Log.e("MessageCount",""+messageUnReadCount);
                                            UnReadMessageCount.setText(messageUnReadCount+"");
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/

            Glide.with(getContext())
                    .load(model.getPhotoUrl())
                    .placeholder(R.mipmap.profile)
                    .centerCrop()
                    .dontAnimate()
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(itemUserImageView);


            itemUserImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreference sharedPreference = new SharedPreference();

                    //Intent i = new Intent(context,FullScreenPictureActivity.class);

                    String dataSplit = String.valueOf(getRef(position));

                    Log.e("after", "" + dataSplit.replaceAll(".*/", ""));

                    //i.putExtra("user_id",dataSplit.replaceAll(".", ""));
                    //context.startActivity(i);

                    sharedPreference.saveFullPicData(context, dataSplit.replaceAll(".*/", ""));

                    //InputNameDialogFragment tFragment = new InputNameDialogFragment();


                    FragmentActivity activity = (FragmentActivity) (context);
                    FragmentManager fm = activity.getSupportFragmentManager();
                    FullScreenPictureActivity tFragment = new FullScreenPictureActivity();
                    tFragment.show(fm, "fragment_alert");
                }
            });



            /*SharedPreferences sharedPreferences1=getActivity().getSharedPreferences("PREF_KEY_TIME",Context.MODE_PRIVATE);
            Long Time=sharedPreferences1.getLong("TIME",0);
            Log.e("Time",""+Time);

            if(Time!=null){
                Time_Update.setText(String.valueOf(Time));
                Time_Update.setVisibility(View.VISIBLE);
            }*/

        }


        public void setUserOnline(Boolean online_status) {
            CardView online_indication = (CardView) mView.findViewById(R.id.online_indication);

            //ImageView indication=(ImageView)mView.findViewById(R.id.indication);

            if (online_status == true) {
                online_indication.setVisibility(View.VISIBLE);
            } else {
                online_indication.setVisibility(View.GONE);
            }


        }


        public void toggleSelection(int position) {
            selectView(position, !mSelectedItemsIds.get(position));
        }

        public void removeSelection() {
            mSelectedItemsIds = new SparseBooleanArray();
            notifyDataSetChanged();
        }

        public void selectView(int position, boolean value) {

            if (value) {
                mSelectedItemsIds.put(position, value);
            } else {
                mSelectedItemsIds.delete(position);
                notifyDataSetChanged();
            }

        }

        public List<Integer> getSelectedItems() {
            List<Integer> items = new ArrayList<>(mSelectedItemsIds.size());
            for (int i = 0; i < mSelectedItemsIds.size(); ++i) {
                items.add(mSelectedItemsIds.keyAt(i));
            }
            return items;
        }

        public boolean isSelected(int position) {
            return getSelectedItems().contains(position);
        }

        public int getSelectedCount() {
            return mSelectedItemsIds.size();
        }

        public SparseBooleanArray getSelectedIds() {
            return mSelectedItemsIds;
        }

    }

    private String getTimePretty(long timestamp) {
        DateTime yesterdayDT = new DateTime(DateTime.now().getMillis() - 1000 * 60 * 60 * 24);
        yesterdayDT = yesterdayDT.withTime(0, 0, 0, 0);
        Interval today = new Interval(DateTime.now().withTimeAtStartOfDay(), Days.ONE);
        Interval yesterday = new Interval(yesterdayDT, Days.ONE);
        DateTimeFormatter timeFormatter = DateTimeFormat.shortTime();
        DateTimeFormatter dateFormatter = DateTimeFormat.shortDate();
        if (today.contains(timestamp)) {
            return timeFormatter.print(timestamp);
        } else if (yesterday.contains(timestamp)) {
            return context.getString(R.string.yesterday);
        } else {
            return dateFormatter.print(timestamp);
        }


    }

    private long getDayTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }


}
