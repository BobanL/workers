package com.example.boban.workers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter_Card extends RecyclerView.Adapter<RecyclerAdapter_Card.ViewHolder>{
    private ArrayList<Jobs> jobs;
    private ArrayList<String> jobsID;
    private LayoutInflater mInflater;
    // data is passed into the constructor

    public RecyclerAdapter_Card(Context context, ArrayList<Jobs> jobs, ArrayList<String> jobsID) {
        this.mInflater = LayoutInflater.from(context);
        this.jobs = jobs;
        this.jobsID = jobsID;
    }
    // inflates the row layout from xml when needed

    @Override
    @NonNull
    public RecyclerAdapter_Card.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_card, parent, false);
        return new RecyclerAdapter_Card.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.titleName.setText(jobs.get(i).getJobName());
        viewHolder.titleDescription.setText(jobs.get(i).getJobDescription());
        viewHolder.titleCity.setText(jobs.get(i).getJobCity());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return jobs.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView titleName, titleDescription, titleCity;

        public ViewHolder(final View itemView) {
            super(itemView);
            titleName = itemView.findViewById(R.id.title);
            titleDescription = itemView.findViewById(R.id.txtDescription);
            titleCity = itemView.findViewById(R.id.txtCity);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("jobID", jobsID.get(getAdapterPosition()));
                    // set Fragmentclass Arguments
                    JobFragment fragobj = new JobFragment();
                    fragobj.setArguments(bundle);
                    MainActivity myActivity = (MainActivity)itemView.getContext();
                    myActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, fragobj,"findThisFragment")
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }
}
