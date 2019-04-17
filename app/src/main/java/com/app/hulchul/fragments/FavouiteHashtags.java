package com.app.hulchul.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.activities.FavouritesActivity;
import com.app.hulchul.adapters.HashtagSearchAdapter;
import com.app.hulchul.model.Hashtagsearchdata;
import com.app.hulchul.utils.ConnectionDetector;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouiteHashtags extends Fragment implements FavouritesActivity.OnFavouritesFragmentSelected {

    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    @BindView(R.id.rv_favouritehashtags)
    RecyclerView rv_favouritehashtags;
    private HashtagSearchAdapter adapter;
    private ConnectionDetector connectionDetector;
    private ArrayList<Hashtagsearchdata> hashtagsearchdataList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_favouite_hashtags, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        tv_nodata.setVisibility(View.GONE);
        connectionDetector=new ConnectionDetector(getActivity());
        adapter=new HashtagSearchAdapter(getActivity(),hashtagsearchdataList);
        rv_favouritehashtags.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv_favouritehashtags.setAdapter(adapter);
    }

    @Override
    public void onFavouriteFragmentSelected() {
        hashtagsearchdataList.clear();
        for (int i=0;i<30;i++)
        {
            Hashtagsearchdata model=new Hashtagsearchdata();
            model.setHashTag("Krishna");
            hashtagsearchdataList.add(model);
        }
        adapter.notifyDataSetChanged();
    }
}
