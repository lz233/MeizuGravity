package io.github.lz233.meizugravity.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句 并 执行
        //String sql = "create table history(content varchar(20))";
        //String sql = "create table history(id varchar(255),sourceText varchar(255),translationText varchar(255))";
        //String sql2 = "create table collection(id varchar(255),sourceText varchar(255),translationText varchar(255))";
        //db.execSQL(sql);
        //db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
