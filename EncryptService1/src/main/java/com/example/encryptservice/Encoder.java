package com.example.encryptservice;

import java.util.Arrays;
public class Encoder {

    private static int index = 19;
    private static String[] alphabet = {"a","ą","b","c","ć","d","e","ę","f","g","h","i","j","k","l","ł","m","n","ń","o","ó","p","q","r","s","ś","t","u","v","w","x","y","z","ź","ż",".",",","!"};

    public String encode(String text){
        String new_text = "";
        String[] text_arr = text.split("");
        for(String x : text_arr){
            int idx = Arrays.asList(alphabet).indexOf(x.toLowerCase());
            if(idx != -1){
                new_text += alphabet[(idx+index)%38];
            }else{
                new_text += x;
            }
        }
        return new_text;
    }

}
