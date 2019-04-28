package com.apps.edu_gate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.TutorViewHolder> {

    private Context mCtx;
    private List<Tutorinfo> tutorList;
    private static MyClickListener myClickListener;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    public static class TutorViewHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView fname;
        TextView lname;
        TextView institution;
        RatingBar rating;
        TextView location;
        ImageView profileImage;

        public TutorViewHolder(View itemView) {
            super(itemView);
            fname = (TextView) itemView.findViewById(R.id.fname);
            lname = (TextView) itemView.findViewById(R.id.lname);
            institution = (TextView) itemView.findViewById(R.id.institution);
            rating = (RatingBar) itemView.findViewById(R.id.ratingBar);
            location = (TextView) itemView.findViewById(R.id.location);
            profileImage = (ImageView)itemView.findViewById(R.id.person_photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public TutorAdapter(Context mCtx, List<Tutorinfo> tutorList) {
        this.mCtx = mCtx;
        this.tutorList = tutorList;
    }

    @NonNull
    @Override
    public TutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclertutor, parent, false);
        return new TutorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorViewHolder holder, int position) {
        Tutorinfo tutor = tutorList.get(position);
        ArrayList<Double> temp = tutor.rating;
        double x = 0;
        double count = 0;
        Iterator it = temp.iterator();
        while (it.hasNext()) {
            count++;
            x = x + (long)it.next();
            it.remove(); // avoids a ConcurrentModificationException
        }
        double avgrate = x/count;
        tutor.tempr = avgrate;
        String rate = String.valueOf(avgrate);
        Picasso.get()
                .load(tutor.getProfileImage())
                .placeholder(R.drawable.placeholder_profile_picture)
                .fit()
                .centerCrop()
                .into(holder.profileImage);
        holder.fname.setText(WordUtils.capitalizeFully(tutor.firstName));
        holder.location.setText(WordUtils.capitalizeFully(tutor.tuitionLocation));
        holder.lname.setText(WordUtils.capitalizeFully(tutor.lastName));
        holder.institution.setText(WordUtils.capitalizeFully(tutor.recentInstitution));
        holder.rating.setRating((float)avgrate);

    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }
    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}