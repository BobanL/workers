package com.example.boban.workers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {
    View v;
    FloatingActionButton addProjectButton;
    ListView listViewJobsPosted, listViewJobsApplied;
    ArrayList<Jobs> jobs;
    private static CustomAdapter adapter;

    public DashboardFragment() {
    }

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        addProjectButton = v.findViewById(R.id.addProjectButton);
        listViewJobsApplied = v.findViewById(R.id.listViewApplied);
        listViewJobsPosted = v.findViewById(R.id.listViewPosted);

        jobs = new ArrayList<>();
        jobs.add(new Jobs("Name1", "Loc1", "Pending", "100"));
        jobs.add(new Jobs("Name2", "Loc12", "Pending3", "102"));
        jobs.add(new Jobs("Name3", "Loc12", "Pending3", "103"));
        jobs.add(new Jobs("Name1", "Loc1", "Pending", "100"));
        jobs.add(new Jobs("Name2", "Loc12", "Pending3", "102"));
        jobs.add(new Jobs("Name3", "Loc12", "Pending3", "103"));
        jobs.add(new Jobs("Name1", "Loc1", "Pending", "100"));
        jobs.add(new Jobs("Name2", "Loc12", "Pending3", "102"));
        jobs.add(new Jobs("Name3", "Loc12", "Pending3", "103"));
        adapter = new CustomAdapter(jobs,v.getContext());

        listViewJobsPosted.setAdapter(adapter);
        adapter = new CustomAdapter(jobs,v.getContext());
        listViewJobsApplied.setAdapter(adapter);

        addProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        return v;
    }

}