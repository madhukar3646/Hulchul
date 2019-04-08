package com.app.hulchul.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hulchul.R;
import com.app.hulchul.activities.SearchActivity;

import butterknife.ButterKnife;


public class SearchUsers_Fragment extends Fragment implements SearchActivity.OnSearchFragmentSelected{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search_users_, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    public void init(View view)
    {

    }

    @Override
    public void onSearchFragmentSelected() {
        Log.e("Users fragment","Users fragment");
    }
}
