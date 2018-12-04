package com.example.boban.workers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    View v;
    TextView userName, numJobsPosted, numJobsCompleted, aboutMe, email, address, phoneNumber;
    FirebaseDatabase db;
    String userID;
    ImageButton profileEdit;
    ImageView profilePic;
    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        userName = v.findViewById(R.id.userNameLabel);
        numJobsPosted = v.findViewById(R.id.numJobsPosted);
        numJobsCompleted = v.findViewById(R.id.numJobsCompleted);
        aboutMe = v.findViewById(R.id.editUserAboutMe);
        email = v.findViewById(R.id.userEmailAddress);
        address = v.findViewById(R.id.userAddress);
        phoneNumber = v.findViewById(R.id.userPhoneNumber);
        profileEdit = v.findViewById(R.id.editProfilePencil);
        profilePic = v.findViewById(R.id.profilePicture);

        //DB
        db = FirebaseDatabase.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbRef = db.getReference("users").child(userID);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fullName = dataSnapshot.child("fname").getValue().toString() + " " + dataSnapshot.child("lname").getValue().toString();
                userName.setText(fullName);

                if(dataSnapshot.child("description").getValue() != null){
                    String description = dataSnapshot.child("description").getValue().toString();
                    if(description.isEmpty()){
                        aboutMe.setText("It seems that there isn't anything here!");
                    }else{
                        aboutMe.setText(description);
                    }
                }

                String emailString = dataSnapshot.child("email").getValue().toString();
                email.setText(emailString);

                String addressString = "";
                if(dataSnapshot.child("street").getValue() != null){
                    addressString = dataSnapshot.child("street").getValue().toString();
                }
                if(dataSnapshot.child("city").getValue() != null){
                    addressString = addressString + ", " + dataSnapshot.child("city").getValue().toString();
                }
                if(dataSnapshot.child("zipCode").getValue() != null){
                    addressString = addressString + ", " + dataSnapshot.child("zipCode").getValue().toString();
                }
                address.setText(addressString);

                String phoneString = dataSnapshot.child("phoneNumber").getValue().toString();
                phoneNumber.setText(phoneString);

                if(dataSnapshot.child("profilePicture").getValue() != null) {
                    profilePic.setImageBitmap(ImageUtil.convert(dataSnapshot.child("profilePicture").getValue().toString()));
                }else{
                    profilePic.setImageResource(R.drawable.ic_account_circle_black_36dp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileEditFragment editProf = new ProfileEditFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, editProf,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return v;
    }

}
