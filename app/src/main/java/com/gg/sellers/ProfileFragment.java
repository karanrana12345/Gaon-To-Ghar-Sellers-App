package com.gg.sellers;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    public ProfileFragment()
    {

    }

    private TextView sellerName,sellerEmail,sellerPhonenumber,sellerAddress,sellerID;
    private FirebaseUser currentUser;
    private CircleImageView userimage;
    private Button editProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sellerName = view.findViewById(R.id.seller_name);
        sellerEmail = view.findViewById(R.id.seller_email);
        sellerAddress = view.findViewById(R.id.seller_address);
        sellerPhonenumber  = view.findViewById(R.id.seller_phonenumber);
        sellerID = view.findViewById(R.id.seller_id);
        editProfile = view.findViewById(R.id.edit_profile);
        userimage = view.findViewById(R.id.user_image);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful())
                        {
                            String fullname = task.getResult().getString("Fullname");
                            String phonenumber = task.getResult().getString("Phonenumber");
                            String address = task.getResult().getString("Address");
                            String email = task.getResult().getString("Email");
                            String profile = task.getResult().getString("Profile");
                            String id = currentUser.getUid();

                            sellerName.setText(fullname);
                            sellerEmail.setText(email);
                            sellerAddress.setText(address);
                            sellerPhonenumber.setText(phonenumber);
                            sellerID.setText(id);
                            Glide.with(getContext()).load(profile).apply(new RequestOptions().placeholder(R.drawable.profileplaceholder)).into(userimage);
                        }
                        else
                        {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),EditProfile.class);
                intent.putExtra("Name",sellerName.getText());
                intent.putExtra("Email",sellerEmail.getText());
                intent.putExtra("Phonenumber",sellerPhonenumber.getText());
                intent.putExtra("Address",sellerAddress.getText());
                startActivity(intent);
            }
        });

        return view;
    }
}