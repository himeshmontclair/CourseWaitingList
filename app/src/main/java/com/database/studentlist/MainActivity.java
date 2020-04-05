package com.database.studentlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button create_contact, contacts_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById();
    }

    private void findViewById() {
        create_contact = (Button) findViewById(R.id.create_contact);
        create_contact.setOnClickListener(this);

        contacts_list = (Button) findViewById(R.id.contacts_list);
        contacts_list.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.create_contact:
                Intent intent = new Intent(MainActivity.this, CreateContact.class);
                startActivity(intent);
                break;

            case R.id.contacts_list:
                Intent intent1 = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
