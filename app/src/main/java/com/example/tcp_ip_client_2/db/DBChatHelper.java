package com.example.tcp_ip_client_2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBChatHelper extends SQLiteOpenHelper
        implements BaseColumns {

    public static  String DB_NAME = "chat.db";
    public static  String TITLE = "title";
    public static  String DESCRIPTION = "description";
    private static  int SCHEMA = 3; // версия базы данных
    public static  String TABLE_NAME = "testTable";// название таблицы в бд
    // названия столбцов

    public static final String MESSAGE_ID = "_ID";
    public static final String MESSAGE_OUTGOING = "outgoing";
    public static final String MESSAGE_IN = "messageIn";
    public static final String MESSAGE_OUT = "messageOut";
    public static final String MESSAGE_TIME = "time_Message";

    public DBChatHelper(Context context) {
        // конструктор суперкласса
        super(context, DB_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // создаем таблицу с полями
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MESSAGE_OUTGOING  + " INTEGER,"
                + MESSAGE_IN        + " TEXT,"
                + MESSAGE_TIME + " TEXT,"
                + MESSAGE_OUT       + " TEXT);");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( " DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate( db );
    }

    public static String getTITLE() {
        return TITLE;
    }

    public static void setTITLE(String TITLE) {
        DBChatHelper.TITLE = TITLE;
    }

    public static String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public static void setDESCRIPTION(String DESCRIPTION) {
        DBChatHelper.DESCRIPTION = DESCRIPTION;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static void setTableName(String tableName) {

        TABLE_NAME = tableName.replace(" ", "");;
    }

    public static int getSCHEMA() {
        return SCHEMA;
    }

    public static void setSCHEMA(int SCHEMA) {
        DBChatHelper.SCHEMA = SCHEMA;
    }
}
