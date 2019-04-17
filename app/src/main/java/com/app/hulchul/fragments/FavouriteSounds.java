package com.app.hulchul.fragments;

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
import com.app.hulchul.adapters.SoundSearchAdapter;
import com.app.hulchul.model.SoundSearchdata;
import com.app.hulchul.utils.ConnectionDetector;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FavouriteSounds extends Fragment implements FavouritesActivity.OnFavouritesFragmentSelected {

    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    @BindView(R.id.rv_favouritesounds)
    RecyclerView rv_favouritesounds;
    private ConnectionDetector connectionDetector;
    private SoundSearchAdapter adapter;
    private ArrayList<SoundSearchdata> soundsdataList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_favourite_sounds, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        tv_nodata.setVisibility(View.GONE);
        connectionDetector=new ConnectionDetector(getActivity());
        adapter=new SoundSearchAdapter(getActivity(),soundsdataList);
        rv_favouritesounds.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv_favouritesounds.setAdapter(adapter);
    }

    @Override
    public void onFavouriteFragmentSelected() {
        soundsdataList.clear();
        for (int i=0;i<30;i++)
        {
            SoundSearchdata model=new SoundSearchdata();
            model.setName("Chatrapathi");
            model.setFile("chatrapathi.mp3");
            soundsdataList.add(model);
        }
        adapter.notifyDataSetChanged();
    }
}
