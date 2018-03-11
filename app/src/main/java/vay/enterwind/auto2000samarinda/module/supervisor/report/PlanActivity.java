package vay.enterwind.auto2000samarinda.module.supervisor.report;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.adapter.PlanAdapter;
import vay.enterwind.auto2000samarinda.models.Plan;
import vay.enterwind.auto2000samarinda.module.sales.PlanningActivity;
import vay.enterwind.auto2000samarinda.module.sales.RebutanActivity;
import vay.enterwind.auto2000samarinda.module.sales.plans.AddPlanActivity;
import vay.enterwind.auto2000samarinda.utils.Config;

public class PlanActivity extends AppCompatActivity {

    private static final String TAG = "PlanActivity";
    private Context mContext = PlanActivity.this;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.kosong) CardView kosong;
    @BindView(R.id.txtNama) TextView txtNama;
    @BindView(R.id.btnBack) ImageView btnBack;

    private List<Plan> planList = new ArrayList<>();
    private PlanAdapter mAdapter;

    SpotsDialog dialog;

    String email, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spv_plan);
        ButterKnife.bind(this);

        email = getIntent().getExtras().getString("EMAIL");
        nama = getIntent().getExtras().getString("NAMA");
        txtNama.setText(nama);

        dialog = new SpotsDialog(this);
        dialog.show();

        init();
    }

    private void init() {
        recyclerView.setAdapter(null);
        mAdapter = new PlanAdapter(this, planList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareData();
    }

    private void prepareData() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.URL_PLAN + email,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: "+response);
                        if(response.length() == 0) {
                            kosong.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        planList.clear();
                        for(int i = 0; i<response.length(); i++){
                            JSONObject obj;
                            try {
                                obj = response.getJSONObject(i);
                                Plan plan = new Plan(
                                        obj.getString(Config.TAG_UUID),
                                        obj.getString(Config.TAG_NAMA),
                                        obj.getString(Config.TAG_TELEPON),
                                        obj.getString(Config.TAG_ALAMAT),
                                        obj.getString(Config.TAG_STATUS),
                                        obj.getString(Config.TAG_LONGITUDE),
                                        obj.getString(Config.TAG_LATITUDE)
                                );
                                planList.add(plan);
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
                        StyleableToast.makeText(PlanActivity.this, "Periksa koneksi internet Anda.", R.style.ToastGagal).show();
                        dialog.dismiss();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    @OnClick(R.id.btnBack) void onBack() {
        finish();
    }

}
