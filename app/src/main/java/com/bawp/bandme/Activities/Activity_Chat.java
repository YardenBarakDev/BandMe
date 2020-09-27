package com.bawp.bandme.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawp.bandme.R;

public class Activity_Chat extends AppCompatActivity {

    private ImageView Chat_IMAGE_profile;
    private TextView Chat_LBL_name;

    private RecyclerView Chat_RecyclerView_messages;

    private ImageButton Chat_BTN_send;
    private EditText Chat_LBL_message;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        findViews();
        Chat_BTN_send.setOnClickListener(ChatOnClickListener);
    }

    View.OnClickListener ChatOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //get text from EditText
            String message = Chat_LBL_message.getText().toString();

            if (message != null && !message.trim().isEmpty()){
                //if the message is not empty send the message
                sendMessage();
            }


        }
    };

    private void sendMessage() {


    }

    private void findViews() {

        Chat_IMAGE_profile = findViewById(R.id.Chat_IMAGE_profile);
        Chat_LBL_name = findViewById(R.id.Chat_LBL_name);
        Chat_RecyclerView_messages = findViewById(R.id.Chat_RecyclerView_messages);
        Chat_BTN_send = findViewById(R.id.Chat_BTN_send);
        Chat_LBL_message = findViewById(R.id.Chat_LBL_message);
       // List_IMAGEBTN_sendMessage = findViewById(R.id.List_IMAGEBTN_sendMessage);

    }
}