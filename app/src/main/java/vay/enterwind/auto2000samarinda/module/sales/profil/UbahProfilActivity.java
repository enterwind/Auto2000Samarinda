package vay.enterwind.auto2000samarinda.module.sales.profil;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import dmax.dialog.SpotsDialog;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.session.AuthManagement;
import vay.enterwind.auto2000samarinda.utils.Config;

public class UbahProfilActivity extends AppCompatActivity {
    private static final String TAG = "UbahProfilActivity";

    @BindView(R.id.btnBack) ImageView btnBack;

    @BindView(R.id.btnSimpan) Button btnSimpan;
    @BindView(R.id.namaLengkap) TextView namaLengkap;
    @BindView(R.id.npk) TextView npk;
    @BindView(R.id.telepon) TextView telepon;
    @BindView(R.id.alamat) TextView alamat;

    SpotsDialog dialog;
    AuthManagement session;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_profil_akun);
        ButterKnife.bind(this);
        dialog = new SpotsDialog(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setupSession();
    }

    private void setupSession() {
        session = new AuthManagement(getApplicationContext());
        HashMap<String, String> detail = session.getUserDetails();
        email = detail.get(AuthManagement.KEY_EMAIL);

        namaLengkap.setText(detail.get(AuthManagement.KEY_NAMA));
        npk.setText(detail.get(AuthManagement.KEY_NPK));
        telepon.setText(detail.get(AuthManagement.KEY_TELEPON));
        alamat.setText(detail.get(AuthManagement.KEY_ALAMAT));
    }

    @OnClick(R.id.btnBack) void onBack() {
        finish();
    }

    @OnClick(R.id.btnSimpan) void onSimpanClick() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_PROFIL_SAVE + email,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("sukses")) {

                            session.updateProfil(namaLengkap.getText().toString().trim(),
                                    telepon.getText().toString().trim(),
                                    npk.getText().toString().trim(),
                                    alamat.getText().toString().trim());

                            StyleableToast.makeText(UbahProfilActivity.this, "Profil Anda berhasil diubah!", R.style.ToastSukses).show();
                            dialog.dismiss();
                        } else {
                            StyleableToast.makeText(UbahProfilActivity.this, "Maaf, terjadi gangguan koneksi atau ada masalah pada server kami.", R.style.ToastGagal).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        StyleableToast.makeText(UbahProfilActivity.this, "Koneksi kurang stabil, pastikan Anda terkoneksi dengan internet.", R.style.ToastGagal).show();
                        dialog.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("nama", namaLengkap.getText().toString().trim());
                params.put("npk", npk.getText().toString().trim());
                params.put("telepon", telepon.getText().toString().trim());
                params.put("alamat", alamat.getText().toString().trim());

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
