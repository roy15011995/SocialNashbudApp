package com.estar.nashbud.camera_package;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.estar.nashbud.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageSettingActivity extends AppCompatActivity {
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_setting);
        imageView = (ImageView)findViewById(R.id.Camera_setting_image);

        byte[] picByte = getIntent().getExtras().getByteArray("photo");
        Bitmap bm = BitmapFactory.decodeByteArray(picByte, 0, picByte.length);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        imageView.setImageBitmap(decoded);

    }
}
