package com.bawp.bandme.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.bawp.bandme.R;
import com.bawp.bandme.model.BandMeProfile;
import com.bawp.bandme.util.FireBaseMethods;
import com.bawp.bandme.util.MyUtil;
import com.bawp.bandme.util.ValidateUserAccountInfo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;

public class Activity_UpdateInfo extends AppCompatActivity {

    private MaterialButton Update_BTN_update;
    private TextInputLayout Update_LAY_district;
    private AppCompatAutoCompleteTextView Update_Spinner_district;
    private TextInputLayout Update_TF_age;
    private TextInputEditText Update_LBL_age;
    private TextInputLayout Update_TF_firstName;
    private TextInputEditText Update_LBL_firstName;
    private TextInputLayout Update_TF_lastName;
    private TextInputEditText Update_LBL_lastName;
    private TextInputLayout Update_TF_info;
    private TextInputEditText Update_LBL_info;
    private TextInputEditText Update_LBL_Other_instrument;
    private ImageView Update_IMAGE_electric_guitar;
    private ImageView Update_IMAGE_bass_guitar;
    private ImageView Update_IMAGE_drums;
    private ImageView Update_IMAGE_singer;
    private ImageView Update_IMAGE_dj;
    private ImageView Update_IMAGE_mandolin;
    private ImageView Update_IMAGE_violin;
    private ImageView Update_IMAGE_percussion;
    private ImageView Update_IMAGE_keyboard;
    private ImageView Update_IMAGE_flute;
    private ImageView Update_IMAGE_piano;
    private ImageView Update_IMAGE_saxophone;

