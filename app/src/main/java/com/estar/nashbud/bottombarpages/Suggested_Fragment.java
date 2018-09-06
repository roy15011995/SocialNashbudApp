package com.estar.nashbud.bottombarpages;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;
import com.estar.nashbud.post.Post;
import com.estar.nashbud.profile.FriendsProfileActivity;
import com.estar.nashbud.profile.MyProfilePage;
import com.estar.nashbud.upload_photo.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class Suggested_Fragment extends Fragment implements SwipyRefreshLayout.OnRefreshListener {
    View view;
    static ArrayList<String> alContacts = new ArrayList<String>();
    public static final int REQUEST_READ_CONTACTS = 79;
    Context context;
    String contactNumber,ModifiedContact,Contact,getUid;
    private DatabaseReference mDatabase;
    RecyclerView recyclerView;
    SwipyRefreshLayout swipyRefreshLayout;
    EditText SearchUser;
    ProgressDialog dialog;
    private static Suggested_Fragment ins;

    public Suggested_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_suggested, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.discover_people_RecyclerView_sync);
        swipyRefreshLayout = (SwipyRefreshLayout)view.findViewById(R.id.swipyrefreshlayout);
        SearchUser = (EditText)view.findViewById(R.id.firebase_user_search_sync);
        swipyRefreshLayout.setOnRefreshListener(this);
        context = getActivity();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { Manifest.permission.READ_CONTACTS },
                    REQUEST_READ_CONTACTS);

        } else {

            if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {

                new BackgroundContactsFetching(getActivity()).execute();
            }

        }

        SearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String SearchResult = s.toString();
                FirebaseUserSearch(SearchResult);


            }
        });

        return view;
    }

    private void readContacts() {

        ContentResolver cr = context.getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(cursor.moveToFirst())
        {
            do
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                    while (pCur.moveToNext())
                    {
                         contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                              ModifiedContact = contactNumber.replaceAll("[\\s\\-()]", "");
                              if(ModifiedContact.length()==10 ){

                                  Contact = "+91"+ModifiedContact;
                                  alContacts.add(Contact);
                               }
                               else if(ModifiedContact.startsWith("+91"))
                              {
                                  alContacts.add(ModifiedContact);
                              }

                        break;
                    }

                    pCur.close();
                }

            } while (cursor.moveToNext()) ;
            Log.e("alContact","" + alContacts);
        }
    }

    public class BackgroundContactsFetching extends AsyncTask<Void,Void,ArrayList<String>>
    {
      Context Ctx;

        public BackgroundContactsFetching(Context ctx) {
            Ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Your Contacts is Syncing...!!!");
            dialog.setMessage("Please Wait...!!!");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ContentResolver cr = context.getContentResolver(); //Activity/Application android.content.Context
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if(cursor.moveToFirst())
            {
                do
                {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                    {
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                        while (pCur.moveToNext())
                        {
                            contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            ModifiedContact = contactNumber.replaceAll("[\\s\\-()]", "");
                            if(ModifiedContact.length()==10 ){

                                Contact = "+91"+ModifiedContact;
                                alContacts.add(Contact);
                            }
                            else if(ModifiedContact.startsWith("+91"))
                            {
                                alContacts.add(ModifiedContact);
                            }

                            break;
                        }

                        pCur.close();
                    }

                } while (cursor.moveToNext()) ;
            }
            return alContacts;
        }

        @Override
        protected void onPostExecute(ArrayList<String> AllContacts) {
            Log.e("GetAllMobileContacts ",""+AllContacts);
            dialog.dismiss();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_READ_CONTACTS :
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //readContacts();
                            new BackgroundContactsFetching(getActivity()).execute();
                        }
                    },2000);

                } else {

                    if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_CONTACTS)) {
                        new AlertDialog.Builder(getContext()).
                                setTitle("Read Contacts permission").
                                setMessage("You need to grant read contacts permission to use read" +
                                        " contacts feature. Retry and grant it !").show();
                    } else {

                        new AlertDialog.Builder(getContext()).
                                setTitle("Read Contacts permission denied").
                                setMessage("You denied read contacts permission." +
                                        " So, the feature will be disabled. To enable it" +
                                        ", go on settings and " +
                                        "grant read contacts for the application").show();
                           }

                      }

                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<User,Suggested_Fragment.SuggestedViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<User, Suggested_Fragment.SuggestedViewHolder>
                (
                        User.class,
                        R.layout.suggested_row_layout,
                        Suggested_Fragment.SuggestedViewHolder.class,
                        mDatabase.orderByChild("mobileNo")
                )
        {
            @Override
            protected void populateViewHolder(Suggested_Fragment.SuggestedViewHolder viewHolder, final User model, int position) {

                viewHolder.setUserDetails(context,model.getPhotoUrl(),model.getDisplayName(),model.getMobileNo(),model.getStatus(), model.getUserName(),model.getUid());


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
    public void onRefresh(final SwipyRefreshLayoutDirection direction) {
        Log.d("MainActivity", "Refresh triggered at "
                + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));

        FirebaseRecyclerAdapter<User,Suggested_Fragment.SuggestedViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<User, Suggested_Fragment.SuggestedViewHolder>
                (
                        User.class,
                        R.layout.suggested_row_layout,
                        Suggested_Fragment.SuggestedViewHolder.class,
                        mDatabase.orderByChild("mobileNo")
                )
        {
            @Override
            protected void populateViewHolder(Suggested_Fragment.SuggestedViewHolder viewHolder, final User model, int position) {

                viewHolder.setUserDetails(context,model.getPhotoUrl(),model.getDisplayName(),model.getMobileNo(),model.getStatus() , model.getUserName(),model.getUid());


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

    public static class SuggestedViewHolder extends RecyclerView.ViewHolder{

        ImageView profile_pic;
        TextView profile_name;
        TextView profile_mobile;
        LinearLayout item_row;
        View view_line;

        public SuggestedViewHolder(View itemView) {
            super(itemView);
            profile_pic = (ImageView)itemView.findViewById(R.id.people_suggested_pic);
            profile_name = (TextView)itemView.findViewById(R.id.people_suggested_name);
            profile_mobile= (TextView)itemView.findViewById(R.id.people_suggested_mobile);
            item_row = (LinearLayout)itemView.findViewById(R.id.item_row);
            view_line = (View)itemView.findViewById(R.id.view_line);

        }

        private void setUserDetails(Context context, String Profile_pic, String Profile_name, String Profile_mobile, String Profile_Status, String userName,String Uid){
            FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
            String Current_User = firebaseUser.getUid().toString();
            Log.e("GetUidInMethod ",""+Current_User);

            if(alContacts.contains(Profile_mobile))
            {
                if(Uid.contains(Current_User))
                {
                    item_row.setVisibility(View.GONE);
                    view_line.setVisibility(View.GONE);
                }
                else
                {
                    Log.e("GetMobileNumberMatch ",""+Profile_mobile);
                    profile_name.setText(Profile_name);
                    profile_mobile.setText("@"+userName);

                    Glide.with(context)
                            .load(Profile_pic)
                            .placeholder(R.drawable.user_profile_pic)
                            .centerCrop()
                            .dontAnimate()
                            .bitmapTransform(new CropCircleTransformation(context))
                            .into(profile_pic);
                }

            }
            else if(!alContacts.contains(Profile_mobile))
            {
                item_row.setVisibility(View.GONE);
                view_line.setVisibility(View.GONE);

                ////////////////////////////////////////////////////////

            }

        }

    }

    private void FirebaseUserSearch(String SearchText)
    {
        Query query = mDatabase.orderByChild("displayName").startAt(SearchText).endAt(SearchText + "\uf8ff");

        FirebaseRecyclerAdapter<User,Suggested_Fragment.SuggestedViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<User, Suggested_Fragment.SuggestedViewHolder>
                (
                        User.class,
                        R.layout.suggested_row_layout,
                        Suggested_Fragment.SuggestedViewHolder.class,
                        query
                )
        {
            @Override
            protected void populateViewHolder(Suggested_Fragment.SuggestedViewHolder viewHolder, final User model, int position) {

                viewHolder.setUserDetails(context,model.getPhotoUrl(),model.getDisplayName(),model.getMobileNo(),model.getStatus(), model.getUserName(),model.getUid());


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



}
