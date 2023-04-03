package com.example.upmood;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static  final String DB_NAME = "login.db";
    public DBHelper(Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table users(username TEXT primary key,password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists users");
    }

//    xu ly them du lieu vao database
    public Boolean insertData(String username,String password){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username",username);
        values.put("password",password);

        long result = database.insert("users",null,values);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

//    xu ly kiem tra nguoi dung
    public Boolean checkUsername(String username){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from users where username=?",new String[] {username});
        if(cursor.getCount()>0){
            return true;
        }else{
            return false;
        }
    }

//    xu ly kiem tra ten dang nhap va password
public Boolean checkUserPass(String username, String password){
    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery("select * from users where username=? and password=?",new String[] {username,password});
    if(cursor.getCount()>0){
        return true;
    }else{
        return false;
    }
}
}
