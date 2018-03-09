package vay.enterwind.auto2000samarinda.module.others.references;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.module.sales.plans.AddPlanActivity;
import vay.enterwind.auto2000samarinda.session.AuthManagement;
import vay.enterwind.auto2000samarinda.utils.Config;

public class AddReferenceActivity extends AppCompatActivity {
    private static final String TAG = "AddReferenceActivity";
    private Context mContext = AddReferenceActivity.this;

    @BindView(R.id.btnBack) ImageView btnBack;

    @BindView(R.id.btnSimpan) Button btnSimpan;
    @BindView(R.id.txtNama) TextView txtNama;
    @BindView(R.id.txtTelepon) EditText txtTelepon;
    @BindView(R.id.txtAlamat) EditText txtAlamat;
    @BindView(R.id.txtCatatan) EditText txtCatatan;
    @BindView(R.id.txtJenis) Spinner txtJenis;

    ProgressDialog progress;
    AuthManagement session;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reference);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setupSession();
    }

    private void setupSession() {
        session = new AuthManagement(getApplicationContext());
        HashMap<String, String> detail = session.getUserDetails();
        email = detail.get(AuthManagement.KEY_EMAIL);
    }

    @OnClick(R.id.btnBack) void onBack() {
        finish();
    }

    @OnClick(R.id.btnSimpan) void onSimpan() {
        progress = ProgressDialog.show(mContext, "Loading...", "Tunggu Sebentar");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_PROSPEK + email + "/send",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("sukses")) {
                            StyleableToast.makeText(mContext, "Referensi Berhasil Terkirim!", R.style.ToastSukses).show();

                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();

                            progress.dismiss();
                        } else {
                            StyleableToast.makeText(mContext, "Maaf, terjadi gangguan koneksi atau ada masalah pada server kami.", R.style.ToastGagal).show();
                            progress.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        StyleableToast.makeText(mContext, "Koneksi kurang stabil, pastikan Anda terkoneksi dengan internet.", R.style.ToastGagal).show();
                        progress.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("jenis", String.valueOf(txtJenis.getSelectedItem()));
                params.put("nama", txtNama.getText().toString().trim());
                params.put("telepon", txtTelepon.getText().toString().trim());
                params.put("alamat", txtAlamat.getText().toString().trim());
                params.put("catatan", txtCatatan.getText().toString().trim());

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
