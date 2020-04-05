package com.database.studentlist;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.database.studentlist.adapter.ContactsAdapter;
import com.database.studentlist.database.DatabaseHelper;
import com.database.studentlist.interfaces.UpdatePassword;
import com.database.studentlist.model.Contact;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, UpdatePassword {

    ContactsAdapter contactsAdapter;
    private RecyclerView contact_recyclerview;
    private SearchView search_view;
    private DatabaseHelper helper;
    Dao<Contact, Integer> contactsDao = null;
    private List<Contact> contactdbList;
    // private List<Contact> contactdbList = new ArrayList<Contact>();
    private EditText search_parent;
    private CardView card_view;
    private int screen_Width, screen_height;
    private ImageView cancel_popup;
    private Button update_button, delete_button;
    private EditText name_editext;
    private Spinner mobile_number, mail;
    private Dao<Contact, Integer> contactDao;
    private TextView idno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        helper = new DatabaseHelper(ContactsActivity.this);
        try {
            contactsDao = helper.getContactDao();
            contactdbList = contactsDao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        findViewes();
        setupSearchView();
        DisplayMetrics dm = new DisplayMetrics();
        ContactsActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screen_Width = dm.widthPixels;
        screen_height = dm.heightPixels;

    }

    private void findViewes() {

        contact_recyclerview = (RecyclerView) findViewById(R.id.contact_recyclerview);
        search_view = (SearchView) findViewById(R.id.search_view);


        search_parent = (EditText) findViewById(R.id.search_parent);

        contacts_list();
    }

    private void contacts_list() {
        contactsAdapter = new ContactsAdapter(ContactsActivity.this, contactdbList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ContactsActivity.this);
        contact_recyclerview.setLayoutManager(layoutManager);
        contact_recyclerview.setNestedScrollingEnabled(false);
        contact_recyclerview.setAdapter(contactsAdapter);
        contactsAdapter.setCallBack(ContactsActivity.this);
        contactsAdapter.notifyDataSetChanged();
    }

    private void setupSearchView() {
        search_view.setIconifiedByDefault(false);
        search_view.setOnQueryTextListener(this);
        search_view.setFocusableInTouchMode(true);
        search_view.setSubmitButtonEnabled(false);
        search_view.setQueryHint("Search Student Name");
        search_parent.requestFocus();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (contactsAdapter != null) {
            contactsAdapter.filter(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (contactsAdapter != null) {
            contactsAdapter.filter(query);
        }
        return true;
    }


    @Override
    public void updatePassword(int Postion, Contact contact) {
        popup(contact);
    }

    private void popup(final Contact obj) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_details);
        dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (screen_Width * 0.95);//WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        name_editext = (EditText) dialog.findViewById(R.id.name_editext);
        mobile_number = (Spinner) dialog.findViewById(R.id.mobile_number);
        mail = (Spinner) dialog.findViewById(R.id.mail);

        List<String> list1 = new ArrayList<String>();
        list1.add(obj.getMobile_Number());
        list1.add("Course 1");
        list1.add("Course 2");
        list1.add("Course 3");

        final ArrayAdapter<String> data_Adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list1);
        data_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        List<String> list = new ArrayList<String>();
        list.add(obj.getEmail());
        list.add("Graduate");
        list.add("Under Graduate");
        list.add("Online");
        list.add("Research");

        final ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        name_editext.setText(obj.getName());
        mobile_number.setAdapter(data_Adapter);
        mail.setAdapter(dataAdapter1);
        cancel_popup = (ImageView) dialog.findViewById(R.id.cancel_popup);
        cancel_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        update_button = (Button) dialog.findViewById(R.id.update_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mob = String.valueOf(mobile_number.getSelectedItem());
                String maill = String.valueOf(mail.getSelectedItem());

                obj.setName(name_editext.getText().toString());
                obj.setMobile_Number(mob);
                obj.setEmail(maill);
                obj.setId(obj.getId());

                try {
                    contactDao = helper.getContactDao();
                    contactDao.createOrUpdate(obj);

                    Toast.makeText(getApplicationContext(), "successfuly Saved", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    contactsAdapter.notifyDataSetChanged();

                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });

        delete_button = (Button) dialog.findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    contactDao = helper.getContactDao();
                    contactDao.delete(obj);

                    Toast.makeText(getApplicationContext(), "successfuly deleted", Toast.LENGTH_LONG).show();
                    dialog.dismiss();

                    contactDao = helper.getContactDao();
                    contactdbList = contactsDao.queryForAll();

                    contactsAdapter = new ContactsAdapter(ContactsActivity.this, contactdbList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ContactsActivity.this);
                    contact_recyclerview.setLayoutManager(layoutManager);
                    contact_recyclerview.setNestedScrollingEnabled(false);
                    contact_recyclerview.setAdapter(contactsAdapter);
                    contactsAdapter.setCallBack(ContactsActivity.this);
                    contactsAdapter.notifyDataSetChanged();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        dialog.show();

    }


}
