package com.estar.nashbud.thread;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.estar.nashbud.R;
import com.estar.nashbud.camera_package.CropView;
import com.estar.nashbud.camera_package.ImageModel;
import com.estar.nashbud.camera_package.Photo_GridAdapter;
import com.estar.nashbud.camera_package.Photo_GridAdapter1;
import com.estar.nashbud.camera_package.ShowImageActivity;

import java.util.ArrayList;

public class ShowImageChat extends AppCompatActivity {

    ArrayList<ImageModel> photoModels = new ArrayList<>();
    GridView gridView, gridView1;
    ImageView imageView;
    public static String[] arrPath;
    Toolbar toolbar;
    ImageModel imageModel;
    CropView cropView;
    Bundle bundle;
    String place, Address;
    String[] mStringArray;
    private int REQUEST_POST = 0, REQUEST_CHAT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_chat);


        Intent i = getIntent();
        ArrayList<String> Image = i.getStringArrayListExtra("image");
        toolbar = (Toolbar) findViewById(R.id.toolbar_all);
        bundle = getIntent().getExtras();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("PREF_KEY_LOCATION", Context.MODE_PRIVATE);
        Address = sharedPreferences.getString("Address", "");
        place = sharedPreferences.getString("Place", "");


        Log.e("PlaceShow", place);
        Log.e("AddressShow", Address);


        imageModel = new ImageModel(Image);
        //Image = getIntent().getStringArrayListExtra("image");

     /*   for(int y=0;y<Image.size();y++){
            //System.out.println(ShowImageActivity.arrPath[Integer.parseInt(Image.get(y))].toString());
            Log.e("ImageShowActivity :",""+Image);
        }*/
        Log.e("ImageShowActivity :", "" + Image);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        toolbar.setTitle(" ");
        Drawable close = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        close.setColorFilter(getResources().getColor(R.color.black_de), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(close);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //imageView = (ImageView)findViewById(R.id.image_show_gallery_show);
        cropView = new CropView(getApplicationContext());

        gridView = (GridView) findViewById(R.id.grid_view_photo_gallery);
        gridView1 = (GridView) findViewById(R.id.grid_view_photo_gallery1);

        Log.e("Get Image", "" + new ImageModel(Image).getAl_imagepath());

        mStringArray = new String[Image.size()];
        mStringArray = Image.toArray(mStringArray);

        for (int i1 = 0; i1 < mStringArray.length; i1++) {
            Log.e("Image_Size", "" + Image.size());

            ImageModel imageModel = new ImageModel();
            imageModel.setImagePath(mStringArray[i1]);
            photoModels.add(imageModel);

            //photoModels.add(i1,new ImageModel(Image));


            Log.e("GetBreakImage :", "" + new ImageModel(Image).getAl_imagepath().get(i1));
        }


        Photo_GridAdapter photo_gridAdapter = new Photo_GridAdapter(ShowImageChat.this, photoModels);
        Photo_GridAdapter1 photo_gridAdapter1 = new Photo_GridAdapter1(ShowImageChat.this, photoModels);

        int size_tag = photoModels.size();
        int totalWidth_tag = (360 * size_tag) * 2;

        LinearLayout.LayoutParams params_tag = new LinearLayout.LayoutParams(
                totalWidth_tag, LinearLayout.LayoutParams.WRAP_CONTENT);

        gridView.setLayoutParams(params_tag);
        //gridView.setStretchMode(GridView.STRETCH_SPACING);
        gridView.setNumColumns(size_tag);
        gridView.setAdapter(photo_gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ImageUrl :", "" + imageModel.getAl_imagepath().get(position));

                Uri imageUri = Uri.parse(imageModel.getAl_imagepath().get(position));

                Log.e("ImageUriThrougUri", "" + imageUri);
                cropView.setImageURI(imageUri);

            }
        });

        int size_tag1 = photoModels.size();
        int totalWidth_tag1 = (145 * size_tag1) * 2;

        LinearLayout.LayoutParams params_tag1 = new LinearLayout.LayoutParams(
                totalWidth_tag1, LinearLayout.LayoutParams.WRAP_CONTENT);

        gridView1.setLayoutParams(params_tag1);
        //gridView1.setStretchMode(GridView.STRETCH_SPACING);
        gridView1.setNumColumns(size_tag);
        gridView1.setAdapter(photo_gridAdapter1);


    }


    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(

            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.next_photo_chat, menu);
        /*SpannableString s = new SpannableString("My red MenuItem");
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        item.setTitle(s);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.next_photo_chat:
                Intent intent = new Intent("com.estar.nashbud.USER_ACTION_CHAT");
                intent.putStringArrayListExtra("GetPhoto", imageModel.getAl_imagepath());
                finish();
                sendBroadcast(intent);


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_POST && resultCode == RESULT_OK) {

                String requiredValue = data.getStringExtra("GetPhoto");
            }
        } catch (Exception ex) {
            Toast.makeText(ShowImageChat.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }


    }
}
