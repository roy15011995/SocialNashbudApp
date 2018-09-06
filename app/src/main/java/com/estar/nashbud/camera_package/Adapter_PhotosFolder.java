package com.estar.nashbud.camera_package;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.estar.nashbud.R;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.ArrayList;
import java.util.List;

public class Adapter_PhotosFolder extends ArrayAdapter<PhotoModel> implements StickyGridHeadersSimpleAdapter{

    Context context;
    ViewHolder viewHolder;
    ArrayList<PhotoModel> al_menu = new ArrayList<>();
    private SparseBooleanArray mSelectedItemsIds;
    LayoutInflater layoutInflater;
    private List<PhotoModel> hasHeaderIdList;
    private GridView mGridView;

    public Adapter_PhotosFolder(Context context, ArrayList<PhotoModel> al_menu,GridView mGridView) {
        super(context, R.layout.galchild, al_menu);
        mSelectedItemsIds = new SparseBooleanArray();
        this.al_menu = al_menu;
        this.context = context;
        this.mGridView = mGridView;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {

        Log.e("ADAPTER LIST SIZE", al_menu.size() + "");
        return al_menu.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_menu.size() > 0) {
            return al_menu.size();
        } else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void remove(@Nullable PhotoModel object) {
        al_menu.remove(object);
        notifyDataSetChanged();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Bitmap bitmap;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.galchild, parent, false);
            /*viewHolder.tv_foldern = (TextView) convertView.findViewById(R.id.tv_folder);
            viewHolder.tv_foldersize = (TextView) convertView.findViewById(R.id.tv_folder2);*/
            viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.ImageView01);
            viewHolder.cardView = (CardView) convertView.findViewById(R.id.selected_overlay_grid);
            viewHolder.video = (ImageView) convertView.findViewById(R.id.video_symbol);
            viewHolder.gif = (ImageView) convertView.findViewById(R.id.gif_symbol);
            viewHolder.jpg = (ImageView) convertView.findViewById(R.id.jpg_symbol);

           /* if(al_menu.get(position).getAl_imagepath().get(0).contains(".jpg")){
                bitmap = BitmapFactory.decodeFile(al_menu.get(position).getAl_imagepath().get(0));
            }
            else if(al_menu.get(position).getAl_imagepath().get(0).contains(".mp4")){
                bitmap = ThumbnailUtils.createVideoThumbnail(al_menu.get(position).getAl_imagepath().get(0),0);
                viewHolder.video.setVisibility(View.VISIBLE);
            }*/

            if(al_menu.get(position).getAl_imagepath().get(0).contains(".mp4")){
                //bitmap = ThumbnailUtils.createVideoThumbnail(al_menu.get(position).getAl_imagepath().get(0),0);
                viewHolder.video.setVisibility(View.VISIBLE);
            }
            else  if(al_menu.get(position).getAl_imagepath().get(0).contains(".gif")){
                //bitmap = ThumbnailUtils.createVideoThumbnail(al_menu.get(position).getAl_imagepath().get(0),0);
                viewHolder.gif.setVisibility(View.VISIBLE);
            }
            else  if(al_menu.get(position).getAl_imagepath().get(0).contains(".jpg")){
                //bitmap = ThumbnailUtils.createVideoThumbnail(al_menu.get(position).getAl_imagepath().get(0),0);
                viewHolder.jpg.setVisibility(View.VISIBLE);
            }
            else  if(al_menu.get(position).getAl_imagepath().get(0).contains(".jpge")){
                //bitmap = ThumbnailUtils.createVideoThumbnail(al_menu.get(position).getAl_imagepath().get(0),0);
                viewHolder.jpg.setVisibility(View.VISIBLE);
            }
            else  if(al_menu.get(position).getAl_imagepath().get(0).contains(".JPG")){
                //bitmap = ThumbnailUtils.createVideoThumbnail(al_menu.get(position).getAl_imagepath().get(0),0);
                viewHolder.jpg.setVisibility(View.VISIBLE);
            }
            else  if(al_menu.get(position).getAl_imagepath().get(0).contains(".png")){
                //bitmap = ThumbnailUtils.createVideoThumbnail(al_menu.get(position).getAl_imagepath().get(0),0);
                viewHolder.jpg.setVisibility(View.VISIBLE);
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

   /*     viewHolder.tv_foldern.setText(al_menu.get(position).getStr_folder());
        viewHolder.tv_foldersize.setText(al_menu.get(position).getAl_imagepath().size()+"");*/



        Glide.with(context)
                .load("file://" + al_menu.get(position).getAl_imagepath().get(0))
                .crossFade()
                .centerCrop()
                .into(viewHolder.iv_image);


        viewHolder.cardView.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);


        return convertView;

    }

    @Override
    public long getHeaderId(int position) {
        return al_menu.get(position).getHeaderId();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        HeaderViewHolder mHeaderHolder;

        if (convertView == null) {
            mHeaderHolder = new HeaderViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_header, parent, false);
            mHeaderHolder.tvHeader = (TextView) convertView
                    .findViewById(R.id.header_name);
            convertView.setTag(mHeaderHolder);
        } else {
            mHeaderHolder = (HeaderViewHolder) convertView.getTag();
        }
        mHeaderHolder.tvHeader.setText(al_menu.get(position).getTime());
        Log.e("GetTime",""+al_menu.get(position).getTime());



        return convertView;
    }


    /*@Override
    public View getHeaderView(int position, View view, ViewGroup parent) {
        HeaderViewHolder holder;
        if (view != null)
            holder = (HeaderViewHolder) view.getTag();
        else {
            view = layoutInflater.inflate(R.layout.item_header, parent, false);
            holder = new HeaderViewHolder(view);
            view.setTag(holder);
        }
        holder.decorate(al_menu.get(position));
        return view;
    }

    @Override
    public long getHeaderId(int position) {
        return al_menu.get(position).getStr_folder().charAt(0);
    }*/


    private static class ViewHolder {
        TextView tv_foldern, tv_foldersize;
        ImageView iv_image,video,gif,jpg;
        CardView cardView;


    }


    public static class HeaderViewHolder {

        TextView tvHeader;

        /*public HeaderViewHolder(View view) {
            tvHeader = (TextView) view.findViewById(R.id.header_name);
        }

        public void decorate(PhotoModel model) {
            if (model.getStr_folder() == null || model.getStr_folder().isEmpty()) return;
            tvHeader.setText(model.getStr_folder().toUpperCase().charAt(0));
        }*/
    }

    public List<PhotoModel> getWorldPopulation() {
        return al_menu;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(mSelectedItemsIds.size());
        for (int i = 0; i < mSelectedItemsIds.size(); ++i) {
            items.add(mSelectedItemsIds.keyAt(i));
        }
        return items;
    }
    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

}
