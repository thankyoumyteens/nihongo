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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.nihongo.model.Word;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fragment;
import static android.R.attr.id;
import static com.example.admin.nihongo.MainActivity.database;
import static com.example.admin.nihongo.R.id.chineseInput;
import static com.example.admin.nihongo.R.id.idEdit;
import static com.example.admin.nihongo.R.id.japaneseInput;
import static com.example.admin.nihongo.R.id.kanJiInput;
import static com.example.admin.nihongo.R.id.nominalInput;


public class HomeFragment extends Fragment {
    List<Word> list;
    LinearLayout showList;
    LinearLayout edit;
    TextView idEdit;
    EditText japaneseEdit;
    EditText kanJiEdit;
    Spinner nominalEdit;
    EditText chineseEdit;
    View currentView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        showList = view.findViewById(R.id.list);
        edit = view.findViewById(R.id.edit);
        idEdit = view.findViewById(R.id.idEdit);
        japaneseEdit = view.findViewById(R.id.japaneseEdit);
        kanJiEdit = view.findViewById(R.id.kanJiEdit);
        nominalEdit = view.findViewById(R.id.nominalEdit);
        chineseEdit = view.findViewById(R.id.chineseEdit);
        init();
        RecyclerView recyclerView = view.findViewById(R.id.wordList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        final WordListAdapter adapter = new WordListAdapter(list);
        // 创建匿名类来实现接口
        adapter.setItemOnClickListener(new WordListAdapter.OnItemOnClickListener() {
            /**
             * 修改单词
             * @param view
             * @param pos
             */
            @Override
            public void onItemOnClick(View view, int pos) {
                currentView = view;
                String id = ((TextView) view.findViewById(R.id.wordId)).getText().toString();
                String japaneseText = ((TextView) view.findViewById(R.id.wordJapanese)).getText().toString();
                String kanJiText = ((TextView) view.findViewById(R.id.wordKanJi)).getText().toString();
                String nominalAndChineseText = ((TextView) view.findViewById(R.id.wordChinese)).getText().toString();
                String chineseText = nominalAndChineseText.substring(nominalAndChineseText.indexOf("]") + 1);
                String nominalText = nominalAndChineseText.substring(nominalAndChineseText.indexOf("[") + 1, nominalAndChineseText.indexOf("]"));
                int nominalPos = getPos(nominalText);
                idEdit.setText(id);
                japaneseEdit.setText(japaneseText);
                kanJiEdit.setText(kanJiText);
                nominalEdit.setSelection(nominalPos);
                chineseEdit.setText(chineseText);
                hide();
            }

            @Override
            public void onItemOnTouch(View view, int pos) {

            }

            /**
             * 删除单词
             * @param view
             * @param pos
             */
            @Override
            public void onItemLongOnClick(View view, int pos) {
                // 删除操作
                TextView idT = view.findViewById(R.id.wordId);
                int id = Integer.parseInt(idT.getText().toString());
                showNormalDialog(view, id, adapter, pos);
            }
        });
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.editWord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idEdit.getText().toString();
                String japaneseText = japaneseEdit.getText().toString();
                String kanJiText = kanJiEdit.getText().toString();
                String nominalText = nominalEdit.getSelectedItem().toString();
                String chineseText = chineseEdit.getText().toString();
                String queryString = "update JapaneseWords set Japanese=?, KanJi=?, Nominal=?, Chinese=? where Id=?";
                Object[] params = {
                        "".equals(japaneseText) ? " " : japaneseText,
                        "".equals(kanJiText) ? " " : kanJiText,
                        "".equals(nominalText) ? " " : nominalText,
                        "".equals(chineseText) ? " " : chineseText,
                        "".equals(id) ? "0" : id
                };
                database.execSQL(queryString, params);
                // 回显
                TextView wordJapanese = currentView.findViewById(R.id.wordJapanese);
                TextView wordKanJi = currentView.findViewById(R.id.wordKanJi);
                TextView nominalAndChinese = currentView.findViewById(R.id.wordChinese);
                wordJapanese.setText(japaneseText);
                wordKanJi.setText(kanJiText);
                nominalAndChinese.setText("[" + nominalText + "]" + chineseText);
                show();
            }
        });
        return view;
    }

    private int getPos(String val) {
        String[] items = getResources().getStringArray(R.array.nominal);
        for (int i = 0; i < items.length; i++) {
            if (val.equals(items[i])) {
                return i;
            }
        }
        return 0;
    }

    private void reload() {

    }

    private void hide() {
        showList.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
    }

    private void show() {
        edit.setVisibility(View.GONE);
        showList.setVisibility(View.VISIBLE);
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
