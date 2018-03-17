package vay.enterwind.auto2000samarinda.module.sales.checkpoint;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import vay.enterwind.auto2000samarinda.R;
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
    boolean harus1, harus2, harus3;

    SpotsDialog dialog;

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
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();
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
    }

    @OnClick(R.id.foto1) void onFoto1() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1);
    }

    @OnClick(R.id.foto2) void onFoto2() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 2);
    }

    @OnClick(R.id.foto3) void onFoto3() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 3);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            switch (requestCode) {
                case 1:
                    foto1.setImageBitmap(photo);
                    harus1 = true;
                    break;
                case 2:
                    foto2.setImageBitmap(photo);
                    harus2 = true;
                    break;
                case 3:
                    foto3.setImageBitmap(photo);
                    harus3 = true;
                    break;
            }
        }
    }

    @OnClick(R.id.btnBack) void onBack() {
        finish();
    }

    @OnClick(R.id.btnSimpan) void onSimpan() {
        if(harus1 && harus2 && harus3) {
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

                        Log.d(TAG, "onResponsesss: "+response);

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

                String kamera1;
                foto1.buildDrawingCache();
                Bitmap bm1 = foto1.getDrawingCache();
                kamera1 = (bm1 == null) ? "null" : getStringImage(bm1);
                params.put("foto1", kamera1);

                String kamera2;
                foto2.buildDrawingCache();
                Bitmap bm2 = foto2.getDrawingCache();
                kamera2 = (bm2 == null) ? "null" : getStringImage(bm2);
                params.put("foto2", kamera2);

                String kamera3;
                foto3.buildDrawingCache();
                Bitmap bm3 = foto3.getDrawingCache();
                kamera3 = (bm3 == null) ? "null" : getStringImage(bm3);
                params.put("foto3", kamera3);

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

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

}
