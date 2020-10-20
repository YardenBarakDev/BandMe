package com.bawp.bandme.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bawp.bandme.Activities.Activity_Main;
import com.bawp.bandme.Activities.Activity_SignIn;
import com.bawp.bandme.Activities.Activity_UpdateInfo;
import com.bawp.bandme.call_back_interface.CallBack_UpdateProfilePhoto;
import com.bawp.bandme.model.BandMeProfile;
import com.bawp.bandme.R;
import com.bawp.bandme.util.FireBaseMethods;
import com.bawp.bandme.util.MyUtil;
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
    private TextView UserProfile_LBL_district;
    private Toolbar UserProfile_Toolbar;
    private BandMeProfile bandMeProfile;
    private final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private final int REQUEST_CODE_SELECT_IMAGE = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (view == null){
            view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        }
        //make the options appear in your Toolbar
        setHasOptionsMenu(true);

        findView();
        glideBackground();
        fetchUserInfoFromFirebase();
        FireBaseMethods.getInstance().setCallBack_updateProfilePhoto(callBack_updateProfilePhoto);
        UserProfile_IMAGE_profilePicture.setOnClickListener(userProfileClickListener);
        return view;
    }

    private void fetchUserInfoFromFirebase() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        //MyBand user fetch info
        //check for info in directory /Users/UID
        FireBaseMethods.getInstance().getMyRef().child(Objects.requireNonNull(FireBaseMethods.getInstance().getmAuth().getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //get the bandMe user object from fire base
                        BandMeProfile profile = snapshot.getValue(BandMeProfile.class);

                        //if it is not null update the user info
                        if (profile != null){
                            Log.d("jjjj", "onDataChange: " + profile.getFirstName());
                            bandMeProfile = profile;
                            updateUserInfo();
                        }
                        progressDialog.dismiss();
                    }

                    //in case the server is unable to bring the data
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Connection lost", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
        profilePictureFetch();
    }

    private void profilePictureFetch(){
        //Profile Picture Fetch
        FireBaseMethods.getInstance().getStorageRef().child(Objects.requireNonNull(FireBaseMethods.getInstance().getmAuth().getCurrentUser()).getUid()).
                getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                //find picture in storage, upload it to the user profile from link using glide
                setProfilePictureFromFireBaseLink(uri); }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //if can't find picture, set profile picture as drawable
                UserProfile_IMAGE_profilePicture.setImageResource(R.drawable.profile);
            }
        });
    }
    private void updateUserInfo() {
        //set user data (first name, last name etc) in the relevant places
        UserProfile_LBL_firstName.setText(bandMeProfile.getFirstName());
        UserProfile_LBL_lastName.setText(bandMeProfile.getLastName());
        UserProfile_LBL_age.setText(bandMeProfile.getAge());
        UserProfile_LBL_info.setText(bandMeProfile.getSelfInfo());
        UserProfile_LBL_district.setText(bandMeProfile.getDistrict());

        //take care of the instruments arrayList items
        //organize all the items in a string
        if (bandMeProfile.getInstruments().size() > 0){
            StringBuilder instruments = new StringBuilder();
            for (int i = 0; i < bandMeProfile.getInstruments().size(); i++) {
                if (i == bandMeProfile.getInstruments().size() -1)
                    instruments.append(bandMeProfile.getInstruments().get(i));
                else
                    instruments.append(bandMeProfile.getInstruments().get(i)).append(", ");
            }
            UserProfile_LBL_instruments.setText(instruments);
        }
        else{
            UserProfile_LBL_instruments.setText("");
        }
    }

    public static Fragment_UserProfile newInstance(){
        return new Fragment_UserProfile();
    }

    /*
    public void setActivityCallBack(CallBack_RegistrationLoginInfo callBack_registrationLoginInfo){
        this.callBack_registrationLoginInfo = callBack_registrationLoginInfo;
    }
     */

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
       Intent intent = new Intent();
       intent.setType("image/*");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);


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


    //once the user choose a profile picture this method will execute and will upload the photo to firebase storage
    //and update the image url in user info database.
    //also change the user profile picture instantly
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == -1 && data.getData() != null){
            Uri uri = data.getData();
            FireBaseMethods.getInstance().uploadProfilePicture(uri, getActivity());
        }
    }

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.UserProfile_Toolbar_SignOut) {
            //sign out from the app
            FireBaseMethods.getInstance().getmAuth().signOut();

            //close this activity and open the sign in activity
            Intent intent = new Intent(getActivity(), Activity_SignIn.class);
            startActivity(intent);
            if (getActivity() != null)
                getActivity().finish();
        }
        if (item.getItemId() == R.id.UserProfile_Toolbar_Update_Profile){
            Intent intent = new Intent(getActivity(), Activity_UpdateInfo.class);
            intent.putExtra(MyUtil.KEYS.BAND_ME_PROFILE, bandMeProfile);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
        UserProfile_LBL_district = view.findViewById(R.id.UserProfile_LBL_district);

        //Toolbar
        UserProfile_Toolbar = view.findViewById(R.id.UserProfile_Toolbar);
        if ((Activity_Main)getActivity() != null){
            Log.d("jjjj", "findView: not null");
            ((AppCompatActivity)getActivity()).setSupportActionBar(UserProfile_Toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    //inflate toolbar
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.userprofile_toolbar, menu);

    }

    @Override
    public void onResume() {
        super.onResume();
        fetchUserInfoFromFirebase();
    }
    CallBack_UpdateProfilePhoto callBack_updateProfilePhoto = new CallBack_UpdateProfilePhoto() {
        @Override
        public void updateProfilePicture(Uri uri) {
            profilePictureFetch();
        }
    };

}