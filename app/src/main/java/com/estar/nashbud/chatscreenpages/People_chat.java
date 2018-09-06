package com.estar.nashbud.chatscreenpages;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.estar.nashbud.R;
import com.estar.nashbud.upload_photo.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class People_chat extends Fragment {

    DatabaseReference databaseReference;
    View view;
    User users;

    public People_chat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_people_chat, container, false);

        users = new User();
        Cursor cursor;
        // Get the ContentResolver
        ContentResolver cr = getActivity().getContentResolver();
        // Get the Cursor of all the contacts
        cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        // Move the cursor to first. Also check whether the cursor is empty or not.
        try{
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        // Iterate through the cursor
                        do {
                            // Get the contacts name
                            String id = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID));
                            Cursor phones = getActivity().getContentResolver()
                                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                            null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                            null,
                                            null);
                            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            if(phones!=null){

                                while (phones.moveToNext()){
                                    String phoneNumber = phones.getString(
                                            phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    Log.e("PhoneNumber", "doMagicContacts: " + name + " " + phoneNumber);
                                    //contacts.add(new Model(name,phoneNumber));
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

                                    databaseReference.orderByChild("mobileNo").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot Sdata : dataSnapshot.getChildren())
                                            {

                                                users = Sdata.getValue(User.class);
                                                Log.e("NumberMatch :",""+"\n"+users.getMobileNo());
                                                Log.e("Name :",""+"\n"+users.getDisplayName());
                                                Log.e("Photo :",""+"\n"+users.getPhotoUrl());
                                                Log.e("UserIdTable",""+"\n"+users.getUid());



                                                /*list.add(users);
                                                customAdapter = new CustomAdapterLike(LikeActivity.this,list);
                                                listView.setAdapter(customAdapter);*/
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }
                            phones.close();


                        } while (cursor.moveToNext());
                    }
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        // Close the curosor
        cursor.close();

        return view;
    }

}
