package com.app.hulchul.activities;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.app.hulchul.R;
import com.app.hulchul.adapters.InviteFriendsAdapter;
import com.app.hulchul.model.ContactsModel;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteFriendsActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
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
       back_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });

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
}
