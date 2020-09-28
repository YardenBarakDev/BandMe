package com.bawp.bandme.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bawp.bandme.R;
import com.bawp.bandme.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MyChatAdapter extends RecyclerView.Adapter<MyChatAdapter.ViewHolder>{


    private Context context;
    private ArrayList<Chat> chats;

    //fireBase
    private FirebaseUser fireBaseUser;


    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;


    public MyChatAdapter(Context context, ArrayList<Chat> chats){
        Log.d("jjjj", "consractur");
        this.context = context;
        this.chats = chats;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            Log.d("jjjj", "chat adapter: sender");
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_current_user, parent, false);
            return new ViewHolder(view);

        }else{
            Log.d("jjjj", "chat adapter: reciver");
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_other_user, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = chats.get(position);
        holder.show_message.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    Chat getItem(int position) {
        return chats.get(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView show_message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("jjjj", "chat adapter: get item typed");
        //get current user
        fireBaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //check which layout to choose
        //left -> other user messages
        //right -> current user messages
        if (chats.get(position).getSender().equals(fireBaseUser.getUid())){
            Log.d("jjjj", "message current user");
            return MSG_TYPE_RIGHT;
        }else
            Log.d("jjjj", "message current user");
        return MSG_TYPE_LEFT;
    }
}
