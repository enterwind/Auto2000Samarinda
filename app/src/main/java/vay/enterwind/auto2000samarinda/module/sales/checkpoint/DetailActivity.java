package vay.enterwind.auto2000samarinda.module.sales.checkpoint;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.module.sales.plans.MapsActivity;
import vay.enterwind.auto2000samarinda.utils.Config;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    @BindView(R.id.btnBack) ImageView btnBack;
    @BindView(R.id.btnSimpan) Button btnSimpan;
    @BindView(R.id.judulNama) TextView judulNama;

    @BindView(R.id.txtNama) TextView txtNama;
    @BindView(R.id.txtTelepon) TextView txtTelepon;
    @BindView(R.id.txtAlamat) TextView txtAlamat;

    @BindView(R.id.foto1) ImageView foto1;
    @BindView(R.id.foto2) ImageView foto2;
    @BindView(R.id.foto3) ImageView foto3;

    String uuid, nama, telepon, alamat;
    Double longitude, latitude;

    SpotsDialog dialog;

    ContentValues cv;
    Uri imageUri;
    String foto1Url, foto2Url, foto3Url, upload1, upload2, upload3;

    int foto = 0;
    String urlFoto1, urlFoto2, urlFoto3;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_checkpoint_detail);
        ButterKnife.bind(this);
        dialog = new SpotsDialog(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        trackLocation();
        initIntent();
    }

    private void trackLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (DetailActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DetailActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            } else  if (location1 != null) {
                latitude = location1.getLatitude();
                longitude = location1.getLongitude();
            } else  if (location2 != null) {
                latitude = location2.getLatitude();
                longitude = location2.getLongitude();
            }else{
                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void initIntent() {
        uuid = getIntent().getExtras().getString("UUID");
        nama = getIntent().getExtras().getString("NAMA");
        telepon = getIntent().getExtras().getString("TELEPON");
        alamat = getIntent().getExtras().getString("ALAMAT");

        judulNama.setText(nama);
        txtNama.setText(nama);
        txtTelepon.setText(telepon);
        txtAlamat.setText(alamat);

        checkPhoto(uuid);
    }

    private void checkPhoto(String uuid) {
        dialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = Config.URL_PLAN + uuid + "/detail";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray photos = response.getJSONArray("foto");
                    foto = photos.length();

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

    @OnClick(R.id.foto1) void onFoto1() {
        cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "The Enterwind Inc.");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.foto2) void onFoto2() {
        cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "The Enterwind Inc.");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 2);
    }

    @OnClick(R.id.foto3) void onFoto3() {
        cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "The Enterwind Inc.");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 3);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            Bitmap thumbnail = null;
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (requestCode) {
                case 1:
                    try {
                        foto1.setImageBitmap(thumbnail);
                        foto1Url = getRealPathFromURI(imageUri);
                        upload1 = getStringImage(foto1Url);
                        savePhoto(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        foto2.setImageBitmap(thumbnail);
                        foto2Url = getRealPathFromURI(imageUri);
                        upload2 = getStringImage(foto2Url);
                        savePhoto(2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        foto3.setImageBitmap(thumbnail);
                        foto3Url = getRealPathFromURI(imageUri);
                        upload3 = getStringImage(foto3Url);
                        savePhoto(3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void savePhoto(final int i) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_CHECKPOINT + uuid + "/save-photo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponsess: "+response);
                        if(response.equals("sukses")) {
                            StyleableToast.makeText(DetailActivity.this, "Foto berhasil direkam!", R.style.ToastSukses).show();
                            dialog.dismiss();
                        } else {
                            StyleableToast.makeText(DetailActivity.this, "Terjadi kesalahan. Coba lagi!", R.style.ToastGagal).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        StyleableToast.makeText(DetailActivity.this, "Server Error! " + error, R.style.ToastGagal).show();
                        dialog.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                if(i == 1) params.put("foto1", upload1);
                if(i == 2) params.put("foto2", upload2);
                if(i == 3) params.put("foto3", upload3);
                params.put("longitude", String.valueOf(longitude));
                params.put("latitude", String.valueOf(latitude));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {return 50000;}
            @Override
            public int getCurrentRetryCount() {return 50000;}
            @Override
            public void retry(VolleyError error) throws VolleyError {}
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String getStringImage(String uri) {
        Bitmap bm = BitmapFactory.decodeFile(uri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    @OnClick(R.id.btnBack) void onBack() {
        finish();
    }

    @OnClick(R.id.btnSimpan) void onSimpan() {
         if(foto == 3) {
            parseToServer();
         } else {
            StyleableToast.makeText(DetailActivity.this, "Anda harus melengkapi minimal 3 foto yang diminta.", R.style.ToastGagal).show();
         }
    }

    private void parseToServer() {
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_CHECKPOINT + uuid + "/send",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("sukses")) {
                            StyleableToast.makeText(DetailActivity.this, "Checkpoint Berhasil Dibuat!", R.style.ToastSukses).show();
                            dialog.dismiss();

                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            StyleableToast.makeText(DetailActivity.this, "Terjadi kesalahan. Coba lagi!", R.style.ToastGagal).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        StyleableToast.makeText(DetailActivity.this, "Server Error! " + error, R.style.ToastGagal).show();
                        dialog.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("nama", nama);
                params.put("telepon", telepon);
                params.put("alamat", alamat);
                params.put("longitude", String.valueOf(longitude));
                params.put("latitude", String.valueOf(latitude));

                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {return 50000;}
            @Override
            public int getCurrentRetryCount() {return 50000;}
            @Override
            public void retry(VolleyError error) throws VolleyError {}
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
