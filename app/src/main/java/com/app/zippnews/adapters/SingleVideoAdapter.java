package com.app.zippnews.adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.zippnews.R;
import com.app.zippnews.model.VideoModel;
import com.app.zippnews.utils.ApiUrls;
import com.squareup.picasso.Picasso;

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
        Picasso.with(context).load(ApiUrls.VIDEOTHUMBNAIL+getThumbnailpath(modelArrayList.get(position).getVideo())).error(R.mipmap.app_icon).placeholder(R.mipmap.app_icon).into(holder.iv_thumb);
    }

    @Override public int getItemCount() {
        return modelArrayList.size();
    }

    private String getThumbnailpath(String videopath)
    {
        String jpeg=null;
        if(videopath!=null && videopath.trim().length()>4)
        {
            jpeg=videopath.substring(0,videopath.length()-4);
            jpeg=jpeg+"_image.jpeg";
        }
        return jpeg;
    }
}