package com.bawp.bandme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bawp.bandme.model.BandMeProfile;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<BandMeProfile> bandMeProfiles = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    private ProfileListClickListener profileListClickListener;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, ArrayList<BandMeProfile> data) {
        this.mInflater = LayoutInflater.from(context);
        this.bandMeProfiles = data;
        this.context = context;
    }

    public void setClickListener(ProfileListClickListener profileListClickListener){
        this.profileListClickListener = profileListClickListener;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_musicians, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BandMeProfile bandMeProfile = bandMeProfiles.get(position);

        //check if user uploaded a picture
        if (!bandMeProfile.getImageUrl().equals("")){
            Glide.with(context)
                    .load(bandMeProfile.getImageUrl())
                    .into(holder.List_IMAGE_profilePicture);
        }
        else{
            holder.List_IMAGE_profilePicture.setImageResource(R.drawable.profile);
        }
        holder.List_LBL_firstName.setText(bandMeProfile.getFirstName());
        holder.List_LBL_lastName.setText(bandMeProfile.getLastName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return bandMeProfiles.size();
    }


    // convenience method for getting data at click position
    BandMeProfile getItem(int position) {
        return bandMeProfiles.get(position);
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
                    if (profileListClickListener != null)
                        profileListClickListener.getProfile(getItem(getAdapterPosition()));
                }
            });
        }
    }

    public interface ProfileListClickListener {
        void getProfile(BandMeProfile bandMeProfile);
    }

}
