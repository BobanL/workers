package com.example.boban.workers;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;


public class AddProjectFragment extends Fragment{
    EditText jobName, jobStreet, jobCity, jobZip, jobDescription;
    View v;
    Button addImage, submitProject;
    RecyclerView recyclerView;
    private RecyclerAdapter_Image adapter;
    ArrayList<Bitmap> imgSrc = new ArrayList<>();
    private DatabaseReference db;

    public AddProjectFragment() {
    }

    public static AddProjectFragment newInstance(String param1, String param2) {
        AddProjectFragment fragment = new AddProjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_project, container, false);
        db = FirebaseDatabase.getInstance().getReference();
        jobName = v.findViewById(R.id.jobNameText);
        jobStreet = v.findViewById(R.id.jobStreetText);
        jobCity = v.findViewById(R.id.jobCityText);
        jobZip = v.findViewById(R.id.jobZipCodeText);
        jobDescription = v.findViewById(R.id.jobDescriptionText);
        recyclerView = v.findViewById(R.id.jobGallery);
        submitProject = v.findViewById(R.id.submitJobButton);
        addImage = v.findViewById(R.id.jobAddImage);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                //TODO: do what you have to...
                                imgSrc.add(r.getBitmap());
                                updateRecyclerView();
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                            }
                        }).show(getFragmentManager());
            }
        });
        submitProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> images = new ArrayList<>();
                for (Bitmap b: imgSrc) {
                    images.add(ImageUtil.convert(b));
                }
                Jobs j = new Jobs(jobName.getText().toString(), jobDescription.getText().toString(), images, jobStreet.getText().toString(),
                        jobCity.getText().toString(), Integer.parseInt(jobZip.getText().toString()),
                        FirebaseAuth.getInstance().getCurrentUser().getUid(), System.currentTimeMillis());
                db.child("jobs").child(UUID.randomUUID().toString()).setValue(j).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Job Posted.",
                                    Toast.LENGTH_SHORT).show();
                            DashboardFragment nextFrag = new DashboardFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_layout, nextFrag,"findThisFragment")
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            Toast.makeText(getContext(), task.getResult().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        //RecyclerView
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        updateRecyclerView();

        return v;
    }

    private void updateRecyclerView(){
        adapter = new RecyclerAdapter_Image(getContext(), imgSrc);
        recyclerView.setAdapter(adapter);
    }

}




