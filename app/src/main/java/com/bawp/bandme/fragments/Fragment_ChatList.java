package com.bawp.bandme.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bawp.bandme.R;

import java.util.ArrayList;

public class Fragment_ChatList extends Fragment {

    protected View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (view == null){
            view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        }
        return view;
    }

    public static Fragment_ChatList newInstance(){
        return new Fragment_ChatList();
    }




}