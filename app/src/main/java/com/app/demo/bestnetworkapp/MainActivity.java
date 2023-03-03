package com.app.demo.bestnetworkapp;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anchorfree.partner.api.ClientInfo;
import com.anchorfree.partner.api.auth.AuthMethod;
import com.anchorfree.partner.api.response.User;
import com.anchorfree.reporting.TrackingConstants;
import com.anchorfree.sdk.HydraTransportConfig;
import com.anchorfree.sdk.NotificationConfig;
import com.anchorfree.sdk.SessionConfig;
import com.anchorfree.sdk.TransportConfig;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.sdk.rules.TrafficRule;
import com.anchorfree.vpnsdk.callbacks.CompletableCallback;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.anchorfree.vpnsdk.transporthydra.HydraTransport;
import com.anchorfree.vpnsdk.vpnservice.VPNState;
import com.app.demo.bestnetworkapp.CommanClass.Parameter_Class;
import com.app.demo.bestnetworkapp.CommanClass.Prefrences_network;
import com.app.demo.bestnetworkapp.CommanClass.Server_Interface;
import com.app.demo.bestnetworkapp.CommanClass.Utils;
import com.app.demo.bestnetworkapp.Model.TraficLimitResponse;
import com.northghost.caketube.CaketubeTransport;
import com.northghost.caketube.OpenVpnTransportConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show();

        SetupData();
    }

    public static void disconnectFromVnp(OnConnect onConnect_new) {
        UnifiedSDK.getInstance().getVPN().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
            @Override
            public void complete() {
                Parameter_Class.server_Start = false;
                Prefrences_network.setisServerConnect(false);
                onConnect.onconnect("disconnect");
            }

            @Override
            public void error(@NonNull VpnException e) {
                onConnect.onconnect("disconnect");
            }
        });
    }

    private static void SetupData() {

        Utils.country_List = Parameter_Class.countryLists;
        Prefrences_network.setCountry_list(Utils.country_List);
// TODO :  ID PASS
        Prefrences_network.setServer_id(Parameter_Class.Server_Id);
        Prefrences_network.setServer_password(Parameter_Class.Server_password);


        // TODO : URL
        Prefrences_network.setUrl_type(Parameter_Class.url_Type);
        Prefrences_network.setUrl_default(Parameter_Class.Server_Url_Default);


        // TODO : SERVER CONNECTION
        Prefrences_network.setRendomserver(Parameter_Class.Server_random);
        Prefrences_network.setserver_Show(Parameter_Class.Server_Show);
        Prefrences_network.setdirect_connect(Parameter_Class.Server_Direct_Connect);


        // TODO : DEFAULT SERVER CONNECT
        Prefrences_network.set_server_short(Parameter_Class.Server_code);
        Prefrences_network.setserver_name(Parameter_Class.Server_name);
        Prefrences_network.setServer_image(Parameter_Class.Server_imageurl);
        CheckConnection();

    }

    private static void CheckConnection() {

        UnifiedSDK.getVpnState(new com.anchorfree.vpnsdk.callbacks.Callback<VPNState>() {

            @Override
            public void success(@NonNull VPNState vpnState) {
                switch (vpnState) {
                    case IDLE: {
                        Prefrences_network.setisServerConnect(false);
                        Server_Connection();
                        break;
                    }
                    case CONNECTED: {
                        Prefrences_network.setisServerConnect(true);
                        onConnect.onconnect("connect");
                        break;
                    }
                    case CONNECTING_VPN: {

                        break;
                    }

                    case CONNECTING_CREDENTIALS:
                    case CONNECTING_PERMISSIONS:
                    case PAUSED: {
                        break;
                    }
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                onConnect.onconnect("disconnect");
            }
        });


    }

    public static void Server_Connection() {
        if (Prefrences_network.getserver_Show()) {
            Server_Initialize();
        } else {
            onConnect.onconnect("disconnect");
            ;
        }
    }

    static UnifiedSDK unifiedSDK;
    static String Server_Key = "";
    static String Server_Password = "";
    private static final String CHANNEL_ID = "Server_Master";

    public static void Server_Initialize() {

        Server_Key = Prefrences_network.getServer_id();
        Server_Password = Prefrences_network.getServer_password();

        createNotificationChannel();

        ClientInfo clientInfo;

        if (Prefrences_network.getUrl_type()) {
            clientInfo = ClientInfo.newBuilder()
                    .addUrls(Parameter_Class.unknown_url_list)
                    .carrierId(Server_Key)
                    .build();
        } else {
            clientInfo = ClientInfo.newBuilder()
                    .addUrl(Prefrences_network.getUrl_default())
                    .carrierId(Server_Key)
                    .build();
        }

        List<TransportConfig> transportConfigList = new ArrayList<>();
        transportConfigList.add(HydraTransportConfig.create());
        transportConfigList.add(OpenVpnTransportConfig.tcp());
        transportConfigList.add(OpenVpnTransportConfig.udp());
        UnifiedSDK.update(transportConfigList, CompletableCallback.EMPTY);
        unifiedSDK = UnifiedSDK.getInstance(clientInfo);
        NotificationConfig notificationConfig = NotificationConfig.newBuilder()
                .title(contexts.getResources().getString(R.string.app_namess))
                .channelId(CHANNEL_ID)
                .build();
        UnifiedSDK.update(notificationConfig);

        LoginToServer();
    }

    private static void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Server_Master";
            String description = "Server notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = contexts.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void LoginToServer() {
        AuthMethod authMethod = AuthMethod.anonymous();
        UnifiedSDK.getInstance().getBackend().login(authMethod, new com.anchorfree.vpnsdk.callbacks.Callback<User>() {
            @Override
            public void success(@NonNull User user) {
                Prefrences_network.setAura_user_id(user.getSubscriber().getId());
                LoginAPi_Token();
            }

            @Override
            public void failure(@NonNull VpnException e) {
                Prefrences_network.setserver_Show(false);
                onConnect.onconnect("disconnect");
                ;
            }
        });

    }

    private static void LoginAPi_Token() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-prod.northghost.com/partner/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Server_Interface apiInterface_local = retrofit.create(Server_Interface.class);
        Call<TraficLimitResponse> call = apiInterface_local.Call_Add_Trafic("login?login=" + Server_Key + "&password=" + Server_Password);
        call.enqueue(new Callback<TraficLimitResponse>() {
            @Override
            public void onResponse(Call<TraficLimitResponse> call, Response<TraficLimitResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().result.equals("OK")) {
                        Prefrences_network.setAccessToken(response.body().access_token);
                        IntentActivity();
                    } else {
                        IntentActivity();
                    }
                } else {
                    IntentActivity();
                }
            }

            @Override
            public void onFailure(Call<TraficLimitResponse> call, Throwable t) {
                IntentActivity();
            }
        });
    }

    private static void IntentActivity() {
        if (Prefrences_network.getserver_Show()) {
            if (Prefrences_network.getdirect_connect()) {
                AutoVNStart();
                return;
            }
        }
        onConnect.onconnect("disconnect");
        ;
    }

    private static void AutoVNStart() {
        if (Prefrences_network.getRendomserver()) {
            Utils.setUpCountry();
        }
        ConnectVN();
    }


    private static void ConnectVN() {
        if (Prefrences_network.getisServerConnect()) {
            Parameter_Class.server_Start = true;
            status("connected");
        } else {
            prepareVpn();
        }
    }

    public static void status(String status) {
        if (status.equals("connect")) {
            Parameter_Class.server_Start = false;
            Prefrences_network.setisServerConnect(false);
        } else if (status.equals("connecting")) {
            Prefrences_network.setisServerConnect(false);
        } else if (status.equals("connected")) {
            Prefrences_network.setisServerConnect(true);
            onConnect.onconnect("connect");
            ;
        }
    }

    private static void prepareVpn() {
        if (!Parameter_Class.server_Start) {
            Utils.isConnectingToInternet(contexts, new Utils.OnCheckNet() {
                @Override
                public void OnCheckNet(boolean b) {
                    if (b) {

                        Intent intent = VpnService.prepare(contexts);
                        if (intent != null) {
                            contexts.startActivityForResult(intent, 1);
                        } else {
                            start();
                        }
                    } else {
                        onConnect.onconnect("disconnect");
                    }
                }
            });
        }
    }

    /*  @Override
      public void onActivityResult(int requestCode, int resultCode, Intent data) {
          super.onActivityResult(requestCode, resultCode, data);
          Log.d("MainActivity12", "onActivityResult");
          if (resultCode == RESULT_OK) {
              Log.d("MainActivity12", "RESULT_OK");
              Utils.isConnectingToInternet(MainActivity.this, new Utils.OnCheckNet() {
                  @Override
                  public void OnCheckNet(boolean b) {
                      Log.d("MainActivity12", "OnCheckNet");
                      if (b) {
                          Log.d("MainActivity12", "OnCheckNet 1");
                          startServer();
                      } else {
                          onConnect.onconnect("disconnect");;
                      }
                  }
              });

          } else {
              Log.d("MainActivity12", "OnCheckNet 2");
              onConnect.onconnect("disconnect");
          }
      }
  */
    public static void start() {
        status("connecting");
        Server_Connecting();
    }

    public static void Server_Connecting() {

        isLoggedIn(new com.anchorfree.vpnsdk.callbacks.Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    List<String> fallbackOrder = new ArrayList<>();
                    fallbackOrder.add(HydraTransport.TRANSPORT_ID);
                    fallbackOrder.add(CaketubeTransport.TRANSPORT_ID_TCP);
                    fallbackOrder.add(CaketubeTransport.TRANSPORT_ID_UDP);
                    //showConnectProgress();
                    List<String> bypassDomains = new LinkedList<>();
                    bypassDomains.add("*facebook.com");
                    bypassDomains.add("*wtfismyip.com");
                    UnifiedSDK.getInstance().getVPN().start(new SessionConfig.Builder()
                            .withReason(TrackingConstants.GprReasons.M_UI)
                            .withTransportFallback(fallbackOrder)
                            .withVirtualLocation(Prefrences_network.getServer_short().toLowerCase())
                            .withTransport(HydraTransport.TRANSPORT_ID)
                            .addDnsRule(TrafficRule.Builder.bypass().fromDomains(bypassDomains))
                            .build(), new CompletableCallback() {
                        @Override
                        public void complete() {
                            Log.d("MainActivity12", "complete");
                            Parameter_Class.server_Start = true;
                            status("connected");
                        }

                        @Override
                        public void error(@NonNull VpnException e) {
                            Log.d("MainActivity12", "error = " + e.getMessage());
                            status("connect");
                            Parameter_Class.server_Start = false;
                            if (e.getMessage().contains("TRAFFIC_EXCEED")) {
                                Set_Limit_size();
                            } else {
                                onConnect.onconnect("disconnect");
                            }
                        }
                    });
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                onConnect.onconnect("disconnect");
            }
        });


    }

    public static void isLoggedIn(com.anchorfree.vpnsdk.callbacks.Callback<Boolean> callback) {
        UnifiedSDK.getInstance().getBackend().isLoggedIn(callback);
    }


    private static void Set_Limit_size() {
        int New_limit_traffic = 1000;
        long total_bytes = New_limit_traffic * 1048576;
        Delete_ApiCall(total_bytes);
    }

    private static void Delete_ApiCall(long total_bytes) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-prod.northghost.com/partner/subscribers/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Server_Interface mApiInterface = retrofit.create(Server_Interface.class);
        Call<TraficLimitResponse> call = mApiInterface.Call_Delete_Trafic(String.valueOf(Prefrences_network.getAura_user_id()) + "/traffic?access_token=" + Prefrences_network.getAccessToken());
        call.enqueue(new Callback<TraficLimitResponse>() {
            @Override
            public void onResponse(Call<TraficLimitResponse> call, Response<TraficLimitResponse> response) {
                if (response.isSuccessful()) {
                    Add_Trafic_size(total_bytes);
                } else {
                    onConnect.onconnect("disconnect");
                }
            }

            @Override
            public void onFailure(Call<TraficLimitResponse> call, Throwable t) {
                onConnect.onconnect("disconnect");
            }
        });
    }

    private static void Add_Trafic_size(long total_bytes) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-prod.northghost.com/partner/subscribers/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Server_Interface mApiInterface = retrofit.create(Server_Interface.class);
        Call<TraficLimitResponse> call = mApiInterface.Call_Add_Trafic(String.valueOf(Prefrences_network.getAura_user_id()) + "/traffic?access_token=" + Prefrences_network.getAccessToken() + "&traffic_limit=" + String.valueOf(total_bytes));
        call.enqueue(new Callback<TraficLimitResponse>() {
            @Override
            public void onResponse(Call<TraficLimitResponse> call, Response<TraficLimitResponse> response) {
                onConnect.onconnect("disconnect");
            }

            @Override
            public void onFailure(Call<TraficLimitResponse> call, Throwable t) {
                onConnect.onconnect("disconnect");
            }
        });
    }
}