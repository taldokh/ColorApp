package com.example.user.colorapp;

public class UnmatchedColor {

    private String result;
    private String wrongColor;

    public UnmatchedColor(){}

    public UnmatchedColor(String result, String wrongColor){

        this.result = result; // the real color
        this.wrongColor = wrongColor; // the wrong color you saw
    }

    public void setResult(String result){
        this.result = result;
    }

    public String getResult(){
        return result;
    }

    public void setWrongColor(String wrongColor){
        this.wrongColor = wrongColor;
    }

    public String getWrongColor(){
        return wrongColor;
    }
}
