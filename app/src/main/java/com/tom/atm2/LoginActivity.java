package com.tom.atm2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class LoginActivity extends Activity {

    private static final int REQUEST_CODE_CAMERA = 5;
    private EditText edUserid;
    private EditText edPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int permisson = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permisson == PackageManager.PERMISSION_GRANTED) {
            takePhoto();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        }

        edUserid = findViewById(R.id.userid);
        edPasswd = findViewById(R.id.passwd);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //takePhoto();//test for permission
            }
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    public void login (View view) {

        String userid = edUserid.getText().toString();
        String passwd = edPasswd.getText().toString();
        FirebaseDatabase.getInstance().getReference("users").child(userid).child("password")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String pw = snapshot.getValue().toString();
                        if (pw.equals(passwd)) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("LOGIN")
                                    .setMessage("LOGIN FAILED")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
        /*String username = "jack";
        String passcode = "1234";

        if (username.equals(userid)&&passcode.equals(passwd)){
            setResult(RESULT_OK);
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("LOGIN")
                    .setMessage("LOGIN FAILED")
                    .setPositiveButton("OK",null)
                    .show();
        }*/

    }

    public void cancel (View view) {

    }
}
