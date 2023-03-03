package com.app.demo.bestnetworkapp.CommanClass;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.demo.bestnetworkapp.MainActivity;
import com.app.demo.bestnetworkapp.Model.CountryList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Prefrences_network {

    private static final String url_type = "url_type";
    private static final String url_default = "url_default";
    private static final String country_list = "country_list";
    private static final String isServerConnect = "isServerConnect";
    private static final String server_Show = "server_Show";
    private static final String Server_id = "Server_id";
    private static final String Server_password = "Server_password";
    private static final String aura_user_id = "aura_user_id";
    private static final String accessToken = "accessToken";
    private static final String direct_connect = "direct_connect";
    private static final String rendomserver = "rendomserver";
    private static final String server_short = "server_short";
    private static final String server_name = "server_name";
    private static final String server_image = "server_image";
    private static final String server_policy = "server_policy";


    public static List<CountryList> getCountry_list() {
        Gson gson = new Gson();
        String json = get().getString(country_list, null);
        Type type = new TypeToken<ArrayList<CountryList>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void setCountry_list(List<CountryList> playlistArray) {
        Gson gson = new Gson();
        String hashMapString = gson.toJson(playlistArray);
        get().edit().putString(country_list, hashMapString).apply();

    }

    public static void set_server_policy(String value) {
        get().edit().putString(server_policy, value).apply();
    }

    public static String getserver_policy() {
        return get().getString(server_policy, "");
    }

    public static void set_server_short(String value) {
        get().edit().putString(server_short, value).apply();
    }

    public static String getServer_short() {
        return get().getString(server_short, "US");
    }


    public static void setserver_name(String value) {
        get().edit().putString(server_name, value).apply();
    }

    public static String getserver_name() {
        return get().getString(server_name, "");
    }

    public static void setServer_image(String value) {
        get().edit().putString(server_image, value).apply();
    }

    public static String getServer_image() {
        return get().getString(server_image, "");
    }


    public static void setRendomserver(boolean value) {
        get().edit().putBoolean(rendomserver, value).apply();
    }

    public static boolean getRendomserver() {
        return get().getBoolean(rendomserver, false);
    }

    public static void setdirect_connect(boolean value) {
        get().edit().putBoolean(direct_connect, value).apply();
    }

    public static boolean getdirect_connect() {
        return get().getBoolean(direct_connect, false);
    }


    public static void setAccessToken(String value) {
        get().edit().putString(accessToken, value).apply();
    }

    public static String getAccessToken() {
        return get().getString(accessToken, "");
    }

    public static void setAura_user_id(long value) {
        get().edit().putLong(aura_user_id, value).apply();
    }

    public static long getAura_user_id() {
        return get().getLong(aura_user_id, 0);
    }


    public static void setUrl_default(String value) {
        get().edit().putString(url_default, value).apply();
    }

    public static String getUrl_default() {
        return get().getString(url_default, "https://d2isj403unfbyl.cloudfront.net");
    }

    public static void setUrl_type(Boolean value) {
        get().edit().putBoolean(url_type, value).apply();
    }

    public static Boolean getUrl_type() {
        return get().getBoolean(url_type, false);
    }


    private static SharedPreferences get() {
        return MainActivity.contexts.getSharedPreferences(
                "MyApp_Controller", Context.MODE_PRIVATE);

    }

    public static boolean getisServerConnect() {
        return get().getBoolean(isServerConnect, false);
    }

    public static void setisServerConnect(boolean value) {
        get().edit().putBoolean(isServerConnect, value).apply();
    }

    public static void setserver_Show(boolean value) {
        get().edit().putBoolean(server_Show, value).apply();
    }

    public static boolean getserver_Show() {
        return get().getBoolean(server_Show, false);
    }

    public static String getServer_id() {
        return get().getString(Server_id, "no_vpn");
    }

    public static void setServer_id(String value) {
        get().edit().putString(Server_id, value).apply();
    }


    public static String getServer_password() {
        return get().getString(Server_password, "ZEziW6v0O3");
    }

    public static void setServer_password(String value) {
        get().edit().putString(Server_password, value).apply();
    }



}
