package vay.enterwind.auto2000samarinda.module.sales.plans;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.session.AuthManagement;
import vay.enterwind.auto2000samarinda.utils.Config;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "DetailActivity";

    @BindView(R.id.btnBack) ImageView btnBack;

    @BindView(R.id.txtNama) TextView txtNama;
    @BindView(R.id.txtTelepon) TextView txtTelepon;
    @BindView(R.id.txtAlamat) TextView txtAlamat;
    @BindView(R.id.txtCatatan) TextView txtCatatan;

    @BindView(R.id.foto1) ImageView foto1;
    @BindView(R.id.foto2) ImageView foto2;
    @BindView(R.id.foto3) ImageView foto3;

    String uuid, nama, urlFoto1, urlFoto2, urlFoto3;
    Double longitude = 117.09580518, latitude = -0.52840624;

    SpotsDialog dialog;

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_detail);
        ButterKnife.bind(this);

        dialog = new SpotsDialog(this);

        init();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void init() {

        uuid = getIntent().getExtras().getString("UUID");
        nama = getIntent().getExtras().getString("NAMA");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        dialog.show();
        String URL = Config.URL_PLAN + uuid + "/detail";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    txtNama.setText(response.getString("nama"));
                    txtTelepon.setText(response.getString("telepon"));
                    txtAlamat.setText(response.getString("alamat"));
                    txtCatatan.setText(response.getString("catatan"));

                    if(response.getString("longitude") == null) {
                        longitude = Double.valueOf(response.getString("longitude"));
                        latitude = Double.valueOf(response.getString("latitude"));
                    }

                    JSONArray photos = response.getJSONArray("foto");

                    for (int i = 0; i < photos.length(); i++) {

                        JSONObject obj = photos.getJSONObject(i);

                        if(i == 0) {
                            urlFoto1 = obj.getString("foto");
                            Picasso.with(DetailActivity.this).load(urlFoto1)
                                    .error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher)
                                    .into(foto1);
                        }

                        if(i == 1) {
                            urlFoto2 = obj.getString("foto");
                            Picasso.with(DetailActivity.this).load(urlFoto2)
                                    .error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher)
                                    .into(foto2);
                        }

                        if(i == 2) {
                            urlFoto3 = obj.getString("foto");
                            Picasso.with(DetailActivity.this).load(urlFoto3)
                                    .error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher)
                                    .into(foto3);
                        }

                        if(response.getString("longitude") == null) {
                            longitude = Double.valueOf(obj.getString("longitude"));
                            latitude = Double.valueOf(obj.getString("latitude"));
                        }

                    }
                    dialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                dialog.dismiss();
            }
        });

        requestQueue.add(jsonObjReq);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng lokasi = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(lokasi).title(nama));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
    }

    @OnClick(R.id.foto1) void foto1() {
        Intent i = new Intent(DetailActivity.this, FotoActivity.class);
        i.putExtra("FOTO", urlFoto1);
        startActivity(i);
    }

    @OnClick(R.id.foto2) void foto2() {
        Intent i = new Intent(DetailActivity.this, FotoActivity.class);
        i.putExtra("FOTO", urlFoto2);
        startActivity(i);
    }

    @OnClick(R.id.foto3) void foto3() {
        Intent i = new Intent(DetailActivity.this, FotoActivity.class);
        i.putExtra("FOTO", urlFoto3);
        startActivity(i);
    }
}
