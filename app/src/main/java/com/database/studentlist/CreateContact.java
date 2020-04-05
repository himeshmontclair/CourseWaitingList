package com.database.studentlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.database.studentlist.database.DatabaseHelper;
import com.database.studentlist.model.Contact;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateContact extends AppCompatActivity implements View.OnClickListener {

    private EditText name_edttxt ;
    Spinner mobile_number_edttxt,email_edttxt;
    private Button save_button;
    private String Name_str, Mobile_Number_str, Email_str,password_str;

    private DatabaseHelper helper;
    Dao<Contact, Integer> contactDao = null;
    Random Number;
    int Rnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //intilizing the Database
        findViewes();
    }

    private void findViewes() {
        name_edttxt = (EditText) findViewById(R.id.name_edttxt);
        mobile_number_edttxt = (Spinner) findViewById(R.id.mobile_number_edttxt);
        email_edttxt = (Spinner) findViewById(R.id.email_edttxt);
        save_button = (Button) findViewById(R.id.save_button);
        save_button.setOnClickListener(this);

        List<String> list = new ArrayList<String>();
        list.add("Course 1");
        list.add("Course 2");
        list.add("Course 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mobile_number_edttxt.setAdapter(dataAdapter);

        List<String> list1 = new ArrayList<String>();
        list1.add("Graduate");
        list1.add("Under Graduate");
        list1.add("Online");
        list1.add("Research");

        ArrayAdapter<String> data_Adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list1);
        data_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        email_edttxt.setAdapter(data_Adapter);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.save_button:
                savedOnDatabase();
                break;
        }
    }

    private void savedOnDatabase() {
            Name_str = name_edttxt.getText().toString();
            Mobile_Number_str = String.valueOf(mobile_number_edttxt.getSelectedItem());
            Email_str = String.valueOf(email_edttxt.getSelectedItem());

            Number = new Random();
            Rnumber = Number.nextInt(100);

            Contact contact = new Contact();
            contact.setId(Integer.toString(Rnumber));
            contact.setName(Name_str);
            contact.setMobile_Number(Mobile_Number_str);
            contact.setEmail(Email_str);
        if(!Name_str.isEmpty()) {
            try {
                helper = new DatabaseHelper(CreateContact.this);
                contactDao = helper.getContactDao();
                contactDao.createOrUpdate(contact);
                Toast.makeText(getApplicationContext(), "Saved successfuly", Toast.LENGTH_LONG).show();

                name_edttxt.setText("");
                mobile_number_edttxt.setSelection(0);
                email_edttxt.setSelection(0);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "Please give the fields", Toast.LENGTH_SHORT).show();
        }
    }
}