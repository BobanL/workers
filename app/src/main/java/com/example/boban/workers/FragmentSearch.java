package com.example.boban.workers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;


public class FragmentSearch extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    GoogleMap map;
    LocationManager lm;
    View v;
    Location location;
    double longitude, latitude;
    private FirebaseDatabase db;
    String userID;
    DatabaseReference dbRef;
    ArrayList<MarkerOptions> markers;
    RecyclerView recyclerView;
    RecyclerAdapter_Card adapter;
    ArrayList<Jobs> jobs = new ArrayList<>();
    ArrayList<String> jobsID = new ArrayList<>();
    Geocoder geocoder;
    SearchView searchView;
    String searchResults;

    public FragmentSearch() {
    }

    public static FragmentSearch newInstance() {
        FragmentSearch fragment = new FragmentSearch();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        geocoder = new Geocoder(getContext());
        jobs = new ArrayList<>();
        jobsID = new ArrayList<>();
        v = inflater.inflate(R.layout.fragment_fragment_search, container, false);
        db = FirebaseDatabase.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = db.getReference("jobs");
        markers = new ArrayList<>();
        recyclerView = v.findViewById(R.id.searchResults);
        searchView = (SearchView) v.findViewById(R.id.searchBar);


        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        if ((ContextCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

            lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if(location == null){
            //DB
            db = FirebaseDatabase.getInstance();
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dbRef = db.getReference("users").child(userID);

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String zipCode = (dataSnapshot.child("zipCode").getValue().toString());
                    Geocoder geocoder = new Geocoder(v.getContext());
                    try {
                        ArrayList<Address> addresses = (ArrayList) geocoder.getFromLocationName(zipCode, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            latitude = addresses.get(0).getLatitude();
                            longitude = addresses.get(0).getLongitude();
                            LatLng latLng = new LatLng(latitude, longitude);
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                            mapView.onResume();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            for (MarkerOptions mark: markers) {
                map.addMarker(mark);
            }
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchResults = s;
                onMapReady(map);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        updateMarkers();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        mapView.getMapAsync(this);
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
        map.clear();
        updateMarkers();
        LatLng latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        mapView.onResume();
    }

    public void updateMarkers(){
        jobs.clear();
        jobsID.clear();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Jobs j = snapshot.getValue(Jobs.class);
                    if(searchResults == null || searchResults.isEmpty()){
                        jobs.add(j);
                        jobsID.add(snapshot.getKey());
                        String address = j.getJobStreet() + "," + j.getJobCity() + "," + j.getJobZip();
                        try {
                            ArrayList<Address> addresses = (ArrayList) geocoder.getFromLocationName(address, 1);
                            LatLng latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                            map.addMarker(new MarkerOptions()
                                    .position(latLng).title(j.getJobName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                    .draggable(false).visible(true));
                        } catch (Exception e) {
                        }

                    }else if(j.getJobDescription().toLowerCase().contains(searchResults.toLowerCase()) || j.getJobCity().toLowerCase().contains(searchResults.toLowerCase()) || j.getJobName().toLowerCase().contains(searchResults.toLowerCase()) ) {
                        jobs.add(j);
                        jobsID.add(snapshot.getKey());
                        String address = j.getJobStreet() + "," + j.getJobCity() + "," + j.getJobZip();
                        try {
                            ArrayList<Address> addresses = (ArrayList) geocoder.getFromLocationName(address, 1);
                            LatLng latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                            map.addMarker(new MarkerOptions()
                                    .position(latLng).title(j.getJobName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                    .draggable(false).visible(true));
                        } catch (Exception e) {
                        }

                    }
                }
                //RecyclerView
                LinearLayoutManager horizontalLayoutManager
                        = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(horizontalLayoutManager);
                adapter = new RecyclerAdapter_Card(v.getContext(), jobs, jobsID);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
