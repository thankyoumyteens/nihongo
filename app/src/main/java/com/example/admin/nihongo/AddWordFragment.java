package com.example.admin.nihongo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.nihongo.util.DatabaseUtil;
import com.example.admin.nihongo.util.FragmentEvents;

import static com.example.admin.nihongo.R.id.japaneseInput;
import static com.example.admin.nihongo.R.id.kanJiInput;


public class AddWordFragment extends Fragment {
    private FragmentEvents events;
    TextView japaneseInput;
    TextView kanJiInput;
    TextView nominalInput;
    TextView chineseInput;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        events = (FragmentEvents) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_word, container, false);
        japaneseInput = view.findViewById(R.id.japaneseInput);
        kanJiInput = view.findViewById(R.id.kanJiInput);
        nominalInput = view.findViewById(R.id.nominalInput);
        chineseInput = view.findViewById(R.id.chineseInput);
        view.findViewById(R.id.addWord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String queryString = "insert into JapaneseWords(Japanese, KanJi, Nominal, Chinese) " +
                        "values(?, ?, ?, ?)";
                String p1 = japaneseInput.getText().toString();
                String p2 = kanJiInput.getText().toString();
                String p3 = nominalInput.getText().toString();
                String p4 = chineseInput.getText().toString();
                Object[] params = {
                        "".equals(p1) ? "未设定" : p1,
                        "".equals(p2) ? "未设定" : p2,
                        "".equals(p3) ? "未设定" : p3,
                        "".equals(p4) ? "未设定" : p4
                };
//                DatabaseUtil databaseUtil = new DatabaseUtil(AddWordFragment.this.getContext(), "words.db", null, 3);
//                SQLiteDatabase database = databaseUtil.getReadableDatabase();
//                database.execSQL(queryString, params);
                events.executeQueryString(queryString, params);
            }
        });
        return view;
    }
}
