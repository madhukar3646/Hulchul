package com.app.hulchul.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hulchul.R;
import com.app.hulchul.adapters.NotificationsAdapter;
import com.app.hulchul.model.NotificationModel;
import com.app.hulchul.model.NotificationsResponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Notification_fragment extends Fragment implements NotificationsAdapter.OnNotificationClickListener{

    @BindView(R.id.rv_notifications)
    RecyclerView rv_notifications;
    private NotificationsAdapter notificationsAdapter;
    private ArrayList<NotificationModel> notificationModelArrayList=new ArrayList<>();
    private ConnectionDetector connectionDetector;
    SessionManagement sessionManagement;
    private int listcount;

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
        connectionDetector=new ConnectionDetector(getActivity());
        sessionManagement=new SessionManagement(getActivity());
        notificationsAdapter=new NotificationsAdapter(getActivity(),notificationModelArrayList);
        notificationsAdapter.setOnNotificationClickListener(this);
        rv_notifications.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv_notifications.setAdapter(notificationsAdapter);

        if(connectionDetector.isConnectingToInternet())
            setNotificationsData("20","0");
        else
            Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));

        rv_notifications.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
                int pos = layoutManager.findLastCompletelyVisibleItemPosition();
                int numItems = recyclerView.getAdapter().getItemCount();
                Log.e("pos"+pos,"numitems "+numItems);
                if ((pos + 1) >= numItems)
                {
                    if(numItems<listcount)
                    {
                        if (connectionDetector.isConnectingToInternet()) {
                            setNotificationsData("20", "" + numItems);
                        } else
                            Utils.callToast(getActivity(), getResources().getString(R.string.internet_toast));
                    }
                }
            }
        });
    }

    private void setNotificationsData(String limit,String offset){
        Utils.showDialog(getContext());
        Call<NotificationsResponse> call= RetrofitApis.Factory.createTemp(getContext()).notificationsService(sessionManagement.getValueFromPreference(SessionManagement.USERID),limit,offset);
        call.enqueue(new Callback<NotificationsResponse>() {
            @Override
            public void onResponse(Call<NotificationsResponse> call, Response<NotificationsResponse> response) {
                Utils.dismissDialog();
                NotificationsResponse body=response.body();
                if(body.getStatus()==1){
                    listcount=body.getTotalcount();
                    if(body.getData()!=null && body.getData().size()>0)
                        notificationModelArrayList.addAll(body.getData());
                    notificationsAdapter.notifyDataSetChanged();
                }
                else {
                    Utils.callToast(getActivity(),body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<NotificationsResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }

    @Override
    public void onNotificationClick(NotificationModel model) {

    }
}
