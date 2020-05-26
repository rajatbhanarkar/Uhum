package com.example.somebody;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    ImageView AddPic, GPSLoc, Male, Female;
    CircleImageView ProfilePic;
    Button Register;
    File file;
    EditText FullName, Email;
    TextView City, State;
    String phno = "", Gender = "Male";

    UserDetails userDetails;

    ProgressDialog progressDialog;
    StorageReference storageReference;
    DatabaseReference myRef;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    LocationManager locationManager;
    LocationListener locationListener;

    Dialog LoaderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        FullName = (EditText)findViewById(R.id.etpsafullname);
        Email = (EditText)findViewById(R.id.etpsaemailadd);
        City = (TextView) findViewById(R.id.tvpsacity);
        State = (TextView) findViewById(R.id.tvpsastate);
        Register = (Button)findViewById(R.id.btnpsasubmit);
        ProfilePic = (CircleImageView) findViewById(R.id.ivpsaprofilepic);
        AddPic = (ImageView)findViewById(R.id.ivpsaaddpic);
        GPSLoc = (ImageView)findViewById(R.id.ivlocation);
        Male = (ImageView)findViewById(R.id.ivpsamale);
        Female = (ImageView)findViewById(R.id.ivpsafemale);

        sharedPreferences = getSharedPreferences("SomebodySharedPreferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userDetails = new Gson().fromJson(sharedPreferences.getString("UserDetails",""), UserDetails.class);

        FullName.setText(userDetails.getName());
        Email.setText(userDetails.getEmail());
        City.setText(userDetails.getCity());
        State.setText(userDetails.getState());

        if (userDetails.getProfPic().equals("Male")){
            if (Male.getPaddingTop() == 20){
                Male.setPadding(0, 0, 0, 0);
            }
            else{
                Male.setPadding(20, 20, 20, 20);
            }
            Female.setPadding(0, 0, 0, 0);
            Gender = "Male";
        }
        else if (userDetails.getProfPic().equals("Female")){
            if (Female.getPaddingTop() == 20){
                Female.setPadding(0, 0, 0, 0);
            }
            else{
                Female.setPadding(20, 20, 20, 20);
            }
            Male.setPadding(0, 0, 0, 0);
            Gender = "Female";
        }
        else{
            Glide.with(this).load(userDetails.getProfPic()).into(ProfilePic);
        }

        phno = sharedPreferences.getString("PhoneNumber", "");

        myRef = FirebaseDatabase.getInstance().getReference().child("UserDetails");

        AddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(EditProfileActivity.this)
                        .cropSquare()
                        .compress(512)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    LoaderDialog = new Dialog(EditProfileActivity.this);
                    View vieww = getLayoutInflater().inflate(R.layout.loader_layout, null);
                    ProgressBar progressBar = (ProgressBar) vieww.findViewById(R.id.spinKit);
                    DoubleBounce doubleBounce = new DoubleBounce();
                    progressBar.setIndeterminateDrawable(doubleBounce);
                    LoaderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    LoaderDialog.setContentView(vieww);
                    LoaderDialog.show();

                    userDetails.setName(""+FullName.getText().toString().trim());
                    userDetails.setMobNo(""+phno);
                    userDetails.setEmail(""+Email.getText().toString().trim());
                    userDetails.setCity(""+City.getText().toString().trim());
                    userDetails.setState(""+State.getText().toString().trim());

                    if (file != null) {
                        storageReference = FirebaseStorage.getInstance().getReference();
                        storageReference = storageReference.child("UserProfileImages/" + file.getName());
                        storageReference.putFile(Uri.fromFile(file)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        userDetails.setProfPic(""+uri.toString());

                                        phno = phno.replace("+91", "");
                                        myRef.child(phno).setValue(userDetails);

                                        LoaderDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Profile Setup Successful!", Toast.LENGTH_SHORT).show();

                                        editor.putInt("SetUp", 1);
                                        editor.putString("UserDetails", new Gson().toJson(userDetails));
                                        editor.apply(); editor.commit();

                                        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        LoaderDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Error! Please Try Again Later!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                LoaderDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Error! Please Try Again Later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if (Gender.length() != 0){
                        userDetails.setProfPic(""+Gender);

                        phno = phno.replace("+91", "");
                        myRef.child(phno).setValue(userDetails);

                        LoaderDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Profile Setup Successful!", Toast.LENGTH_SHORT).show();

                        editor.putInt("SetUp", 1);
                        editor.putString("UserDetails", new Gson().toJson(userDetails));
                        editor.apply(); editor.commit();

                        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        phno = phno.replace("+91", "");
                        myRef.child(phno).setValue(userDetails);

                        LoaderDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Profile Setup Successful!", Toast.LENGTH_SHORT).show();

                        editor.putInt("SetUp", 1);
                        editor.putString("UserDetails", new Gson().toJson(userDetails));
                        editor.apply(); editor.commit();

                        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        City.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProfileActivity.this, "Click on GPS Icon to get location", Toast.LENGTH_SHORT).show();
            }
        });

        State.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProfileActivity.this, "Click on GPS Icon to get location", Toast.LENGTH_SHORT).show();
            }
        });

        Male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Male.getPaddingTop() == 20){
                    Male.setPadding(0, 0, 0, 0);
                }
                else{
                    Male.setPadding(20, 20, 20, 20);
                }
                Female.setPadding(0, 0, 0, 0);
                Gender = "Male";
            }
        });

        Female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Female.getPaddingTop() == 20){
                    Female.setPadding(0, 0, 0, 0);
                }
                else{
                    Female.setPadding(20, 20, 20, 20);
                }
                Male.setPadding(0, 0, 0, 0);
                Gender = "Female";
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    Geocoder geocoder = new Geocoder(EditProfileActivity.this);
                    Address address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
                    City.setText(""+address.getLocality());
                    State.setText(""+address.getAdminArea());
                    LoaderDialog.dismiss();
                } catch (IOException e) {
                    Toast.makeText(EditProfileActivity.this, "Error! Please ensure your GPS is turned on!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {
            }
            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        GPSLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3000);
                    return;
                }
                else{
                    LoaderDialog = new Dialog(EditProfileActivity.this);
                    View vieww = getLayoutInflater().inflate(R.layout.loader_layout, null);
                    ProgressBar progressBar = (ProgressBar) vieww.findViewById(R.id.spinKit);
                    DoubleBounce doubleBounce = new DoubleBounce();
                    progressBar.setIndeterminateDrawable(doubleBounce);
                    LoaderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    LoaderDialog.setContentView(vieww);
                    LoaderDialog.setCancelable(false);
                    LoaderDialog.show();

                    locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
                }
            }
        });
    }

    boolean validate(){
        if((FullName.getText().toString().trim().length() == 0) || (FullName.getText().toString().trim().split(" ").length<2)){
            Toast.makeText(this, "Please enter your Full Name! (FirstName LastName)", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if ((Email.getText().toString().trim().length() == 0) || !(Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString().trim()).matches())){
            Toast.makeText(this, "Please enter valid email address!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if((City.getText().toString().trim().length() == 0) || (State.getText().toString().trim().length() == 0)){
            Toast.makeText(this, "Enable GPS and click GPS icon to get State and City!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            file = ImagePicker.Companion.getFile(data);
            Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
            ProfilePic.setImageBitmap(image);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 3000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions Granted, Click on GPS Icon!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}