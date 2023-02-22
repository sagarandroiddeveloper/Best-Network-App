package com.app.demo.bestnetworkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public static void Toast_Sp(Context context){
        Toast.makeText(context, "Welcome To Our App ", Toast.LENGTH_SHORT).show();
    }
}