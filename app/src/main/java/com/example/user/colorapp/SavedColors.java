package com.example.user.colorapp;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SavedColors extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    ArrayList<UnmatchedColor> arrayList;
    ColorAdapter adapter;
    MyDBHandler myDb = MainActivity.getInstance().getMyDb();
    private FloatingActionButton share;

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){ // deletes the selected item in the list

        UnmatchedColor item = arrayList.get(position);
        myDb.deleteHandler(item.getResult());
        arrayList.remove(item);
        adapter = new ColorAdapter(this, arrayList);
        listView.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_colors);

        share = (FloatingActionButton) findViewById(R.id.share);

        listView = (ListView) findViewById(R.id.list_view);

        arrayList = new ArrayList<>();

        arrayList = loadUnmatchedColors();

        adapter = new ColorAdapter(this, arrayList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.share) {
            share();
        }
        return super.onOptionsItemSelected(item);
    }



    public ArrayList<UnmatchedColor> loadUnmatchedColors() { // returnes an array list of unmatchedColors from the database
        MyDBHandler dbHandler = new MyDBHandler(this);
        return dbHandler.loadHandler();
    }

    public void share(){  // sets an orderd messege of all the colors in the list and allows the user to share it

        String mail = "";

        ArrayList<UnmatchedColor> arrayList = loadUnmatchedColors();

        for (int i = 0; i < arrayList.size(); i++){

            UnmatchedColor unmatchedColor = arrayList.get(i);
            String result = unmatchedColor.getResult();
            String wrongColor = unmatchedColor.getWrongColor();
            mail += "Real Color:" + result + ", The Color That I See:" + wrongColor + " \n ";
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Color App - your Colors");
        intent.putExtra(Intent.EXTRA_TEXT, mail);

        startActivity(Intent.createChooser(intent, "Send Email"));
    }


}
