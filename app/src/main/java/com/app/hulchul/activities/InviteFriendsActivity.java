package com.app.hulchul.activities;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.InviteFriendsAdapter;
import com.app.hulchul.model.ContactsModel;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteFriendsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    @BindView(R.id.layout_invite)
    RelativeLayout layout_invite;
    @BindView(R.id.tv_select_deselect)
    TextView tv_select_deselect;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private InviteFriendsAdapter inviteFriendsAdapter;
    private ArrayList<ContactsModel> contactsModelArrayList=new ArrayList<>();
    private HashMap<String,String> filterContacts_hmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
       connectionDetector=new ConnectionDetector(InviteFriendsActivity.this);
       sessionManagement=new SessionManagement(InviteFriendsActivity.this);
       back_btn.setOnClickListener(this);
       layout_invite.setOnClickListener(this);
       tv_select_deselect.setOnClickListener(this);

       rv_list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
       inviteFriendsAdapter=new InviteFriendsAdapter(InviteFriendsActivity.this,contactsModelArrayList);
       rv_list.setAdapter(inviteFriendsAdapter);
       showContacts();
    }

    private void showContacts()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showDialog(InviteFriendsActivity.this);
                getContacts();
            }
        });
    }

    private void displayContacts()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.dismissDialog();
                inviteFriendsAdapter.notifyDataSetChanged();
                Type listType = new TypeToken<List<ContactsModel>>() {}.getType();
                Gson gson = new Gson();
                String json = gson.toJson(contactsModelArrayList, listType);
                Log.e("contacts string",""+json);
            }
        });
    }

    public void getContacts() {
        Cursor c = null;
        try {
            c = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            filterContacts_hmap=new HashMap<>();
            while (c.moveToNext()) {
                ContactsModel model=new ContactsModel();
                String contactID = c.getString(c
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String contactName = c.getString(c
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phNumber = c.getString(c
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    String contact = phNumber.replace(" ", "");
                    String contact1 = contact.replace("-", "");
                    //String contact4 = contact1.replace("+", "");
                    String contact2 = contact1.replace("(", "");
                    String contact3 = contact2.replace(")", "");
                    String number=contact3;

                if(!filterContacts_hmap.containsKey(contactID)){
                    filterContacts_hmap.put(contactID, contactID);
                    model.setId(contactID);
                    model.setName(contactName);
                    model.setNumber(number);
                    contactsModelArrayList.add(model);
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        displayContacts();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.back_btn:
                finish();
                break;
            case R.id.layout_invite:
                ArrayList<ContactsModel> invitesList=inviteFriendsAdapter.getSelectedInvites();
                if(invitesList.size()>0)
                {
                    for(int i=0;i<invitesList.size();i++)
                        Log.e("invited",invitesList.get(i).getName());
                }
                else {
                    Utils.callToast(InviteFriendsActivity.this,"Please select contacts and invite");
                }
                break;

            case R.id.tv_select_deselect:
                if(tv_select_deselect.getText().toString().equalsIgnoreCase("Select All"))
                {
                    tv_select_deselect.setText("Unselect All");
                    inviteFriendsAdapter.selectAll();
                }
                else {
                   tv_select_deselect.setText("Select All");
                   inviteFriendsAdapter.unselectAll();
                }
                break;
        }
    }
}
