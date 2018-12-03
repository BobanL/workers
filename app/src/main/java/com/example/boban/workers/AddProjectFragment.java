package com.example.boban.workers;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;


public class AddProjectFragment extends Fragment{
    EditText jobName, jobLocation, jobDescription;
    View v;
    Button addImage;
    RecyclerView recyclerView;
    private RecyclerAdapter_Image adapter;
    ArrayList<Bitmap> imgSrc = new ArrayList<>();
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
        jobName = v.findViewById(R.id.jobNameText);
        jobLocation = v.findViewById(R.id.jobLocationText);
        jobDescription = v.findViewById(R.id.jobDescriptionText);
        recyclerView = v.findViewById(R.id.jobGallery);
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




