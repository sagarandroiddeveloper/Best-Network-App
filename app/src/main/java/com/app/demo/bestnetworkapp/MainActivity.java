package com.app.demo.bestnetworkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.app.demo.bestnetworkapp.CommanClass.Parameter_Class;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public static OnConnect onConnect;

    public static interface OnConnect {
        void onconnect(String connect);
    }

    public static Activity contexts;

    public static void Toast_Sp(Activity context, OnConnect onConnect_new) {
        contexts = context;
        onConnect = onConnect_new;
        Toast.makeText(context, "Welcome To Our Apps", Toast.LENGTH_SHORT).show();
        onConnect.onconnect("OK");
    }


    public static void stop(OnConnect onConnect_new) {
        onConnect = onConnect_new;
        onConnect.onconnect("stop");
    }

    public static void start() {
        onConnect.onconnect("stop");
    }
}