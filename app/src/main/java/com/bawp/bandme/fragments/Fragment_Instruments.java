package com.bawp.bandme.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.bawp.bandme.call_back_interface.CallBack_RegistrationInstruments;
import com.bawp.bandme.R;
import com.bawp.bandme.util.MyUtil;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;


public class Fragment_Instruments extends Fragment {

    protected View view;
    private CallBack_RegistrationInstruments callBack_registrationInstruments;
    private ImageView Instruments_IMAGE_backGround;
    ArrayList<String> instruments;

    //instruments Images
    private ImageView Instruments_IMAGE_electric_guitar;
    private ImageView Instruments_IMAGE_bass_guitar;
    private ImageView Instruments_IMAGE_drums;
    private ImageView Instruments_IMAGE_keyboard;
    private ImageView Instruments_IMAGE_singer;
    private ImageView Instruments_IMAGE_flute;
    private ImageView Instruments_IMAGE_dj;
    private ImageView Instruments_IMAGE_mandolin;
    private ImageView Instruments_IMAGE_violin;
    private ImageView Instruments_IMAGE_percussion;
    private ImageView Instruments_IMAGE_piano;
    private ImageView Instruments_IMAGE_saxophone;

    //navigation images
    private ImageView Instruments_IMAGE_leftArrow;
    private ImageView Instruments_IMAGE_rightArrow;

