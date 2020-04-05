package com.database.studentlist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.database.studentlist.model.Contact;

import java.sql.SQLException;

/**
 * Created by KSTL on 13-07-2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    Context mContext;
    private static final String DATABASE_NAME = "Contacts.db";
    private static final int DATABASE_VERSION = 1;

    public Dao<Contact, Integer> mPastDue_Dao = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Contact.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, Contact.class, true);

            onCreate(db, connectionSource);
        } catch (SQLException e) {
        }
    }

    public Dao<Contact, Integer> getContactDao() throws SQLException {
        if (mPastDue_Dao == null) {
            mPastDue_Dao = getDao(Contact.class);
        }

        return mPastDue_Dao;
    }


    @Override
    public void close() {
        mPastDue_Dao = null;
        super.close();
    }
}
