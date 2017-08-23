package com.example.admin.nihongo.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.admin.nihongo.MainActivity;
import com.example.admin.nihongo.model.Word;

import java.util.ArrayList;
import java.util.List;

import static com.example.admin.nihongo.MainActivity.database;

/**
 * Created by Admin on 2017/8/23.
 */

public class DatabaseOperation {
    private static DatabaseUtil databaseUtil;

    public static void init(Context context, String database, int version) {
        databaseUtil = new DatabaseUtil(context, database, null, version);
    }

    public static List<Word> getAllWords() {
        List<Word> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase();
        String queryString = "select Id, Japanese, KanJi, Nominal, Chinese from JapaneseWords";
        //查询获得游标
        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);
        //判断游标是否为空
        if(cursor.moveToFirst()) {
            //遍历游标
            while(cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String japanese = cursor.getString(1);
                String kanJi = cursor.getString(2);
                String nominal = cursor.getString(3);
                String chinese = cursor.getString(4);
                list.add(new Word(id, japanese, kanJi, nominal, chinese));
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return list;
    }

    public static int getRecordCount(String queryString, String[] params) {
        int count = 0;
        SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryString, params);
        //判断游标是否为空
        if(cursor.moveToFirst()) {
//            cursor.moveToNext();
            count = cursor.getInt(0);
            cursor.close();
        }
        sqLiteDatabase.close();
        return count;
    }

    public static void exec(String queryString, Object[] params) {
        SQLiteDatabase sqLiteDatabase = databaseUtil.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            if (params == null) {
                sqLiteDatabase.execSQL(queryString);
            } else {
                sqLiteDatabase.execSQL(queryString, params);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        }
    }
}
