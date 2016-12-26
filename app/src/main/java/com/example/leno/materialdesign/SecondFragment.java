package com.example.leno.materialdesign;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by LENO on 21-12-2016.
 */

public class SecondFragment extends Fragment {

        private String title;
        private int page;

        // newInstance constructor for creating fragment with arguments
        public static com.example.leno.materialdesign.SecondFragment newInstance(int page, String title) {
            com.example.leno.materialdesign.SecondFragment fragmentSecond = new com.example.leno.materialdesign.SecondFragment();
            Bundle args = new Bundle();
            args.putInt("someInt", page);
            args.putString("someTitle", title);
            fragmentSecond.setArguments(args);
            return fragmentSecond;
        }

        // Store instance variables based on arguments passed
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            page = getArguments().getInt("someInt", 0);
            title = getArguments().getString("someTitle");
        }

        // Inflate the view for the fragment based on layout XML
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_two, container, false);
            TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
            tvLabel.setText(page + " -- " + title);
            return view;
        }
    }

