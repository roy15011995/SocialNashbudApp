package com.estar.nashbud.upload_photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.estar.nashbud.chatscreenpages.ChatScreenOne;
import com.estar.nashbud.R;

public class InviteFriends extends AppCompatActivity implements View.OnClickListener {
    Button buttonInviteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        buttonInviteAll = findViewById(R.id.buttonInviteAll);
        buttonInviteAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonInviteAll) {
            Intent intent = new Intent(InviteFriends.this, ChatScreenOne.class);
            startActivity(intent);
            finish();
        }
    }
}