    private ArrayList<String> instruments;
    private BandMeProfile bandMeProfile;
    private ValidateUserAccountInfo validateUserAccountInfo;
    private String userDistrict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        instruments = new ArrayList<>();
        findAllViews();
        setListeners();
        bandMeProfile = (BandMeProfile)getIntent().getSerializableExtra(MyUtil.KEYS.BAND_ME_PROFILE);
        validateUserAccountInfo = new ValidateUserAccountInfo();
        if (bandMeProfile != null)
            showUserCurrentInfo();
    }

    View.OnClickListener UpdateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getTag().equals("Update_BTN_update")){

                //add the "Other" instrument to instruments array if the user changed the value
                if (Update_LBL_Other_instrument.getText() != null && !Update_LBL_Other_instrument.getText().toString().trim().isEmpty())
                    instruments.add(Update_LBL_Other_instrument.getText().toString().trim());
                updateUserInfoInFireBase();
            }
            else {
                instrumentsImages(view);
            }
        }
    };

    private void updateUserInfoInFireBase() {
        if (validInfo() && Update_LBL_firstName.getText() != null && Update_LBL_lastName.getText() != null && Update_LBL_age.getText() != null
                && Update_LBL_info.getText() != null){

            //if the user updated his first name, it will update it in the database
            if (!bandMeProfile.getFirstName().equals(Update_LBL_firstName.getText().toString()))
                FireBaseMethods.getInstance().getMyRef().child(bandMeProfile.getUid()).child(FireBaseMethods.KEYS.FIRST_NAME).setValue(Update_LBL_firstName.getText().toString());

            //if the user updated his last name, it will update it in the database
            if (!bandMeProfile.getLastName().equals(Update_LBL_lastName.getText().toString()))
                FireBaseMethods.getInstance().getMyRef().child(bandMeProfile.getUid()).child(FireBaseMethods.KEYS.LAST_NAME).setValue(Update_LBL_lastName.getText().toString());

            //if the user updated his info, it will update it in the database
            if (!bandMeProfile.getSelfInfo().equals(Update_LBL_info.getText().toString()))
                FireBaseMethods.getInstance().getMyRef().child(bandMeProfile.getUid()).child(FireBaseMethods.KEYS.SELF_INFO).setValue(Update_LBL_info.getText().toString());

            //if the user updated his age, it will update it in the database
            if (!bandMeProfile.getAge().equals(Update_LBL_age.getText().toString()))
                FireBaseMethods.getInstance().getMyRef().child(bandMeProfile.getUid()).child(FireBaseMethods.KEYS.AGE).setValue(Update_LBL_age.getText().toString());

            //if the user updated instruments or district, it will update it in the database
             if (!bandMeProfile.getDistrict().equals(userDistrict) || !bandMeProfile.getInstruments().containsAll(instruments) || !instruments.containsAll(bandMeProfile.getInstruments())){
                FireBaseMethods.getInstance().getMyRef().child(bandMeProfile.getUid()).child(FireBaseMethods.KEYS.DISTRICT).setValue(userDistrict);
                FireBaseMethods.getInstance().getMyRef().child(bandMeProfile.getUid()).child(FireBaseMethods.KEYS.INSTRUMENTS).setValue(instruments);
                updateDistrictInFireBase();
                updateInstrumentsInFireBase();
            }
            finish();
        }
    }

    private void updateDistrictInFireBase() {
        //remove old district record

        //database/districts/user district/uid
        FireBaseMethods.getInstance().getDatabase().getReference().child(FireBaseMethods.KEYS.DISTRICTS).child(bandMeProfile.getDistrict()).child(bandMeProfile.getUid()).removeValue();

        for (int i = 0; i < bandMeProfile.getInstruments().size(); i++) {

            //database/districts and instruments/user district/user instrument/uid
            FireBaseMethods.getInstance().getDatabase().getReference().child(FireBaseMethods.KEYS.DISTRICTS_AND_INSTRUMENTS)
                    .child(bandMeProfile.getDistrict()).child(bandMeProfile.getInstruments().get(i)).child(bandMeProfile.getUid()).removeValue();
        }

        //update current district
        FireBaseMethods.getInstance().getDatabase().getReference().child(FireBaseMethods.KEYS.DISTRICTS).child(userDistrict).child(bandMeProfile.getUid()).child(FireBaseMethods.KEYS.UID).setValue(bandMeProfile.getUid());
    }

    private void updateInstrumentsInFireBase() {

        ArrayList<String> allInstruments = new ArrayList<>();
        for (int i = 1; i < MyUtil.Arrays.instruments.length; i++) {
            allInstruments.add(MyUtil.Arrays.instruments[i]);
        }

        //update the database
        for (int i = 0; i < allInstruments.size(); i++) {

            //instruments array doesn't contain the instrument but the bandme account contain it. need to delete the record
            if (!instruments.contains(allInstruments.get(i)) && bandMeProfile.getInstruments().contains(allInstruments.get(i))){
                //database/all instruments/instrument/user uid
                FireBaseMethods.getInstance().getDatabase().getReference().child(FireBaseMethods.KEYS.ALL_INSTRUMENTS).child(allInstruments.get(i)).child(bandMeProfile.getUid()).removeValue();
            }

            //un updated bandme profile contain the instrument but instrument array list doesn't contain it. need to update the record
            if (instruments.contains(allInstruments.get(i))){
                //database/all instruments/instrument/user uid
                FireBaseMethods.getInstance().getDatabase().getReference().child(FireBaseMethods.KEYS.ALL_INSTRUMENTS).child(allInstruments.get(i)).child(bandMeProfile.getUid()).child(FireBaseMethods.KEYS.UID).setValue(bandMeProfile.getUid());

                //database/ districts and instruments/district/instrument/uid
                FireBaseMethods.getInstance().getDatabase().getReference().child(FireBaseMethods.KEYS.DISTRICTS_AND_INSTRUMENTS).child(userDistrict).child(allInstruments.get(i)).child(bandMeProfile.getUid()).child(FireBaseMethods.KEYS.UID).setValue(bandMeProfile.getUid());

            }
        }
        //add the "other" instrument
        for (int i = 0; i < instruments.size(); i++) {
            if (!allInstruments.contains(instruments.get(i))){
                FireBaseMethods.getInstance().getDatabase().getReference().child(FireBaseMethods.KEYS.ALL_INSTRUMENTS).child(FireBaseMethods.KEYS.OTHER).child(bandMeProfile.getUid()).child(FireBaseMethods.KEYS.UID).setValue(bandMeProfile.getUid());
                FireBaseMethods.getInstance().getDatabase().getReference().child(FireBaseMethods.KEYS.DISTRICTS_AND_INSTRUMENTS).child(userDistrict).child(FireBaseMethods.KEYS.OTHER).child(bandMeProfile.getUid()).child(FireBaseMethods.KEYS.UID).setValue(bandMeProfile.getUid());

            }
        }

    }

    //check if all the input is legal
    private boolean validInfo() {
        boolean checkFirstName = validFirstName();
        boolean checkLastName = validLastName();
        boolean checkDistrict = validDistrict();
        boolean checkAge = validAge();
        boolean checkInfo = validPrivateInfo();

        return checkFirstName && checkLastName && checkDistrict && checkAge && checkInfo;
    }

    private boolean validPrivateInfo() {
        if (Update_LBL_info.getText() == null)
            return false;
        if (Update_LBL_info.getText().toString().isEmpty()){
            Update_TF_info.setError("Please write a little bit about yourself");
            return false;
        }
        Update_TF_info.setError("");
        return true;
    }

    private boolean validAge() {
        if (Update_LBL_age.getText() == null)
            return false;
        if (Update_LBL_age.getText().toString().isEmpty()){
            Update_TF_age.setError("Mandatory field");
            return false;
        }
        Update_TF_age.setError("");
        return true;
    }

    private boolean validFirstName(){
        //check if null
        if (Update_LBL_firstName.getText() == null)
            return false;

            //check if the user typed more then 1 digit
        else if (Update_LBL_firstName.getText().toString().trim().isEmpty()){
            Update_TF_firstName.setError("Mandatory field");
            return false;
        }
        //check if the name length is longer then 2
        else if (Update_LBL_firstName.getText().toString().trim().length() < 2){
            Update_TF_firstName.setError("Must contain at least two letters");
            return false;
        }
        //check if user only entered letters
        else if (!validateUserAccountInfo.validateNames(Update_LBL_firstName.getText().toString().trim())){
            Update_TF_firstName.setError("Must contain only alphabetical letters");
            return false;
        }
        else{
            Update_TF_firstName.setError("");
            return true;
        }
    }

    private boolean validLastName() {
        //check if null
        if (Update_LBL_lastName.getText() == null)
            return false;

            //check if the user typed more then 1 digit
        else if (Update_LBL_lastName.getText().toString().trim().isEmpty()){
            Update_TF_lastName.setError("Mandatory field");
            return false;
        }
        //check if the name length is longer then 2
        else if (Update_LBL_lastName.getText().toString().trim().length() < 2){
            Update_TF_lastName.setError("Must contain at least two letters");
            return false;
        }
        //check if user only entered letters
        else if (!validateUserAccountInfo.validateNames(Update_LBL_lastName.getText().toString().trim())){
            Update_TF_lastName.setError("Must contain only alphabetical letters");
            return false;
        }
        else{
            Update_TF_lastName.setError("");
            return true;
        }
    }

    private boolean validDistrict() {
        if (userDistrict == null){
            Update_LAY_district.setError("Mandatory field");
            return false;
        }
        else{
            Update_LAY_district.setError("");
            return true;
        }
    }


    private void showUserCurrentInfo() {
        //init spinner array
        Update_Spinner_district.setAdapter(new ArrayAdapter<>(Activity_UpdateInfo.this, R.layout.spinner_layout, MyUtil.Arrays.districtsForSpinner));

        Update_LBL_firstName.setText(bandMeProfile.getFirstName());
        Update_LBL_lastName.setText(bandMeProfile.getLastName());
        Update_LBL_age.setText(bandMeProfile.getAge());
        Update_LBL_info.setText(bandMeProfile.getSelfInfo());

        //color the images according to the user instruments
        for (int i = 0; i < bandMeProfile.getInstruments().size(); i++) {
            switch (bandMeProfile.getInstruments().get(i)) {
                case MyUtil.KEYS.ELECTRIC_GUITAR:
                    Update_IMAGE_electric_guitar.setImageResource(R.drawable.electric_guitar_chose);
                    instruments.add(MyUtil.KEYS.ELECTRIC_GUITAR);
                    break;
                case MyUtil.KEYS.BASS_GUITAR:
                    Update_IMAGE_bass_guitar.setImageResource(R.drawable.bass_guitar_chose);
                    instruments.add(MyUtil.KEYS.BASS_GUITAR);
                    break;
                case MyUtil.KEYS.DRUMS:
                    Update_IMAGE_drums.setImageResource(R.drawable.drums_chose);
                    instruments.add(MyUtil.KEYS.DRUMS);
                    break;
                case MyUtil.KEYS.KEYBOARD:
                    Update_IMAGE_keyboard.setImageResource(R.drawable.keyboard_chose);
                    instruments.add(MyUtil.KEYS.KEYBOARD);
                    break;
                case MyUtil.KEYS.MICROPHONE:
                    Update_IMAGE_singer.setImageResource(R.drawable.microphone_chose);
                    instruments.add(MyUtil.KEYS.MICROPHONE);
                    break;
                case MyUtil.KEYS.FLUTE:
                    Update_IMAGE_flute.setImageResource(R.drawable.flute_chose);
                    instruments.add(MyUtil.KEYS.FLUTE);
                    break;
                case MyUtil.KEYS.DJ:
                    Update_IMAGE_dj.setImageResource(R.drawable.dj_chose);
                    instruments.add(MyUtil.KEYS.DJ);
                    break;
                case MyUtil.KEYS.MANDOLIN:
                    Update_IMAGE_mandolin.setImageResource(R.drawable.mandolin_chose);
                    instruments.add(MyUtil.KEYS.MANDOLIN);
                    break;
                case MyUtil.KEYS.VIOLIN:
                    Update_IMAGE_violin.setImageResource(R.drawable.violin_chose);
                    instruments.add(MyUtil.KEYS.VIOLIN);
                    break;
                case MyUtil.KEYS.PERCUSSION:
                    Update_IMAGE_percussion.setImageResource(R.drawable.percussion_chose);
                    instruments.add(MyUtil.KEYS.PERCUSSION);
                    break;
                case MyUtil.KEYS.PIANO:
                    Update_IMAGE_piano.setImageResource(R.drawable.piano_chose);
                    instruments.add(MyUtil.KEYS.PIANO);
                    break;
                case MyUtil.KEYS.SAXOPHONE:
                    Update_IMAGE_saxophone.setImageResource(R.drawable.saxophone_chose);
                    instruments.add(MyUtil.KEYS.SAXOPHONE);
                    break;
                default:
                    Update_LBL_Other_instrument.setText(bandMeProfile.getInstruments().get(i));
            }
        }
    }

    //check which instrument the user clicked and add it to the instruments arrayList and change to color photo
    //if the user click again remove from the arrayList and change to black and white photo
    private void instrumentsImages(View view) {

        switch ((String)view.getTag()){
            case "Update_IMAGE_electric_guitar":
                if (instruments.contains(MyUtil.KEYS.ELECTRIC_GUITAR)) {
                    Update_IMAGE_electric_guitar.setImageResource(R.drawable.electric_guitar);
                    instruments.remove(MyUtil.KEYS.ELECTRIC_GUITAR);
                }
                else{
                    Update_IMAGE_electric_guitar.setImageResource(R.drawable.electric_guitar_chose);
                    instruments.add(MyUtil.KEYS.ELECTRIC_GUITAR);
                }
                break;
            case "Update_IMAGE_bass_guitar":
                if (instruments.contains(MyUtil.KEYS.BASS_GUITAR)) {
                    Update_IMAGE_bass_guitar.setImageResource(R.drawable.bass_guitar);
                    instruments.remove(MyUtil.KEYS.BASS_GUITAR);
                }
                else{
                    Update_IMAGE_bass_guitar.setImageResource(R.drawable.bass_guitar_chose);
                    instruments.add(MyUtil.KEYS.BASS_GUITAR);
                }
                break;
            case "Update_IMAGE_drums":
                if (instruments.contains(MyUtil.KEYS.DRUMS)) {
                    Update_IMAGE_drums.setImageResource(R.drawable.drums);
                    instruments.remove(MyUtil.KEYS.DRUMS);
                }
                else{
                    Update_IMAGE_drums.setImageResource(R.drawable.drums_chose);
                    instruments.add(MyUtil.KEYS.DRUMS);
                }
                break;
            case "Update_IMAGE_keyboard":
                if (instruments.contains(MyUtil.KEYS.KEYBOARD)) {
                    Update_IMAGE_keyboard.setImageResource(R.drawable.keyboard);
                    instruments.remove(MyUtil.KEYS.KEYBOARD);
                }
                else{
                    Update_IMAGE_keyboard.setImageResource(R.drawable.keyboard_chose);
                    instruments.add(MyUtil.KEYS.KEYBOARD);
                }
                break;
            case "Update_IMAGE_singer":
                if (instruments.contains(MyUtil.KEYS.MICROPHONE)) {
                    Update_IMAGE_singer.setImageResource(R.drawable.microphone);
                    instruments.remove(MyUtil.KEYS.MICROPHONE);
                }
                else{
                    Update_IMAGE_singer.setImageResource(R.drawable.microphone_chose);
                    instruments.add(MyUtil.KEYS.MICROPHONE);
                }
                break;
            case "Update_IMAGE_flute":
                if (instruments.contains(MyUtil.KEYS.FLUTE)) {
                    Update_IMAGE_flute.setImageResource(R.drawable.flute);
                    instruments.remove(MyUtil.KEYS.FLUTE);
                }
                else{
                    Update_IMAGE_flute.setImageResource(R.drawable.flute_chose);
                    instruments.add(MyUtil.KEYS.FLUTE);
                }
                break;
            case "Update_IMAGE_dj":
                if (instruments.contains(MyUtil.KEYS.DJ)) {
                    Update_IMAGE_dj.setImageResource(R.drawable.dj);
                    instruments.remove(MyUtil.KEYS.DJ);
                }
                else{
                    Update_IMAGE_dj.setImageResource(R.drawable.dj_chose);
                    instruments.add(MyUtil.KEYS.DJ);
                }
                break;
            case "Update_IMAGE_mandolin":
                if (instruments.contains(MyUtil.KEYS.MANDOLIN)) {
                    Update_IMAGE_mandolin.setImageResource(R.drawable.mandolin);
                    instruments.remove(MyUtil.KEYS.MANDOLIN);
                }
                else{
                    Update_IMAGE_mandolin.setImageResource(R.drawable.mandolin_chose);
                    instruments.add(MyUtil.KEYS.MANDOLIN);
                }
                break;
            case "Update_IMAGE_violin":
                if (instruments.contains(MyUtil.KEYS.VIOLIN)) {
                    Update_IMAGE_violin.setImageResource(R.drawable.violin);
                    instruments.remove(MyUtil.KEYS.VIOLIN);
                }
                else{
                    Update_IMAGE_violin.setImageResource(R.drawable.violin_chose);
                    instruments.add(MyUtil.KEYS.VIOLIN);
                }
                break;
            case "Update_IMAGE_percussion":
                if (instruments.contains(MyUtil.KEYS.PERCUSSION)) {
                    Update_IMAGE_percussion.setImageResource(R.drawable.percussion);
                    instruments.remove(MyUtil.KEYS.PERCUSSION);
                }
                else{
                    Update_IMAGE_percussion.setImageResource(R.drawable.percussion_chose);
                    instruments.add(MyUtil.KEYS.PERCUSSION);
                }
                break;
            case "Update_IMAGE_piano":
                if (instruments.contains(MyUtil.KEYS.PIANO)) {
                    Update_IMAGE_piano.setImageResource(R.drawable.piano);
                    instruments.remove(MyUtil.KEYS.PIANO);
                }
                else{
                    Update_IMAGE_piano.setImageResource(R.drawable.piano_chose);
                    instruments.add(MyUtil.KEYS.PIANO);
                }
                break;
            case "Update_IMAGE_saxophone":
                if (instruments.contains(MyUtil.KEYS.SAXOPHONE)) {
                    Update_IMAGE_saxophone.setImageResource(R.drawable.saxophone);
                    instruments.remove(MyUtil.KEYS.SAXOPHONE);
                }
                else{
                    Update_IMAGE_saxophone.setImageResource(R.drawable.saxophone_chose);
                    instruments.add(MyUtil.KEYS.SAXOPHONE);
                }
                break;
        }
    }

    private void setListeners() {
        //instruments Images
        Update_IMAGE_electric_guitar.setOnClickListener(UpdateClickListener);
        Update_IMAGE_bass_guitar.setOnClickListener(UpdateClickListener);
        Update_IMAGE_drums.setOnClickListener(UpdateClickListener);
        Update_IMAGE_keyboard.setOnClickListener(UpdateClickListener);
        Update_IMAGE_singer.setOnClickListener(UpdateClickListener);
        Update_IMAGE_flute.setOnClickListener(UpdateClickListener);
        Update_IMAGE_dj.setOnClickListener(UpdateClickListener);
        Update_IMAGE_mandolin.setOnClickListener(UpdateClickListener);
        Update_IMAGE_violin.setOnClickListener(UpdateClickListener);
        Update_IMAGE_percussion.setOnClickListener(UpdateClickListener);
        Update_IMAGE_piano.setOnClickListener(UpdateClickListener);
        Update_IMAGE_saxophone.setOnClickListener(UpdateClickListener);

        //button
        Update_BTN_update.setOnClickListener(UpdateClickListener);

        //spinner
        Update_Spinner_district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userDistrict = (String) adapterView.getItemAtPosition(i);
            }
        });

    }

    private void findAllViews() {
        Update_BTN_update = findViewById(R.id.Update_BTN_update);
        Update_LAY_district = findViewById(R.id.Update_LAY_district);
        Update_Spinner_district = findViewById(R.id.Update_Spinner_district);
        Update_TF_age = findViewById(R.id.Update_TF_age);
        Update_LBL_age = findViewById(R.id.Update_LBL_age);
        Update_TF_firstName = findViewById(R.id.Update_TF_firstName);
        Update_LBL_firstName = findViewById(R.id.Update_LBL_firstName);
        Update_TF_lastName = findViewById(R.id.Update_TF_lastName);
        Update_LBL_lastName = findViewById(R.id.Update_LBL_lastName);
        Update_TF_info = findViewById(R.id.Update_TF_info);
        Update_LBL_info = findViewById(R.id.Update_LBL_info);
        Update_LBL_Other_instrument = findViewById(R.id.Update_LBL_Other_instrument);
        Update_IMAGE_electric_guitar = findViewById(R.id.Update_IMAGE_electric_guitar);
        Update_IMAGE_bass_guitar = findViewById(R.id.Update_IMAGE_bass_guitar);
        Update_IMAGE_drums = findViewById(R.id.Update_IMAGE_drums);
        Update_IMAGE_singer = findViewById(R.id.Update_IMAGE_singer);
        Update_IMAGE_dj = findViewById(R.id.Update_IMAGE_dj);
        Update_IMAGE_mandolin = findViewById(R.id.Update_IMAGE_mandolin);
        Update_IMAGE_violin = findViewById(R.id.Update_IMAGE_violin);
        Update_IMAGE_percussion = findViewById(R.id.Update_IMAGE_percussion);
        Update_IMAGE_keyboard = findViewById(R.id.Update_IMAGE_keyboard);
        Update_IMAGE_flute = findViewById(R.id.Update_IMAGE_flute);
        Update_IMAGE_piano = findViewById(R.id.Update_IMAGE_piano);
        Update_IMAGE_saxophone = findViewById(R.id.Update_IMAGE_saxophone);

    }
}