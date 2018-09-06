package com.estar.nashbud.camera_package;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.estar.nashbud.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomGalleryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private GalleryAlbumAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AlbumsModel> albumsModels;
    private View view;
    String val;

    public CustomGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_custom_gallery, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        // create an Object for Adapter
        mAdapter = new GalleryAlbumAdapter (getActivity(),getGalleryAlbumImages ());
        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new GalleryAlbumAdapter.OnItemClickListener ()
        {
            @Override
            public void onItemClick (View v, int position) {
                // do something with position
                Intent galleryAlbumsIntent = new Intent(getActivity()
                        ,ShowAlbumImagesActivity.class);
                galleryAlbumsIntent.putExtra ("position",position);
                galleryAlbumsIntent.putExtra ("albumsList", getGalleryAlbumImages());
                galleryAlbumsIntent.putExtra("IsPROFILE",val);
                galleryAlbumsIntent.putExtra("folder_name",mAdapter.getGalleryImagesList().get(position).getFolderName());
                startActivity (galleryAlbumsIntent);
                getActivity().finish();
            }
        });

        return view;

    }

    private ArrayList<AlbumsModel> getGalleryAlbumImages() {
        final String[] columns = { MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID ,MediaStore.Images.Media.DATE_ADDED};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                null, null, orderBy + " DESC");
        albumsModels = Utils.getAllDirectoriesWithImages(imagecursor);
        return albumsModels;
    }

    public void GetValueIntent(String Value)
    {
        val = new String();
        val=Value;
        Log.e("GetValueGallery ",""+val);
    }

}
