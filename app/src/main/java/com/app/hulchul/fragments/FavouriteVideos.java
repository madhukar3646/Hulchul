package com.app.hulchul.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.activities.FavouritesActivity;
import com.app.hulchul.adapters.HashtagsthumnailsAdapter;
import com.app.hulchul.adapters.UsersSearchAdapter;
import com.app.hulchul.adapters.VideothumbnailsAdapter;
import com.app.hulchul.model.VideoModel;
import com.app.hulchul.utils.ConnectionDetector;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteVideos extends Fragment implements FavouritesActivity.OnFavouritesFragmentSelected{

    @BindView(R.id.rv_favouritevideos)
    RecyclerView rv_favouritevideos;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    private VideothumbnailsAdapter adapter;
    private ConnectionDetector connectionDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_favourite_videos, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        tv_nodata.setVisibility(View.GONE);
        connectionDetector=new ConnectionDetector(getActivity());
        adapter=new VideothumbnailsAdapter(getActivity());
        rv_favouritevideos.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rv_favouritevideos.setAdapter(adapter);
    }

    @Override
    public void onFavouriteFragmentSelected()
    {
       adapter.notifyDataSetChanged();
    }
}
