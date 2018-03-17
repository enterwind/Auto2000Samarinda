package vay.enterwind.auto2000samarinda.module.sales;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.LoginActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.pubnub.Constants;
import vay.enterwind.auto2000samarinda.pubnub.example.locationpublish.LocationHelper;
import vay.enterwind.auto2000samarinda.session.AuthManagement;
import vay.enterwind.auto2000samarinda.utils.Config;

public class HomeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = HomeActivity.this;

    @BindView(R.id.regional) TextView regional;
    @BindView(R.id.supervisor) TextView supervisor;
    @BindView(R.id.jabatanTelepon) TextView jabatanTelepon;
    @BindView(R.id.namaLengkap) TextView namaLengkap;

    @BindView(R.id.reported) TextView reported;
    @BindView(R.id.remains) TextView remains;
    @BindView(R.id.todayPlans) TextView todayPlans;

    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.btnConnect) ImageView btnConnect;
    @BindView(R.id.btnDisconnect) ImageView btnDisconnect;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txtAlasan;
    private PubNub pubNub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_home);
        ButterKnife.bind(this);

        pubnubInit();
        init();

        swipeRefresh.setOnRefreshListener(this);
        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

    private void pubnubInit() {
        this.pubNub = initPubNub(sessionEmail);
        this.pubNub.subscribe().channels(Arrays.asList(Constants.PUBLISH_CHANNEL_NAME)).withPresence().execute();

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

    @SuppressLint("SetTextI18n")
    private void init() {
        namaLengkap.setText(sessionNama);
        jabatanTelepon.setText(sessionJabatan + " (" + sessionTelepon + ")");
        supervisor.setText("Supervisor: " + sessionSupervisor);
        regional.setText("Regional: " + sessionRegional);

        getServer(sessionEmail);

        initKoneksi();
    }

    private void initKoneksi() {

        pubNub.getPresenceState()
                .channels(Arrays.asList(Constants.PUBLISH_CHANNEL_NAME))
                .uuid(sessionEmail)
                .async(new PNCallback<PNGetStateResult>() {
                    @Override
                    public void onResponse(PNGetStateResult result, PNStatus status) {
                        JsonElement shit = result.getStateByUUID().get(Constants.PUBLISH_CHANNEL_NAME);
                        JsonObject fuck = shit.getAsJsonObject();
                        JsonElement data = fuck.get("nameValuePairs");
                        if(data!= null) {
                            // Online
                            btnConnect.setVisibility(View.VISIBLE);
                            btnDisconnect.setVisibility(View.GONE);
                        } else {
                            // Offline
                            btnConnect.setVisibility(View.GONE);
                            btnDisconnect.setVisibility(View.VISIBLE);
                        }

                    }
                });

    }

    @Override
    public void onRefresh() {
        init();
        swipeRefresh.setRefreshing(false);
    }

    private void getServer(String email) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String widgetUrl = Config.URL_WIDGET + email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, widgetUrl, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                if(response.equals("gagal")) {
                    StyleableToast.makeText(HomeActivity.this, "Koneksi kurang stabil, pastikan Anda terkoneksi dengan internet.", R.style.ToastGagal).show();
                } else {
                    String[] list = response.split(",");

                    todayPlans.setText(list[0] + " Planning");
                    reported.setText(list[1] + " Planning");
                    remains.setText(list[2] + " Planning");

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

    @OnClick(R.id.btnConnect) void onConnect() {
        pubnubInit();
    }

    @OnClick(R.id.btnDisconnect) void onDisconnect() {
        dialog = new AlertDialog.Builder(HomeActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_leave, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Alasan Izin Keluar");

        txtAlasan = (EditText) dialogView.findViewById(R.id.txtAlasan);
        dialog.setPositiveButton("KIRIM", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Unsubscribe

                // Kirim Alasan Ke Server

                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
