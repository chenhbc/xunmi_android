package com.egrcc.search.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by egrcc on 2/26/15.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_DOWNLOAD = "create table Download ("
            + "id integer primary key autoincrement, "
            + "created_date date, "
            + "size integer, "
            + "name text)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DOWNLOAD);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
