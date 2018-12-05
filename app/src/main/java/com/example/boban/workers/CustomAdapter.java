package com.example.boban.workers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Jobs>{

    private ArrayList<Jobs> dataSet;
    private ArrayList<String> jobID;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView jobName;
        TextView jobLocation;
        TextView jobStatus;
        TextView jobCost;
    }

    public CustomAdapter(ArrayList<Jobs> data, Context context, ArrayList<String> jobID) {
        super(context, R.layout.list_job, data);
        this.dataSet = data;
        this.mContext=context;
        this.jobID = jobID;
    }


    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Jobs Jobs = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_job, parent, false);
            viewHolder.jobName = (TextView) convertView.findViewById(R.id.jobNameText);
            viewHolder.jobLocation = (TextView) convertView.findViewById(R.id.jobLocationText);
            viewHolder.jobStatus = (TextView) convertView.findViewById(R.id.jobStatusText);
            viewHolder.jobCost = (TextView) convertView.findViewById(R.id.jobCostText);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("jobID", jobID.get(position));
                // set Fragmentclass Arguments
                JobFragment fragobj = new JobFragment();
                fragobj.setArguments(bundle);
                MainActivity myActivity = (MainActivity)getContext();
                myActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, fragobj,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }

        });

        lastPosition = position;

        viewHolder.jobName.setText(Jobs.getJobName());
        viewHolder.jobLocation.setText(Jobs.getJobCity());
        viewHolder.jobStatus.setText(Jobs.getJobStatus());
        if(Jobs.getJobCost() != null && !Jobs.getJobCost().isEmpty()){
            viewHolder.jobCost.setText(Jobs.getJobCost());
        }else{
            viewHolder.jobCost.setText("N/A");
        }
        // Return the completed view to render on screen
        return convertView;
    }

}

