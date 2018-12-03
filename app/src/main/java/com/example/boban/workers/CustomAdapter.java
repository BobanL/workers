package com.example.boban.workers;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Jobs> implements View.OnClickListener{

    private ArrayList<Jobs> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView jobName;
        TextView jobLocation;
        TextView jobStatus;
        TextView jobCost;
    }

    public CustomAdapter(ArrayList<Jobs> data, Context context) {
        super(context, R.layout.list_job, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Jobs Jobs = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

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

        lastPosition = position;

        viewHolder.jobName.setText(Jobs.getJobName());
        viewHolder.jobLocation.setText(Jobs.getJobCity());
        viewHolder.jobStatus.setText(Jobs.getJobStatus());
        viewHolder.jobCost.setText(Jobs.getJobCost());
        // Return the completed view to render on screen
        return convertView;
    }
}
