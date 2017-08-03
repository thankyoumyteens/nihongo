package com.example.admin.nihongo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import static com.example.admin.nihongo.MainActivity.database;

/**
 * Created by Admin on 2017/8/3.
 */

public class DatabaseUtil extends SQLiteOpenHelper {
    private Context context;

    public DatabaseUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "create table JapaneseWords(" +
                "Id integer primary key autoincrement," +
                "Japanese text, " +
                "KanJi text, " +
                "Nominal text, " +
                "Chinese text " +
                ")";
        sqLiteDatabase.execSQL(createTable);
        /**
         * 蜜汁bug: 表创建后第一次insert无效, 只好先insert一条无用数据
         */
        sqLiteDatabase.execSQL("insert into JapaneseWords(Japanese, KanJi, Nominal, Chinese) values('','','','')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
