package com.example.admin.nihongo;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.admin.nihongo.util.FragmentEvents;


public class NavBarFragment extends Fragment {
    private FragmentEvents events;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        events = (FragmentEvents) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_bar, container, false);
        view.findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                events.changeFragment(new HomeFragment());
            }
        });
        view.findViewById(R.id.addWord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                events.changeFragment(new AddWordFragment());
            }
        });
        view.findViewById(R.id.other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                events.changeFragment(new OtherFragment());
            }
        });
        return view;
    }

}
