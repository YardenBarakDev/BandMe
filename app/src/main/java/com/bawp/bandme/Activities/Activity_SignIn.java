package com.bawp.bandme.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bawp.bandme.R;
import com.bawp.bandme.util.FireBaseMethods;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;


public class Activity_SignIn extends AppCompatActivity {

    private TextInputLayout SignIn_ET_email;
    private TextInputLayout SignIn_ET_password;

    private TextView SignIn_LBL_forgot_password;
    private TextView SignIn_LBL_firstTime_register;

    private MaterialButton SignIn_BTN_login;

    private ImageView signIn_IMAGE_background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        findViews();
        addListeners();
        glideBackground();
    }



    View.OnClickListener SignInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SignInActivityClick(view);
        }
    };

    private void SignInActivityClick(View view) {
        switch (((String) view.getTag())) {
            case "SignIn_BTN_login":

                break;
            case "SignIn_LBL_firstTime_register":
                Intent intent = new Intent(Activity_SignIn.this, Activity_Register.class);
                startActivity(intent);
                break;

            case "SignIn_LBL_forgot_password":
                sendPassword();
                break;
        }
    }

    private void sendPassword() {
        //set dialog
        Log.d("emailsend", "sendPassword: ");
        final EditText resetPassword = new EditText(Activity_SignIn.this);
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(Activity_SignIn.this);

        //custom the dialog
        passwordResetDialog.setTitle("Reset Password");
        passwordResetDialog.setMessage("Enter your email to reset the password");
        passwordResetDialog.setView(resetPassword);

        //set Listener
        passwordResetDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FireBaseMethods.getInstance().getmAuth().sendPasswordResetEmail(resetPassword.getText().toString().trim());
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();

    }


    private void addListeners() {
        SignIn_BTN_login.setOnClickListener(SignInClickListener);
        SignIn_LBL_firstTime_register.setOnClickListener(SignInClickListener);
        SignIn_LBL_forgot_password.setOnClickListener(SignInClickListener);

    }

    private void findViews() {

        SignIn_ET_email = findViewById(R.id.SignIn_ET_email);
        SignIn_ET_password = findViewById(R.id.SignIn_ET_password);

        SignIn_LBL_forgot_password = findViewById(R.id.SignIn_LBL_forgot_password);
        SignIn_LBL_firstTime_register = findViewById(R.id.SignIn_LBL_firstTime_register);

        SignIn_BTN_login = findViewById(R.id.SignIn_BTN_login);

        signIn_IMAGE_background = findViewById(R.id.signIn_IMAGE_background);
    }

    private void glideBackground() {
        Glide
                .with(this)
                .load(R.drawable.background_image)
                .into(signIn_IMAGE_background);
    }

    @Override
    public void onStart() {
        super.onStart();
        //for sign out
        //FireBaseMethods.getInstance().getmAuth().signOut();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (FireBaseMethods.getInstance().getmAuth().getCurrentUser() != null){
            String email = FireBaseMethods.getInstance().getmAuth().getCurrentUser().getEmail();
            Log.d("jjjj", "onStart: "+ email);
            Intent intent = new Intent(Activity_SignIn.this, Activity_Main.class);
            startActivity(intent);
            finish();
        }
    }
}