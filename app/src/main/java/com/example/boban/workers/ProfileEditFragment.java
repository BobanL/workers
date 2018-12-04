package com.example.boban.workers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;


public class ProfileEditFragment extends Fragment {
    View v;
    TextView userName, numJobsPosted, numJobsCompleted;
    EditText aboutMe, email, address, phoneNumber;
    FirebaseDatabase db;
    String userID;
    Button updateProfilePic;
    ImageButton saveForm;
    ImageView profilePic;
    public ProfileEditFragment() {
    }

    public static ProfileEditFragment newInstance() {
        ProfileEditFragment fragment = new ProfileEditFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        userName = v.findViewById(R.id.userNameLabel);
        numJobsPosted = v.findViewById(R.id.numJobsPosted);
        numJobsCompleted = v.findViewById(R.id.numJobsCompleted);
        saveForm = v.findViewById(R.id.saveForm);
        aboutMe = v.findViewById(R.id.editUserAboutMe);
        email = v.findViewById(R.id.editUserEmailAddress);
        address = v.findViewById(R.id.editUserAddress);
        phoneNumber = v.findViewById(R.id.editUserPhoneNumber);
        updateProfilePic = v.findViewById(R.id.editProfileImage);
        profilePic = v.findViewById(R.id.editProfilePicture);
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
        //Update Profile Picture
        updateProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                profilePic.setImageBitmap(r.getBitmap());
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                            }
                        }).show(getFragmentManager());

            }
        });
        //Save Form
        saveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail, sAddress, sPhone, sDescription, sImage;
                sEmail = email.getText().toString();
                sAddress = address.getText().toString();
                sPhone = phoneNumber.getText().toString();
                sDescription = aboutMe.getText().toString();
                sImage = ImageUtil.convert(((BitmapDrawable)profilePic.getDrawable()).getBitmap());

                DatabaseReference dbRef = db.getReference("users").child(userID);
                if(sEmail != null){
                    dbRef.child("email").setValue(sEmail);
                }
                if(sAddress != null && sAddress.contains(",")){
                    String[] s = sAddress.split(",");
                    dbRef.child("street").setValue(s[0]);
                    dbRef.child("city").setValue(s[1]);
                    dbRef.child("zipCode").setValue(s[2]);
                }else{
                    dbRef.child("zipCode").setValue(sAddress);
                }
                if (sPhone != null) {
                    dbRef.child("phoneNumber").setValue(sPhone);
                }
                if(sDescription != null){
                    dbRef.child("description").setValue(sDescription);
                }

                if(sImage != null){
                    dbRef.child("profilePicture").setValue(sImage);
                }

                ProfileFragment editProf = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, editProf,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return v;
    }


}
