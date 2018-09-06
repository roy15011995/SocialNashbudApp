package com.estar.nashbud.bottombarpages;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.profile.FriendsProfileActivity;
import com.estar.nashbud.profile.MyProfilePage;
import com.estar.nashbud.upload_photo.User;
import com.estar.nashbud.utils.SharedPreference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class People_Fragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    Context context;
    User user;
    String getUid;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    SharedPreference sharedPreference;
    EditText FindUser;
    ImageButton imageButton;


    public People_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_people, container, false);
        // Inflate the layout for this fragment
        recyclerView=(RecyclerView)view.findViewById(R.id.discover_people_RecyclerView);
        FindUser = (EditText)view.findViewById(R.id.firebase_user_search);
        //imageButton = (ImageButton)view.findViewById(R.id.search_btn);
        context = getActivity();
        sharedPreference = new SharedPreference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        /*imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


      /*  if(FindUser.isCursorVisible())
        {
            View view1 = getActivity().getLayoutInflater().inflate(R.layout.discover_fragment,null);
            TabLayout tabLayout = (TabLayout)view1.findViewById(R.id.Tab_Layout_discover);
            tabLayout.setVisibility(View.GONE);

        }*/

        FindUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                //FirebaseNumberSearch(GetSearchResult);

            }

            @Override
            public void afterTextChanged(Editable s) {
                String GetSearchResult = s.toString();
                //String firstLetterCapital = GetSearchResult.substring(0, 1).toUpperCase() + GetSearchResult.substring(1);

                FirebaseUserSearch(GetSearchResult);
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void FirebaseNumberSearch(String Number)
    {
        Query query = mDatabase.orderByChild("mobileNo").startAt(Number).endAt(Number + "\uf8ff");
        FirebaseRecyclerAdapter<User,PeopleViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<User, PeopleViewHolder>
                (
                        User.class,
                        R.layout.discover_people_row,
                        People_Fragment.PeopleViewHolder.class,
                        query
                )
        {
            @Override
            protected void populateViewHolder(PeopleViewHolder viewHolder, User model, int position) {

                //viewHolder.setUserDetails(context,model.getPhotoUrl(),model.getDisplayName(),model.getStatus(), model.getUserName(),model.getUid());

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void FirebaseUserSearch(String SearchText)
    {

        Query query = mDatabase.orderByChild("displayName").startAt(SearchText).endAt(SearchText + "\uf8ff");
        Log.i("xyzQui" , query.toString());
        FirebaseRecyclerAdapter<User,PeopleViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<User, PeopleViewHolder>
                (
                        User.class,
                        R.layout.discover_people_row,
                        People_Fragment.PeopleViewHolder.class,
                        query
                )
        {
            @Override
            protected void populateViewHolder(PeopleViewHolder viewHolder, final User model, int position) {

                viewHolder.setUserDetails(context,model.getPhotoUrl(),model.getDisplayName(), model.getUserName(),model.getUid());

                viewHolder.profile_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e("GetPhotoPath :",""+model.getPhotoUrl());

                        Log.e("GetUid :",""+model.getUid());

                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        getUid = firebaseUser.getUid();

                        if (getUid.equals(model.getUid())){
                            Log.e("GetResult","BothUidEqual");
                            Intent intent = new Intent(getActivity(), MyProfilePage.class);
                            startActivity(intent);
                        }
                        else if(model.getUid()!=null)
                        {
                            //Log.e("GetResult","BothUidNotEqual");
                            Intent i = new Intent(getActivity(), FriendsProfileActivity.class);
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
                            Intent intent = new Intent(getActivity(), MyProfilePage.class);
                            startActivity(intent);
                        }
                        else if(model.getUid()!=null)
                        {
                            //Log.e("GetResult","BothUidNotEqual");
                            Intent i = new Intent(getActivity(), FriendsProfileActivity.class);
                            i.putExtra("user_id",model.getUid());
                            startActivity(i);
                        }
                    }
                });

                viewHolder.item_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("GetPhotoPath :",""+model.getPhotoUrl());

                        Log.e("GetUid :",""+model.getUid());

                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        getUid = firebaseUser.getUid();

                        if (getUid.equals(model.getUid())){
                            Log.e("GetResult","BothUidEqual");
                            Intent intent = new Intent(getActivity(), MyProfilePage.class);
                            startActivity(intent);
                        }
                        else if(model.getUid()!=null)
                        {
                            //Log.e("GetResult","BothUidNotEqual");
                            Intent i = new Intent(getActivity(), FriendsProfileActivity.class);
                            i.putExtra("user_id",model.getUid());
                            startActivity(i);
                        }
                    }
                });

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<User,PeopleViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<User, PeopleViewHolder>
                (
                        User.class,
                        R.layout.discover_people_row,
                        People_Fragment.PeopleViewHolder.class,
                        mDatabase
                )
        {
            @Override
            protected void populateViewHolder(PeopleViewHolder viewHolder, final User model, int position) {

                viewHolder.setUserDetails(context,model.getPhotoUrl(),model.getDisplayName(),model.getUserName(),model.getUid());


                viewHolder.profile_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e("111111111GetPhotoPath :",""+model.getPhotoUrl());

                        Log.e("122222222GetUid :",""+model.getUid());

                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        getUid = firebaseUser.getUid();

                        if (getUid.equals(model.getUid())){
                            Log.e("GetResult","BothUidEqual");
                            Intent intent = new Intent(getActivity(), MyProfilePage.class);
                            startActivity(intent);
                        }
                        else if(model.getUid()!=null)
                        {
                            //Log.e("GetResult","BothUidNotEqual");
                            Intent i = new Intent(getActivity(), FriendsProfileActivity.class);
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
                            Intent intent = new Intent(getActivity(), MyProfilePage.class);
                            startActivity(intent);
                        }
                        else if(model.getUid()!=null)
                        {
                            //Log.e("GetResult","BothUidNotEqual");
                            Intent i = new Intent(getActivity(), FriendsProfileActivity.class);
                            i.putExtra("user_id",model.getUid());
                            startActivity(i);
                        }
                    }
                });

                viewHolder.item_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("GetPhotoPath :",""+model.getPhotoUrl());

                        Log.e("GetUid :",""+model.getUid());

                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        getUid = firebaseUser.getUid();

                        if (getUid.equals(model.getUid())){
                            Log.e("GetResult","BothUidEqual");
                            Intent intent = new Intent(getActivity(), MyProfilePage.class);
                            startActivity(intent);
                        }
                        else if(model.getUid()!=null)
                        {
                            //Log.e("GetResult","BothUidNotEqual");
                            Intent i = new Intent(getActivity(), FriendsProfileActivity.class);
                            i.putExtra("user_id",model.getUid());
                            startActivity(i);
                        }
                    }
                });



            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class PeopleViewHolder extends RecyclerView.ViewHolder{
        ImageView profile_pic;
        TextView profile_name;
        TextView profile_mobile;
        LinearLayout item_row;
        View view;
        public PeopleViewHolder(View itemView) {
            super(itemView);
             profile_pic = (ImageView)itemView.findViewById(R.id.people_discover_pic);
             profile_name = (TextView)itemView.findViewById(R.id.people_discover_name);
             profile_mobile= (TextView)itemView.findViewById(R.id.people_discover_mobile);
             item_row = (LinearLayout)itemView.findViewById(R.id.item_row_discover);
             view =(View)itemView.findViewById(R.id.view_line);

        }

        private void setUserDetails(Context context, String Profile_pic, String Profile_name, String userName,String Uid){

            FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
            String Current_User = firebaseUser.getUid().toString();
            Log.e("GetUidInMethod ",""+Uid);
            if(Uid!=null)
            {
                if(Uid.contains(Current_User))
                {
                    item_row.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                    if(Uid.equals("null"))
                    {
                        item_row.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                    }
                }
                else
                {
                    profile_name.setText(Profile_name);
                    if(userName != null) {
                        profile_mobile.setText("@" + userName);
                    }

                    Glide.with(context)
                            .load(Profile_pic)
                            .placeholder(R.drawable.user_profile_pic)
                            .centerCrop()
                            .dontAnimate()
                            .bitmapTransform(new CropCircleTransformation(context))
                            .into(profile_pic);
                }
            }else{
                profile_name.setVisibility(View.GONE);
                item_row.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }

        }
    }


 /*   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main,menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }*/

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }*/

}
