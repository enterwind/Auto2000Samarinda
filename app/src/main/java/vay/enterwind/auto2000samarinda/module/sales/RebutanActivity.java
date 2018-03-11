package vay.enterwind.auto2000samarinda.module.sales;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.module.sales.profil.UbahPasswordActivity;
import vay.enterwind.auto2000samarinda.session.AuthManagement;
import vay.enterwind.auto2000samarinda.utils.Config;

public class RebutanActivity extends AppCompatActivity {
    private static final String TAG = "RebutanActivity";

    @BindView(R.id.btnTolak) Button btnTolak;
    @BindView(R.id.btnTerima) Button btnTerima;

    @BindView(R.id.txtNama) TextView txtNama;
    @BindView(R.id.txtAlamat) TextView txtAlamat;
    @BindView(R.id.txtTelepon) TextView txtTelepon;
    @BindView(R.id.txtPengirim) TextView txtPengirim;

    SpotsDialog dialog;
    AuthManagement session;
    String sessionEmail, uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_rebutan);
        ButterKnife.bind(this);
        dialog = new SpotsDialog(this);

        uuid = getIntent().getExtras().getString("ID");

        setupSession();
        getDataFromServer(uuid);
    }

    private void setupSession() {
        session = new AuthManagement(getApplicationContext());
        HashMap<String, String> detail = session.getUserDetails();
        sessionEmail = detail.get(AuthManagement.KEY_EMAIL);
    }

    private void getDataFromServer(String uuid) {
        dialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String fullUrl = Config.URL_REBUTAN + sessionEmail + "/" + uuid;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, fullUrl, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                if(response.equals("gagal")) {
                    StyleableToast.makeText(RebutanActivity.this, "Terjadi kesalahan.", R.style.ToastGagal).show();
                    dialog.dismiss();
                } else {
                    Log.d(TAG, "onResponse: "+response);
                    String[] list = response.split("#");

                    txtNama.setText(list[0]);
                    txtTelepon.setText("Telepon: " + list[1]);
                    txtAlamat.setText(list[2]);
                    txtPengirim.setText("Dari " + list[3]);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                StyleableToast.makeText(RebutanActivity.this, "Error di Server.", R.style.ToastGagal).show();
                dialog.dismiss();
            }
        });
        requestQueue.add(stringRequest);
    }

    @OnClick(R.id.btnTerima) void onTerima() {

        dialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String fullUrl = Config.URL_REBUTAN + sessionEmail + "/" + uuid + "/ambil";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, fullUrl, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                if(response.equals("taken")) {
                    StyleableToast.makeText(RebutanActivity.this, "Prospek telah diambil oleh Sales lain.", R.style.ToastGagal).show();
                }
                if(response.equals("sukses")) {
                    StyleableToast.makeText(RebutanActivity.this, "Prospek berhasil diambil.", R.style.ToastSukses).show();
                }
                dialog.dismiss();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                StyleableToast.makeText(RebutanActivity.this, "Error di Server.", R.style.ToastGagal).show();
                dialog.dismiss();
            }
        });
        requestQueue.add(stringRequest);

    }

    @OnClick(R.id.btnTolak) void onTolak() {
        finish();
    }
}
