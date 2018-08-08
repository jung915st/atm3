package com.tom.atm2;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText edUserid;
    private EditText edPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUserid = findViewById(R.id.userid);
        edPasswd = findViewById(R.id.passwd);


    }

    public void login (View view) {

        String userid = edUserid.getText().toString();
        String passwd = edPasswd.getText().toString();
        String username = "jack";
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
        }

    }

    public void cancel (View view) {

    }
}
