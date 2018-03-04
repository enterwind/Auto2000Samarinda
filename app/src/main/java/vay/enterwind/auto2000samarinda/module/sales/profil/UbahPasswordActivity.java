package vay.enterwind.auto2000samarinda.module.sales.profil;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.session.AuthManagement;
import vay.enterwind.auto2000samarinda.utils.Config;

public class UbahPasswordActivity extends AppCompatActivity {

    @BindView(R.id.btnBack) ImageView btnBack;

    @BindView(R.id.passwordLama) EditText passwordLama;
    @BindView(R.id.passwordBaru) EditText passwordBaru;
    @BindView(R.id.ulangiPassword) EditText ulangiPassword;

    @BindView(R.id.btnSimpan) Button btnSimpan;

    ProgressDialog progress;
    AuthManagement session;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_ubah_password);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setupSession();
    }

    private void setupSession() {
        session = new AuthManagement(getApplicationContext());
        HashMap<String, String> detail = session.getUserDetails();
        email = detail.get(AuthManagement.KEY_EMAIL);
        password = detail.get(AuthManagement.KEY_PASSWORD);
    }

    @OnClick(R.id.btnBack) void onBack() {
        finish();
    }

    @OnClick(R.id.btnSimpan) void onSimpanClick() {
        progress = ProgressDialog.show(UbahPasswordActivity.this, "Loading...", "Tunggu Sebentar");
        if(validasi()) {
            final String baru = passwordBaru.getText().toString().trim();
            RequestQueue requestQueue = Volley.newRequestQueue(UbahPasswordActivity.this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    Config.URL_PASSWORD_SAVE + email + '/' + baru,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("sukses")) {
                                session.updatePassword(baru);
                                StyleableToast.makeText(UbahPasswordActivity.this, "Password berhasil diubah!", R.style.ToastSukses).show();
                                progress.dismiss();
                            } else {
                                StyleableToast.makeText(UbahPasswordActivity.this, "Maaf, terjadi gangguan koneksi atau ada masalah pada server kami.", R.style.ToastGagal).show();
                                progress.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    StyleableToast.makeText(UbahPasswordActivity.this, "Koneksi kurang stabil, pastikan Anda terkoneksi dengan internet.", R.style.ToastGagal).show();
                    progress.dismiss();
                }
            });
            requestQueue.add(stringRequest);
        }
    }

    private boolean validasi() {
        boolean valid = true;
        if(passwordLama.getText().toString().trim().equals("") ||
                passwordBaru.getText().toString().trim().equals("") ||
                ulangiPassword.getText().toString().trim().equals("")) {
            StyleableToast.makeText(this, "Pastikan semua inputan telah terisi", R.style.ToastGagal).show();
            progress.dismiss();
            valid = false;
        } else {
            if(!passwordLama.getText().toString().trim().equals(password)) {
                StyleableToast.makeText(this, "Password lama Anda tidak valid", R.style.ToastGagal).show();
                progress.dismiss();
                valid = false;
            }
            if(!passwordBaru.getText().toString().trim().equals(ulangiPassword.getText().toString().trim())) {
                StyleableToast.makeText(this, "Password dan Ulangi Password harus sama", R.style.ToastGagal).show();
                progress.dismiss();
                valid = false;
            }
        }
        return valid;
    }

}
