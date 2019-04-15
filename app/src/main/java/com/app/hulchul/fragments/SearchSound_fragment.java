package com.app.hulchul.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hulchul.R;
import com.app.hulchul.activities.SearchActivity;
import com.app.hulchul.adapters.HashtagSearchAdapter;
import com.app.hulchul.adapters.SoundSearchAdapter;
import com.app.hulchul.utils.ConnectionDetector;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchSound_fragment extends Fragment implements SearchActivity.OnSearchFragmentSelected {

    @BindView(R.id.rv_sounds)
    RecyclerView rv_sounds;
    private SoundSearchAdapter soundSearchAdapter;
    private ConnectionDetector connectionDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search_sound_fragment, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        soundSearchAdapter=new SoundSearchAdapter(getActivity());
        rv_sounds.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv_sounds.setAdapter(soundSearchAdapter);
    }

    @Override
    public void onSearchFragmentSelected() {
        Log.e("Sound fragment","Sound fragment");
    }

    public void onPerformSearch(String key) {

    }
}
