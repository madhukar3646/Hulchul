package com.app.hulchul.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.hulchul.R;
import com.app.hulchul.model.VideoModel;
import java.util.ArrayList;

public class SingleVideoAdapter extends RecyclerView.Adapter<SingleVideoPlayerViewHolder>{

    private ArrayList<VideoModel> modelArrayList;
    private Context context;
    private SingleVideoPlayerViewHolder.OnVideoCompletedListener onVideoCompletedListener;

    public SingleVideoAdapter(Context context, ArrayList<VideoModel> modelArrayList)
    {
        this.modelArrayList=modelArrayList;
        this.context=context;
    }

    public void setOnVideoCompletedListener(SingleVideoPlayerViewHolder.OnVideoCompletedListener onVideoCompletedListener)
    {
      this.onVideoCompletedListener=onVideoCompletedListener;
    }

    @Override public SingleVideoPlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singlevideo_playdesign, parent, false);
        return new SingleVideoPlayerViewHolder(view);
    }

    @Override public void onBindViewHolder(final SingleVideoPlayerViewHolder holder, final int position) {
        holder.setOnVideoCompletedListener(onVideoCompletedListener);
        holder.bind(Uri.parse(modelArrayList.get(position).getVideo()) /* FIXME use real data */);
    }

    @Override public int getItemCount() {
        return modelArrayList.size();
    }
}