package com.example.admin.nihongo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.nihongo.util.FragmentEvents;


public class AddWordFragment extends Fragment {
    private FragmentEvents events;
    TextView japaneseInput;
    TextView kanJiInput;
    Spinner nominalInput;
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
                String p3 = nominalInput.getSelectedItem().toString();
                String p4 = chineseInput.getText().toString();
                Object[] params = {
                    "".equals(p1) ? " " : p1,
                    "".equals(p2) ? " " : p2,
                    "".equals(p3) ? " " : p3,
                    "".equals(p4) ? " " : p4
                };
                events.executeQueryString(queryString, params);
            }
        });
        return view;
    }
}
