package com.app.hulchul.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hulchul.R;
import com.app.hulchul.activities.MySoundsActivity;
import com.app.hulchul.activities.SearchActivity;
import com.app.hulchul.adapters.HashtagSearchAdapter;
import com.app.hulchul.adapters.MySoundsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchHashtags_fragment extends Fragment implements SearchActivity.OnSearchFragmentSelected{

    @BindView(R.id.rv_hashtags)
    RecyclerView rv_hashtags;
    private HashtagSearchAdapter hashtagSearchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search_hashtags_fragment, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        hashtagSearchAdapter=new HashtagSearchAdapter(getActivity());
        rv_hashtags.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv_hashtags.setAdapter(hashtagSearchAdapter);
    }

    @Override
    public void onSearchFragmentSelected() {
        Log.e("Hashtags fragment","Hashtags fragment");
    }
}
