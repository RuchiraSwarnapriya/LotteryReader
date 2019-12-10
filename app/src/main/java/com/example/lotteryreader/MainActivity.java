package com.example.lotteryreader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private Button uploadBtn;

    private static final int CAMERA_REQUEST_CODE = 1;

    private StorageReference mStorage;

    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStorage = FirebaseStorage.getInstance().getReference();

        uploadBtn = findViewById(R.id.upload);

        mProgress = new ProgressDialog(this);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

            //Bundle bundle = data.getExtras();

            try {
                mProgress.setMessage("Uploading Image ...");
                mProgress.show();

                Uri uri = data.getData();

                    Log.d("uri", uri.toString());

                StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());

                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        mProgress.dismiss();

                        Toast.makeText(MainActivity.this, "Uploading Finshed ...", Toast.LENGTH_LONG).show();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
