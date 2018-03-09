package vay.enterwind.auto2000samarinda.module.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.module.others.references.AddReferenceActivity;
import vay.enterwind.auto2000samarinda.utils.Config;

public class HomeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = HomeActivity.this;

    @BindView(R.id.namaLengkap) TextView namaLengkap;
    @BindView(R.id.jabatanTelepon) TextView jabatanTelepon;

    @BindView(R.id.prospekSales) TextView prospekSales;
    @BindView(R.id.prospekBp) TextView prospekBp;
    @BindView(R.id.prospekGr) TextView prospekGr;

    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.btnAdd) FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_home);
        ButterKnife.bind(this);

        init();

        swipeRefresh.setOnRefreshListener(this);
        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        namaLengkap.setText(sessionNama);
        jabatanTelepon.setText(sessionJabatan + " (" + sessionTelepon + ")");

        getServer(sessionEmail);
    }

    @Override
    public void onRefresh() {
        init();
        swipeRefresh.setRefreshing(false);
    }

    @OnClick(R.id.btnAdd) void onAdd() {
        startActivityForResult(new Intent(mContext, AddReferenceActivity.class), 1);
    }

    private void getServer(String email) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String widgetUrl = Config.URL_WIDGET_OTHERS + email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, widgetUrl, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                if(response.equals("gagal")) {
                    StyleableToast.makeText(HomeActivity.this, "Koneksi kurang stabil, pastikan Anda terkoneksi dengan internet.", R.style.ToastGagal).show();
                } else {
                    String[] list = response.split(",");

                    prospekSales.setText(list[0] + " Prospek");
                    prospekGr.setText(list[1] + " Prospek");
                    prospekBp.setText(list[2] + " Prospek");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                StyleableToast.makeText(HomeActivity.this, "Error di Server.", R.style.ToastGagal).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
