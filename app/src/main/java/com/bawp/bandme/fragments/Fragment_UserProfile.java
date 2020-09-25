package com.bawp.bandme.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.bawp.bandme.model.BandMeProfile;
import com.bawp.bandme.R;
import com.bawp.bandme.util.FireBaseMethods;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;

public class Fragment_UserProfile extends Fragment {
    protected View view;

    private ImageView UserProfile_IMAGE_background;
    private ImageView UserProfile_IMAGE_profilePicture;

    private TextView UserProfile_LBL_firstName;
    private TextView UserProfile_LBL_lastName;
    private TextView UserProfile_LBL_instruments;
    private TextView UserProfile_LBL_age;
    private TextView UserProfile_LBL_info;

    private final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private final int REQUEST_CODE_SELECT_IMAGE = 1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (view == null){
            view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        }
        findView();
        glideBackground();
        fetchUserInfoFromFirebase();
        UserProfile_IMAGE_profilePicture.setOnClickListener(userProfileClickListener);
        return view;
    }

    private void fetchUserInfoFromFirebase() {

        //MyBand user info fetch
        FireBaseMethods.getInstance().getMyRef().child(Objects.requireNonNull(FireBaseMethods.getInstance().getmAuth().getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        BandMeProfile bandMeProfile = snapshot.getValue(BandMeProfile.class);

                        if (bandMeProfile != null){
                            Log.d("jjjj", "onDataChange: " + bandMeProfile.getFirstName());
                            updateUserInfo(bandMeProfile);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //Profile Picture Fetch
        FireBaseMethods.getInstance().getStorageRef().child(Objects.requireNonNull(FireBaseMethods.getInstance().getmAuth().getCurrentUser()).getUid()).
                getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                setProfilePictureFromFireBaseLink(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("jjjj", "profile picture fail");
            }
        });
    }

    private void updateUserInfo(BandMeProfile bandMeProfile) {
        UserProfile_LBL_firstName.setText(bandMeProfile.getFirstName());
        UserProfile_LBL_lastName.setText(bandMeProfile.getLastName());
        UserProfile_LBL_age.setText(bandMeProfile.getAge());
        UserProfile_LBL_info.setText(bandMeProfile.getSelfInfo());

        StringBuilder instruments = new StringBuilder();
        for (int i = 0; i < bandMeProfile.getInstruments().size(); i++) {
            instruments.append(bandMeProfile.getInstruments().get(i)).append(", ");
        }
        //remove the last ','
        String fixedString = instruments.substring(0, instruments.length()-2);
        UserProfile_LBL_instruments.setText(fixedString);
    }

    private void findView() {
        //images
        UserProfile_IMAGE_background = view.findViewById(R.id.UserProfile_IMAGE_background);
        UserProfile_IMAGE_profilePicture = view.findViewById(R.id.UserProfile_IMAGE_profilePicture);

        //textViews
        UserProfile_LBL_firstName = view.findViewById(R.id.UserProfile_LBL_firstName);
        UserProfile_LBL_lastName = view.findViewById(R.id.UserProfile_LBL_lastName);
        UserProfile_LBL_instruments = view.findViewById(R.id.UserProfile_LBL_instruments);
        UserProfile_LBL_age = view.findViewById(R.id.UserProfile_LBL_age);
        UserProfile_LBL_info = view.findViewById(R.id.UserProfile_LBL_info);

    }

    public static Fragment_UserProfile newInstance(){
        return new Fragment_UserProfile();
    }

    /*
    public void setActivityCallBack(CallBack_RegistrationLoginInfo callBack_registrationLoginInfo){
        this.callBack_registrationLoginInfo = callBack_registrationLoginInfo;
    }
     */

    private void glideBackground() {
        Glide
                .with(this)
                .load(R.drawable.background_image)
                .into(UserProfile_IMAGE_background);
    }

    private void setProfilePictureFromFireBaseLink(Uri uri){
        Glide.with(this)
                .load(uri)
                .into(UserProfile_IMAGE_profilePicture);
    }

    View.OnClickListener userProfileClickListener = new View.OnClickListener() {

        //Check permission to access the library
        @Override
        public void onClick(View view) {
            if(ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_STORAGE_PERMISSION);
            }
            else {
                selectImage();
            }
        }
    };

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == -1 && data.getData() != null){
            Uri uri = data.getData();
            UserProfile_IMAGE_profilePicture.setImageURI(uri);
            FireBaseMethods.getInstance().uploadProfilePicture(uri, getActivity());
        }
    }


}