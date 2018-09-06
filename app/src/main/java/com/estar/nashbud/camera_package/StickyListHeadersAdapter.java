package com.estar.nashbud.camera_package;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public interface StickyListHeadersAdapter extends ListAdapter{
    View getHeaderView(int position, View view, ViewGroup parent);
    long getHeaderId(int position);
}
