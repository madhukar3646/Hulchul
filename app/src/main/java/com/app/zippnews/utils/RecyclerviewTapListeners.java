package com.app.zippnews.utils;

import com.app.zippnews.adapters.SimplePlayerViewHolder;

public interface RecyclerviewTapListeners {

    void onDoubletap(SimplePlayerViewHolder holder, int position);
    void onLongClick(SimplePlayerViewHolder holder, int position);
    void onClick(SimplePlayerViewHolder holder, int position);
}
