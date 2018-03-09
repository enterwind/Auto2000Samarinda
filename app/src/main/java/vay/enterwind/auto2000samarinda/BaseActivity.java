package vay.enterwind.auto2000samarinda;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.common.collect.ImmutableMap;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import vay.enterwind.auto2000samarinda.pubnub.Constants;
import vay.enterwind.auto2000samarinda.pubnub.example.*;
import vay.enterwind.auto2000samarinda.pubnub.example.locationpublish.LocationHelper;
import vay.enterwind.auto2000samarinda.pubnub.example.locationpublish.LocationPublishMapAdapter;
import vay.enterwind.auto2000samarinda.pubnub.example.locationpublish.LocationPublishPnCallback;
import vay.enterwind.auto2000samarinda.pubnub.example.util.JsonUtil;
import vay.enterwind.auto2000samarinda.session.AuthManagement;
import vay.enterwind.auto2000samarinda.utils.BottomNavigationViewHelper;

/**
 * Created by novay on 04/03/18.
 */

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = "BaseActivity";
    boolean doubleBackToExitPressedOnce = false;

    public static boolean isAppRunning;

    public AuthManagement session;
    public String sessionId, sessionEmail, sessionNama, sessionAkses, sessionJabatan, sessionTelepon, sessionFoto, sessionSupervisor, sessionRegional;

    private SharedPreferences mSharedPrefs;
    private String userName;
    private PubNub pubNub;
    private LocationHelper locationHelper;

    public String refreshedToken;

    private static ImmutableMap<String, String> getNewLocationMessage(String userName, Location location) {
        return ImmutableMap.<String, String>of("who", userName, "lat", Double.toString(location.getLatitude()), "lng", Double.toString(location.getLongitude()));
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);

        requestPermissions();
        setupSession(getApplicationContext());

        notificationInit();
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        if(sessionAkses == "1") {
            pubnubInit();
        }
    }

    private void notificationInit() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "1";
        String channel2 = "2";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    "Channel 1",NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("This is BNT");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationChannel notificationChannel2 = new NotificationChannel(channel2,
                    "Channel 2", NotificationManager.IMPORTANCE_MIN);

            notificationChannel.setDescription("This is bTV");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel2);

        }
    }

    private void pubnubInit() {
        mSharedPrefs = getSharedPreferences(Constants.DATASTREAM_PREFS, MODE_PRIVATE);
        if (!mSharedPrefs.contains(Constants.DATASTREAM_UUID)) {
            Intent toLogin = new Intent(this, LoginActivity.class);
            startActivity(toLogin);
        }

//        this.userName = mSharedPrefs.getString(Constants.DATASTREAM_UUID, sessionEmail);
        this.userName = "kholiz.auto2000@gmail.com";
        this.pubNub = initPubNub(this.userName);
        this.pubNub.subscribe().channels(Arrays.asList(Constants.PUBLISH_CHANNEL_NAME)).execute();

        this.locationHelper = new LocationHelper(getApplicationContext(), this);
    }

    @NonNull
    private PubNub initPubNub(String userName) {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(Constants.PUBNUB_PUBLISH_KEY);
        pnConfiguration.setSubscribeKey(Constants.PUBNUB_SUBSCRIBE_KEY);
        pnConfiguration.setSecure(true);
        pnConfiguration.setUuid(userName);

        return new PubNub(pnConfiguration);
    }

    @Override
    public void onLocationChanged(Location location) {
        final Map<String, String> message = getNewLocationMessage(this.userName, location);

        Log.d(TAG, "onLocationChangeds: " + message);

        this.pubNub.publish().channel(Constants.PUBLISH_CHANNEL_NAME).message(message).async(
                new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        try {
                            if (!status.isError()) {
                                Log.v(TAG, "publish(" + JsonUtil.asJson(result) + ")");
                            } else {
                                Log.v(TAG, "publishErr(" + status.toString() + ")");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public void requestPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {}
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    public void setupBottomNavigationView(Context context, int ACTIVITY_NUM){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNav);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(context, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    public static void staticBottomNavigationView(Context context, final Activity callingActivity,
                                                  int ACTIVITY_NUM, BottomNavigationViewEx bottomNavigationViewEx){
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(context, callingActivity, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    public void setupSession(Context context) {
        session = new AuthManagement(context);
        session.checkLogin();

        HashMap<String, String> detail = session.getUserDetails();
        sessionNama = detail.get(AuthManagement.KEY_NAMA);
        sessionId = detail.get(AuthManagement.KEY_ID);
        sessionEmail = detail.get(AuthManagement.KEY_EMAIL);
        sessionAkses = detail.get(AuthManagement.KEY_AKSES);
        sessionJabatan = detail.get(AuthManagement.KEY_JABATAN);
        sessionTelepon = detail.get(AuthManagement.KEY_TELEPON);
        sessionFoto = detail.get(AuthManagement.KEY_FOTO);
        sessionSupervisor = detail.get(AuthManagement.KEY_SUPERVISOR);
        sessionRegional = detail.get(AuthManagement.KEY_REGIONAL);
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (doubleBackToExitPressedOnce) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            StyleableToast.makeText(this, "Tekan sekali lagi untuk keluar!", R.style.ToastInfo).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        isAppRunning = false;
    }
}
