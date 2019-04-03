package com.app.hulchul.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hulchul.R;
import com.app.hulchul.adapters.NotificationsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Notification_fragment extends Fragment {


    @BindView(R.id.rv_notifications)
    RecyclerView rv_notifications;
    private NotificationsAdapter notificationsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification_fragment, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        notificationsAdapter=new NotificationsAdapter(getActivity());
        rv_notifications.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv_notifications.setAdapter(notificationsAdapter);
    }
}
