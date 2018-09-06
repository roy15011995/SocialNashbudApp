package com.estar.nashbud.settings;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.estar.nashbud.R;
import com.estar.nashbud.camera_package.Adapter_PhotosFolder;
import com.estar.nashbud.camera_package.AlbumsModel;
import com.estar.nashbud.camera_package.GalleryAlbumAdapter;
import com.estar.nashbud.camera_package.PhotoModel;
import com.estar.nashbud.camera_package.ShowImageActivity;
import com.estar.nashbud.camera_package.Utils;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.io.File;
import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class GalleryActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 100;
    Toolbar toolbar;
    boolean boolean_folder;
    ActionBar actionBar;
    GridView gridview;
    ArrayList<PhotoModel> al_images = new ArrayList<>();
    Adapter_PhotosFolder obj_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        toolbar = (Toolbar)findViewById(R.id.toolbar_all);
        actionBar=getSupportActionBar();

        if(toolbar!=null){
            setSupportActionBar(toolbar);
        }

        toolbar.setTitle("Gallery");
        Drawable close = getResources().getDrawable(R.drawable.ic_close_black_24dp);
        close.setColorFilter(getResources().getColor(R.color.black_de), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(close);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        gridview = (GridView) findViewById(R.id.gridview_gallery);

        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(GalleryActivity.this,
                    WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(GalleryActivity.this,
                    READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(GalleryActivity.this,
                        new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        }else {

           new Fnimagespath().execute();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
       // finish();
        return true;
    }



    class  Fnimagespath extends AsyncTask<Void, Void,ArrayList<PhotoModel>>{


        @Override
        protected ArrayList<PhotoModel> doInBackground(Void... v) {
            int int_position = 0;
            Uri uri,uri1;
            Cursor cursor,cursor1;
            int column_index_data, column_index_folder_name,column_index_date,column_index_folder_name1,column_index_data1;

            String absolutePathOfImage = null;
            String absolutePathOfVideo = null;
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri1 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

            String date_of_photo = null;

            String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
            String jpg_mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg");
            String png_mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("png");
            String mp4_mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp4");
            String[] selectionArgs = new String[]{jpg_mimeType, png_mimeType, mp4_mimeType};


            String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.DATE_ADDED};

            String[] projection1 = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media.DATE_ADDED};

            String[] parameters = { MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION,
                    MediaStore.Video.Thumbnails.DATA,MediaStore.Video.Media.DATE_ADDED
            };

            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            final String orderBy1 = MediaStore.Video.Media.DATE_TAKEN;

            cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
            cursor1 = getApplicationContext().getContentResolver().query(uri1, parameters, null, null, orderBy1 + " DESC");


            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_data1 = cursor1.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            //////////// For Image Fetching from SD Card...................///////////////////////////

            //column_index_date = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
            column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);

                long times = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATE_ADDED));


                Log.e("Absolute_Path",""+absolutePathOfImage);
                Log.e("Column", absolutePathOfImage);
                Log.e("Folder", cursor.getString(column_index_folder_name));
                Log.e("DateFormat", " "+cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATE_ADDED)));


                for (int i = 0; i < al_images.size(); i++) {
                    if (al_images.get(i).getAl_imagepath().equals(cursor.getString(column_index_folder_name))) {

                        Log.e("AllPhotoPath",""+al_images.get(i).getAl_imagepath().equals(cursor.getString(column_index_folder_name)));
                        boolean_folder = true;
                        int_position = i;
                        break;
                    }

                    else {
                        boolean_folder = false;
                    }

                }

                if (boolean_folder) {

                    ArrayList<String> al_path = new ArrayList<>();
                    al_path.addAll(al_images.get(int_position).getAl_imagepath());
                    al_path.add(absolutePathOfImage);
                    al_images.get(int_position).setAl_imagepath(al_path);

                } else {
                    ArrayList<String> al_path = new ArrayList<>();
                    al_path.add(absolutePathOfImage);
                    PhotoModel obj_model = new PhotoModel();
                    obj_model.setStr_folder(cursor.getString(column_index_folder_name));
                    obj_model.setAl_imagepath(al_path);
                    //obj_model.setTime(paserTimeToYMD(times,"yyyy-MM-dd"));
                    al_images.add(obj_model);
                }

            }

            return al_images;
        }

        @Override
        protected void onPostExecute(ArrayList<PhotoModel> photoModels) {
            super.onPostExecute(photoModels);
            obj_adapter = new Adapter_PhotosFolder(getApplicationContext(),photoModels,gridview);

           /* //To GridView item HeaderId data generation
            ArrayList<PhotoModel> hasHeaderIdList = generateHeaderId(al_images);
            //Sort
            Collections.sort(hasHeaderIdList, new YMDComparator());*/
            gridview.setAdapter(obj_adapter);

        }
    }






}
