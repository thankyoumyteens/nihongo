package com.example.admin.nihongo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.nihongo.util.DatabaseOperation;
import com.example.admin.nihongo.util.FragmentEvents;


public class AddWordFragment extends Fragment {
    TextView japaneseInput;
    TextView kanJiInput;
    Spinner nominalInput;
    TextView chineseInput;

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
                String p3 = nominalInput.getSelectedItem().toString();
                String p4 = chineseInput.getText().toString();
                if ("".equals(p1) || "".equals(p4)) {
                    Toast.makeText(getContext(), "请输入假名或汉意", Toast.LENGTH_SHORT).show();
                    return;
                }
                Object[] params = {
                    p1,
                    "".equals(p2) ? " " : p2,
                    p3,
                    p4
                };
                DatabaseOperation.exec(queryString, params);
                japaneseInput.setText("");
                kanJiInput.setText("");
                nominalInput.setSelection(0);
                chineseInput.setText("");
                Toast.makeText(getContext(), "完成",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
