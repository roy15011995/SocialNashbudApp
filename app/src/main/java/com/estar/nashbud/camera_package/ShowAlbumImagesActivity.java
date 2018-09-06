package com.estar.nashbud.camera_package;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estar.nashbud.R;
import com.estar.nashbud.chatscreenpages.RecyclerItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowAlbumImagesActivity extends AppCompatActivity implements
        ShowAlbumImagesAdapter.ViewHolder.ClickListener {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private ShowAlbumImagesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //    private List<Student> studentList;
    private Button btnSelection;
    private ArrayList<AlbumsModel> albumsModels;
    private ArrayList<AlbumsModel> photoModels = new ArrayList<>();
    private int mPosition;
    //private ActionModeCallback actionModeCallback = new ActionModeCallback();
    //private ActionMode actionMode;
    ImageView imgView1, imgView2, imgView3, imgView4;
    public ArrayList<Uri> mShareImages = new ArrayList<Uri> ();
    LinearLayout layout;
    GridView gridView;
    Bundle extra;
    String GetValue,FolderName;
    //TextView text_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_album_images);
        //setContentView(R.layout.activity_adapter_sample);
        toolbar = (Toolbar) findViewById(R.id.toolbar_all);
        //text_ok=(TextView)findViewById(R.id.text_ok);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));


        //btnSelection = (Button) findViewById(R.id.btnShow);
        //layout = (LinearLayout)findViewById(R.id.imgView);
        /*imgView1 = (ImageView) findViewById(R.id.imgView1);
        imgView2 = (ImageView) findViewById(R.id.imgView2);
        imgView3 = (ImageView) findViewById(R.id.imgView3);
        imgView4 = (ImageView) findViewById(R.id.imgView4);*/
//        studentList = new ArrayList<Student>();
//        for (int i = 1; i <= 15; i++) {
//            Student st = new Student("Student " + i, "androidstudent" + i
//                    + "@gmail.com", false);
//
//            studentList.add(st);
//        }
        extra = getIntent().getExtras();
        if(extra!=null)
        {
            GetValue = extra.getString("IsPROFILE");
            FolderName = extra.getString("folder_name");
            Log.e("GetFolderName ",""+FolderName);
            Log.e("GetValue ",""+GetValue);
        }

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(FolderName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
            backArrow.setColorFilter(getResources().getColor(R.color.black_de), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(backArrow);
        }
        mPosition = (int)getIntent ().getIntExtra ("position",0);
        albumsModels = (ArrayList<AlbumsModel>) getIntent ().getSerializableExtra ("albumsList");
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        // create an Object for Adapter
        mAdapter = new ShowAlbumImagesAdapter (ShowAlbumImagesActivity.this,getAlbumImages (),this);
        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);


        /*text_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "";
                List<AlbumImages> stList = ((ShowAlbumImagesAdapter) mAdapter)
                        .getAlbumImagesList ();
                for (int i = 0; i < stList.size(); i++) {
                    AlbumImages singleStudent = stList.get(i);
                    if (singleStudent.isSelected() == true) {
                        //data = data + "\n" + singleStudent.getFolderName ().toString();
                        data = data + "\n" + singleStudent.getAlbumImages().toString();
                    }
                }
                Toast.makeText(ShowAlbumImagesActivity.this,
                        "Selected Image: \n" + data, Toast.LENGTH_LONG)
                        .show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                intent.setType("image/jpeg");
                intent.setPackage("com.whatsapp");
                intent.putParcelableArrayListExtra (Intent.EXTRA_STREAM,mShareImages);
                startActivity(intent);
                //imgView1.setImageURI(mShareImages.toString());
                for (Uri uri: mShareImages) {
                    String uriString = uri.toString();
                    Uri imgUri=Uri.parse(uriString);
                    Log.e("img uri:", uriString);
                    ImageView image = new ImageView(ShowAlbumImagesActivity.this);
                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams
                            (160, 160));
                    image.setMaxHeight(40);
                    image.setMaxWidth(40);
                    //image.setImageURI(null);
                    image.setImageURI(imgUri);
                    // Adds the view to the layout
                    layout.addView(image);
                    //imgView1.setImageURI(null);
                    //imgView1.setImageURI(imgUri);
                    // do whatever you want with your string
                }
            }
        });*/

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.ok_send,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ok_send:

                String data = "";
                List<AlbumImages> stList = ((ShowAlbumImagesAdapter) mAdapter)
                        .getAlbumImagesList ();
                for (int i = 0; i < stList.size(); i++) {
                    AlbumImages singleStudent = stList.get(i);
                    if (singleStudent.isSelected() == true) {
                        //data = data + "\n" + singleStudent.getFolderName ().toString();
                        data = data + "\n" + singleStudent.getAlbumImages().toString();
                    }
                }
                Toast.makeText(ShowAlbumImagesActivity.this,
                        "Selected Image: \n" + data, Toast.LENGTH_LONG)
                        .show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                intent.setType("image/jpeg");
                intent.setPackage("com.whatsapp");
                intent.putParcelableArrayListExtra (Intent.EXTRA_STREAM,mShareImages);
                startActivity(intent);
                //imgView1.setImageURI(mShareImages.toString());
                for (Uri uri: mShareImages) {
                    String uriString = uri.toString();
                    Uri imgUri=Uri.parse(uriString);
                    Log.e("img uri:", uriString);
                    ImageView image = new ImageView(ShowAlbumImagesActivity.this);
                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams
                            (160, 160));
                    image.setMaxHeight(40);
                    image.setMaxWidth(40);
                    //image.setImageURI(null);
                    image.setImageURI(imgUri);
                    // Adds the view to the layout
                    layout.addView(image);
                    //imgView1.setImageURI(null);
                    //imgView1.setImageURI(imgUri);
                    // do whatever you want with your string
                }

                break;
        }
        return true;
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    //    private ArrayList<Uri> getShareImages() {
//            ArrayList<Uri> uris = mShareImages;
//            for (int i = 0; i < uris.size(); i++) {
//                Uri uri = uris.get(i);
//                String path = uri.getPath();
//                File imageFile = new File(path);
//                uri = getImageContentUri(imageFile);
//                uris.set(i, uri);
//            }
//
//        return uris;
//    }
    private Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return this.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
    private ArrayList<AlbumImages> getAlbumImages() {
        Object[] abc = albumsModels.get(mPosition).folderImages.toArray ();
        Log.i("imagesLength", ""+abc.length);
        ArrayList<AlbumImages> paths = new ArrayList<AlbumImages>();
        int size = abc.length;
        for (int i = 0; i < size; i++) {
            AlbumImages albumImages = new AlbumImages ();
            albumImages.setAlbumImages ((String) abc[i]);
            paths.add (albumImages);
            Collections.reverse(paths);
        }
        return  paths;
    }
    @Override
    public void onItemClicked(int position) {

        Uri uriPath = Uri.parse (mAdapter.getAlbumImagesList ().get (position).getAlbumImages ());
        String path = uriPath.getPath ();
        ArrayList<String>PhotoList = new ArrayList<>();
        PhotoList.add(path);
        AlbumsModel albumsModel = new AlbumsModel();
        albumsModel.setAl_imagepath(PhotoList);


        if(albumsModel.getAl_imagepath()!=null)
        {

            Intent intent = new Intent(getApplicationContext(),ShowImageActivity.class);
            intent.putExtra("image",path);
            intent.putExtra("IsPROFILE",GetValue);
            finish();
            startActivity(intent);

          Log.e("GetImagePath :",""+albumsModel.getAl_imagepath());
        }

        //Toast.makeText(getApplicationContext(),albumsModel.getAl_imagepath().toString(),Toast.LENGTH_SHORT).show();


     /*   if(position>=0){
            toggleSelection(position);
            text_ok.setVisibility(View.VISIBLE);
        }
        else {
            text_ok.setVisibility(View.GONE);
        }*/

    }
    @Override
    public boolean onItemLongClicked (int position) {
       /* if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }*/
       /* if(position>=0){
            toggleSelection(position);
            text_ok.setVisibility(View.VISIBLE);
        }
        else {
            text_ok.setVisibility(View.GONE);
        }*/
        return true;
    }
    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

       /* if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
        Log.i ("image path",mAdapter.getAlbumImagesList ().get (position).getAlbumImages ());
        Utils.copyFileToExternal(ShowAlbumImagesActivity.this,mAdapter.
        getAlbumImagesList ().get (position).getAlbumImages ());

        File file = new File(Environment.getExternalStorageState ()+"/ZappShare" + "/SharedImages" + mAdapter.getAlbumImagesList ().get (position).getAlbumImages ());
        Uri uri1 = Uri.fromFile(file);*/

        Log.i ("string path", ""+mAdapter.getAlbumImagesList ().get (position).getAlbumImages ());
        Uri uriPath = Uri.parse (mAdapter.getAlbumImagesList ().get (position).getAlbumImages ());
        String path = uriPath.getPath ();
        File imageFile = new File(path);
        Uri uri = getImageContentUri(imageFile);
        if(mAdapter.isSelected (position)) {
            mShareImages.add (uri);
        }else {
            mShareImages.remove (uri);
        }
        Log.i ("uri path", ""+mShareImages);
    }

    /*private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:
//                    adapter.removeItems(adapter.getSelectedItems());
                    mode.finish();
                    toolbar.setVisibility (View.VISIBLE);

                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) {
            mAdapter.clearSelection();
//            actionMode = null;
        }
    }*/
}
