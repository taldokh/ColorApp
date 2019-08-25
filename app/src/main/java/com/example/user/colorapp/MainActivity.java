package com.example.user.colorapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public MyDBHandler myDb; // DataBase
    FloatingActionButton btnCamera;
    FloatingActionButton btnGallary;
    ImageView closeBtn;
    Button btnSave;
    public static TextView wellComeTxtv;
    EditText editTextView; // edit text to save the color you seen in the image


    private static MainActivity instance;

    private static final int TAKE_PICTURE = 1; // request Code for camera intent

    private static final int PICK_IMAGE = 2; // request Code for gallary intent

    private static final int GET_COLOR = 3; // request Code for getting color from Image activity

    final Context context = this;

    private static final int PERMISSIONS_REQUEST_CODE = 5;

    public boolean permissionCamera = true;

    public boolean permissionStorage = true;

    Dialog dialog;

    MyReceiver receiver;

    IntentFilter inf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new MyDBHandler(this);

        verifyPermissions();

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        final ListView listView = (ListView) findViewById(R.id.list_view); // list view of the the color you seen and the real color - in the savedColors activity
        btnSave = (Button) dialog.findViewById(R.id.btnsave);
        editTextView = (EditText) dialog.findViewById(R.id.editTextView);
        editTextView.setText("");
        btnCamera = (FloatingActionButton) findViewById(R.id.btnCamera); // // camera intent button
        btnGallary = (FloatingActionButton) findViewById(R.id.btnGallary); // gallary intent button
        wellComeTxtv = (TextView) findViewById(R.id.colorView); //WellCome text view
        closeBtn = (ImageView) dialog.findViewById(R.id.close);
        instance = this;

        receiver = new MyReceiver();
        inf = new IntentFilter();
        inf.addAction("android.intent.action.BATTERY_LOW");
        registerReceiver(receiver, inf);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() { // adds the string in the wellComeTxtv TextView ( the real color ) and the string in the editTextView ( the wrong color ) to the database
            @Override
            public void onClick(View view) {
                addUnmatchedColor(); // a function to add unmatchedColor variables to the data base and list in saved colors
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){ // start camera intent
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(permissionCamera) {
                        startActivityForResult(intent, TAKE_PICTURE);
                    }
                    else{
                        verifyPermissions();
                    }
                }
            });


        btnGallary.setOnClickListener(new View.OnClickListener(){ // start gallary intent
            @Override
            public void onClick(View view){
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                if(permissionStorage)
                    startActivityForResult(galleryIntent, PICK_IMAGE);
                else {
                    verifyPermissions();
                }

            }
        });
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerReceiver(receiver, inf);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.navigationmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.savedColors:
                Intent intent = new Intent(MainActivity.this, SavedColors.class);
                startActivity(intent);
                break;

            case R.id.backup:
                Intent intent2 = new Intent(MainActivity.this, Backup.class);
                startActivity(intent2);
                break;
        }

        return true;
    }

    public void addUnmatchedColor() { // taking the unmatchedColor strings and adding them the the addHandler function ( in MyDBHandler ) so they will enter the data base

        editTextView = (EditText) dialog.findViewById(R.id.editTextView);
        String result = wellComeTxtv.getText().toString();
        String wrongColor = editTextView.getText().toString();
        UnmatchedColor unmatchedColor = new UnmatchedColor(result, wrongColor);
            boolean isInserted = myDb.addHandler(unmatchedColor);
        wellComeTxtv.setText("welcome \nPlease take a photo or select an image to get the color"); // after the data enters the data base, the home page will return to its defult.
        editTextView.setText("");
        dialog.dismiss();
        if (isInserted)
            Toast.makeText(MainActivity.this, "added", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MainActivity.this, "this color is already in the list", Toast.LENGTH_LONG).show();
    }



    Bitmap bitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PICTURE) { // if the intent that is selected is to take a picture than it will take the picture and display it in a new activity so you can choose your pixel by touch
                if (resultCode == RESULT_OK) {

                    try {
                        Bundle extras = data.getExtras();
                        bitmap = (Bitmap) extras.get("data");
                        Intent intent = new Intent(MainActivity.this, Image.class);
                        intent.putExtra("bitmap", bitmap);
                        startActivityForResult(intent, GET_COLOR);
                    } catch (Exception e) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        }


        if (requestCode == PICK_IMAGE){
            if (resultCode == RESULT_OK) {

                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                    byte[] byteArray = stream.toByteArray();

                    Intent intent = new Intent(MainActivity.this, Image.class);
                    intent.putExtra("byteArray", byteArray);
                    intent.putExtra("requestCode", PICK_IMAGE);
                    startActivityForResult(intent, GET_COLOR);
                } catch (Exception e) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }

        if (requestCode == GET_COLOR){
            if(resultCode == RESULT_OK){
                startService(new Intent(this, MyService.class));
                dialog.show();
            }
        }

    }




    public static MainActivity getInstance() {
        return instance;
    }

    public MyDBHandler getMyDb(){
        return myDb;
    }

    private void verifyPermissions(){

        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[0]) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[1]) == PackageManager.PERMISSION_DENIED ){
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSIONS_REQUEST_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(grantResults[0] == -1)
            permissionCamera = false;
        else
            permissionCamera = true;

        if(grantResults[1] == -1)
            permissionStorage = false;
        else
            permissionStorage = true;

    }
}

