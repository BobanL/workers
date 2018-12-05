package com.example.boban.workers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JobFragment extends Fragment implements OnMapReadyCallback {
    TextView jobName, jobDescription, submittedBy;
    MapView mapView;
    GoogleMap map;
    RecyclerView recyclerView;
    Button submitEstimate;
    private RecyclerAdapter_Image adapter;
    ArrayList<Bitmap> imgSrc = new ArrayList<>();
    View v;
    LocationManager lm;
    Location location;
    double longitude, latitude;
    private FirebaseDatabase db;
    static String userID, jobID;
    DatabaseReference dbRef;
    String bidders;

    public JobFragment() {
        // Required empty public constructor
    }

    public static JobFragment newInstance(String param1) {
        JobFragment fragment = new JobFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_job, container, false);
        jobID = getArguments().getString("jobID");

        jobName = v.findViewById(R.id.jobNameJobView);
        jobDescription = v.findViewById(R.id.jobDescriptionJobView);
        mapView = v.findViewById(R.id.mapView2);
        recyclerView = v.findViewById(R.id.jobGallery2);
        submitEstimate = v.findViewById(R.id.submitEstimateJobView);
        //RecylerView
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        //MapView
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        //Filling in fields
        db = FirebaseDatabase.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbRef = db.getReference("jobs").child(jobID);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jobName1 = dataSnapshot.child("jobName").getValue().toString();
                jobName.setText(jobName1);
                if(dataSnapshot.child("jobDescription").getValue() != null){
                    String description = dataSnapshot.child("jobDescription").getValue().toString();
                    if(description.isEmpty()){
                        jobDescription.setText("");
                    }else{
                        jobDescription.setText(description);
                    }
                }

                String addressString = "";
                if(dataSnapshot.child("jobStreet").getValue() != null){
                    addressString = dataSnapshot.child("jobStreet").getValue().toString();
                }
                if(dataSnapshot.child("jobCity").getValue() != null){
                    addressString = addressString + ", " + dataSnapshot.child("jobCity").getValue().toString();
                }
                if(dataSnapshot.child("jobZip").getValue() != null){
                    addressString = addressString + ", " + dataSnapshot.child("jobZip").getValue().toString();
                }

                Geocoder geocoder = new Geocoder(getContext());
                try {
                    ArrayList<Address> addresses = (ArrayList) geocoder.getFromLocationName(addressString, 1);
                    LatLng latLng = new LatLng(addresses.get(0).getLongitude(), addresses.get(0).getLatitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                    mapView.onResume();
                } catch (Exception e) {

                }
                bidders = dataSnapshot.child("jobBidder").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dbRef = db.getReference("jobs").child(jobID);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jobName1 = dataSnapshot.child("jobName").getValue().toString();
                jobName.setText(jobName1);

                if(dataSnapshot.child("jobDescription").getValue() != null){
                    String description = dataSnapshot.child("jobDescription").getValue().toString();
                    if(description.isEmpty()){
                        jobDescription.setText("");
                    }else{
                        jobDescription.setText(description);
                    }
                }

                String addressString = "";
                if(dataSnapshot.child("jobStreet").getValue() != null){
                    addressString = dataSnapshot.child("jobStreet").getValue().toString();
                }
                if(dataSnapshot.child("jobCity").getValue() != null){
                    addressString = addressString + ", " + dataSnapshot.child("jobCity").getValue().toString();
                }
                if(dataSnapshot.child("jobZip").getValue() != null){
                    addressString = addressString + ", " + dataSnapshot.child("jobZip").getValue().toString();
                }
                ArrayList<Bitmap> imgs = new ArrayList<>();
                if(dataSnapshot.child("jobImages").exists()){
                    for (DataSnapshot ds : dataSnapshot.child("jobImages").getChildren()) {
                        imgs.add(ImageUtil.convert((String) ds.getValue()));
                    }
                }
                updateRecyclerView(imgs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submitEstimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Submit Estimate");
                final EditText input = new EditText(v.getContext());
                builder.setView(input);
                final DatabaseReference dbRef1 = db.getReference("jobs").child(jobID);
                final String biddz = bidders + "," + userID;
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                         dbRef1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                Jobs j = dataSnapshot.getValue(Jobs.class);
                                if(j.getJobCost().isEmpty() || Integer.parseInt(j.getJobCost()) > Integer.parseInt(input.getText().toString())){
                                    j.setJobCost(input.getText().toString());
                                    j.setJobWinner(userID);
                                }
                                j.setJobBidder(biddz);
                                dbRef1.setValue(j).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                DashboardFragment dashboardFragment = new DashboardFragment();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frame_layout, dashboardFragment,"findThisFragment")
                                        .addToBackStack(null)
                                        .commit();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
                builder.show();

            }
        });

        return v;
    }
    private void updateRecyclerView(ArrayList<Bitmap> img){
        adapter = new RecyclerAdapter_Image(getContext(), img);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        /*for (MarkerOptions m : markers) {
            googleMap.addMarker(new MarkerOptions()
                    .position(m.getPosition())
                    .title(m.getTitle()));
        }*/
        LatLng latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        mapView.onResume();
    }
}
