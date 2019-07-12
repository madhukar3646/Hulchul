package com.app.zippnews.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.zippnews.R;
import com.app.zippnews.model.SoundsCategorylist;
import java.util.ArrayList;
/**
 * Created by admin on 4/20/2017.
 */

public class PlaylistCategoriesAdapter extends RecyclerView.Adapter<PlaylistCategoriesAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;
    private ArrayList<SoundsCategorylist> soundsCategorylist;
    private OnPlaylistItemClickListener onPlaylistItemClickListener;

    public PlaylistCategoriesAdapter(Context context,ArrayList<SoundsCategorylist> soundsCategorylist)
    {
        this.soundsCategorylist=soundsCategorylist;
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
    }

    public void setOnPlaylistItemClickListener(OnPlaylistItemClickListener onPlaylistItemClickListener)
    {
        this.onPlaylistItemClickListener=onPlaylistItemClickListener;
    }

    @Override
    public PlaylistCategoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlistcategoriesmodel, parent, false);

        return new PlaylistCategoriesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlaylistCategoriesAdapter.MyViewHolder holder, final int position)
    {
        final PlaylistCategoriesAdapter.MyViewHolder myViewHolder=holder;
        SoundsCategorylist model=soundsCategorylist.get(position);
        holder.tv_category.setText(model.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(onPlaylistItemClickListener!=null)
                  onPlaylistItemClickListener.onPlayListitemclick(soundsCategorylist.get(position));
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return soundsCategorylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_category;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.getLayoutParams().width=width/3;
            tv_category=(TextView) itemView.findViewById(R.id.tv_category);
        }
    }

    public interface OnPlaylistItemClickListener
    {
        void onPlayListitemclick(SoundsCategorylist playlistitem);
    }
}