    private TextInputLayout Instruments_TF_other;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (view == null){
            view = inflater.inflate(R.layout.fragment_instruments, container, false);
        }
        instruments = new ArrayList<>();
        findView();
        setListenerToImages();
        glideBackground();
        return view;
    }

    private void setListenerToImages() {
        //instruments Images
        Instruments_IMAGE_electric_guitar.setOnClickListener(imageListener);
        Instruments_IMAGE_bass_guitar.setOnClickListener(imageListener);
        Instruments_IMAGE_drums.setOnClickListener(imageListener);
        Instruments_IMAGE_keyboard.setOnClickListener(imageListener);
        Instruments_IMAGE_singer.setOnClickListener(imageListener);
        Instruments_IMAGE_flute.setOnClickListener(imageListener);
        Instruments_IMAGE_dj.setOnClickListener(imageListener);
        Instruments_IMAGE_mandolin.setOnClickListener(imageListener);
        Instruments_IMAGE_violin.setOnClickListener(imageListener);
        Instruments_IMAGE_percussion.setOnClickListener(imageListener);
        Instruments_IMAGE_piano.setOnClickListener(imageListener);
        Instruments_IMAGE_saxophone.setOnClickListener(imageListener);

        //navigation images
        Instruments_IMAGE_rightArrow.setOnClickListener(navigationImagesListener);
        Instruments_IMAGE_leftArrow.setOnClickListener(navigationImagesListener);
    }


    public static Fragment_Instruments newInstance(){
        return new Fragment_Instruments();
    }

    public void setActivityCallBack(CallBack_RegistrationInstruments callBack_registrationInstruments){
        this.callBack_registrationInstruments = callBack_registrationInstruments;
    }


    private View.OnClickListener navigationImagesListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            navigationImages(view);
        }
    };

    private void navigationImages(View view) {
        switch (((String) view.getTag())) {
            case "Instruments_IMAGE_rightArrow":
                if (Instruments_TF_other.getEditText().getText() != null &&
                        !Instruments_TF_other.getEditText().getText().toString().trim().isEmpty()) {
                    instruments.add(Instruments_TF_other.getEditText().getText().toString().trim());
                }
                //change to the next registration fragment
                callBack_registrationInstruments.advanceInstrumentStep(instruments);
                break;
            case "Instruments_IMAGE_leftArrow":
                //return to the previous registration fragment
                callBack_registrationInstruments.returnTOLoginInfo();
                break;
        }
    }


    private View.OnClickListener imageListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            instrumentsImages(view);
        }
    };

    //check which instrument the user clicked and add it to the instruments arrayList and change to color photo
    //if the user click again remove from the arrayList and change to black and white photo
    private void instrumentsImages(View view) {

        switch ((String)view.getTag()){
            case "Instruments_IMAGE_electric_guitar":
                if (instruments.contains(MyUtil.KEYS.ELECTRIC_GUITAR)) {
                    Instruments_IMAGE_electric_guitar.setImageResource(R.drawable.electric_guitar);
                    instruments.remove(MyUtil.KEYS.ELECTRIC_GUITAR);
                }
                else{
                    Instruments_IMAGE_electric_guitar.setImageResource(R.drawable.electric_guitar_chose);
                    instruments.add(MyUtil.KEYS.ELECTRIC_GUITAR);
                }
                break;
            case "Instruments_IMAGE_bass_guitar":
                if (instruments.contains(MyUtil.KEYS.BASS_GUITAR)) {
                    Instruments_IMAGE_bass_guitar.setImageResource(R.drawable.bass_guitar);
                    instruments.remove(MyUtil.KEYS.BASS_GUITAR);
                }
                else{
                    Instruments_IMAGE_bass_guitar.setImageResource(R.drawable.bass_guitar_chose);
                    instruments.add(MyUtil.KEYS.BASS_GUITAR);
                }
                break;
            case "Instruments_IMAGE_drums":
                if (instruments.contains(MyUtil.KEYS.DRUMS)) {
                    Instruments_IMAGE_drums.setImageResource(R.drawable.drums);
                    instruments.remove(MyUtil.KEYS.DRUMS);
                }
                else{
                    Instruments_IMAGE_drums.setImageResource(R.drawable.drums_chose);
                    instruments.add(MyUtil.KEYS.DRUMS);
                }
                break;
            case "Instruments_IMAGE_keyboard":
                if (instruments.contains(MyUtil.KEYS.KEYBOARD)) {
                    Instruments_IMAGE_keyboard.setImageResource(R.drawable.keyboard);
                    instruments.remove(MyUtil.KEYS.KEYBOARD);
                }
                else{
                    Instruments_IMAGE_keyboard.setImageResource(R.drawable.keyboard_chose);
                    instruments.add(MyUtil.KEYS.KEYBOARD);
                }
                break;
            case "Instruments_IMAGE_singer":
                if (instruments.contains(MyUtil.KEYS.MICROPHONE)) {
                    Instruments_IMAGE_singer.setImageResource(R.drawable.microphone);
                    instruments.remove(MyUtil.KEYS.MICROPHONE);
                }
                else{
                    Instruments_IMAGE_singer.setImageResource(R.drawable.microphone_chose);
                    instruments.add(MyUtil.KEYS.MICROPHONE);
                }
                break;
            case "Instruments_IMAGE_flute":
                if (instruments.contains(MyUtil.KEYS.FLUTE)) {
                    Instruments_IMAGE_flute.setImageResource(R.drawable.flute);
                    instruments.remove(MyUtil.KEYS.FLUTE);
                }
                else{
                    Instruments_IMAGE_flute.setImageResource(R.drawable.flute_chose);
                    instruments.add(MyUtil.KEYS.FLUTE);
                }
                break;
            case "Instruments_IMAGE_dj":
                if (instruments.contains(MyUtil.KEYS.DJ)) {
                    Instruments_IMAGE_dj.setImageResource(R.drawable.dj);
                    instruments.remove(MyUtil.KEYS.DJ);
                }
                else{
                    Instruments_IMAGE_dj.setImageResource(R.drawable.dj_chose);
                    instruments.add(MyUtil.KEYS.DJ);
                }
                break;
            case "Instruments_IMAGE_mandolin":
                if (instruments.contains(MyUtil.KEYS.MANDOLIN)) {
                    Instruments_IMAGE_mandolin.setImageResource(R.drawable.mandolin);
                    instruments.remove(MyUtil.KEYS.MANDOLIN);
                }
                else{
                    Instruments_IMAGE_mandolin.setImageResource(R.drawable.mandolin_chose);
                    instruments.add(MyUtil.KEYS.MANDOLIN);
                }
                break;
            case "Instruments_IMAGE_violin":
                if (instruments.contains(MyUtil.KEYS.VIOLIN)) {
                    Instruments_IMAGE_violin.setImageResource(R.drawable.violin);
                    instruments.remove(MyUtil.KEYS.VIOLIN);
                }
                else{
                    Instruments_IMAGE_violin.setImageResource(R.drawable.violin_chose);
                    instruments.add(MyUtil.KEYS.VIOLIN);
                }
                break;
            case "Instruments_IMAGE_percussion":
                if (instruments.contains(MyUtil.KEYS.PERCUSSION)) {
                    Instruments_IMAGE_percussion.setImageResource(R.drawable.percussion);
                    instruments.remove(MyUtil.KEYS.PERCUSSION);
                }
                else{
                    Instruments_IMAGE_percussion.setImageResource(R.drawable.percussion_chose);
                    instruments.add(MyUtil.KEYS.PERCUSSION);
                }
                break;
            case "Instruments_IMAGE_piano":
                if (instruments.contains(MyUtil.KEYS.PIANO)) {
                    Instruments_IMAGE_piano.setImageResource(R.drawable.piano);
                    instruments.remove(MyUtil.KEYS.PIANO);
                }
                else{
                    Instruments_IMAGE_piano.setImageResource(R.drawable.piano_chose);
                    instruments.add(MyUtil.KEYS.PIANO);
                }
                break;
            case "Instruments_IMAGE_saxophone":
                if (instruments.contains(MyUtil.KEYS.SAXOPHONE)) {
                    Instruments_IMAGE_saxophone.setImageResource(R.drawable.saxophone);
                    instruments.remove(MyUtil.KEYS.SAXOPHONE);
                }
                else{
                    Instruments_IMAGE_saxophone.setImageResource(R.drawable.saxophone_chose);
                    instruments.add(MyUtil.KEYS.SAXOPHONE);
                }
                break;
        }
    }

    //set background photo
    private void glideBackground() {
        Glide
                .with(this)
                .load(R.drawable.background_image)
                .into(Instruments_IMAGE_backGround);
    }

    private void findView() {
        //background image
        Instruments_IMAGE_backGround = view.findViewById(R.id.Instruments_IMAGE_backGround);

        //ImageButton
        Instruments_IMAGE_electric_guitar = view.findViewById(R.id.Instruments_IMAGE_electric_guitar);
        Instruments_IMAGE_bass_guitar = view.findViewById(R.id.Instruments_IMAGE_bass_guitar);
        Instruments_IMAGE_drums = view.findViewById(R.id.Instruments_IMAGE_drums);
        Instruments_IMAGE_keyboard = view.findViewById(R.id.Instruments_IMAGE_keyboard);
        Instruments_IMAGE_singer = view.findViewById(R.id.Instruments_IMAGE_singer);
        Instruments_IMAGE_flute = view.findViewById(R.id.Instruments_IMAGE_flute);
        Instruments_IMAGE_dj = view.findViewById(R.id.Instruments_IMAGE_dj);
        Instruments_IMAGE_mandolin = view.findViewById(R.id.Instruments_IMAGE_mandolin);
        Instruments_IMAGE_violin = view.findViewById(R.id.Instruments_IMAGE_violin);
        Instruments_IMAGE_percussion = view.findViewById(R.id.Instruments_IMAGE_percussion);
        Instruments_IMAGE_piano = view.findViewById(R.id.Instruments_IMAGE_piano);
        Instruments_IMAGE_saxophone = view.findViewById(R.id.Instruments_IMAGE_saxophone);

        //navigation images
        Instruments_IMAGE_rightArrow = view.findViewById(R.id.Instruments_IMAGE_rightArrow);
        Instruments_IMAGE_leftArrow = view.findViewById(R.id.Instruments_IMAGE_leftArrow);

        Instruments_TF_other = view.findViewById(R.id.Instruments_TF_other);
    }
}