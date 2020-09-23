package com.bawp.bandme.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bawp.bandme.BandMeProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FireBaseMethods {

    public interface KEYS{
        //data base branch for users
        String USER = "User";
    }
    private static FireBaseMethods instance;
    private FirebaseAuth mAuth;
    private boolean isExist = false;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public FireBaseMethods() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(KEYS.USER);
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

    public void createNewAccount(Activity activity, BandMeProfile bandMeProfile){

        mAuth.createUserWithEmailAndPassword(bandMeProfile.getEmail(), bandMeProfile.getPassword())
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("jjjj", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("jjjj", "createUserWithEmail:failure", task.getException());

                        }

                        // ...
                    }
                });

    }

    public void sendResetPasswordEmail(String email){
        mAuth.sendPasswordResetEmail(email);

    }
    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public FireBaseMethods setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
        return this;
    }
}
