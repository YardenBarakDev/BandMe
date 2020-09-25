package com.bawp.bandme.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
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
        //data base branch for users
        String USER = "User";
        String PROFILE_PICTURE = "ProfileImages";
    }
    private static FireBaseMethods instance;
    private FirebaseAuth mAuth;
    private boolean isExist = false;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private CallBack_FireBaseDatabase callBack_fireBaseDatabase;

    public FireBaseMethods() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference(KEYS.USER);
        this.storage = FirebaseStorage.getInstance();
        this.storageRef = storage.getReference(KEYS.PROFILE_PICTURE);
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

    public void setActivityCallBack(CallBack_FireBaseDatabase callBack_fireBaseDatabase){
        this.callBack_fireBaseDatabase = callBack_fireBaseDatabase;
    }


    //Firebase Authentication
     public boolean checkIfEmailExist(String email){

        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>()
        {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods() != null){
                    if (task.getResult().getSignInMethods().size() == 0){
                        // email not existed
                        Log.d("jjjj", "onComplete: Not");
                        isExist = false;
                    }else {
                        //email exist
                        Log.d("jjjj", "onComplete: yes");
                        isExist = true;
                    }
                }
            }
        });
        return isExist;
    }

    //Firebase Authentication
    public void createNewAccount(Activity activity, String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("jjjj", "createUserWithEmail:success");
                            callBack_fireBaseDatabase.finishedAccountCreation();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("jjjj", "createUserWithEmail:failure", task.getException());

                        }

                    }
                });
    }

    public void addUserClassInfo(BandMeProfile bandMeProfile){
        myRef.child(mAuth.getCurrentUser().getUid()).setValue(bandMeProfile);
    }

    //send password reset to email
    public void sendResetPasswordEmail(String email){
        mAuth.sendPasswordResetEmail(email);
    }

    public void uploadProfilePicture(Uri uri, Activity activity){

        StorageReference riversRef = storageRef.child(mAuth.getCurrentUser().getUid());
        UploadTask uploadTask = riversRef.putFile(uri);

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Uploading Image...");
        progressDialog.show();

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
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
}
