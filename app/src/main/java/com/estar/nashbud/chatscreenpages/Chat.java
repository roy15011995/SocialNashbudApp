package com.estar.nashbud.chatscreenpages;

/**
 * Created by User on 31-08-2017.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.estar.nashbud.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


public class Chat extends AppCompatActivity implements View.OnClickListener {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton,image_attach;
    EditText messageArea;
    ScrollView scrollView;
    TextView accountName;
    Firebase reference1, reference2;
    Bundle extra;
    String sChatWith;
    private LinearLayout mRevealView;
    private boolean hidden = true;
    private ImageButton gallery_btn, photo_btn, video_btn, audio_btn, location_btn, contact_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        layout = findViewById(R.id.layout1);
        layout_2 = findViewById(R.id.layout2);
        sendButton = findViewById(R.id.sendButton);
        image_attach = findViewById(R.id.image_attach);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);
        accountName = findViewById(R.id.user_name);
        initView();
        image_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cx =(mRevealView.getRight() + mRevealView.getLeft()) / 2;
                int cy =(mRevealView.getTop() + mRevealView.getBottom()) / 2;
                int endradius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());
                //Below Android LOLIPOP Version
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    SupportAnimator animator =
                            ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy,0, endradius);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(800);

                    SupportAnimator animator_reverse = animator.reverse();

                    if (hidden) {
                        mRevealView.setVisibility(View.VISIBLE);
                        animator.start();
                        hidden = false;
                    } else {
                        animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                            @Override
                            public void onAnimationStart() {

                            }

                            @Override
                            public void onAnimationEnd() {
                                mRevealView.setVisibility(View.INVISIBLE);
                                hidden = true;

                            }

                            @Override
                            public void onAnimationCancel() {

                            }

                            @Override
                            public void onAnimationRepeat() {

                            }
                        });
                        animator_reverse.start();
                    }
                }
                // Android LOLIPOP And ABOVE Version
                else {
                    if (hidden) {
                        Animator anim = android.view.ViewAnimationUtils.
                                createCircularReveal(mRevealView, cx, cy, 0, endradius);
                        mRevealView.setVisibility(View.VISIBLE);
                        anim.start();
                        hidden = false;
                    } else {
                        Animator anim = android.view.ViewAnimationUtils.
                                createCircularReveal(mRevealView, cx, cy, endradius, 0);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mRevealView.setVisibility(View.INVISIBLE);
                                hidden = true;
                            }
                        });
                        anim.start();
                    }
                }

           //     android.R.id.home
                //supportFinishAfterTransition();

            }
        });
        extra = getIntent().getExtras();
        if (extra != null) {
            sChatWith = extra.getString("chat_with");
            Log.e("chat with getting", "" + sChatWith);
        }
        accountName.setText(sChatWith);
        Firebase.setAndroidContext(this);

        reference1 = new Firebase("https://nashbud-112f5.firebaseio.com/messages/" + UserDetails.mobileNumber + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://nashbud-112f5.firebaseio.com/messages/" +UserDetails.chatWith + "_" + UserDetails.mobileNumber);
        Log.e("reference 1", "is" + reference1);
        Log.e("reference 2", "is" + reference2);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("mobile_number", UserDetails.mobileNumber);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("mobile_number").toString();

                if (userName.equals(UserDetails.mobileNumber)) {
                    addMessageBox("You:-\n" + message, 1);
                } else {
                    addMessageBox(UserDetails.chatWith + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(Chat.this);
        textView.setText(message);
        // String date = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());
        // holder.tvTime.setText(date);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            lp2.gravity = Gravity.END;
            textView.setBackgroundResource(R.drawable.balloon_outgoing_normal);
            //textView.setBackgroundColor(getResources().getColor(R.color.dot_light_screen2));
        } else {
            lp2.gravity = Gravity.START;
            textView.setBackgroundResource(R.drawable.balloon_incoming_normal);
            //textView.setBackgroundColor(getResources().getColor(R.color.dot_light_screen4));
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }

    private void initView() {
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mRevealView = findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.GONE);

        gallery_btn = findViewById(R.id.gallery_img_btn);
        photo_btn = findViewById(R.id.photo_img_btn);
        video_btn = findViewById(R.id.video_img_btn);
        audio_btn = findViewById(R.id.audio_img_btn);
//        location_btn = findViewById(R.id.location_img_btn);
//        contact_btn = findViewById(R.id.contact_img_btn);

        gallery_btn.setOnClickListener(this);
        photo_btn.setOnClickListener(this);
        video_btn.setOnClickListener(this);
        audio_btn.setOnClickListener(this);
//        location_btn.setOnClickListener(this);
//        contact_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        hideRevealView();
        switch (v.getId()) {

            case R.id.gallery_img_btn:
                Toast.makeText(Chat.this, "gallery", Toast.LENGTH_SHORT).show();
                break;
            case R.id.photo_img_btn:
                Toast.makeText(Chat.this, "photo", Toast.LENGTH_SHORT).show();
                break;
            case R.id.video_img_btn:
                Toast.makeText(Chat.this, "video", Toast.LENGTH_SHORT).show();
                break;
            case R.id.audio_img_btn:
                Toast.makeText(Chat.this, "audio", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.location_img_btn:
//                Toast.makeText(MainActivity.this, "location", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.contact_img_btn:
//                Toast.makeText(MainActivity.this, "contact", Toast.LENGTH_SHORT).show();
//                break;
        }
    }

    private void hideRevealView() {
        if (mRevealView.getVisibility() == View.VISIBLE) {
            mRevealView.setVisibility(View.GONE);
            hidden = true;
        }
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.action_clip:
//
//
//        }
//        return super.onOptionsItemSelected(item);
//    }



}