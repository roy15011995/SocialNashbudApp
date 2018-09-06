package com.estar.nashbud.thread;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.estar.nashbud.R;
import com.estar.nashbud.camera_package.Adapter_PhotosFolder;
import com.estar.nashbud.camera_package.PhotoModel;
import com.estar.nashbud.camera_package.ShowImageActivity;

import java.io.File;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class OpenGallery extends AppCompatActivity {

    Toolbar toolbar;
    private GridView gridview = null;
    Adapter_PhotosFolder obj_adapter;
    ArrayList<String> add_Image = new ArrayList<>();
    ArrayList<PhotoModel> al_images = new ArrayList<>();
    boolean boolean_folder;
    private static final int REQUEST_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_open_gallery);

        toolbar = (Toolbar) findViewById(R.id.toolbar_all);
        gridview = (GridView) findViewById(R.id.gridview);


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


        if ((ContextCompat.checkSelfPermission(OpenGallery.this,
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(OpenGallery.this,
                READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(OpenGallery.this,
                    WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(OpenGallery.this,
                    READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(OpenGallery.this,
                        new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        }else {
            Log.e("Else","Else");
            fn_imagespath();
        }


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                PhotoModel photoModel = al_images.get(position);

                if(photoModel.getAl_imagepath().get(0).contains(".mp4")){
                    Intent intent = new Intent(OpenGallery.this,ThreadActivity.class);
                    intent.putStringArrayListExtra("image",photoModel.getAl_imagepath());
                    finish();
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(OpenGallery.this,ShowImageActivity.class);
                    intent.putStringArrayListExtra("image",photoModel.getAl_imagepath());
                    finish();
                    startActivity(intent);
                }


                //Toast.makeText(getActivity(),"Image :"+photoModel.getAl_imagepath(),Toast.LENGTH_SHORT).show();

                Log.e("Image :",""+photoModel.getAl_imagepath());
                File file = new File(photoModel.getAl_imagepath().toString());
                Log.e("FilePath :",""+file);





            }
        });

    }


    public ArrayList<PhotoModel> fn_imagespath() {
        al_images.clear();

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

        cursor = getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        cursor1 = getContentResolver().query(uri1, parameters, null, null, orderBy1 + " DESC");


        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_data1 = cursor1.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        //////////// For Image Fetching from SD Card.................../////////////////////////////////

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


        //////////// For Video Fetching from SD Card.................../////////////////////////////////

        /// this part will come for the next version of Nashbud App................///////////

        /*column_index_folder_name1 = cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        while (cursor1.moveToNext()) {
            absolutePathOfVideo = cursor1.getString(column_index_data1);

           *//* long times = cursor1.getLong(cursor1
                    .getColumnIndex(MediaStore.Images.Media.DATE_ADDED));*//*


            Log.e("Absolute_Path_video",""+absolutePathOfVideo);
            Log.e("Column_Video", absolutePathOfVideo);
            Log.e("Folder_video", cursor1.getString(column_index_folder_name1));
            Log.e("DateFormat_video", " "+cursor1.getLong(cursor1
                    .getColumnIndex(MediaStore.Video.Media.DATE_ADDED)));

            for (int i = 0; i < al_images.size(); i++) {
                if (al_images.get(i).getAl_imagepath().equals(cursor1.getString(column_index_folder_name1))) {

                    Log.e("AllVedioPath",""+al_images.get(i).getAl_imagepath().equals(cursor1.getString(column_index_folder_name1)));
                    boolean_folder = true;
                    int_position = i;
                    break;
                } else {
                    boolean_folder = false;
                }

            }


            if (boolean_folder) {

                ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(al_images.get(int_position).getAl_imagepath());
                al_path.add(absolutePathOfVideo);
                al_images.get(int_position).setAl_imagepath(al_path);

            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfVideo);
                PhotoModel obj_model = new PhotoModel();
                obj_model.setStr_folder(cursor1.getString(column_index_folder_name1));
                obj_model.setAl_imagepath(al_path);
                //obj_model.setTime(paserTimeToYMD(times,"yyyy-MM-dd"));
                al_images.add(obj_model);
            }

        }
*/
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        for (int i = 0; i < al_images.size(); i++) {
            Log.e("al_images PIC","" + al_images.size());
            Log.e("FOLDER PIC", al_images.get(i).getStr_folder());
            for (int j = 0; j < al_images.get(i).getAl_imagepath().size(); j++) {
                Log.e("FILE", al_images.get(i).getAl_imagepath().get(j));


            }
            obj_adapter = new Adapter_PhotosFolder(OpenGallery.this,al_images,gridview);

           /* //To GridView item HeaderId data generation
            ArrayList<PhotoModel> hasHeaderIdList = generateHeaderId(al_images);
            //Sort
            Collections.sort(hasHeaderIdList, new YMDComparator());*/
            gridview.setAdapter(obj_adapter);
            gridview.setNumColumns(3);

            /*stickyGridHeadersGridView.setAdapter(obj_adapter);
            stickyGridHeadersGridView.setNumColumns(3);
            stickyGridHeadersGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);*/

           /* stickyListHeadersListView.setAdapter(obj_adapter);
            stickyListHeadersListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);*/



            //////////////////////// Multi Selection Part will come for the next Version............/////////////////////////



            /*gridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);

            gridview.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    // Capture total checked items
                    final int checkedCount = gridview.getCheckedItemCount();
                    // Set the CAB title according to total checked items
                    mode.setTitle(checkedCount + " Selected");
                    // Calls toggleSelection method from ListViewAdapter Class
                    obj_adapter.toggleSelection(position);
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.ok_send, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.ok_send:
                            // Calls getSelectedIds method from ListViewAdapter Class
                            SparseBooleanArray selected = obj_adapter
                                    .getSelectedIds();
                            // Captures all selected ids with a loop


                            PhotoModel selecteditem = obj_adapter
                                    .getItem(selected.keyAt(0));


                            Log.e("SelectedItem :",""+selecteditem.getAl_imagepath());

                            add_Image.add(selecteditem.getAl_imagepath().get(0));

                            /// This for loop part i will do for the next Version of nashbud app.......///////////

                           *//* add_Image.clear();
                            for (int i = (selected.size() - 1); i >= 0; i--) {
                                if (selected.valueAt(i)) {

                                    PhotoModel selecteditem = obj_adapter
                                            .getItem(selected.keyAt(i));



                                    Log.e("SelectedItem :",""+selecteditem.getAl_imagepath());


                                    for(int i1=0;i1<selecteditem.getAl_imagepath().size();i1++){

                                        add_Image.addAll(i1,selecteditem.getAl_imagepath());


                                    }

                                }
                            }*//*
                            Intent intent = new Intent(OpenGallery.this,ShowImageActivity.class);
                            intent.putStringArrayListExtra("image",add_Image);
                            finish();
                            startActivity(intent);

                            Log.e("SelectedItemAdded :",""+add_Image);
                            // Close CAB
                            mode.finish();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    // TODO Auto-generated method stub
                    obj_adapter.removeSelection();
                }
            });*/

        }


        return al_images;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        fn_imagespath();
                    } else {
                        Toast.makeText(OpenGallery.this, "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
