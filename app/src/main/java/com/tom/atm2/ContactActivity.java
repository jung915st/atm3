package com.tom.atm2;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
//import android.support.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ContactActivity extends Activity {

    private static final int REQUEST_CONTACTS = 200;
    private static final String TAG = ContactActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            readContact();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACTS);
        }
    }

    private void readContact() {
        Cursor MainCursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        while (MainCursor.moveToNext()) {
            int id = MainCursor.getInt(
                    MainCursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = MainCursor.getString(
                    MainCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            int phoneNumber = MainCursor.getInt(
                    MainCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (phoneNumber == 1) {
                Cursor PhoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        new String[]{String.valueOf(id)}, null);
                while (PhoneCursor.moveToNext()) {
                    String phone = PhoneCursor.getString(PhoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                    Log.d(TAG, "readContact: \t" + phone);
                }
            }
            Log.d(TAG, "readContact: " + name);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContact();
            }
        }
    }
}
