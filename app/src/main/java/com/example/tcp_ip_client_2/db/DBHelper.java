package com.example.tcp_ip_client_2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBHelper extends SQLiteOpenHelper
    implements BaseColumns{

    public static final String DB_CONTACTS = "contacts.db";
    public static final String TABLE_NAME = "ip_adresa";
    public static final String NAME = "name";
    public static final String IPADR = "ipadr";
    public static final String PORT = "port";
    private Context context;

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, DB_CONTACTS, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // создаем таблицу с полями
        db.execSQL("CREATE TABLE " + TABLE_NAME
                + "(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME  + " TEXT,"
                + IPADR + " TEXT,"
                + PORT  + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( " DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate( db );

    }
}


