package com.bawp.bandme.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Aaaa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_aaaa);
    }
}

/*
package com.bawp.bandme.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import com.bawp.bandme.R;
import com.bawp.bandme.util.MySP;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class Activity_Register extends AppCompatActivity {

    private EditText Register_ET_userName;
    private EditText Register_ET_email;
    private EditText Register_ET_password;
    private EditText Register_ET_validatePassword;
    private EditText Register_ET_age;
    private EditText Register_ET_info;

    private Spinner Register_Spinner_District;

    private ImageButton Register_IBTN_electric_guitar;
    private ImageButton Register_IBTN_bass_guitar;
    private ImageButton Register_IBTN_drums;
    private ImageButton Register_IBTN_keyboard;
    private ImageButton Register_IBTN_microphone;
    private ImageButton Register_IBTN_flute;
    private ImageButton Register_IBTN_dj;
    private ImageButton Register_IBTN_mandolin;
    private ImageButton Register_IBTN_violin;
    private ImageButton Register_IBTN_percussion;

    private MaterialButton Register_BTN_createAnAccount;

    private ArrayList<String> instruments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__register);
        findViewSById();

        instruments = new ArrayList<>();
        Register_IBTN_electric_guitar.setOnClickListener(registerScreenButtonListener);
        Register_IBTN_bass_guitar.setOnClickListener(registerScreenButtonListener);
        Register_IBTN_drums.setOnClickListener(registerScreenButtonListener);
        Register_IBTN_keyboard.setOnClickListener(registerScreenButtonListener);
        Register_IBTN_microphone.setOnClickListener(registerScreenButtonListener);
        Register_IBTN_flute.setOnClickListener(registerScreenButtonListener);
        Register_IBTN_dj.setOnClickListener(registerScreenButtonListener);
        Register_IBTN_mandolin.setOnClickListener(registerScreenButtonListener);
        Register_IBTN_violin.setOnClickListener(registerScreenButtonListener);
        Register_IBTN_percussion.setOnClickListener(registerScreenButtonListener);
        Register_BTN_createAnAccount.setOnClickListener(registerScreenButtonListener);
    }

    private View.OnClickListener registerScreenButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            registerActivityButtons(view);
        }
    };

    private void registerActivityButtons(View view) {

        switch ((String)view.getTag()){
            case "Register_IBTN_electric_guitar":
                if (instruments.contains(MySP.KEYS.ELECTRIC_GUITAR)) {
                    Register_IBTN_electric_guitar.setImageResource(R.drawable.electric_guitar);
                    instruments.remove(MySP.KEYS.ELECTRIC_GUITAR);
                }
                else{
                    Register_IBTN_electric_guitar.setImageResource(R.drawable.electric_guitar_chose);
                    instruments.add(MySP.KEYS.ELECTRIC_GUITAR);
                }
                break;
            case "Register_IBTN_bass_guitar":
                if (instruments.contains(MySP.KEYS.BASS_GUITAR)) {
                    Register_IBTN_bass_guitar.setImageResource(R.drawable.bass_guitar);
                    instruments.remove(MySP.KEYS.BASS_GUITAR);
                }
                else{
                    Register_IBTN_bass_guitar.setImageResource(R.drawable.bass_guitar_chose);
                    instruments.add(MySP.KEYS.BASS_GUITAR);
                }
                break;
            case "Register_IBTN_drums":
                if (instruments.contains(MySP.KEYS.DRUMS)) {
                    Register_IBTN_drums.setImageResource(R.drawable.drums);
                    instruments.remove(MySP.KEYS.DRUMS);
                }
                else{
                    Register_IBTN_drums.setImageResource(R.drawable.drums_chose);
                    instruments.add(MySP.KEYS.DRUMS);
                }
                break;
            case "Register_IBTN_keyboard":
                if (instruments.contains(MySP.KEYS.KEYBOARD)) {
                    Register_IBTN_keyboard.setImageResource(R.drawable.keyboard);
                    instruments.remove(MySP.KEYS.KEYBOARD);
                }
                else{
                    Register_IBTN_keyboard.setImageResource(R.drawable.keyboard_chose);
                    instruments.add(MySP.KEYS.KEYBOARD);
                }
                break;
            case "Register_IBTN_microphone":
                if (instruments.contains(MySP.KEYS.MICROPHONE)) {
                    Register_IBTN_microphone.setImageResource(R.drawable.microphone);
                    instruments.remove(MySP.KEYS.MICROPHONE);
                }
                else{
                    Register_IBTN_microphone.setImageResource(R.drawable.microphone_chose);
                    instruments.add(MySP.KEYS.MICROPHONE);
                }
                break;
            case "Register_IBTN_flute":
                if (instruments.contains(MySP.KEYS.FLUTE)) {
                    Register_IBTN_flute.setImageResource(R.drawable.flute);
                    instruments.remove(MySP.KEYS.FLUTE);
                }
                else{
                    Register_IBTN_flute.setImageResource(R.drawable.flute_chose);
                    instruments.add(MySP.KEYS.FLUTE);
                }
                break;
            case "Register_IBTN_dj":
                if (instruments.contains(MySP.KEYS.DJ)) {
                    Register_IBTN_dj.setImageResource(R.drawable.dj);
                    instruments.remove(MySP.KEYS.DJ);
                }
                else{
                    Register_IBTN_dj.setImageResource(R.drawable.dj_chose);
                    instruments.add(MySP.KEYS.DJ);
                }
                break;
            case "Register_IBTN_mandolin":
                if (instruments.contains(MySP.KEYS.MANDOLIN)) {
                    Register_IBTN_mandolin.setImageResource(R.drawable.mandolin);
                    instruments.remove(MySP.KEYS.MANDOLIN);
                }
                else{
                    Register_IBTN_mandolin.setImageResource(R.drawable.mandolin_chose);
                    instruments.add(MySP.KEYS.MANDOLIN);
                }
                break;
            case "Register_IBTN_violin":
                if (instruments.contains(MySP.KEYS.VIOLIN)) {
                    Register_IBTN_violin.setImageResource(R.drawable.violin);
                    instruments.remove(MySP.KEYS.VIOLIN);
                }
                else{
                    Register_IBTN_violin.setImageResource(R.drawable.violin_chose);
                    instruments.add(MySP.KEYS.VIOLIN);
                }
                break;
            case "Register_IBTN_percussion":
                if (instruments.contains(MySP.KEYS.PERCUSSION)) {
                    Register_IBTN_percussion.setImageResource(R.drawable.percussion);
                    instruments.remove(MySP.KEYS.PERCUSSION);
                }
                else{
                    Register_IBTN_percussion.setImageResource(R.drawable.percussion_chose);
                    instruments.add(MySP.KEYS.PERCUSSION);
                }
                break;
            case "Register_BTN_createAnAccount":

                break;
        }
    }


    private void findViewSById() {
        //EditText
        Register_ET_userName = findViewById(R.id.Register_ET_userName);
        Register_ET_email = findViewById(R.id.Register_ET_email);
        Register_ET_password = findViewById(R.id.Register_ET_password);
        Register_ET_validatePassword = findViewById(R.id.Register_ET_validatePassword);
        Register_ET_age = findViewById(R.id.Register_ET_age);
        Register_ET_info = findViewById(R.id.Register_ET_info);

        //Spinner
        Register_Spinner_District = findViewById(R.id.Register_Spinner_District);

        //ImageButton
        Register_IBTN_electric_guitar = findViewById(R.id.Register_IBTN_electric_guitar);
        Register_IBTN_bass_guitar = findViewById(R.id.Register_IBTN_bass_guitar);
        Register_IBTN_drums = findViewById(R.id.Register_IBTN_drums);
        Register_IBTN_keyboard = findViewById(R.id.Register_IBTN_keyboard);
        Register_IBTN_microphone = findViewById(R.id.Register_IBTN_microphone);
        Register_IBTN_flute = findViewById(R.id.Register_IBTN_flute);
        Register_IBTN_dj = findViewById(R.id.Register_IBTN_dj);
        Register_IBTN_mandolin = findViewById(R.id.Register_IBTN_mandolin);
        Register_IBTN_violin = findViewById(R.id.Register_IBTN_violin);
        Register_IBTN_percussion = findViewById(R.id.Register_IBTN_percussion);

        //MaterialButton
        Register_BTN_createAnAccount = findViewById(R.id.Register_BTN_createAnAccount);
    }
}
 */