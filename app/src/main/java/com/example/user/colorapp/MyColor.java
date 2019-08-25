package com.example.user.colorapp;

public class MyColor { // a class that stores a color rgb

    int red;

    int green;

    int blue;

    public MyColor(int red, int green, int blue){

        this.red = red;

        this.green = green;

        this.blue = blue;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
}
