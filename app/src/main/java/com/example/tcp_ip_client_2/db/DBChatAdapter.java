package com.example.tcp_ip_client_2.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tcp_ip_client_2.classs.ChatModel;

import java.util.ArrayList;
import java.util.List;

public class DBChatAdapter {

    private DBChatHelper dbChatHelper;
    private SQLiteDatabase db;

    public DBChatAdapter(Context con){
        dbChatHelper = new DBChatHelper(con.getApplicationContext());
    }

    public void openConnection() {
        if (db == null || !db.isOpen()) {
            db = dbChatHelper.getWritableDatabase();
        }
    }

    public void closeConnection() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public DBChatAdapter openBd(){//открытие БД
        db = dbChatHelper.getWritableDatabase();
        return this;
    }

    public void closeBd(){//закрытие БД
        dbChatHelper.close();
    }

    public void createTableIfNotExists() {
        openConnection();
        //openBd();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBChatHelper.TABLE_NAME + " (" +
                DBChatHelper.MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBChatHelper.MESSAGE_OUTGOING + " INTEGER, " +
                DBChatHelper.MESSAGE_IN + " TEXT, " +
                DBChatHelper.MESSAGE_TIME + " TEXT, " +
                DBChatHelper.MESSAGE_OUT + " TEXT);");
        //closeBd();
    }

    @SuppressLint("Range")
    public ArrayList<ChatModel> getMessages() {
        openConnection();
        //openBd();
        ArrayList<ChatModel> messagesList = new ArrayList<>();
        String[] columns = new String[] {DBChatHelper.MESSAGE_ID,DBChatHelper.MESSAGE_OUTGOING, DBChatHelper.MESSAGE_IN, DBChatHelper.MESSAGE_TIME, DBChatHelper.MESSAGE_OUT};
        Cursor cursor = db.query(DBChatHelper.TABLE_NAME, columns, null, null, null, null, null);
            while (cursor.moveToNext()) {
                /*if (cursor.isLast()) {break;}*/
                ChatModel chatModel;
                String msg = "",time_msg = "";
                int id = cursor.getInt(cursor.getColumnIndex(DBChatHelper.MESSAGE_ID));
                @SuppressLint("Range") boolean outgoing = cursor.getInt(cursor.getColumnIndex(DBChatHelper.MESSAGE_OUTGOING)) == 1;
                if (outgoing) {
                    msg = cursor.getString(cursor.getColumnIndex(DBChatHelper.MESSAGE_OUT));
                    time_msg = cursor.getString(cursor.getColumnIndex(DBChatHelper.MESSAGE_TIME));
                    chatModel = new ChatModel( id, msg, time_msg,outgoing);
                    messagesList.add(chatModel);
                }else {
                    msg = cursor.getString(cursor.getColumnIndex(DBChatHelper.MESSAGE_IN));
                    time_msg = cursor.getString(cursor.getColumnIndex(DBChatHelper.MESSAGE_TIME));
                    chatModel = new ChatModel( id, msg, time_msg,outgoing);
                    messagesList.add(chatModel);
                }
            }
        cursor.close();
        //closeBd();
        return messagesList;
    }

    public long getCount(){
        openConnection();
        //openBd();
        long count = DatabaseUtils.queryNumEntries(db, DBChatHelper.TABLE_NAME);;
        //closeBd();
        return count;
    }

    @SuppressLint("Range")
    public ChatModel getMessage(String TableName, long id){
        openConnection();
        //openBd();
        ChatModel chatModel = null;
        String msg = "",time_msg = "";
        String query = String.format("SELECT * FROM %s WHERE %s=?",TableName, DBChatHelper._ID);
        Cursor cursor = db.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            @SuppressLint("Range") boolean outgoing = cursor.getInt(cursor.getColumnIndex(DBChatHelper.MESSAGE_OUTGOING)) == 1;
            time_msg = cursor.getString(cursor.getColumnIndex(DBChatHelper.MESSAGE_TIME));
            if (outgoing) {
                msg = cursor.getString(cursor.getColumnIndex(DBChatHelper.MESSAGE_OUT));
                chatModel = new ChatModel(  msg, time_msg,outgoing);
            }else {
                msg = cursor.getString(cursor.getColumnIndex(DBChatHelper.MESSAGE_IN));
                chatModel = new ChatModel(  msg, time_msg,outgoing);
            }
        }

        cursor.close();
        //closeBd();
        return chatModel;
    }

    // Добавление нового сообщения
    public long addDBMessage(ChatModel msg_in) {
        openConnection();
        //openBd();
        ContentValues values = new ContentValues();
        values.put(DBChatHelper.MESSAGE_OUTGOING, msg_in.getOutgoing());
        values.put(DBChatHelper.MESSAGE_IN, msg_in.getMessage_in());
        values.put(DBChatHelper.MESSAGE_TIME, msg_in.getMessage_time());
        values.put(DBChatHelper.MESSAGE_OUT, msg_in.getMessage_out());
        long res = db.insert(DBChatHelper.TABLE_NAME, null, values);
        //closeBd();
        return res;
    }

    // Удаление сообщения
    public int deleteMessage(int id) {
        openConnection();
        //openBd();
        String where = String.format("%s=%d", DBChatHelper._ID, id);
        int res = db.delete(DBChatHelper.TABLE_NAME, where, null);
        //closeBd();

        return res;
    }

    public void deleteMesagesAllChat(){
        openConnection();
        //openBd();
        db.execSQL("DROP TABLE IF EXISTS " + DBChatHelper.TABLE_NAME);
        //closeBd();
    }

    // Редактирование сообщения
    public int updateMessage(int id, ChatModel msg_in) {;
        openConnection();
        //openBd();
        ContentValues values = new ContentValues();
        values.put(DBChatHelper.MESSAGE_OUTGOING, msg_in.getOutgoing());
        values.put(DBChatHelper.MESSAGE_IN, msg_in.getMessage_in());
        values.put(DBChatHelper.MESSAGE_TIME, msg_in.getMessage_time());
        values.put(DBChatHelper.MESSAGE_OUT, msg_in.getMessage_out());

        String where = String.format("%s=%d", DBChatHelper._ID, id);
        int res = db.update(DBChatHelper.TABLE_NAME, values, where, null);
       // closeBd();
        return res;
    }


// Вывод списка чатов
    public List<String> getChats() {
        openConnection();
        //openBd();
        List<String> tables = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String tableName = cursor.getString(0);
                tables.add(tableName);
                Log.d("TABLE_NAMES", tableName);
            } while (cursor.moveToNext());
            cursor.close();
        }
        //closeBd();
        return tables;
    }


}
