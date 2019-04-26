package com.mezan.smsmanager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PRRC=100;
    ListView listView;
    ArrayList smsList;
    Button btnSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.list);
        btnSMS=(Button)findViewById(R.id.btn);
        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS);
                if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                    showContacts();
                }else {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_SMS},PRRC);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PRRC){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                showContacts();
            }else {
                Toast.makeText(MainActivity.this,"Permission not granted",Toast.LENGTH_LONG).show();
            }
        }
    }
    private void showContacts(){
        Uri inboxUri = Uri.parse("content://sms/inbox");
        smsList = new ArrayList();
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(inboxUri,null,null,null,null);
        while (c.moveToNext()){
            String number = c.getString(c.getColumnIndexOrThrow("address")).toString();
            String body = c.getString(c.getColumnIndexOrThrow("body")).toString();
            smsList.add("Number : "+number+"\nBody : "+body);
        }
        c.close();
        ArrayAdapter adapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,smsList);
        listView.setAdapter(adapter);
    }
}
