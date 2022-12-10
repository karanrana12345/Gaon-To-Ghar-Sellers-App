package com.gg.sellers;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    private EditText productName, productQuantity, productPrice, productQuality, productTime, productHarvestTime;

    private FirebaseUser currentUser;
    private Button addProductBtn;

    private String sellerName, sellerAddress, sellerPhonenumber, sellerEmail, sellerID;

    private DatePickerDialog.OnDateSetListener DTsetListener;
    private DatePickerDialog.OnDateSetListener HTsetListener;

    private Dialog shareDialog;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productName = findViewById(R.id.product_name);
        productQuantity = findViewById(R.id.product_quantity);
        productPrice = findViewById(R.id.product_price);
        productQuality = findViewById(R.id.product_quality);
        productTime = findViewById(R.id.product_time);
        addProductBtn = findViewById(R.id.add_product);
        productHarvestTime = findViewById(R.id.product_harvest_time);

        MobileAds.initialize(ProductActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        shareDialog = new Dialog(ProductActivity.this);
        shareDialog.setContentView(R.layout.sharedialog);
        shareDialog.setCancelable(true);
        shareDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cancelBtn = shareDialog.findViewById(R.id.cancel);
        Button shareBtn = shareDialog.findViewById(R.id.share);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDialog.dismiss();
                Intent intent = new Intent(ProductActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            sellerName = task.getResult().getString("Fullname");
                            sellerPhonenumber = task.getResult().getString("Phonenumber");
                            sellerAddress = task.getResult().getString("Address");
                            sellerEmail = task.getResult().getString("Email");
                            sellerID = currentUser.getUid();

                            addProductBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String ProductDispatchTime = productTime.getText().toString();
                                    String ProductHarvestTime = productHarvestTime.getText().toString();
                                    String ProductName = productName.getText().toString();
                                    String ProductQuantity = productQuantity.getText().toString();
                                    String ProductPrice = productPrice.getText().toString();
                                    String ProductQuality = productQuality.getText().toString();
                                    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                                    if (TextUtils.isEmpty(ProductDispatchTime)) {
                                        productTime.setError("Dispatch time is required");
                                        return;
                                    }
                                    if (TextUtils.isEmpty(ProductHarvestTime)) {
                                        productHarvestTime.setError("Harvest time is required");
                                        return;
                                    }
                                    if (TextUtils.isEmpty(ProductName)) {
                                        productName.setError("Name is required");
                                        return;
                                    }
                                    if (TextUtils.isEmpty(ProductPrice)) {
                                        productPrice.setError("Price is required");
                                        return;
                                    }
                                    if (TextUtils.isEmpty(ProductQuality)) {
                                        productQuality.setError("Yes/No is required");
                                        return;
                                    }
                                    if (TextUtils.isEmpty(ProductQuantity)) {
                                        productQuantity.setError("Quantity is required");
                                        return;
                                    }

                                    if (Integer.parseInt(ProductQuantity) < 100 )
                                    {
                                        productQuantity.setError("Minimum 100kgs");
                                        return;
                                    }

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Product_Orders");

                                    String productid = reference.push().getKey();

                                    HashMap<String, Object> userdata = new HashMap<>();
                                    userdata.put("SellerName", sellerName);
                                    userdata.put("SellerPhoneNumber", sellerPhonenumber);
                                    userdata.put("SellerAddress", sellerAddress);
                                    userdata.put("SellerEmail", sellerEmail);
                                    userdata.put("SellerID", sellerID);
                                    userdata.put("ProductID", productid);
                                    userdata.put("ProductName", ProductName);
                                    userdata.put("ProductQuantity", ProductQuantity);
                                    userdata.put("ProductPrice", ProductPrice);
                                    userdata.put("ProductQuality", ProductQuality);
                                    userdata.put("ProductDispatchTime", ProductDispatchTime);
                                    userdata.put("ProductHarvestTime",ProductHarvestTime);
                                    userdata.put("UploadDate", date);

                                    reference.child(productid).setValue(userdata);

                                    Toast.makeText(ProductActivity.this, "Thank-you we'll contact soon!", Toast.LENGTH_LONG).show();

                                    shareDialog.show();

                                    shareBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String whatsappURL = "https://wa.me/+917456989840?text=Hi there, I want to sell my product,details are given below:"
                                                    +"\n"
                                                    +"\nSeller Name: " + sellerName
                                                    +"\n"
                                                    +"\nSeller Phonenumber: " + sellerPhonenumber
                                                    +"\n"
                                                    +"\nSeller Email: "+ sellerEmail
                                                    +"\n"
                                                    +"\nSeller Address: " + sellerAddress
                                                    +"\n"
                                                    +"\nSeller ID: " + sellerID
                                                    +"\n"
                                                    +"\nProduct Name: "+ ProductName
                                                    +"\n"
                                                    +"\nProduct Quantity: "+ ProductQuantity
                                                    +"\n"
                                                    +"\nProduct ID: "+ productid
                                                    +"\n"
                                                    +"\nProduct Price: "+ ProductPrice
                                                    +"\n"
                                                    +"\nProduct Dispatch Time: "+ ProductDispatchTime
                                                    +"\n"
                                                    +"\nProduct Harvest Time: "+ ProductHarvestTime
                                                    +"\n"
                                                    +"\nUpload Date: "+ date
                                                    +"\n"
                                                    +"\nThank-you";

                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse(whatsappURL));
                                            startActivity(intent);

                                            productName.setText("");
                                            productQuantity.setText("");
                                            productPrice.setText("");
                                            productQuality.setText("");
                                            productTime.setText("");
                                            productHarvestTime.setText("");

                                            shareDialog.dismiss();

                                        }
                                    });

                                }

                            });
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(ProductActivity.this, error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        productTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProductActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,DTsetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        DTsetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                productTime.setText(date);
            }
        };

        productHarvestTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProductActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,HTsetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        HTsetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                productHarvestTime.setText(date);
            }
        };

    }
}