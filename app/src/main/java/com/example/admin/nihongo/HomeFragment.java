package com.example.admin.nihongo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.nihongo.model.Word;

import java.util.ArrayList;
import java.util.List;

import static com.example.admin.nihongo.MainActivity.database;


public class HomeFragment extends Fragment {
    List<Word> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        RecyclerView recyclerView = view.findViewById(R.id.wordList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        final WordListAdapter adapter = new WordListAdapter(list);
        // 创建匿名类来实现接口
        adapter.setItemOnClickListener(new WordListAdapter.OnItemOnClickListener() {
            @Override
            public void onItemOnClick(View view, int pos) {

            }

            @Override
            public void onItemOnTouch(View view, int pos) {

            }

            @Override
            public void onItemLongOnClick(View view, int pos) {
                // 删除操作
                TextView idT = view.findViewById(R.id.wordId);
                int id = Integer.parseInt(idT.getText().toString());
                showNormalDialog(view, id, adapter, pos);
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    /**
     * 确认删除对话框
     * @param view
     * @param id
     * @param adapter
     * @param pos
     */
    private void showNormalDialog(View view, final int id, final WordListAdapter adapter, final int pos) {
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(view.getContext());
        normalDialog.setTitle("提示");
        normalDialog.setMessage("是否删除?");
        normalDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String queryString = "delete from JapaneseWords where Id=?";
                Object[] params = {
                        id
                };
                // 从数据库中删除
                database.execSQL(queryString, params);
                // 从RecyclerView控件中删除
                adapter.removeItem(pos);
            }
        });
        normalDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // 显示
        normalDialog.show();
    }

    /**
     * 查询数据库中的所有单词
     */
    private void init() {
        list = new ArrayList<>();

        String queryString = "select Id, Japanese, KanJi, Nominal, Chinese from JapaneseWords";
        //查询获得游标
        Cursor cursor = database.rawQuery(queryString, null);
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
    }
}
