package com.example.nakajima.mimamorisystemmonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nakajima on 2017/09/28.
 */

public class MimamoriSystemDBOperator {

    private SQLiteDatabase db;

    public MimamoriSystemDBOperator(Context context) {
        MimamoriSystemSQLiteOpenHelper helper = new MimamoriSystemSQLiteOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public void setRaspiInfo(String uuid, String ip) {
        ContentValues values = new ContentValues();
        values.put("uuid", uuid);
        values.put("ip_address", ip);
        db.insert("raspi_list", null, values);
    }

    private class MimamoriSystemSQLiteOpenHelper extends SQLiteOpenHelper {

        static final String DB = "mimamori_system.db";
        static final int DB_VERSION = 1;
        static final String CREATE_TABLE = "create table raspi_list ( uuid char(16) primary key, ip_address varchar(16) not null );";
        static final String DROP_TABLE = "drop table raspi_list;";

        public MimamoriSystemSQLiteOpenHelper(Context context) {
            super(context, DB, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
    }

}
