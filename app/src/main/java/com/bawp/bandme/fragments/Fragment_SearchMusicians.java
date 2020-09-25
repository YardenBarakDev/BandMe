package com.bawp.bandme.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bawp.bandme.R;

public class Fragment_SearchMusicians extends Fragment {

    protected View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (view == null){
            view = inflater.inflate(R.layout.fragment_search, container, false);
        }

        return view;
    }

    public static Fragment_SearchMusicians newInstance(){
        return new Fragment_SearchMusicians();
    }

    /*
    public void setActivityCallBack(CallBack_RegistrationLoginInfo callBack_registrationLoginInfo){
        this.callBack_registrationLoginInfo = callBack_registrationLoginInfo;
    }
     */
}