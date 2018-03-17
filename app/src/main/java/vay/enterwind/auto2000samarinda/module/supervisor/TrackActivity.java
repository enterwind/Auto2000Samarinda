package vay.enterwind.auto2000samarinda.module.supervisor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.adapter.TrackAdapter;
import vay.enterwind.auto2000samarinda.models.Sales;
import vay.enterwind.auto2000samarinda.module.supervisor.track.FullMapActivity;
import vay.enterwind.auto2000samarinda.pubnub.Constants;
import vay.enterwind.auto2000samarinda.utils.Config;

public class TrackActivity extends BaseActivity {
    private static final String TAG = "TrackActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = TrackActivity.this;

    @BindView(R.id.btnMap) FloatingActionButton btnMap;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.kosong) CardView kosong;

    private List<Sales> salesList = new ArrayList<>();
    private TrackAdapter mAdapter;
    public PubNub pubNub;

    SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svp_track);
        ButterKnife.bind(this);
        dialog = new SpotsDialog(this);
        dialog.show();

        pubnubInit();
        init();

        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

    private void pubnubInit() {
        this.pubNub = initPubNub();
        // this.pubNub.subscribe().channels(Arrays.asList(Constants.PUBLISH_CHANNEL_NAME)).withPresence().execute();
    }

    @NonNull
    private PubNub initPubNub() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(Constants.PUBNUB_PUBLISH_KEY);
        pnConfiguration.setSubscribeKey(Constants.PUBNUB_SUBSCRIBE_KEY);
        pnConfiguration.setSecure(true);
        return new PubNub(pnConfiguration);
    }

    private void init() {
        recyclerView.setAdapter(null);
        mAdapter = new TrackAdapter(this, salesList, this.pubNub);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareData();
    }

    private void prepareData() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.URL_SALES + sessionEmail,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: " + response);
                        if(response.length() == 0) {
                            kosong.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        salesList.clear();
                        for(int i = 0; i<response.length(); i++){
                            JSONObject obj;
                            try {
                                obj = response.getJSONObject(i);
                                Sales sales = new Sales(
                                        obj.getString(Config.TAG_EMAIL),
                                        obj.getString(Config.TAG_FOTO),
                                        obj.getString(Config.TAG_NAMA),
                                        obj.getString(Config.TAG_TELEPON),
                                        obj.getString(Config.TAG_ALAMAT),
                                        obj.getString(Config.TAG_STATUS),
                                        obj.getString("planning"),
                                        obj.getString("reported"),
                                        obj.getString("remains")
                                );
                                salesList.add(sales);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
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

    @OnClick(R.id.btnMap) void onMap() {
        startActivity(new Intent(TrackActivity.this, FullMapActivity.class));
    }
}
