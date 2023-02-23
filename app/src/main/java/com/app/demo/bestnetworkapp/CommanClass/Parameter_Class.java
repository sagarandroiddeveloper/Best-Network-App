package com.app.demo.bestnetworkapp.CommanClass;

import com.app.demo.bestnetworkapp.Model.CountryList;

import java.util.ArrayList;
import java.util.List;

public class Parameter_Class {

    public static boolean server_Start = false;

    public static List<CountryList> countryLists =  new ArrayList<>();
    public static Boolean Server_random= false;
    public static Boolean Server_Show= true;
    public static Boolean Server_Direct_Connect= true;
    public static String Privacy_policy= "";

    // TODO :  ID PASS
    public static String Server_Id = "touchvpn";
    public static String Server_password = "Nopassword";

    // TODO : URL
    public static Boolean url_Type= false;
    public static String Server_Url_Default = "https://backend.northghost.com";
    public static List<String> unknown_url_list = new ArrayList<>();


    // TODO : DEFAULT COUNTRY CONNECT
    public static String Server_code = "US";
    public static String Server_name = "United States";
    public static String Server_imageurl = "http://157.245.125.183:1161/upload/countryimg-1653906499006.png";

}
