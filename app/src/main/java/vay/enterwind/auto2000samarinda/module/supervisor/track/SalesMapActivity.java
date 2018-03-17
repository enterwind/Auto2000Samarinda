package vay.enterwind.auto2000samarinda.module.supervisor.track;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.LoginActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.pubnub.Constants;
import vay.enterwind.auto2000samarinda.pubnub.example.locationpublish.LocationHelper;
import vay.enterwind.auto2000samarinda.pubnub.example.locationpublish.LocationPublishMapAdapter;
import vay.enterwind.auto2000samarinda.pubnub.example.locationpublish.LocationPublishPnCallback;
import vay.enterwind.auto2000samarinda.pubnub.example.locationsubscribe.LocationSubscribeMapAdapter;
import vay.enterwind.auto2000samarinda.pubnub.example.locationsubscribe.LocationSubscribePnCallback;
import vay.enterwind.auto2000samarinda.pubnub.example.locationsubscribe.LocationSubscribeTabContentFragment;
import vay.enterwind.auto2000samarinda.pubnub.example.util.JsonUtil;

public class SalesMapActivity extends FragmentActivity implements OnMapReadyCallback  {
    private static final String TAG = "SalesMapActivity";

    private GoogleMap mMap;
    private PubNub pubNub;
    private LocationHelper locationHelper;

    @BindView(R.id.txtNama) TextView txtNama;
    @BindView(R.id.txtEmail) TextView txtEmail;
    @BindView(R.id.btnTelepon) Button btnTelepon;
    @BindView(R.id.imgPhoto) CircleImageView imgPhoto;

    public String userName, nama;
    Double longitude, latitude;

    private static ImmutableMap<String, String> getNewLocationMessage(String userName, Location location) {
        return ImmutableMap.<String, String>of("who", userName, "lat", Double.toString(location.getLatitude()), "lng", Double.toString(location.getLongitude()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_map);
        ButterKnife.bind(this);

        init();
        pubnubInit();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        this.userName = getIntent().getExtras().getString("EMAIL");
        this.nama = getIntent().getExtras().getString("NAMA");
        txtNama.setText(getIntent().getExtras().getString("NAMA"));
        txtEmail.setText("Email: " + this.userName);
        Picasso.with(this).load(getIntent().getExtras().getString("PHOTO"))
                .error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher)
                .into(imgPhoto);
    }

    private void pubnubInit() {
        this.pubNub = initPubNub(this.userName);
        // this.pubNub.subscribe().channels(Arrays.asList(Constants.PUBLISH_CHANNEL_NAME)).withPresence().execute();
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
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.animateCamera( CameraUpdateFactory.zoomTo( 14.0f ) );

        this.pubNub.addListener(new LocationPublishPnCallback(new LocationPublishMapAdapter(this, mMap), Constants.PUBLISH_CHANNEL_NAME));
        // this.pubNub.subscribe().channels(Arrays.asList(Constants.PUBLISH_CHANNEL_NAME)).withPresence().execute();

        this.pubNub.getPresenceState()
                .channels(Arrays.asList(Constants.PUBLISH_CHANNEL_NAME))
                .uuid(this.userName)
                .async(new PNCallback<PNGetStateResult>() {
                    @Override
                    public void onResponse(PNGetStateResult result, PNStatus status) {

                        JsonElement shit = result.getStateByUUID().get(Constants.PUBLISH_CHANNEL_NAME);
                        JsonObject fuck = shit.getAsJsonObject();
                        JsonElement data = fuck.get("nameValuePairs");
                        if(data!= null) {
                            latitude = data.getAsJsonObject().get("latitude").getAsDouble();
                            longitude = data.getAsJsonObject().get("longitude").getAsDouble();

                            LatLng lokasi = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(lokasi).title(nama));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi));
                        } else {
                            Toast.makeText(SalesMapActivity.this, "" + nama + " Sedang Offline!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });

    }

    @OnClick(R.id.btnTelepon)
    void onTelepon() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + getIntent().getExtras().getString("TELEPON")));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 999);
        } else {
            try {
                startActivity(callIntent);
            } catch(SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 999: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the phone call
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


}
