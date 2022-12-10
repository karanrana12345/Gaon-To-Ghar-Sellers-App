package com.gg.sellers;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment()
    {

    }

    private Button addBtn;
    private RecyclerView recyclerView;
    UserproductAdapter userproductAdapter;
    List<Post>postList;
    String profileid;
    private FirebaseUser currentUser;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    public static LottieAnimationView emptyAnimation;
    public static TextView emptyTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        profileid = currentUser.getUid();

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        addBtn = view.findViewById(R.id.add_btn);
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyAnimation = view.findViewById(R.id.empty_animation);
        emptyTV = view.findViewById(R.id.empty_tv);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {}
                });
                AdRequest iadRequest = new AdRequest.Builder().build();

                InterstitialAd.load(getContext(),"ca-app-pub-7407231061173432/7331428948", iadRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                mInterstitialAd.show(getActivity());
                                Log.i(TAG, "onAdLoaded");
                            }
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.d(TAG, loadAdError.toString());
                                mInterstitialAd = null;
                            }
                        });

                Intent intent = new Intent(getContext(),ProductActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        userproductAdapter = new UserproductAdapter(getContext(),postList);
        recyclerView.setAdapter(userproductAdapter);

        myPosts();

        return view;
    }

    private void myPosts()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Product_Orders");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Post post = snapshot.getValue(Post.class);
                    if (post.getSellerID().equals(profileid))
                    {
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                userproductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}