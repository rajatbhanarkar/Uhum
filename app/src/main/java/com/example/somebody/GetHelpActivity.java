package com.example.somebody;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class GetHelpActivity extends AppCompatActivity {

    CardView Food, Medical, Financial, Shelter, Other;
    TextView Post, More, Less;
    boolean sel1 = false, sel2 = false, sel3 = false, sel4 = false, sel5 = false;
    ImageView Back, Upload, UploadedImage;
    EditText add1, city, NOP, description, phone;
    String category = "";

    ProgressDialog progressDialog;
    StorageReference storageReference;
    DatabaseReference myRef;

    File file;

    HelpDetails helpDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_help);

        Food = (CardView) findViewById(R.id.cvhsacard1);
        Medical = (CardView) findViewById(R.id.cvhsacard2);
        Financial = (CardView) findViewById(R.id.cvhsacard3);
        Shelter = (CardView) findViewById(R.id.cvhsacard4);
        Other = (CardView) findViewById(R.id.cvhsacard5);
        Post = (TextView) findViewById(R.id.tvghaconfirm);

        add1 = (EditText) findViewById(R.id.etadd1);
        city = (EditText) findViewById(R.id.etcity);
        phone = (EditText) findViewById(R.id.etghaphonenumber);
        description = (EditText) findViewById(R.id.etdescription);

        Less = (TextView) findViewById(R.id.tvghaless);
        More = (TextView) findViewById(R.id.tvghamore);
        NOP = (EditText) findViewById(R.id.etghanop);

        Back = (ImageView) findViewById(R.id.ivghaback);

        Upload = (ImageView) findViewById(R.id.ivghaupload);
        UploadedImage = (ImageView) findViewById(R.id.ivghauploadedimage);

        myRef = FirebaseDatabase.getInstance().getReference().child("AskForHelp").child("UID000001");

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel1) {
                    deselect((RelativeLayout) Food.getChildAt(0));
                    sel1 = false;
                } else {
                    select((RelativeLayout) Food.getChildAt(0));
                    sel1 = true;
                }
            }
        });

        Medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel2) {
                    deselect((RelativeLayout) Medical.getChildAt(0));
                    sel2 = false;
                } else {
                    select((RelativeLayout) Medical.getChildAt(0));
                    sel2 = true;
                }
            }
        });

        Financial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel3) {
                    deselect((RelativeLayout) Financial.getChildAt(0));
                    sel3 = false;
                } else {
                    select((RelativeLayout) Financial.getChildAt(0));
                    sel3 = true;
                }
            }
        });

        Shelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel4) {
                    deselect((RelativeLayout) Shelter.getChildAt(0));
                    sel4 = false;
                } else {
                    select((RelativeLayout) Shelter.getChildAt(0));
                    sel4 = true;
                }
            }
        });

        Other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel5) {
                    deselect((RelativeLayout) Other.getChildAt(0));
                    sel5 = false;
                } else {
                    select((RelativeLayout) Other.getChildAt(0));
                    sel5 = true;
                }
            }
        });

        More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NOP.getText().toString().trim().equals("")) {
                    NOP.setText("1");
                }
                int num = Integer.parseInt(NOP.getText().toString());
                if (num < 999) {
                    num++;
                }
                NOP.setText("" + num);
                NOP.clearFocus();
            }
        });

        Less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NOP.getText().toString().trim().equals("")) {
                    NOP.setText("1");
                }
                int num = Integer.parseInt(NOP.getText().toString());
                if (num > 1) {
                    num--;
                }
                NOP.setText("" + num);
                NOP.clearFocus();
            }
        });

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(GetHelpActivity.this)
                        .compress(512)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category += (sel1)?("Food "):("");
                category += (sel2)?("Medical "):("");
                category += (sel3)?("Financial "):("");
                category += (sel4)?("Shelter "):("");
                category += (sel4)?("Other "):("");

                progressDialog = new ProgressDialog(GetHelpActivity.this);
                progressDialog.setMessage("Posting...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("AskForHelpImages/UID000001/" + file.getName());
                storageReference.putFile(Uri.fromFile(file)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                helpDetails = new HelpDetails();
                                helpDetails.setUserID("UID000001");
                                helpDetails.setHelpAddress(add1.getText().toString() + ", " + city.getText().toString());
                                helpDetails.setDescription(description.getText().toString());
                                helpDetails.setNop(Integer.parseInt(NOP.getText().toString()));
                                helpDetails.setHelpContact(phone.getText().toString());
                                helpDetails.setHelpPicLink(uri.toString());
                                helpDetails.setCategory(category);

                                myRef.setValue(helpDetails);

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Successfully Posted!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(GetHelpActivity.this, "Error in posting!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error! Please Try Again Later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    void select(RelativeLayout view) {
        view.setBackgroundTintList(getResources().getColorStateList(R.color.darkcerulian));
        TextView tv = (TextView) view.getChildAt(1);
        tv.setTextColor(Color.parseColor("#ffffff"));
    }

    void deselect(RelativeLayout view) {
        view.setBackgroundTintList(null);
        TextView tv = (TextView) view.getChildAt(1);
        tv.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            file = ImagePicker.Companion.getFile(data);
            Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
            UploadedImage.setImageBitmap(image);
        }
    }
}
