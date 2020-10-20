package com.bawp.bandme.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import com.bawp.bandme.call_back_interface.CallBack_FireBaseEmailChecker;
import com.bawp.bandme.call_back_interface.CallBack_UpdateProfilePhoto;
import com.bawp.bandme.model.BandMeProfile;
import com.bawp.bandme.call_back_interface.CallBack_FireBaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FireBaseMethods {

    public interface KEYS{
        //database User data
        String USER = "User";
        String UID = "uid";
        String PROFILE_PICTURE_STORAGE = "ProfileImages";
        String PROFILE_PICTURE_REAL_TIME = "imageUrl";
        String INSTRUMENTS = "instruments";
        String CONTACTS = "Contacts";
        String PARTICIPANT = "participant";
        String AGE = "age";
        String DISTRICT = "district";
        String FIRST_NAME = "firstName";
        String LAST_NAME = "lastName";
        String SELF_INFO = "selfInfo";


        String CHAT = "chat";
        String KEY = "Key";
        String OTHER = "Other";
        //database branches
        String DISTRICTS = "Districts";
        String ALL_INSTRUMENTS = "All instruments";
        String DISTRICTS_AND_INSTRUMENTS = "Districts and instruments";

    }
    private static FireBaseMethods instance;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    //callbacks
    private CallBack_FireBaseDatabase callBack_fireBaseDatabase;
    private CallBack_FireBaseEmailChecker callBack_fireBaseEmailChecker;
    private CallBack_UpdateProfilePhoto callBack_updateProfilePhoto;

    public FireBaseMethods() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference(KEYS.USER);
        this.storage = FirebaseStorage.getInstance();
        this.storageRef = storage.getReference(KEYS.PROFILE_PICTURE_STORAGE);
    }

    public static FireBaseMethods getInstance(){
        return instance;
    }


    public static FireBaseMethods initHelper(){
        if (instance == null){
            instance = new FireBaseMethods();
        }
        return instance;
    }

    //Firebase Authentication

    //check if the email is already in use
    //using it in the registration process
     public void checkIfEmailExist(String email){

        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>()
        {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods() != null){
                    if (task.getResult().getSignInMethods().size() == 0){
                        // email not existed
                        Log.d("jjjj", "checkIfEmailExist: Not");
                        //return false, the registration can continue
                        callBack_fireBaseEmailChecker.isEmailExist(false);
                    }else {
                        //email exist
                        Log.d("jjjj", "checkIfEmailExist: yes");
                        //return true it means the email exist, so the user will have
                        //to use a different email
                        callBack_fireBaseEmailChecker.isEmailExist(true);
                    }
                }
            }
        });
    }

    //Firebase Authentication

    //create new user under Users/UID
    public void createNewAccount(Activity activity, String email, String password, final BandMeProfile bandMeProfile){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("jjjj", "createUserWithEmail:success");
                            String uid = mAuth.getUid();

                            //add user info to real time database
                            addUserClassInfo(bandMeProfile, uid);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("jjjj", "createUserWithEmail:failure", task.getException());

                        }

                    }
                });
    }

    //add user info under User/UID
    public void addUserClassInfo(BandMeProfile bandMeProfile,String uid){
        bandMeProfile.setUid(uid);

        //add user info to -> database/user/userID
        myRef.child(uid).setValue(bandMeProfile);

        //add user info to -> database/districts/specific district/userID
        database.getReference().child(KEYS.DISTRICTS).child(bandMeProfile.getDistrict()).child(bandMeProfile.getUid()).child(KEYS.UID).setValue(bandMeProfile.getUid());



        //add user info to -> instrument/userID
        String [] instrumentsArray = MyUtil.Arrays.instruments;
        for (int i = 0; i < bandMeProfile.getInstruments().size(); i++) {
            for (int j = 0; j < instrumentsArray.length; j++) {
                if (bandMeProfile.getInstruments().get(i).equals(instrumentsArray[j]) && !instrumentsArray[j].equals("All") && !instrumentsArray[j].equals("Other")) {
                    database.getReference().child(KEYS.ALL_INSTRUMENTS).child(instrumentsArray[j]).child(bandMeProfile.getUid()).child(KEYS.UID).setValue(bandMeProfile.getUid());

                    database.getReference().child(KEYS.DISTRICTS_AND_INSTRUMENTS).child(bandMeProfile.getDistrict()).child(instrumentsArray[j]).child(bandMeProfile.getUid()).child(KEYS.UID).setValue(bandMeProfile.getUid());

                    break;
                }
                if (j == instrumentsArray.length -1) {
                    database.getReference().child(KEYS.ALL_INSTRUMENTS).child(KEYS.OTHER).child(bandMeProfile.getUid()).child(KEYS.UID).setValue(bandMeProfile.getUid());
                    database.getReference().child(KEYS.DISTRICTS_AND_INSTRUMENTS).child(bandMeProfile.getDistrict()).child(KEYS.OTHER).child(bandMeProfile.getUid()).child(KEYS.UID).setValue(bandMeProfile.getUid());

                }
            }
        }
        callBack_fireBaseDatabase.finishedAccountCreation();
    }

    //send password reset to email
    public void sendResetPasswordEmail(String email){
        mAuth.sendPasswordResetEmail(email);
    }


    //upload the profile picture under ProfileImages/UID
    public void uploadProfilePicture(final Uri uri, Activity activity){

        final StorageReference riversRef = storageRef.child(mAuth.getCurrentUser().getUid());
        UploadTask uploadTask = riversRef.putFile(uri);

        //create dialog that show the percentage of the download
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Uploading Image...");
        progressDialog.show();

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            //add the image link to his info in the database to later upload it for profile picture
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Log.d("jjjj", "" + uri.toString());
                        //update the url in the user info Users/UID/imageUrl
                        myRef.child(mAuth.getCurrentUser().getUid()).child(KEYS.PROFILE_PICTURE_REAL_TIME).setValue(uri.toString());
                        callBack_updateProfilePhoto.updateProfilePicture(uri);

                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                //calculate percentage ((100*bytes transferred)/ total bytes)
                long progressPercent = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Percentage: " + (int)progressPercent + "%");
            }
        });
    }

    //getters and setters

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public FireBaseMethods setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
        return this;
    }

    public DatabaseReference getMyRef() {
        return myRef;
    }

    public FireBaseMethods setMyRef(DatabaseReference myRef) {
        this.myRef = myRef;
        return this;
    }

    //FireBase Storage
    public FirebaseStorage getStorage() {
        return storage;
    }

    public FireBaseMethods setStorage(FirebaseStorage storage) {
        this.storage = storage;
        return this;
    }

    public StorageReference getStorageRef() {
        return storageRef;
    }

    public FireBaseMethods setStorageRef(StorageReference storageRef) {
        this.storageRef = storageRef;
        return this;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    //callbacks getter and setters
    public CallBack_FireBaseDatabase getCallBack_fireBaseDatabase() {
        return callBack_fireBaseDatabase;
    }

    public FireBaseMethods setCallBack_fireBaseDatabase(CallBack_FireBaseDatabase callBack_fireBaseDatabase) {
        this.callBack_fireBaseDatabase = callBack_fireBaseDatabase;
        return this;
    }

    public CallBack_FireBaseEmailChecker getCallBack_fireBaseEmailChecker() {
        return callBack_fireBaseEmailChecker;
    }

    public FireBaseMethods setCallBack_fireBaseEmailChecker(CallBack_FireBaseEmailChecker callBack_fireBaseEmailChecker) {
        this.callBack_fireBaseEmailChecker = callBack_fireBaseEmailChecker;
        return this;
    }

    public CallBack_UpdateProfilePhoto getCallBack_updateProfilePhoto() {
        return callBack_updateProfilePhoto;
    }

    public FireBaseMethods setCallBack_updateProfilePhoto(CallBack_UpdateProfilePhoto callBack_updateProfilePhoto) {
        this.callBack_updateProfilePhoto = callBack_updateProfilePhoto;
        return this;
    }
}
