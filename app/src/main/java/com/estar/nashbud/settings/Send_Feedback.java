package com.estar.nashbud.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estar.nashbud.R;
import com.estar.nashbud.camera_package.CropView;
import com.estar.nashbud.camera_package.CustomGalleryFragment;
import com.estar.nashbud.camera_package.ImageModel;
import com.estar.nashbud.camera_package.Photo_GridAdapter;
import com.estar.nashbud.camera_package.ShowImageActivity;
import com.estar.nashbud.chatscreenpages.EditPost;
import com.estar.nashbud.chatscreenpages.SliderPagerAdapter;
import com.estar.nashbud.post.Post;
import com.estar.nashbud.post.Post_Adapter;
import com.estar.nashbud.post.Post_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Send_Feedback extends AppCompatActivity {
    TextView text_topic;
    ImageView image_back;
    ImageView image_send_feedback;
    EditText sendText;
    private static ImageButton Add_Screenshots;
    private static ArrayList<ImageModel> photoModels = new ArrayList<>();
    private static ArrayList<String> getPhoto = new ArrayList<>();
    private static GridView gridView,gridView1;
    ImageView imageView;
    public static String[] arrPath;
    Toolbar toolbar;
    ImageModel imageModel;
    CropView cropView;
    Bundle bundle;
    String place,Address;
    String[] mStringArray;
    private int REQUEST_POST = 0, REQUEST_CHAT = 1;
    private static Send_Feedback ins;
    LinearLayout linear_photo_grid;
    ViewPager viewPager;
    SliderPagerAdapterSendFeedback sliderPagerAdapter;
    DatabaseReference databaseReference_sendFeedback;
    FirebaseAuth mAuth;
    private static String CurrentUser;
    private static String GetText,CurrentDate,CurrentTime,RandomSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send__feedback);

        text_topic = (TextView) findViewById(R.id.send_feedback);
        image_back = (ImageView) findViewById(R.id.image_back_feedback);
        image_send_feedback = (ImageView) findViewById(R.id.image_send_feedback);
        Add_Screenshots = (ImageButton) findViewById(R.id.add_screen_button);
        //gridView =(GridView) findViewById(R.id.feedback_screen_row);
        ins = this;
         linear_photo_grid = (LinearLayout) findViewById(R.id.linear_photo_send_feedback);
         viewPager = (ViewPager)findViewById(R.id.vp_slider_send_feedback);
         mAuth = FirebaseAuth.getInstance();
         CurrentUser = mAuth.getCurrentUser().getUid();

        if(CurrentUser!=null){
            Log.e("GetCurrentUser",""+CurrentUser);
        }


        databaseReference_sendFeedback = FirebaseDatabase.getInstance().getReference().child("Feedback");



        Add_Screenshots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(Send_Feedback.this,GalleryActivity.class);
               startActivity(intent);

            }
        });

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_topic.setText("Send Feedback");
    }

    public static Send_Feedback getInstace(){
        return ins;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);


    }


    public void SendFeedback(final ArrayList<String>sendPhoto){

        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        CurrentDate = simpleDateFormat.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH : mm");
        CurrentTime = simpleTimeFormat.format(calendarTime.getTime());

        RandomSave = CurrentDate + CurrentTime;

        sendText = (EditText)findViewById(R.id.write_feedback);
        GetText = sendText.getText().toString();
        final ImageModel imageModel = new ImageModel();
        imageModel.setAl_imagepath(sendPhoto);
        imageModel.setSendFeedback(GetText);


        image_send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference_sendFeedback.child(CurrentUser + RandomSave).setValue(imageModel)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isComplete()){
                                    Toast.makeText(getApplicationContext(),"Feedback Successfully Send..",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else
                                {
                                    String messages = task.getException().getMessage();
                                    Toast.makeText(getApplicationContext(),"Failed to send...Pls Try again Later "+messages,Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

            }
        });
    }

    public void updateGridViewPhoto(final ArrayList<String>getGalleryPhoto){
        if(getGalleryPhoto==null){
            linear_photo_grid.setVisibility(View.GONE);
            Add_Screenshots.setVisibility(View.VISIBLE);

        }

        if(getGalleryPhoto!=null)
        {
            Log.e("GetGalleryPhotoOnUi",""+getGalleryPhoto);

            linear_photo_grid.setVisibility(View.VISIBLE);
            Add_Screenshots.setVisibility(View.GONE);

            mStringArray = new String[getGalleryPhoto.size()];
            mStringArray = getGalleryPhoto.toArray(mStringArray);
            photoModels.clear();

            Log.e("GetPhotoForPost",""+getGalleryPhoto);

            try{
                for(int i=0;i<mStringArray.length;i++){

                    Log.e("Image_Size",""+getGalleryPhoto.size());

                    ImageModel imageModel = new ImageModel();

                    imageModel.setImagePath(mStringArray[i]);

                    photoModels.add(imageModel);

                    //list.add(i,new Post_Model(getPhoto));

                    Log.e("GetBreakImage :",""+new ImageModel(getGalleryPhoto).getAl_imagepath().get(i));
                }
            }

            catch (Exception e){
                e.printStackTrace();
            }
        }

        Send_Feedback.getInstace().runOnUiThread(new Runnable() {
            @Override
            public void run() {
               /* Photo_GridAdapter photo_gridAdapter = new Photo_GridAdapter(Send_Feedback.this,photoModels);

                int size_tag=photoModels.size();
                int totalWidth_tag = (360 * size_tag) * 2;

                LinearLayout.LayoutParams params_tag = new LinearLayout.LayoutParams(
                        totalWidth_tag, LinearLayout.LayoutParams.WRAP_CONTENT);

                gridView.setLayoutParams(params_tag);
                gridView.setStretchMode(GridView.STRETCH_SPACING);
                gridView.setNumColumns(size_tag);
                gridView.setAdapter(photo_gridAdapter);*/

                sliderPagerAdapter = new SliderPagerAdapterSendFeedback(Send_Feedback.this,photoModels);
                viewPager.setAdapter(sliderPagerAdapter);

                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

            }
        });



    }

    public static class SendFeedback extends BroadcastReceiver {
        Bundle Extra;
        String[]TagPeople;
        @Override
        public void onReceive(Context context, Intent intent) {
            Extra = intent.getExtras();
            if(Extra!=null){

                if(Extra.containsKey("GetPhoto")){

                    try{
                        getPhoto = Extra.getStringArrayList("GetPhoto");
                        Log.e("GetGalleryPhoto",""+getPhoto);
                        Send_Feedback.getInstace().updateGridViewPhoto(getPhoto);
                        Send_Feedback.getInstace().SendFeedback(getPhoto);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}
