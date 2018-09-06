package com.estar.nashbud.post;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estar.nashbud.R;
import com.estar.nashbud.camera_package.PhotoModel;
import com.estar.nashbud.thread.ThreadActivity;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.Constants;
import com.estar.nashbud.utils.SharedPreference;
import com.estar.nashbud.widgets.EmptyStateRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

public class Tag_People extends AppCompatActivity {
    ImageView imageback,Tick,tickNull,tickTick;
    TextView text_topic;
    EmptyStateRecyclerView usersRecycler;
    @BindView(R.id.activity_main_empty_view)
    TextView emptyView;
    FloatingActionButton floatingActionButton;
    Context context;
    User user;
    String getUid;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    SharedPreference sharedPreference;
    int SelectedPosition=-1;
    GridView gridView;
    ArrayList<Tag_People_Model> list = new ArrayList<>();
    ArrayList<Tag_People_Model> Tag_People_List=new ArrayList<>();
    ArrayList<String> listItem = new ArrayList<>();
    Tag_People_GridAdapter tag_people_gridAdapter;
    LinearLayout linear_grid_item,linear_tag_people_row;
    TagPeopleAdapter.UserViewHolder userViewHolder;
    EditText editSearch;
    TextView TagName;
    HorizontalScrollView HSV;
    Animation Fade_in,Fade_out;
    Tag_People_Model tag_people_model;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    User user1=new User();
    Post.MyReceiver myReceiver;
    IntentFilter intentFilter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag__people);
        imageback=(ImageView)findViewById(R.id.image_back);
        text_topic=(TextView)findViewById(R.id.text_topic);
        linear_grid_item=(LinearLayout)findViewById(R.id.linear_grid_item);
        Tick=(ImageView)findViewById(R.id.tick);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_topic.setText("Tag People");
        context = getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.activity_main_users_recycler_tagPeople);
        editSearch=(EditText)findViewById(R.id.search_tag_people);
        HSV=(HorizontalScrollView)findViewById(R.id.title_horizontalScrollView);

        Fade_in=(Animation) AnimationUtils.loadAnimation(context,R.anim.fade_in);
        Fade_out=(Animation)AnimationUtils.loadAnimation(context,R.anim.fade_out);
        myReceiver = new Post.MyReceiver();
        intentFilter = new IntentFilter("com.estar.nashbud.USER_ACTION");

        Tick.setVisibility(View.VISIBLE);

        initializeUsersRecycler();
        initializeFirebaseAuthListener();

        // Fixed the screen Orientation......
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Search Tag_People_Item........................//
        Tick.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                try
                {
                    for(int i=0;i<list.size();i++){

                        tag_people_model = list.get(i);

                        listItem.add(tag_people_model.getName());

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                Log.e("NameOfListItem",""+listItem);
                       Intent intent=new Intent("com.estar.nashbud.USER_ACTION");
                       intent.putStringArrayListExtra("ArrayListData",listItem);
                       finish();
                       sendBroadcast(intent);
                //addTagPeopleToCurrentSignInUser();


            }
        });


    }

    // Method of to show the TagPeople list in Recycler View************************//////////////////

        private void initializeUsersRecycler() {
        final TagPeopleAdapter adapter = new TagPeopleAdapter(this.getApplicationContext(), mDatabase.child("users"));
        Log.e("mDatabase","" + mDatabase.child("users"));
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
           @Override
           public void onItemClick(View view, int position) {

               LayoutInflater layoutInflater=getLayoutInflater();
               view = layoutInflater.inflate(R.layout.tag_people_row,null);

               adapter.toggleSelection(position);

               //tag_people_mode= list.get(position);

               SparseBooleanArray selected = adapter
                       .getSelectedIds();
                for (int i = (selected.size() - 1); i >= 0; i--) {
                   if (selected.valueAt(i)) {
                       User selecteditem = adapter
                               .getItem(selected.keyAt(i));
                       String name=adapter.getItem(position).getDisplayName();
                       String Image=adapter.getItem(position).getPhotoUrl();

                       Log.e("SelectedItem :",""+selecteditem.getDisplayName());
                   }
                   else
                   {
                       Toast.makeText(getApplicationContext(),"Nothing happen...!!!",Toast.LENGTH_SHORT).show();
                   }
               }

                   String name=adapter.getItem(position).getDisplayName();
                   String Image=adapter.getItem(position).getPhotoUrl();
                   int SelecteCount = adapter.getSelectedCount();

                   /*int image_cross=adapter.getItem(position).getImage_cross();*/
                   String Uid=adapter.getItem(position).getUid();
                   tickNull=(ImageView)view.findViewById(R.id.tick_null);
                   tickTick=(ImageView)view.findViewById(R.id.tick_tick);
                   linear_tag_people_row=(LinearLayout)view.findViewById(R.id.linear_tag_people_row);
                   TagName=(TextView)view.findViewById(R.id.block_name);

                    /* if(position!=list.size()){

                   }*/
                       Log.e("UserName :",name);
                       Log.e("UserPic :",Image);
                       /*Log.e("CrossPic",String.valueOf(image_cross));*/
                       Log.e("Uid",Uid);
                       Log.e("Clicked", String.valueOf(linear_tag_people_row.getId()));
              /* if(!(SelecteCount<1))
               {

                   list.add(new Tag_People_Model(name,Image));
                   Log.e("ListItem",list.toString());
                   tag_people_gridAdapter=new Tag_People_GridAdapter(getApplicationContext(),list);
               }*/

                      list.add(new Tag_People_Model(name,Image));
                      Log.e("ListItem",list.toString());
                      tag_people_gridAdapter=new Tag_People_GridAdapter(getApplicationContext(),list);

                       gridView=(GridView)findViewById(R.id.grid_view_photo);
                       int size_tag=list.size();
                       int totalWidth_tag = (83 * size_tag) * 2;

                       LinearLayout.LayoutParams params_tag = new LinearLayout.LayoutParams(
                               totalWidth_tag, LinearLayout.LayoutParams.WRAP_CONTENT);

                       gridView.setLayoutParams(params_tag);
                       gridView.setStretchMode(GridView.STRETCH_SPACING);
                       gridView.setNumColumns(size_tag);
                       gridView.setAdapter(tag_people_gridAdapter);
                       /*view.startAnimation(Fade_in);*/
                       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                           @Override
                           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                               //Toast.makeText(getApplicationContext(),"Position :"+position,Toast.LENGTH_SHORT).show();
                               list.remove(position);
                               tag_people_gridAdapter.notifyDataSetChanged();
                               Log.e("Remaining_Item",""+list);

                               if(list.isEmpty()){
                                   linear_grid_item.setVisibility(View.GONE);
                               }
                           }
                       });
                       linear_grid_item.setVisibility(View.VISIBLE);
                      /* tickNull.setVisibility(View.GONE);
                       tickTick.setVisibility(View.VISIBLE);
                       Tick.setVisibility(View.VISIBLE);*/
                 /*  else {
                       try {
                               list.remove(adapter.getItem(position).getDisplayName());
                               tickNull.setVisibility(View.VISIBLE);
                               tickTick.setVisibility(View.GONE);
                       }
                       catch (Exception e){
                           e.printStackTrace();
                           Log.e("Exception :",e.toString());
                       }

                   }*/

           }
       }));

            editSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
    }

    ///Method of Firebase Authentication User*********************/////

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
                    getUid = user.getUid();
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

        mDatabase.child("users")
                .child(user.getUid()).setValue(user);

        String instanceId = FirebaseInstanceId.getInstance().getToken();
        if (instanceId != null) {
            mDatabase.child("users")
                    .child(getUid)
                    .child("instanceId")
                    .setValue(instanceId);
        }
    }

    //// Method of TagPeople data to save into fireBase****************************//////

    private void addTagPeopleToCurrentSignInUser(){
        mAuth = FirebaseAuth.getInstance();
        Log.e("Auth","" + mAuth );
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.e("current user","" + user );
                if (user != null) {
                    //addUserToDatabase(user);
                    getUid = user.getUid();
                    user1.setUid(getUid);
                    String instanceId = FirebaseInstanceId.getInstance().getToken();
                    user1.setInstanceId(instanceId);
                    mDatabase=FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Tag_People")
                            .child(user1.getUid())
                           /* .child(user1.getDisplayName())
                            .child(user1.getMobileNo())
                            .child(user1.getPhotoUrl())*/
                            .child(user1.getInstanceId())
                            .setValue(user1);

                    Log.e("@@@@", "home:signed_in:" + user.getUid());
                } else {
                    Log.e("@@@@", "home:signed_out");

                }
            }
        };

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserSelected(DatabaseReference selectedRef) {

        Intent thread = new Intent(context, ThreadActivity.class);
        thread.putExtra(Constants.USER_ID_EXTRA, selectedRef.getKey());
        Log.e("thread activity","" + selectedRef.getKey());
        startActivity(thread);

    }

   // For RecyclerView ItemClick Class*******************************/////

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }
}
