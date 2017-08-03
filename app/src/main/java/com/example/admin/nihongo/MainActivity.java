package com.example.admin.nihongo;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.admin.nihongo.util.DatabaseUtil;
import com.example.admin.nihongo.util.FragmentEvents;

public class MainActivity extends AppCompatActivity implements FragmentEvents {
    DatabaseUtil databaseUtil;
    public static SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 创建数据库
        databaseUtil = new DatabaseUtil(this, "words.db", null, 3);
        database = databaseUtil.getWritableDatabase();
        // 初始界面
        changeFragment(new HomeFragment());
    }

    @Override
    public void changeFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    @Override
    public void executeQueryString(String queryString, Object[] params) {
        if (params == null) {
            database.execSQL(queryString);
        } else {
            database.execSQL(queryString, params);
        }
        Toast.makeText(MainActivity.this, "完成",Toast.LENGTH_SHORT).show();
    }
}
