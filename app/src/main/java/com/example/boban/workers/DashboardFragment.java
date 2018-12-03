package com.example.boban.workers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


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
        adapter = new CustomAdapter(jobs,v.getContext());
        listViewJobsPosted.setAdapter(adapter);
        adapter = new CustomAdapter(jobs,v.getContext());
        listViewJobsApplied.setAdapter(adapter);

        addProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProjectFragment nextFrag = new AddProjectFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return v;
    }

}
