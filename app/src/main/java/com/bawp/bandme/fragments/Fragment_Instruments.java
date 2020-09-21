package com.bawp.bandme.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bawp.bandme.CallBack_Registration;
import com.bawp.bandme.R;


public class Fragment_Instruments extends Fragment {

    protected View view;
    private CallBack_Registration callBack_registration;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (view == null){
            view = inflater.inflate(R.layout.fragment_instruments, container, false);
        }
        return view;
    }

    public static Fragment_Instruments newInstance(){
        return new Fragment_Instruments();
    }

    public void setActivityCallBack(CallBack_Registration callBack_registration){
        this.callBack_registration = callBack_registration;
    }
}