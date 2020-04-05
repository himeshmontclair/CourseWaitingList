package com.database.studentlist.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.TextView;

import com.database.studentlist.ContactsActivity;
import com.database.studentlist.R;
import com.database.studentlist.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KSTL on 24-04-2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    Context mContext;
    List<Contact> contactsList, filterList;
    ContactsActivity mCallBack;

    public ContactsAdapter(Context mContext, List<Contact> contactsList) {
        this.mContext = mContext;
        this.contactsList = contactsList;
        this.filterList = new ArrayList<Contact>();

        this.filterList.addAll(this.contactsList);
    }


    @SuppressLint("NewApi")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        viewGroup.setClipToPadding(false);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_list, viewGroup, false);
        view.setOutlineProvider(ViewOutlineProvider.BOUNDS);
        view.setElevation(30);
        return new ViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Contact contact = filterList.get(position);
        holder.contact_name.setText(contact.getName());
        holder.contact_number.setText(contact.getMobile_Number());
        holder.email.setText(contact.getEmail());

        holder.card_view.setTag(contact);
        holder.card_view.setContentDescription(String.valueOf(position));
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.updatePassword(Integer.parseInt(v.getContentDescription().toString()),(Contact) v.getTag());
            }
        });

    }

    @Override
    public int getItemCount() {
        //return eventsList.size();
        return (null != filterList ? filterList.size() : 0);
    }

    public void setCallBack(ContactsActivity contactsActivity) {
        this.mCallBack=contactsActivity;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView contact_name, contact_number,email;
        CardView card_view;

        public ViewHolder(View view) {
            super(view);
            contact_name = (TextView) view.findViewById(R.id.name_text);
            contact_number = (TextView) view.findViewById(R.id.mobile_number);
            email = (TextView) view.findViewById(R.id.email);
            card_view=(CardView) view. findViewById(R.id.card_view);

        }
    }

    public void filter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    filterList.addAll(contactsList);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (Contact item : contactsList) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getName().contains(text.toLowerCase())) {
                            // Adding Matched items
                            filterList.add(item);
                        }
                    }
                }

                // Set on UI Thread
                ((ContactsActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }
}
