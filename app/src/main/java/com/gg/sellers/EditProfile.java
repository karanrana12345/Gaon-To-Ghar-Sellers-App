package com.gg.sellers;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    private EditText nameField, emailField, addressField, phonenumberField;
    private Button updateBtn;
    private Dialog loadingDialog,passwordDialog;
    private String name, email, address, phonenumber,photo;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private EditText password;
    private Button doneBtn;
    private Uri selectedImageUri;
    private boolean updatePhoto = false;
    private CircleImageView circleImageView;
    private Button changePhotoBtn,removeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        loadingDialog = new Dialog(EditProfile.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        passwordDialog = new Dialog(EditProfile.this);
        passwordDialog.setContentView(R.layout.password_confirmation_dialog);
        passwordDialog.setCancelable(true);
        passwordDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        passwordDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        password = passwordDialog.findViewById(R.id.password);
        doneBtn = passwordDialog.findViewById(R.id.done_btn);
        circleImageView = findViewById(R.id.user_image);
        changePhotoBtn = findViewById(R.id.change_photo_btn);
        removeBtn = findViewById(R.id.remove_photo_btn);

        nameField = findViewById(R.id.name);
        emailField = findViewById(R.id.email);
        addressField = findViewById(R.id.address);
        phonenumberField = findViewById(R.id.phonenumber);
        updateBtn = findViewById(R.id.updateBtn);

        name = getIntent().getStringExtra("Name");
        email = getIntent().getStringExtra("Email");
        address = getIntent().getStringExtra("Address");
        phonenumber = getIntent().getStringExtra("Phonenumber");

        Glide.with(EditProfile.this).load(photo).apply(new RequestOptions().placeholder(R.drawable.profileplaceholder)).into(circleImageView);
        nameField.setText(name);
        emailField.setText(email);
        addressField.setText(address);
        phonenumberField.setText(phonenumber);

        changePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                launchSomeActivity.launch(i);
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImageUri = null;
                updatePhoto = true;
                Glide.with(EditProfile.this).load(R.drawable.profileplaceholder).into(circleImageView);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailandPassword();
            }
        });

        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addressField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phonenumberField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(phonenumberField.getText()))
        {
            if (!TextUtils.isEmpty(addressField.getText()))
            {
                if (!TextUtils.isEmpty(emailField.getText()))
                {
                    if (!TextUtils.isEmpty(nameField.getText()))
                    {
                        updateBtn.setEnabled(true);
                        updateBtn.setTextColor(Color.rgb(255, 255, 255));
                    }
                    else
                    {
                        updateBtn.setEnabled(false);
                        updateBtn.setTextColor(Color.argb(50, 255, 255, 255));
                    }
                }
                else
                {
                    updateBtn.setEnabled(false);
                    updateBtn.setTextColor(Color.argb(50, 255, 255, 255));
                }
            }
            else
            {
                updateBtn.setEnabled(false);
                updateBtn.setTextColor(Color.argb(50, 255, 255, 255));
            }
        }
        else
        {
            updateBtn.setEnabled(false);
            updateBtn.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    private void checkEmailandPassword()
    {
        if (emailField.getText().toString().matches(emailpattern))
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (emailField.getText().toString().toLowerCase().trim().equals(email.toLowerCase().trim()))
            {
                loadingDialog.show();
                updatePhoto(user);
            }
            else //update email
            {
                passwordDialog.show();
                doneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        loadingDialog.show();

                        String userPassword = password.getText().toString();
                        passwordDialog.dismiss();

                        AuthCredential credential = EmailAuthProvider
                                .getCredential(email,userPassword);

                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            user.updateEmail(emailField.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        updatePhoto(user);
                                                    }
                                                    else
                                                    {
                                                        loadingDialog.dismiss();
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(EditProfile.this,error,Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                        else
                                        {
                                            loadingDialog.dismiss();
                                            String error = task.getException().getMessage();
                                            Toast.makeText(EditProfile.this,error,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
            }
        }
        else
        {
            emailField.setError("Invalid Email !");
        }
    }
    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->
    {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null)
            {
                selectedImageUri = data.getData();
                updatePhoto = true;
                Glide.with(EditProfile.this).load(selectedImageUri).into(circleImageView);
            }
        }
    });

    private void updateFields(FirebaseUser user, Map<String,Object> updateData)
    {
        FirebaseFirestore.getInstance().collection("USERS").document(user.getUid())
                .update(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            if (updateData.size() > 1)
                            {
                                DBQueries.email = emailField.getText().toString().trim();
                                DBQueries.fullname = nameField.getText().toString().trim();
                                DBQueries.address = addressField.getText().toString().trim();
                                DBQueries.phonenumebr = phonenumberField.getText().toString().trim();
                            }
                            else
                            {
                                DBQueries.fullname = nameField.getText().toString().trim();
                                DBQueries.email = emailField.getText().toString().trim();
                                DBQueries.address = addressField.getText().toString().trim();
                                DBQueries.phonenumebr = phonenumberField.getText().toString().trim();
                            }
                            finish();
                            Toast.makeText(EditProfile.this,"Successfully Updated !",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String error = task.getException().getMessage();
                            Toast.makeText(EditProfile.this,error,Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }
                });
    }
    private void updatePhoto(FirebaseUser user)
    {
        //Updating Photo

        if (updatePhoto)
        {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Profile/"+user.getUid() + ".jpg");
            if (selectedImageUri != null)
            {
                Glide.with(EditProfile.this).asBitmap().load(selectedImageUri).circleCrop().into(new ImageViewTarget<Bitmap>(circleImageView) {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful())
                                            {
                                                selectedImageUri = task.getResult();
                                                DBQueries.profile = task.getResult().toString();
                                                Glide.with(EditProfile.this).load(DBQueries.profile).into(circleImageView);

                                                Map<String,Object> updateData = new HashMap<>();
                                                updateData.put("Email",emailField.getText().toString());
                                                updateData.put("Fullname",nameField.getText().toString());
                                                updateData.put("Address",addressField.getText().toString());
                                                updateData.put("Phonenumber",phonenumberField.getText().toString());
                                                updateData.put("Profile",DBQueries.profile);

                                                updateFields(user,updateData);
                                            }
                                            else
                                            {
                                                loadingDialog.dismiss();
                                                DBQueries.profile = "";
                                                String error = task.getException().getMessage();
                                                Toast.makeText(EditProfile.this,error,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    loadingDialog.dismiss();
                                    String error = task.getException().getMessage();
                                    Toast.makeText(EditProfile.this,error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        return;
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        circleImageView.setImageResource(R.drawable.profileplaceholder);
                    }
                });

            }
            else
            //remove photo
            {
                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            DBQueries.profile = "";

                            Map<String,Object> updateData = new HashMap<>();
                            updateData.put("Email",emailField.getText().toString());
                            updateData.put("Fullname",nameField.getText().toString());
                            updateData.put("Profile","");

                            updateFields(user,updateData);
                        }
                        else
                        {
                            loadingDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(EditProfile.this,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else
        {
            Map<String,Object> updateData = new HashMap<>();
            updateData.put("Fullname",nameField.getText().toString());
            updateData.put("Email",emailField.getText().toString());
            updateData.put("Address",addressField.getText().toString());
            updateData.put("Phonenumber",phonenumberField.getText().toString());

            updateFields(user,updateData);
        }

        //Updating Photo
    }

}