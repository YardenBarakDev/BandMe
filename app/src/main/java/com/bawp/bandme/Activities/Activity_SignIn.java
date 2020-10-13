package com.bawp.bandme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bawp.bandme.R;
import com.bawp.bandme.util.FireBaseMethods;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

public class Activity_SignIn extends AppCompatActivity {

    private TextInputEditText SignIn_LBL_email;
    private TextInputEditText SignIn_LBL_password;
    private TextView SignIn_LBL_forgot_password;
    private TextView SignIn_LBL_firstTime_register;
    private TextView SignIn_LBL_errorText;

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
                checkCredentials();
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

    private void checkCredentials() {

        if (SignIn_LBL_email.getText() != null && !SignIn_LBL_email.getText().toString().isEmpty() &&
                SignIn_LBL_password.getText() != null && !SignIn_LBL_password.getText().toString().isEmpty()){

            //validate user input, check if the user exist and if the credentials are valid
            FireBaseMethods.getInstance().getmAuth().signInWithEmailAndPassword(
                     SignIn_LBL_email.getText().toString(),
                     SignIn_LBL_password.getText().toString())

                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(Activity_SignIn.this, Activity_Main.class);
                                startActivity(intent);
                                finish();
                            } else {
                                //Sign in failed will alert the user the credentials are not valid
                                SignIn_LBL_errorText.setText(R.string.SignIn_ET_errorText);
                            }
                        }
                    });
        }
    }

    private void sendPassword() {
        //set dialog
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


    private void glideBackground() {
        Glide
                .with(this)
                .load(R.drawable.background_image)
                .into(signIn_IMAGE_background);
    }

    private void findViews() {

        SignIn_LBL_email = findViewById(R.id.SignIn_LBL_email);

        SignIn_LBL_password = findViewById(R.id.SignIn_LBL_password);

        SignIn_LBL_forgot_password = findViewById(R.id.SignIn_LBL_forgot_password);
        SignIn_LBL_firstTime_register = findViewById(R.id.SignIn_LBL_firstTime_register);

        SignIn_BTN_login = findViewById(R.id.SignIn_BTN_login);
        SignIn_LBL_errorText = findViewById(R.id.SignIn_LBL_errorText);
        signIn_IMAGE_background = findViewById(R.id.signIn_IMAGE_background);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (not null) and update UI accordingly.
        //if the user is connected this activity will close and start the Main activity
        if (FireBaseMethods.getInstance().getmAuth().getCurrentUser() != null){
            Intent intent = new Intent(Activity_SignIn.this, Activity_Main.class);
            startActivity(intent);
            finish();
        }
    }
}