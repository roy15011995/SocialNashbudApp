package com.estar.nashbud.contacts;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.bottombarpages.People_Fragment;
import com.estar.nashbud.profile.FriendsProfileActivity;
import com.estar.nashbud.profile.MyProfilePage;
import com.estar.nashbud.thread.ThreadActivity;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.Constants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ContactList extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    User user;
    String getUid;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    Context context;
    Menu menu;
    Toolbar toolbar;
    ImageView imageBack;
    ContactsAdapter adapter;
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String CurrentUser = firebaseUser.getUid();
        Query query = mDatabase.child("Friends").child(CurrentUser);
        Log.i("xyzQui" , query.toString());

        FirebaseRecyclerAdapter<User,ContactList.ContactListViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<User, ContactListViewHolder>
                (
                        User.class,
                        R.layout.contact_raw_data,
                        ContactList.ContactListViewHolder.class,
                        query
                )
        {
            @Override
            protected void populateViewHolder(ContactList.ContactListViewHolder viewHolder, final User model, final int position) {
                String url = "";
                String name = "";
                String uname = "";
                String uid = "";
                if (model.getPhotoUrl() != null){
                    url = model.getPhotoUrl().toString();
                }
                if (model.getDisplayName() != null){
                    name = model.getDisplayName().toString();
                }
                if ( model.getUserName() != null){
                    uname = model.getUserName().toString();
                }
                if (model.getUid() != null){
                    uid = model.getUid().toString();
                }
                viewHolder.setUserDetails(context,url,name,uname);


                viewHolder.profile_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e("GetPhotoPath :",""+model.getPhotoUrl());

                        Log.e("GetUid :",""+model.getUid());

                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        getUid = firebaseUser.getUid();

                        if (getUid.equals(model.getUid())){
                            Log.e("GetResult","BothUidEqual");
                            Intent intent = new Intent(getApplicationContext(), MyProfilePage.class);
                            startActivity(intent);
                        }
                        else if(model.getUid()!=null)
                        {
                            //Log.e("GetResult","BothUidNotEqual");
                            Intent i = new Intent(getApplicationContext(), FriendsProfileActivity.class);
                            i.putExtra("user_id",model.getUid());
                            startActivity(i);
                        }
                    }
                });

                viewHolder.profile_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e("GetPhotoPath :",""+model.getPhotoUrl());

                        Log.e("GetUid :",""+model.getUid());

                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        getUid = firebaseUser.getUid();

                        if (getUid.equals(model.getUid())){
                            Log.e("GetResult","BothUidEqual");
                            Intent intent = new Intent(getApplicationContext(), MyProfilePage.class);
                            startActivity(intent);
                        }
                        else if(model.getUid()!=null)
                        {
                            //Log.e("GetResult","BothUidNotEqual");
                            Intent i = new Intent(getApplicationContext(), FriendsProfileActivity.class);
                            i.putExtra("user_id",model.getUid());
                            startActivity(i);
                        }
                    }
                });

                viewHolder.linear_user_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(getRef(position));
                    }
                });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void FirebaseUserSearch(String SearchText)
    {
        try{
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String CurrentUser = firebaseUser.getUid();
            Query query = mDatabase.child("Friends").child(CurrentUser).orderByChild("displayName").startAt(SearchText).endAt(SearchText + "\uf8ff");
            FirebaseRecyclerAdapter<User,ContactList.ContactListViewHolder> firebaseRecyclerAdapter
                    =new FirebaseRecyclerAdapter<User, ContactListViewHolder>
                    (
                            User.class,
                            R.layout.contact_raw_data,
                            ContactList.ContactListViewHolder.class,
                            query
                    )
            {
                @Override
                protected void populateViewHolder(ContactList.ContactListViewHolder viewHolder, final User model, final int position) {

                    viewHolder.setUserDetails(context,model.getPhotoUrl(),model.getDisplayName(),model.getUserName());

                    viewHolder.profile_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Log.e("GetPhotoPath :",""+model.getPhotoUrl());

                            Log.e("GetUid :",""+model.getUid());

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            getUid = firebaseUser.getUid();

                            if (getUid.equals(model.getUid())){
                                Log.e("GetResult","BothUidEqual");
                                Intent intent = new Intent(getApplicationContext(), MyProfilePage.class);
                                startActivity(intent);
                            }
                            else if(model.getUid()!=null)
                            {
                                //Log.e("GetResult","BothUidNotEqual");
                                Intent i = new Intent(getApplicationContext(), FriendsProfileActivity.class);
                                i.putExtra("user_id",model.getUid());
                                startActivity(i);
                            }

                        }
                    });

                    viewHolder.profile_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Log.e("GetPhotoPath :",""+model.getPhotoUrl());

                            Log.e("GetUid :",""+model.getUid());

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            getUid = firebaseUser.getUid();

                            if (getUid.equals(model.getUid())){
                                Log.e("GetResult","BothUidEqual");
                                Intent intent = new Intent(getApplicationContext(), MyProfilePage.class);
                                startActivity(intent);
                            }
                            else if(model.getUid()!=null)
                            {
                                //Log.e("GetResult","BothUidNotEqual");
                                Intent i = new Intent(getApplicationContext(), FriendsProfileActivity.class);
                                i.putExtra("user_id",model.getUid());
                                startActivity(i);
                            }
                        }
                    });

                    viewHolder.linear_user_data.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(getRef(position));
                        }
                    });

                }
            };

            recyclerView.setAdapter(firebaseRecyclerAdapter);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity);
        toolbar = findViewById(R.id.activity_thread_toolbar);
        setSupportActionBar(toolbar);
        context = ContactList.this;

        imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle("");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.activity_main_users_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        initializeFirebaseAuthListener();
    }



    public static class ContactListViewHolder extends RecyclerView.ViewHolder{
        ImageView profile_pic;
        TextView profile_name;
        TextView profile_mobile;
        LinearLayout linear_user_data;
        public ContactListViewHolder(View itemView) {
            super(itemView);
            profile_pic = (ImageView)itemView.findViewById(R.id.item_user_image_view);
            profile_name = (TextView)itemView.findViewById(R.id.item_friend_name_text_view);
            profile_mobile= (TextView)itemView.findViewById(R.id.item_friend_email_text_view);
            linear_user_data = (LinearLayout)itemView.findViewById(R.id.linear_user_data);

        }

        private void setUserDetails(Context context,String Profile_pic,String Profile_name,String UserName){

            profile_name.setText(Profile_name);
            profile_mobile.setText("@"+UserName);

            Glide.with(context)
                    .load(Profile_pic)
                    .placeholder(R.drawable.user_profile_pic)
                    .centerCrop()
                    .dontAnimate()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(profile_pic);
        }
    }


    private void initializeFirebaseAuthListener() {
        mAuth = FirebaseAuth.getInstance();
        Log.e("Auth","" + mAuth );
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.e("current user","" + user );
                if (user != null) {
                    //addUserToDatabase(user);
                    getUid = user.getUid().toString();
                    Log.e("@@@@", "home:signed_in:" + user.getUid().toString());
                } else {
                    Log.e("@@@@", "home:signed_out");

                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.setTitle("");
        getMenuInflater().inflate(R.menu.contacts_menu, menu);
        this.menu = menu;
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                FirebaseUserSearch(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.view_profile:
                Toast.makeText(context,"demo clicked",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserSelected(DatabaseReference selectedRef) {

        Intent thread = new Intent(context, ThreadActivity.class);
        thread.putExtra(Constants.USER_ID_EXTRA, selectedRef.getKey());
        thread.putExtra("CheckStatus","I am alive");
        Log.e("thread activity","" + selectedRef.getKey());
        startActivity(thread);

    }
}
