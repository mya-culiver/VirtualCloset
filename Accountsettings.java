package com.example.tommyscloset;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.Account;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.HashMap;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class Accountsettings extends AppCompatActivity {

    // Firebase
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    // path to images of user prolfile
    String storagePath = "Users_Profile_Imgs/";

    // Views from xml
    TextView fullName, accEmail, accGender;
    ImageView accImage;
    Button buttonLogout, buttonDelete, buttonReset;
    ProgressBar progressBar;
    FloatingActionButton fab;
    ProgressDialog pd;

    //permissions consts
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    //array of permissions requested
    String cameraPermissions[];
    String storagePermissions[];

    // uri of images
    Uri image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);

        // init permissions
       cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
       storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // init views
        fullName = findViewById(R.id.profileName);
        accEmail = findViewById(R.id.profileEmail);
        accGender = findViewById(R.id.profileGender);
        accImage = findViewById(R.id.profileImage);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonReset = findViewById(R.id.buttonReset);
        progressBar = findViewById(R.id.progressBar);
        fab = findViewById(R.id.fab);
        pd = new ProgressDialog(Accountsettings.this);

        // init Firebase
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference(); // firebase storage reference


        Query query = databaseReference.orderByChild("email").equalTo(fUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // check until required data is found
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    // get data
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String gender = "" + ds.child("gender").getValue();
                    String image = "" + ds.child("image").getValue();

                    // set data
                    fullName.setText(name);
                    accEmail.setText(email);
                    accGender.setText(gender);
                    try {
                        // if image is received then set
                        Picasso.get().load(image).into(accImage);
                    } catch (Exception e) {
                        // if there is any exception while getting image then default image
                        Picasso.get().load(R.drawable.ic_launcher_background).into(accImage);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // fab button click
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        /* Took out to follow https://www.youtube.com/watch?v=8FN0Itw28LE&list=PLs1bCj3TvmWmM-qN3FsCuPTTX-29I8Gh7&index=6
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    fullName.setText(documentSnapshot.getString("fName"));
                }
                if (documentSnapshot != null) {
                    email.setText(documentSnapshot.getString("email"));
                }
            }
        });
         */

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                startActivity( new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Accountsettings.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your " +
                        "account from the system and you won't be able to access the app");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        fUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    startActivity( new Intent(getApplicationContext(), MainActivity.class));
                                    Toast.makeText(getApplicationContext(), "Account Deleted", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetEmail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Receive Reset Link.");
                passwordResetDialog.setView(resetEmail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Get email and send reset link
                                String mail = resetEmail.getText().toString();
                                fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Reset Link Sent To Your Email M7", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error! No Link Sent M8" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        }
                );

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });
    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(Accountsettings.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return  result;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission(){
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(Accountsettings.this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(Accountsettings.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return  result && result1;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission(){
       requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void showEditProfileDialog(){

        // options to show in dialog
        String options[] = {"Edit Profile Picture", "Edit Name", "Edit Gender"};
        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Accountsettings.this);
        // set title
        builder.setTitle("Choose Action");
        // set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // handle dialog item clicks
                if (which == 0){
                    // Edit profile pic clicked
                    pd.setMessage("Updating Profile Picture");
                    showImagePicDialog();
                }
                else if (which == 1){
                    // Edit Name
                    pd.setMessage("Updating Name");
                    // calling method and pass key "name" as parameter to update it's value in database
                    showNameGenderUpdatedialog("name");
                }
                else if (which == 2){
                    // Edit Gender
                    pd.setMessage("Updating Gender");
                    showNameGenderUpdatedialog("gender");

                }
            }
        });

        // create and show dialog
        builder.create().show();
    }

    private void showNameGenderUpdatedialog(final String key) {

        // custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Accountsettings.this);
        builder.setTitle("Update" + key);   // e.g. Update name or update gender

        // set layout of dialog
        LinearLayout linearLayout = new LinearLayout(Accountsettings.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        // add edit text
        final EditText editText = new EditText(Accountsettings.this);
        editText.setHint("Enter " + key); // e.g. Edit name or edit gender
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        // add button to update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // input text from edit text
                String value = editText.getText().toString().trim();
                // validate something is there
                if (!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(fUser.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(Accountsettings.this, "Updated..." , Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(Accountsettings.this, "" + e.getMessage() , Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Toast.makeText(Accountsettings.this, "Please enter " + key + "", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // add button to cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // create and show dialog
        builder.create().show();
    }

    private void showImagePicDialog() {
        // show dialog containing options for Camera and Gallery to pick image

        String options[] = {"Camera", "Gallery"};
        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Accountsettings.this);
        // set title
        builder.setTitle("Pick Image From");
        // set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // handle dialog item clicks
                if (which == 0){
                    // Camera clicked
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }
                }
                else if (which == 1){
                    // Gallery clicked
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        pickFromGallery();
                    }
                }
            }
        });
        // create and show dialog
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Handle if user approves or denies permission request
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                // pick from camera
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted){
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(Accountsettings.this, "Please enable camera & storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length > 0){
                    // pick from gallery
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted){
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(Accountsettings.this, "Please enable storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    // onActivityResult for startActivityForResult(cameraaIntent, IMAGE_PICK_CAMERA_CODE);
    // or ----- startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    // request code will tell us, result code will tell us if
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // After picking image from Camera or Gallery
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                // image picked from gallery, get uri
                image_uri = data.getData();
                uploadProfileCoverPhoto(image_uri);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE){
                // image picked from camera, get uri
                uploadProfileCoverPhoto(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {
        //show progress
        pd.show();
        // path and name of image stored in firebase storage
        String filePathAndName = storagePath + "_" + fUser.getUid();
        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();
                        // check if image was uploaded and uri is recieved
                        if (uriTask.isSuccessful()){
                            // add/update url in user's database
                            HashMap<String, Object> results = new HashMap<>();
                            // First parameter is image, second is the url of image stored in firebase storage, this
                            // will be saved as value against key "image"
                            results.put("image", downloadUri.toString());

                            databaseReference.child(fUser.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // url in database of user is added successfully
                                            // dismiss progress bar
                                            pd.dismiss();
                                            Toast.makeText(Accountsettings.this, "Image updated...", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            pd.dismiss();
                                            Toast.makeText(Accountsettings.this, "Error updating image...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else {
                            pd.dismiss();
                            Toast.makeText(Accountsettings.this, "Some error occurred while uploading", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Accountsettings.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        // put image uri
        image_uri = Accountsettings.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // intent to start camera
        Intent cameraaIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraaIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraaIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);

    }
}
