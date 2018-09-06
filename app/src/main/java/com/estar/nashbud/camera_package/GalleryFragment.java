package com.estar.nashbud.camera_package;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

//import com.estar.nashbud.Manifest;
import com.estar.nashbud.camera_package.Utils;

import com.estar.nashbud.R;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TimeZone;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment implements GalleryAlbumAdapter.ClickListener{

    private static Uri[] mUrls = null;
    private static String[] strUrls = null;
    private String[] mNames = null;
    private GridView gridview = null;
    private Cursor cc = null;
    private Button btnMoreInfo = null;
    private ProgressDialog myProgressDialog = null;
    View view;
    private RecyclerView mRecyclerView;
    private GalleryAlbumAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AlbumsModel> albumsModels;
    Utils utils;
    Toolbar toolbar;
    ArrayList<PhotoModel> al_images = new ArrayList<>();
    boolean boolean_folder;
    GridView gv_folder;
    private static final int REQUEST_PERMISSIONS = 100;
    Adapter_PhotosFolder obj_adapter;
    ImageView show_dropdown;
    StickyListHeadersListView stickyListHeadersListView;
    StickyGridHeadersGridView stickyGridHeadersGridView;
    ArrayList<String> add_Image = new ArrayList<>();
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;



    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*CommonFunctions.setLanguage(getBaseContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gallery, container, false);


        gridview = (GridView) view.findViewById(R.id.gridview);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PhotoModel photoModel = al_images.get(position);

                if(photoModel.getAl_imagepath().get(0).contains(".mp4")){
                    Intent intent = new Intent(getActivity(),ShowImageActivity.class);
                    intent.putStringArrayListExtra("image",photoModel.getAl_imagepath());
                    getActivity().finish();
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getActivity(),ShowImageActivity.class);
                    intent.putStringArrayListExtra("image",photoModel.getAl_imagepath());
                    getActivity().finish();
                    startActivity(intent);
                }


                //Toast.makeText(getActivity(),"Image :"+photoModel.getAl_imagepath(),Toast.LENGTH_SHORT).show();

                Log.e("Image :",""+photoModel.getAl_imagepath());
                File file = new File(photoModel.getAl_imagepath().toString());
                Log.e("FilePath :",""+file);

            }
        });
        //stickyGridHeadersGridView = (StickyGridHeadersGridView) view.findViewById(R.id.gridview_sticky);

        //show_dropdown = (ImageView)view.findViewById()
        //stickyListHeadersListView = (StickyListHeadersListView) view.findViewById(R.id.list);




       /* if ((ContextCompat.checkSelfPermission(getActivity(),
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(),
                READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        }else {
            Log.e("Else","Else");
            fn_imagespath();
        }*/


        //mRecyclerView = (RecyclerView)view.findViewById(R.id.photo_recycler);


       /* mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(new GridLayoutManager (getActivity(),3));
        // create an Object for Adapter
        mAdapter = new GalleryAlbumAdapter (getActivity(),getGalleryAlbumImages ());
        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);

            //gridview.setAdapter(new ImageAdapter(getActivity()));
            *//*recyclerView.setAdapter(new MyRecyclerViewAdapter(getActivity()));
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));*//*

           ///////////////////////////////////////////////////////////////////////////////////////////////////

            mAdapter.SetOnItemClickListener(new GalleryAlbumAdapter.OnItemClickListener ()
        {
            @Override
            public void onItemClick (View v, int position) {
                // do something with position
                Intent galleryAlbumsIntent = new Intent(getActivity(),ShowAlbumImagesActivity.class);
                galleryAlbumsIntent.putExtra ("position",position);
                galleryAlbumsIntent.putExtra ("albumsList", getGalleryAlbumImages());
                startActivity (galleryAlbumsIntent);
            }
        });
*/

       /* gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        currentPage++;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    // I load the next page of gigs using a background task,
                    // but you can call any function here.
                    loading = true;
                }
            }
        });*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                fn_imagespath();
            }
        },100);


        return view;
    }

    private ArrayList<AlbumsModel> getGalleryAlbumImages() {
        final String[] columns = { MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = getActivity().managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                null, null, orderBy + " DESC");
        utils = new Utils();
        albumsModels = utils.getAllDirectoriesWithImages(imagecursor);

        return albumsModels;
    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }


    /*private ArrayList<String> getAllGalleryImages(){


        return null;
    }*/

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

        cursor = getActivity().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        cursor1 = getActivity().getContentResolver().query(uri1, parameters, null, null, orderBy1 + " DESC");


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

        // it will update for next version....../////

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

        }*/

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        for (int i = 0; i < al_images.size(); i++) {
            Log.e("al_images PIC","" + al_images.size());
            Log.e("FOLDER PIC", al_images.get(i).getStr_folder());
            for (int j = 0; j < al_images.get(i).getAl_imagepath().size(); j++) {
                Log.e("FILE", al_images.get(i).getAl_imagepath().get(j));
            }

            obj_adapter = new Adapter_PhotosFolder(getActivity(),al_images,gridview);

           /* //To GridView item HeaderId data generation
            ArrayList<PhotoModel> hasHeaderIdList = generateHeaderId(al_images);
            //Sort
            Collections.sort(hasHeaderIdList, new YMDComparator());*/
            gridview.setAdapter(obj_adapter);
            gridview.setNumColumns(3);
            setDynamicHeight(gridview);


            /*stickyGridHeadersGridView.setAdapter(obj_adapter);
            stickyGridHeadersGridView.setNumColumns(3);
            stickyGridHeadersGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);*/

           /* stickyListHeadersListView.setAdapter(obj_adapter);
            stickyListHeadersListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);*/




           //////////////////////// Multi Selection Part will come for the next Version............/////////////////////////


            //gridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);

            /*gridview.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
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

                          *//*  add_Image.clear();

                            for (int i = (selected.size() - 1); i >= 0; i--) {
                                if (selected.valueAt(i)) {
                                    PhotoModel selecteditem1 = obj_adapter
                                            .getItem(selected.keyAt(0));


                                    Log.e("SelectedItem :",""+selecteditem1.getAl_imagepath());

                                    //add_Image.add(selecteditem.getAl_imagepath().get(0));
                                    //add_Image.add(selecteditem.getAl_imagepath().get(0));


                                    for(int i1=0;i1<selecteditem.getAl_imagepath().size();i1++){

                                       add_Image.addAll(i1,selecteditem1.getAl_imagepath());


                                    }


                                }
                            }*//*

                            //Intent intent = new Intent(getActivity(),ShowImageActivity.class); <----ShowImageActivity part will come for next Version--->
                            Intent intent = new Intent(getActivity(),ShowImageActivity.class);
                            intent.putStringArrayListExtra("image",add_Image);
                            getActivity().finish();
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



   /* private ArrayList<PhotoModel> generateHeaderId(ArrayList<PhotoModel> nonHeaderIdList) {
        Map<String, Integer> mHeaderIdMap = new HashMap<String, Integer>();
        int mHeaderId = 1;
        ArrayList<PhotoModel> hasHeaderIdList;

        for(ListIterator<PhotoModel> it = nonHeaderIdList.listIterator(); it.hasNext();){
            PhotoModel mGridItem = it.next();
            String ymd = mGridItem.getTime();

            //Log.e("GetTime",""+ymd);
            if(!mHeaderIdMap.containsKey(ymd)){
                mGridItem.setHeaderId(mHeaderId);
                mHeaderIdMap.put(ymd, mHeaderId);
                mHeaderId ++;
            }else{
                mGridItem.setHeaderId(mHeaderIdMap.get(ymd));
            }
        }
        hasHeaderIdList = nonHeaderIdList;

        return hasHeaderIdList;
    }
    public static String paserTimeToYMD(long time, String pattern ) {
        System.setProperty("user.timezone", "IST");
        TimeZone tz = TimeZone.getTimeZone("IST");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(time * 1000L));
    }

    public class YMDComparator implements Comparator<PhotoModel> {

        @Override
        public int compare(PhotoModel o1, PhotoModel o2) {
            return o2.getTime().compareTo(o1.getTime());
        }

    }*/




   /* @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        fn_imagespath();
                    } else {
                        Toast.makeText(getActivity(), "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }*/

private ArrayList<PhotoModel> getData(){

    ArrayList<PhotoModel> getPhotoData = new ArrayList<>();

    File downloadFolder = new File(Environment.getExternalStorageDirectory()+"/Files");

    if(downloadFolder.exists())
    {
        File[] files=downloadFolder.listFiles();

        for(int i=0;i<files.length;i++){

            File file = files[i];

            PhotoModel photoModel=new PhotoModel();

            photoModel.setUri(Uri.fromFile(file));

            getPhotoData.add(photoModel);
        }

    }
    return getPhotoData;
}

    private void setDynamicHeight(GridView gridView) {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = gridViewAdapter.getCount();
        int rows = 0;

        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > 3){
            x = items/3;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }




}
