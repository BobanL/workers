package com.example.boban.workers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class DashboardFragment extends Fragment {
    View v;
    FloatingActionButton addProjectButton;
    ListView listViewJobsPosted, listViewJobsApplied;
    ArrayList<Jobs> jobsPosted = new ArrayList<>(), jobsApplied = new ArrayList<>();
    FirebaseDatabase db;
    String userID;

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

        db = FirebaseDatabase.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbRef = db.getReference("jobs");


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Jobs j = snapshot.getValue(Jobs.class);
                    if(j.getJobBidder() != null && j.getJobBidder().contains(userID)){
                        jobsApplied.add(j);
                    }
                    if (j.getJobSubmitter().contains(userID)) {
                        jobsPosted.add(j);
                    }
                }
                Collections.sort(jobsApplied, new Comparator<Jobs>() {
                    @Override
                    public int compare(Jobs o1, Jobs o2) {
                        return Double.compare(o1.getJobPostedDate(), o2.getJobPostedDate());
                    }
                });
                Collections.reverse(jobsApplied);
                Collections.sort(jobsPosted, new Comparator<Jobs>() {
                    @Override
                    public int compare(Jobs o1, Jobs o2) {
                        return Double.compare(o1.getJobPostedDate(), o2.getJobPostedDate());
                    }
                });
                Collections.reverse(jobsPosted);

                adapter = new CustomAdapter(jobsPosted,v.getContext());
                listViewJobsPosted.setAdapter(adapter);
                adapter = new CustomAdapter(jobsApplied,v.getContext());
                listViewJobsApplied.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
