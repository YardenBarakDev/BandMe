package com.bawp.bandme.fragments;

import android.Manifest;
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

    private Toolbar UserProfile_Toolbar;
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
        UserProfile_IMAGE_profilePicture.setOnClickListener(userProfileClickListener);
        return view;
    }

    private void fetchUserInfoFromFirebase() {

        //MyBand user fetch info



        //check for info in directory /Users/UID
        FireBaseMethods.getInstance().getMyRef().child(Objects.requireNonNull(FireBaseMethods.getInstance().getmAuth().getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //get the bandMe user object from fire base
                        BandMeProfile bandMeProfile = snapshot.getValue(BandMeProfile.class);

                        //if it is not null update the user info
                        if (bandMeProfile != null){
                            Log.d("jjjj", "onDataChange: " + bandMeProfile.getFirstName());
                            updateUserInfo(bandMeProfile);
                        }
                    }

                    //in case the server is unable to bring the data
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Connection lost", Toast.LENGTH_LONG).show();
                    }
                });

        //Profile Picture Fetch
        FireBaseMethods.getInstance().getStorageRef().child(Objects.requireNonNull(FireBaseMethods.getInstance().getmAuth().getCurrentUser()).getUid()).
                getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                //find picture in storage, upload it to the user profile from link using glide
                setProfilePictureFromFireBaseLink(uri);
                Log.d("jjjj", "onSuccess: " + uri.getPath());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("jjjj", "profile picture fail");
                //if can't find picture, set profile picture as drawable
                UserProfile_IMAGE_profilePicture.setImageResource(R.drawable.profile);
            }
        });
    }

    private void updateUserInfo(BandMeProfile bandMeProfile) {
        //set user data (first name, last name etc) in the relevant places
        UserProfile_LBL_firstName.setText(bandMeProfile.getFirstName());
        UserProfile_LBL_lastName.setText(bandMeProfile.getLastName());
        UserProfile_LBL_age.setText(bandMeProfile.getAge());
        UserProfile_LBL_info.setText(bandMeProfile.getSelfInfo());

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
       /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
*/
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
            UserProfile_IMAGE_profilePicture.setImageURI(uri);
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

        //Toolbar
        UserProfile_Toolbar = view.findViewById(R.id.UserProfile_Toolbar);
        if ((Activity_Main)getActivity() != null){
            Log.d("jjjj", "findView: not null");
            ((AppCompatActivity)getActivity()).setSupportActionBar(UserProfile_Toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
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
        return super.onOptionsItemSelected(item);
    }

    //inflate toolbar
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.userprofile_toolbar, menu);

    }

}