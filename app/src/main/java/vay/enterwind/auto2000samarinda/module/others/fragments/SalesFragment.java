package vay.enterwind.auto2000samarinda.module.others.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.adapter.CheckpointAdapter;
import vay.enterwind.auto2000samarinda.adapter.ReferenceAdapter;
import vay.enterwind.auto2000samarinda.models.Plan;
import vay.enterwind.auto2000samarinda.models.Reference;
import vay.enterwind.auto2000samarinda.session.AuthManagement;
import vay.enterwind.auto2000samarinda.utils.Config;


public class SalesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "SalesFragment";

    private List<Reference> referenceList = new ArrayList<>();
    private ReferenceAdapter mAdapter;

    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.kosong) CardView kosong;

    ProgressDialog progress;
    public AuthManagement session;
    String sessionEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales, container, false);
        ButterKnife.bind(this, view);
        progress = ProgressDialog.show(getContext(), "Loading...", "Tunggu Sebentar");

        initSession();
        init();

        swipeRefresh.setOnRefreshListener(this);
        return view;
    }

    private void initSession() {
        session = new AuthManagement(getContext());
        session.checkLogin();

        HashMap<String, String> detail = session.getUserDetails();
        sessionEmail = detail.get(AuthManagement.KEY_EMAIL);
    }

    private void init() {
        mRecyclerView.setAdapter(null);
        mAdapter = new ReferenceAdapter(getActivity(), referenceList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        prepareData();
    }

    private void prepareData() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.URL_PROSPEK + sessionEmail + "/history/sales",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: "+response);
                        if(response.length() == 0) {
                            kosong.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        } else {
                            kosong.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }
                        referenceList.clear();
                        for(int i = 0; i<response.length(); i++){
                            JSONObject obj;
                            try {
                                obj = response.getJSONObject(i);
                                Reference reference = new Reference(
                                        obj.getString(Config.TAG_UUID),
                                        obj.getString(Config.TAG_NAMA),
                                        obj.getString(Config.TAG_TELEPON),
                                        obj.getString(Config.TAG_ALAMAT),
                                        obj.getString(Config.TAG_CATATAN),
                                        obj.getString(Config.TAG_JENIS),
                                        obj.getString(Config.TAG_STATUS),
                                        obj.getString(Config.TAG_PEGAWAI),
                                        obj.getString(Config.TAG_TAKEN_BY)
                                );
                                referenceList.add(reference);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: "+error);
                        progress.dismiss();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }
    
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
                init();
            }
        }, 1000);
    }
}
