package vay.enterwind.auto2000samarinda.module.supervisor.track;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import dmax.dialog.SpotsDialog;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.models.Sales;
import vay.enterwind.auto2000samarinda.pubnub.Constants;
import vay.enterwind.auto2000samarinda.session.AuthManagement;
import vay.enterwind.auto2000samarinda.utils.Config;
import vay.enterwind.auto2000samarinda.utils.PicassoMarker;

public class FullMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "FullMapActivity";

    private GoogleMap mMap;
    SpotsDialog dialog;
    private PubNub pubNub;

    public AuthManagement session;
    String sessionEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svp_full_map);
        dialog = new SpotsDialog(this);
        initSession();

        pubNub = initPubNub();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initSession() {
        session = new AuthManagement(this);
        HashMap<String, String> detail = session.getUserDetails();
        sessionEmail = detail.get(AuthManagement.KEY_EMAIL);
    }

    @NonNull
    private PubNub initPubNub() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(Constants.PUBNUB_PUBLISH_KEY);
        pnConfiguration.setSubscribeKey(Constants.PUBNUB_SUBSCRIBE_KEY);
        pnConfiguration.setSecure(true);

        return new PubNub(pnConfiguration);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.animateCamera( CameraUpdateFactory.zoomTo( 13.0f ) );

        initMarker();
    }

    private void initMarker() {
        dialog.show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.URL_SALES + sessionEmail,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for(int i = 0; i<response.length(); i++){
                            JSONObject obj;
                            try {
                                obj = response.getJSONObject(i);

                                String email = obj.getString(Config.TAG_EMAIL);
                                final String nama = obj.getString(Config.TAG_NAMA);
                                final String foto = obj.getString(Config.TAG_FOTO);

                                pubNub.getPresenceState()
                                        .channels(Arrays.asList(Constants.PUBLISH_CHANNEL_NAME))
                                        .uuid(email)
                                        .async(new PNCallback<PNGetStateResult>() {
                                            @Override
                                            public void onResponse(PNGetStateResult result, PNStatus status) {

                                                JsonElement shit = result.getStateByUUID().get(Constants.PUBLISH_CHANNEL_NAME);
                                                JsonObject fuck = shit.getAsJsonObject();
                                                JsonElement data = fuck.get("nameValuePairs");
                                                if(data!= null) {
                                                    Double latitude = data.getAsJsonObject().get("latitude").getAsDouble();
                                                    Double longitude = data.getAsJsonObject().get("longitude").getAsDouble();

                                                    final LatLng lokasi = new LatLng(latitude, longitude);

                                                    Picasso.with(getApplicationContext())
                                                            .load(foto)
                                                            .resize(64, 64)
                                                            .into(new com.squareup.picasso.Target() {
                                                                @Override
                                                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                                    mMap.addMarker(new MarkerOptions().position(lokasi).title(nama)
                                                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                                                                }
                                                                @Override
                                                                public void onBitmapFailed(Drawable errorDrawable) {
                                                                }
                                                                @Override
                                                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                                                }
                                                            });

                                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi));
                                                }

                                            }
                                        });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }
}
