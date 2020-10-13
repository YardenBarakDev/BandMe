package com.bawp.bandme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bawp.bandme.R;
import com.bawp.bandme.model.BandMeContact;
import com.bawp.bandme.model.BandMeProfile;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private ArrayList<BandMeContact> contacts = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    private chatListClickListener chatListClickListener;

    // data is passed into the constructor
    public ChatListAdapter(Context context, ArrayList<BandMeContact> data) {
        this.mInflater = LayoutInflater.from(context);
        this.contacts = data;
        this.context = context;
    }

    public void setClickListener(ChatListAdapter.chatListClickListener chatListClickListener){
        this.chatListClickListener = chatListClickListener;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_musicians, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BandMeContact bandMeContact = contacts.get(position);

        //check if user uploaded a picture
        if (!bandMeContact.getImageURL().equals("")) {
            Glide.with(context)
                    .load(bandMeContact.getImageURL())
                    .into(holder.List_IMAGE_profilePicture);
        } else {
            holder.List_IMAGE_profilePicture.setImageResource(R.drawable.profile);
        }
        holder.List_LBL_firstName.setText(bandMeContact.getFirstName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return contacts.size();
    }


    // convenience method for getting data at click position
    BandMeContact getItem(int position) {
        return contacts.get(position);
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView List_IMAGE_profilePicture;
        TextView List_LBL_firstName;
        TextView List_LBL_lastName;

        ViewHolder(View view) {
            super(view);

            //find views
            List_LBL_firstName = view.findViewById(R.id.List_LBL_firstName);
            List_LBL_lastName = view.findViewById(R.id.List_LBL_lastName);
            List_IMAGE_profilePicture = view.findViewById(R.id.List_IMAGE_profilePicture);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (chatListClickListener != null)
                        chatListClickListener.getProfile(getItem(getAdapterPosition()));
                }
            });

        }
    }
    public interface chatListClickListener {
        void getProfile(BandMeContact bandMeContact);
        void moveToChatActivity(BandMeContact bandMeContact, BandMeProfile bandMeProfile);
    }
}