package vay.enterwind.auto2000samarinda.module.sales.plans;

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
import com.muddzdev.styleabletoastlibrary.ToastLengthTracker;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.module.sales.ProfilActivity;
import vay.enterwind.auto2000samarinda.module.sales.profil.UbahPasswordActivity;
import vay.enterwind.auto2000samarinda.module.sales.profil.UbahProfilActivity;
import vay.enterwind.auto2000samarinda.session.AuthManagement;
import vay.enterwind.auto2000samarinda.utils.Config;

public class AddPlanActivity extends AppCompatActivity {

    private Context mContext = AddPlanActivity.this;

    @BindView(R.id.btnBack) ImageView btnBack;

    @BindView(R.id.btnSimpan) Button btnSimpan;
    @BindView(R.id.alamat) TextView alamat;
    @BindView(R.id.telepon) EditText telepon;
    @BindView(R.id.namaLengkap) EditText namaLengkap;

    @BindView(R.id.longitude) EditText longitude;
    @BindView(R.id.latitude) EditText latitude;

    SpotsDialog dialog;
    AuthManagement session;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_add_plan);
        ButterKnife.bind(this);
        dialog = new SpotsDialog(this);

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

    @OnClick(R.id.alamat) void onMap() {
        startActivityForResult(new Intent(mContext, MapsActivity.class), 1);
    }

    @OnClick(R.id.btnSimpan) void onSimpan() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_PLAN + email + "/add",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("sukses")) {
                            StyleableToast.makeText(AddPlanActivity.this, "Perencanaan Berhasil Dibuat!", R.style.ToastSukses).show();
                            dialog.dismiss();

                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();

                        } else {
                            StyleableToast.makeText(AddPlanActivity.this, "Maaf, terjadi gangguan koneksi atau ada masalah pada server kami.", R.style.ToastGagal).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        StyleableToast.makeText(AddPlanActivity.this, "Koneksi kurang stabil, pastikan Anda terkoneksi dengan internet.", R.style.ToastGagal).show();
                        dialog.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("nama", namaLengkap.getText().toString().trim());
                params.put("telepon", telepon.getText().toString().trim());
                params.put("alamat", alamat.getText().toString().trim());
                params.put("longitude", longitude.getText().toString().trim());
                params.put("latitude", latitude.getText().toString().trim());

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                longitude.setText(data.getStringExtra("longitude"));
                latitude.setText(data.getStringExtra("latitude"));
                alamat.setText(data.getStringExtra("alamat"));
            }
        }
    }
}
