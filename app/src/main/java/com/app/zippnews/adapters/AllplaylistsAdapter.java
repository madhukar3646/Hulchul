package com.app.zippnews.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.model.PlaylistModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Manish on 10/3/2017.
 */

public class AllplaylistsAdapter extends RecyclerView.Adapter<AllplaylistsAdapter.ViewHolder>{

    private Activity context;
    private OnPlaylistClickListener onPlaylistClickListener;
    private ArrayList<PlaylistModel> playlistModelArrayList;

    public AllplaylistsAdapter(Activity context,ArrayList<PlaylistModel> playlistModelArrayList){
        this.context = context;
        this.playlistModelArrayList=playlistModelArrayList;
    }

    public void setOnPlaylistClickListener(OnPlaylistClickListener onPlaylistClickListener)
    {
        this.onPlaylistClickListener=onPlaylistClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.allplaylists_model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AllplaylistsAdapter.ViewHolder myViewHolder=holder;
        PlaylistModel model=playlistModelArrayList.get(position);
        holder.tv_playlistname.setText(model.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onPlaylistClickListener!=null)
                    onPlaylistClickListener.onPlaylistItemclick(playlistModelArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_playlistthumbnail)
        ImageView iv_playlistthumbnail;
        @BindView(R.id.tv_playlistname)
        TextView tv_playlistname;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnPlaylistClickListener
    {
        void onPlaylistItemclick(PlaylistModel playlistModel);
    }
}
