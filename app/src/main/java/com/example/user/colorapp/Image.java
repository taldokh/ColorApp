package com.example.user.colorapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Image extends AppCompatActivity {

    private static final int TAKE_PICTURE = 1; // request Code for camera intent

    private static final int PICK_IMAGE = 2; // request Code for gallary intent

    private static final int GET_COLOR = 3; // request Code for getting color from Image activity

    enum ColorNames // enum of all the main colors names
    {
        RED, LIGHT_RED, DARK_RED, GREEN, LIGHT_GREEN, DARK_GREEN, BLUE, LIGHT_BLUE, DARK_BLUE, BLACK, WHITE, YELLOW, LIGHT_YELLOW, DARK_YELLOW, PINK, LIGHT_PINK, DARK_PINK, ORANGE, LIGHT_ORANGE, DARK_ORANGE, PURPLE, LIGHT_PURPLE, DARK_PURPLE
    }

    private MyColor[] getKnownColors() {  // gets an array of MyColor with all the main colors rgb details
        MyColor[] knownColors = new MyColor[ColorNames.values().length];

        MyColor red = new MyColor(255,0,0);

        MyColor lightRed = new MyColor(255,128,128);

        MyColor darkRed = new MyColor(128,0,0);

        MyColor green = new MyColor(0,255,0);

        MyColor lightGreen = new MyColor(102,255,153);

        MyColor darkGreen = new MyColor(0,128,43);

        MyColor blue = new MyColor(0,0,255);

        MyColor lightBlue = new MyColor(0,51,128);

        MyColor darkBlue = new MyColor(0,0,153);

        MyColor black = new MyColor(0,0,0);

        MyColor white = new MyColor(191,191,191);

        MyColor yellow = new MyColor(255,255,0);

        MyColor lightYellow = new MyColor(255,255,128);

        MyColor darkYellow = new MyColor(179,179,0);

        MyColor pink = new MyColor(255,0,255);

        MyColor lightPink = new MyColor(255,128,255);

        MyColor darkPink = new MyColor(128,0,128);

        MyColor orange = new MyColor(255,153,0);

        MyColor lightOrange = new MyColor(255,194,102);

        MyColor darkOrange = new MyColor(179,107,0);

        MyColor purple = new MyColor(191,0,255);

        MyColor lightPurple = new MyColor(223,128,255);

        MyColor darkPurple = new MyColor(96,0,128);

        knownColors[ColorNames.RED.ordinal()] = red;
        knownColors[ColorNames.LIGHT_RED.ordinal()] = lightRed;
        knownColors[ColorNames.DARK_RED.ordinal()] = darkRed;
        knownColors[ColorNames.GREEN.ordinal()] = green;
        knownColors[ColorNames.LIGHT_GREEN.ordinal()] = lightGreen;
        knownColors[ColorNames.DARK_GREEN.ordinal()] = darkGreen;
        knownColors[ColorNames.BLUE.ordinal()] = blue;
        knownColors[ColorNames.LIGHT_BLUE.ordinal()] = lightBlue;
        knownColors[ColorNames.DARK_BLUE.ordinal()] = darkBlue;
        knownColors[ColorNames.BLACK.ordinal()] = black;
        knownColors[ColorNames.WHITE.ordinal()] = white;
        knownColors[ColorNames.YELLOW.ordinal()] = yellow;
        knownColors[ColorNames.LIGHT_YELLOW.ordinal()] = lightYellow;
        knownColors[ColorNames.DARK_YELLOW.ordinal()] = darkYellow;
        knownColors[ColorNames.PINK.ordinal()] = pink;
        knownColors[ColorNames.LIGHT_PINK.ordinal()] = lightPink;
        knownColors[ColorNames.DARK_PINK.ordinal()] = darkPink;
        knownColors[ColorNames.ORANGE.ordinal()] = orange;
        knownColors[ColorNames.LIGHT_ORANGE.ordinal()] = lightOrange;
        knownColors[ColorNames.DARK_ORANGE.ordinal()] = darkOrange;
        knownColors[ColorNames.PURPLE.ordinal()] = purple;
        knownColors[ColorNames.LIGHT_PURPLE.ordinal()] = lightPurple;
        knownColors[ColorNames.DARK_PURPLE.ordinal()] = darkPurple;

        return knownColors;
    }

    public static ImageView takenShot; // image view of the picture that was taken

    Bitmap bitmap;

    String pixelColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);


        int requestCode = getIntent().getIntExtra("requestCode", 1); // get the request code from main ( if the user chose to take a picture with the camera or choose an image from the gallary )

        takenShot = (ImageView) findViewById(R.id.cameraShotImg);

        if(requestCode == TAKE_PICTURE)
            bitmap = getIntent().getParcelableExtra("bitmap");
        else if (requestCode == PICK_IMAGE) {
            byte[] byteArray = getIntent().getByteArrayExtra("byteArray");
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }

        takenShot.setImageBitmap(bitmap);

        bitmap = Bitmap.createScaledBitmap(bitmap,2000, 2000, true);

        takenShot.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                int x = (int)event.getX();
                int y = (int)event.getY();
                int pixel = bitmap.getPixel(x,y); // get the pixel in position x and y

                int redValue = Color.red(pixel); // the pixel red attribute
                int greenValue = Color.green(pixel); // the pixel green attribute
                int blueValue = Color.blue(pixel); // the pixel blue attribute

                MyColor[] knownColors = getKnownColors(); // the array of all the main colors

                pixelColor = getPixelColor(redValue, greenValue, blueValue, knownColors); // the method returnes the pixel main color
                Toast toast = Toast.makeText(getApplicationContext(), deleteUnderScore(pixelColor), Toast.LENGTH_LONG);
                toast.show();
                MainActivity.wellComeTxtv.setText(deleteUnderScore(pixelColor));
                Intent intent = new Intent(Image.this, MainActivity.class);
                setResult(RESULT_OK, intent);
                finish();
                return false;
            }
        });
    }

    private String getPixelColor(int r, int g, int b, MyColor[] knownColors){ // returnes a string of the color that his RGB is the closest to color that was chosen by touch

        ColorNames[] colorsArray = ColorNames.values(); // an array of colors names
        String result = colorsArray[0].name();
        double min = distance(r, g, b, knownColors[0]);
        for ( int i = 1; i < knownColors.length; i++){
            if (min > distance(r,g,b, knownColors[i])){
                result = colorsArray[i].name();
                min = distance(r,g,b, knownColors[i]);
            }
        }

        return result;

    }

    public String deleteUnderScore(String string){ // if the resulted color name include an underscore ( becuse of the enum "ColorNames" ) then it will delete the underscore and return the final color name to display to the user

        for (int i = 0; i < string.length(); i++){

            if(string.charAt(i) == '_') {
                string = string.substring(0, i) + " " + string.substring(i + 1);
                i = string.length();
            }
        }

        return string;
    }

    private double distance(int r, int g, int b, MyColor myColor){ // the distance bewteen two RGB

        return Math.abs(Math.pow(r - myColor.getRed(),2 ) + Math.pow(g - myColor.getGreen(),2 ) + Math.pow(b - myColor.getBlue(),2 ));
    }
}
