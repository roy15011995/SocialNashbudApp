package com.estar.nashbud.camera_package;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.estar.nashbud.R;

import java.util.ArrayList;

public class ShowAlbumImagesAdapter extends SelectableAdapter<ShowAlbumImagesAdapter.ViewHolder> {
    public Context mContext;
    private ArrayList<AlbumImages> mAlbumImages;
    public boolean mShowCheckBox;
    private ViewHolder.ClickListener clickListener;
    static OnItemClickListener mItemClickListener;
    //    public ArrayList<Uri> mShareImages = new ArrayList<Uri> ();
    public ShowAlbumImagesAdapter (Context context, ArrayList<AlbumImages>
            galleryImagesList,ViewHolder.ClickListener clickListener) {
        this.mAlbumImages = galleryImagesList;
        this.mContext = context;
        this.clickListener = clickListener;
    }
    // Create new views
    @Override
    public ShowAlbumImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_show_gallery_images, null);
        /*View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_list_item, null);*/
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView,clickListener);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final int pos = position;
        Log.e("FilePathFind",""+mAlbumImages.get(position).getAlbumImages());
        Glide.with (mContext)
                .load("file://"+mAlbumImages.get (position).getAlbumImages ())
                .centerCrop()
                .placeholder(R.drawable.gallery)
                .crossFade()
                .into (viewHolder.imgAlbum);
        viewHolder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }
    // Return the size arraylist
    @Override
    public int getItemCount() {
        return mAlbumImages.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,View.OnLongClickListener {
        private final CardView selectedOverlay;
        public ImageView imgAlbum;
        public AlbumsModel singleItem;
        private ClickListener listener;
        public ViewHolder(View itemLayoutView,ClickListener listener) {
            super(itemLayoutView);
            this.listener = listener;
            imgAlbum = (ImageView) itemLayoutView.findViewById(R.id.img_album);
            selectedOverlay = (CardView) itemView.findViewById(R.id.selected_overlay);
            itemLayoutView.setOnClickListener(this);
            itemLayoutView.setOnLongClickListener (this);
        }
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition ());
            }
        }
        @Override
        public boolean onLongClick (View view) {
            if (listener != null) {
                return listener.onItemLongClicked(getAdapterPosition ());
            }
            return false;
        }
        public interface ClickListener {
            public void onItemClicked(int position);
            public boolean onItemLongClicked(int position);
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }
    public void  SetOnItemClickListener(final ShowAlbumImagesAdapter.OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }
    // method to access in activity after updating selection
    public ArrayList<AlbumImages> getAlbumImagesList() {
        return mAlbumImages;
    }
//    public ArrayList<Uri> getShareImageList() {
//        return mShareImages;
//    }
}
