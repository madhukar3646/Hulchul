package com.app.hulchul.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hulchul.R;
import com.app.hulchul.adapters.HashtagsCategoriesAdapter;
import com.app.hulchul.adapters.TrendingHashtagsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Discover_fragment extends Fragment {

    @BindView(R.id.rv_trendinghashtags)
    RecyclerView rv_trendinghashtags;
    @BindView(R.id.rv_hashtagslistcontainer)
    RecyclerView rv_hashtagslistcontainer;
    TrendingHashtagsAdapter adapter;
    HashtagsCategoriesAdapter hashtagsCategoriesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_discover_fragment, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        rv_trendinghashtags.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        adapter=new TrendingHashtagsAdapter(getActivity());
        rv_trendinghashtags.setAdapter(adapter);

        rv_hashtagslistcontainer.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        hashtagsCategoriesAdapter=new HashtagsCategoriesAdapter(getActivity());
        rv_hashtagslistcontainer.setAdapter(hashtagsCategoriesAdapter);
    }
}
