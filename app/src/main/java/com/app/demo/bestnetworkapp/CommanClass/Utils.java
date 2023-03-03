package com.app.demo.bestnetworkapp.CommanClass;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.app.demo.bestnetworkapp.Model.CountryList;
import com.app.demo.bestnetworkapp.R;

import java.util.List;
import java.util.Random;

public class Utils {

    public static List<CountryList> country_List = null;

    public static void setUpCountry() {
        Utils.country_List = Prefrences_network.getCountry_list();
        if (Utils.country_List == null && Utils.country_List.size() == 0) {
            return;
        }
        int pos = new Random().nextInt(Utils.country_List.size());
        if (Utils.country_List.size() > 2 && Utils.country_List.get(pos).name.equals(Prefrences_network.getserver_name())) {
            setUpCountry();
            return;
        }

        String selectedCountryCode = Utils.country_List.get(pos).code;
        String selectedCountryName = Utils.country_List.get(pos).name;
        String selectedCountryImage = Utils.country_List.get(pos).cuntryimages;
        Log.d("selectedCountry", "pos = " + pos);
        Log.d("selectedCountry", "selectedCountry = " + selectedCountryCode);
        Log.d("selectedCountry", "selectedCountryName = " + selectedCountryName);
        Prefrences_network.set_server_short(selectedCountryCode);
        Prefrences_network.setserver_name(selectedCountryName);
        Prefrences_network.setServer_image(selectedCountryImage);
    }

    private static OnCheckNet onChecknet;

    public interface OnCheckNet {
        void OnCheckNet(boolean b);
    }

    public static void isConnectingToInternet(Context context, OnCheckNet onChecknets, boolean... booleans) {
        onChecknet = onChecknets;
        CheckInternetData(context);
    }

    private static void CheckInternetData(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            onChecknet.OnCheckNet(true);
        } else {
            try {
                showAlertDialog(context, context.getString(R.string.app_name),
                        context.getString(R.string.disconnected),
                        "Retry");
            } catch (NumberFormatException ex) { // handle your exception
            }
        }
    }

    public static void showAlertDialog(Context context, String title, String msg, String positiveText) {
        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CheckInternetData(context);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onChecknet.OnCheckNet(false);
                    }
                })
                .show();
    }


    public static Dialog dialog;

    public static void pdialog(Context context) {
        try {
            if (dialog != null)
                if (dialog.isShowing())
                    //dialog.dismiss();
                    return;
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.custom_progressbar);
            TextView textView = (TextView) dialog.findViewById(R.id.txt_label);
            textView.setText("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void pdialog_dismiss() {
        try {
            if (dialog.isShowing())
                dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
