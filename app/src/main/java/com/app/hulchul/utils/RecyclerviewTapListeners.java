package com.app.hulchul.utils;

import com.app.hulchul.adapters.SimplePlayerViewHolder;

public interface RecyclerviewTapListeners {

    void onDoubletap(SimplePlayerViewHolder holder, int position);
    void onLongClick(SimplePlayerViewHolder holder, int position);
    void onClick(SimplePlayerViewHolder holder, int position);
}
