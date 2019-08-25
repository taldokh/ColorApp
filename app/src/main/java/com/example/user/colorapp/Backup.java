package com.example.user.colorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Backup extends AppCompatActivity {

    Button backup;

    EditText editText;

    MyDBHandler myDb = MainActivity.getInstance().getMyDb();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        backup = (Button) findViewById(R.id.backup);

        editText = (EditText) findViewById(R.id.string_edit_text);

        backup.setOnClickListener(new View.OnClickListener() { // takes the string in the editText and divides it into unmathcedColors and inserts them to the database
            @Override
            public void onClick(View view) {

                String mailString = editText.getText().toString();

                for (int i = 1; i < mailString.length(); i++){
                    String result = "";
                    String wrongColor = "";
                    while (mailString.charAt(i - 1) != ':') {
                        ++i;
                    }

                    while (mailString.charAt(i) != ','){
                        result += mailString.charAt(i);
                        ++i;
                    }

                    while (mailString.charAt(i - 1) != ':') {
                        ++i;
                    }

                    while (i < mailString.length() && mailString.charAt(i) != ' ') {
                        wrongColor += mailString.charAt(i);
                        ++i;
                    }
                    UnmatchedColor unmatchedColor = new UnmatchedColor(result, wrongColor);
                    myDb.addHandler(unmatchedColor);

                }
                Intent intent = new Intent(Backup.this, SavedColors.class);
                startActivity(intent);
            }
        });
    }


}