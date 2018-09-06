package com.estar.nashbud.chatscreenpages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.estar.nashbud.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Sudipta on 9/20/2017.
 */

public class Chats_fragment extends Fragment {
    Bitmap bm = null;
    Context context;
    File destination;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> items = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    private String userChoosenTask, conImage, output;
    private String stEditName;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView image;
    ChatListAdapter chatListAdapter;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;

    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    private static final String USER_ID = "53";


    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chats_fragment, container, false);
        context = getActivity();
        usersList = view.findViewById(R.id.usersList);
        noUsersText = view.findViewById(R.id.noUsersText);

        getMobile();
//        image = view.findViewById(R.id.image_camera);
//        image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImage();
//            }
//        });

        /*mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        tvNoMovies = (TextView) view.findViewById(R.id.tv_no_movies);

        //scale animation to shrink floating actionbar
        //shrinkAnim = new ScaleAnimation(1.15f, 0f, 1.15f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);


        //Say Hello to our new Firebase UI Element, i.e., FirebaseRecyclerAdapter
        final FirebaseRecyclerAdapter<UserDetails,MovieViewHolder> adapter = new FirebaseRecyclerAdapter<UserDetails, MovieViewHolder>(
                UserDetails.class,
                R.provider_paths.user_raw_data,
                MovieViewHolder.class,
                //referencing the node where we want the database to store the data from our Object
                mDatabaseReference.child("mobile_number").getRef()
        ) {
            @Override
            protected void populateViewHolder(MovieViewHolder viewHolder, UserDetails model, int position) {
                if(tvNoMovies.getVisibility()== View.VISIBLE){
                    tvNoMovies.setVisibility(View.GONE);
                }
                viewHolder.tvMovieName.setText(model.getMobileNumber());
                //viewHolder.ratingBar.setRating(model.getMovieRating());
                //Picasso.with(context).load(model.getProfileImg()).into(viewHolder.ivMoviePoster);
            }
        };

        mRecyclerView.setAdapter(adapter);*/

        return view;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder{

        TextView tvMovieName;
        RatingBar ratingBar;
        ImageView ivMoviePoster;

        public MovieViewHolder(View v) {
            super(v);
            tvMovieName = (TextView) v.findViewById(R.id.item_friend_name_text_view);
            //ratingBar = (RatingBar) v.findViewById(R.id.rating_bar);
            ivMoviePoster = (ImageView) v.findViewById(R.id.item_user_image_view);
        }
    }

    private void getMobile() {
        pd = new ProgressDialog(context);
        pd.setMessage("Updating chat...");
        pd.show();
        String url = "https://nashbud-112f5.firebaseio.com/mobile_number.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error", "" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

    public void doOnSuccess(String s) {

        try {
            JSONObject jsonResponse = new JSONObject(s);
            Iterator i = jsonResponse.keys();
            UserDetails notification = new UserDetails();
            String key = "";

            if (i != null){
                while (i.hasNext()) {
                    //notification.setMobileNumber(UserDetails.mobileNumber);
                    key = i.next().toString();

                    if (!key.equals(UserDetails.mobileNumber)) {
                        //notification.setMobileNumber(UserDetails.mobileNumber);
                        Log.e("mobile no do on success", "" + key);
                        notification.setMobileNumber(key);
                        items.add(key);
                        /*chatListAdapter = new ChatListAdapter(context.getApplicationContext(),items);
                        usersList.setAdapter(chatListAdapter);*/
                        //chatListAdapter.notifyDataSetInvalidated();
                        usersList.setAdapter(new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_list_item_1, items));
                    }
                    totalUsers++;
                }

            }
            else {

                Log.e("data not","found");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (totalUsers <= 1) {
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        } else {
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);

            //usersList.setAdapter(new ArrayAdapter<String>(context.getApplicationContext(), android.R.provider_paths.simple_list_item_1, al));
        }
        pd.dismiss();

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = items.get(position);
                Log.e("item position","" + items.get(position));
                Log.e("mobile no do on success", "" + UserDetails.mobileNumber);
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("chat_with", UserDetails.chatWith);
                startActivity(intent);
            }
        });
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (userChoosenTask.equals("Take Photo"))
//                        cameraIntent();
//                    else if (userChoosenTask.equals("Choose from Library"))
//                        galleryIntent();
//                }
//                break;
//        }
//    }

//    private void selectImage() {
//        final CharSequence[] items = {"Take Photo", "Choose from Library"};
//
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
//        builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                boolean result = Utility.checkPermission(context);
//
//                if (items[item].equals("Take Photo")) {
//                    userChoosenTask = "Take Photo";
//                    if (result)
//                        cameraIntent();
//
//                } else if (items[item].equals("Choose from Library")) {
//                    userChoosenTask = "Choose from Library";
//                    if (result)
//                        galleryIntent();
//
//                }
//            }
//        });
//        builder.show();
//    }
//
//    private void galleryIntent() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_PICK);//
//        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
//    }
//
//    private void cameraIntent() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_CAMERA);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == SELECT_FILE)
//                onSelectFromGalleryResult(data);
//            else if (requestCode == REQUEST_CAMERA)
//                onCaptureImageResult(data);
//        }
//    }
//
//    private void onCaptureImageResult(Intent data) {
//        bm = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//
//        destination = new File(Environment.getExternalStorageDirectory(),
//                System.currentTimeMillis() + ".jpg");
//
//        FileOutputStream fo;
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //profile_image.setImageBitmap(bm);
//
//        byte[] imageBytes = bytes.toByteArray();
//        conImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        //output = adding.concat(conImage);
//        output = conImage;
//
//        Log.e("image converted", "" + output);
//
//        Log.e("image DESTINATION", "" + destination);
//
//
//        output = output.replaceAll("\\s+", "");
//        Log.e("space removed", "" + output);
//
//        //uploadImage();
//        Log.e("image capture", "" + bm);
//        /*String base = output;
//
//        Log.e("base64 getting","" + base);
//
//        String base64String = output;
//        String base64Image = base64String.split(",")[1];
//
//        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        imageProfilePic.setImageBitmap(decodedByte);*/
//    }
//
//    @SuppressWarnings("deprecation")
//    private void onSelectFromGalleryResult(Intent data) {
//
//
//        if (data != null) {
//            try {
//                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        //profile_image.setImageBitmap(bm);
//
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//
//        destination = new File(Environment.getExternalStorageDirectory(),
//                System.currentTimeMillis() + ".jpg");
//
//        FileOutputStream fo;
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        byte[] imageBytes = bytes.toByteArray();
//        conImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        //output = adding + conImage;
//        output = conImage;
//
//        //uploadImage();
//        Log.e("image gallery location", "" + destination);
//
//        Log.e("image gallery", "" + bm);
//
//
//    }

//    private void uploadImage() {
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Uploading, please wait...");
//        progressDialog.show();
//        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
//            @Override
//            public void onResponse(String s) {
//                progressDialog.dismiss();
//                if(s.equals("true")){
//                    Toast.makeText(context, "Uploaded Successful", Toast.LENGTH_LONG).show();
//                }
//                else{
//                    Toast.makeText(context, "Some error occurred!", Toast.LENGTH_LONG).show();
//                }
//            }
//        },new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(context, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
//            }
//        }) {
//            //adding parameters to send
//            @Override
//            protected Map<String, > getParams() throws AuthFailureError {
//                Map<String, String> parameters = new HashMap<String, String>();
//                parameters.put("image", destination);
//                return parameters;
//            }
//        };
//
//        RequestQueue rQueue = Volley.newRequestQueue(context);
//        rQueue.add(request);
//    }
//}
}