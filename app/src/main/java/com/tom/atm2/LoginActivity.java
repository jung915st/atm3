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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class LoginActivity extends Activity {

    private static final int REQUEST_CODE_CAMERA = 5;
    private EditText edUserid;
    private EditText edPasswd;
    private CheckBox cbRemeber;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int permisson = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permisson == PackageManager.PERMISSION_GRANTED) {
            //takePhoto();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        edUserid = findViewById(R.id.userid);
        edPasswd = findViewById(R.id.passwd);
        cbRemeber = findViewById(R.id.cb_rem_userid);
        cbRemeber.setChecked(getSharedPreferences("atm3", MODE_PRIVATE)
                .getBoolean("REMEMBER_USERID", false));
        cbRemeber.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getSharedPreferences("atm3", MODE_PRIVATE)
                    .edit().putBoolean("REMEMBER_USERID", isChecked)
                    .apply();

        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();//test for permission
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
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
                            boolean remember = getSharedPreferences("atm3", MODE_PRIVATE)
                                    .getBoolean("REMEMBER_USERID", false);
                            if (remember) {
                                getSharedPreferences("atm3", MODE_PRIVATE)
                                        .edit().putString("USERID", userid)
                                        .apply();
                            }
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

    public void cancel(View view) {

    }

}
