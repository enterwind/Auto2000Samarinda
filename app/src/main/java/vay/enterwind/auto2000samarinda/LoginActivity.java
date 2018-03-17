package vay.enterwind.auto2000samarinda;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.pubnub.api.PubNub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import vay.enterwind.auto2000samarinda.module.sales.HomeActivity;
import vay.enterwind.auto2000samarinda.module.sales.PlanningActivity;
import vay.enterwind.auto2000samarinda.pubnub.Constants;
import vay.enterwind.auto2000samarinda.session.AuthManagement;
import vay.enterwind.auto2000samarinda.utils.Config;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Context mContext = LoginActivity.this;

    @BindView(R.id.txtLupa) TextView txtLupa;
    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etSandi) EditText etSandi;
    @BindView(R.id.btnMasuk) Button btnMasuk;

    String email, password;
    AuthManagement session;
    ProgressDialog progress;

    private PubNub pubNub;

    String fcmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(R.drawable.splashscreen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        session = new AuthManagement(getApplicationContext());
        fcmId = FirebaseInstanceId.getInstance().getToken();
    }

    @OnClick(R.id.txtLupa) void onLupa() {
        new PrettyDialog(this)
                .setIcon(
                        R.drawable.pdlg_icon_info,
                        R.color.pdlg_color_green,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {}
                        })
                .setTitle("Anda lupa Password?")
                .setTitleColor(R.color.pdlg_color_blue)
                .setAnimationEnabled(true)
                .setMessage("Silahkan hubungi pihak Admin untuk informasi lebih lanjut.")
                .setMessageColor(R.color.pdlg_color_gray)
                .setTypeface(ResourcesCompat.getFont(this, R.font.oswald)).show();
    }

    @OnClick(R.id.btnMasuk) void onMasuk() {
        progress = ProgressDialog.show(LoginActivity.this, "Loading...", "Tunggu Sebentar");
        email = etEmail.getText().toString().trim();
        password = etSandi.getText().toString().trim();
        if (!email.isEmpty() && !password.isEmpty()) {
            checkLogin(email, password);
        } else {
            progress.dismiss();
            Toast.makeText(getApplicationContext(), "Silahkan masukkan Email dan Kata Sandi Anda!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void checkLogin(final String email, final String password)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String loginUrl = Config.URL_LOGIN + email + "/" + password + "/" + fcmId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("gagal")) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Email dan Kata Sandi Anda tidak cocok!", Toast.LENGTH_LONG).show();
                } else {
                    String[] list = response.split(",");

                    SharedPreferences sp = getSharedPreferences(Constants.DATASTREAM_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString(Constants.DATASTREAM_UUID, list[1]);
                    edit.apply();

                    switch (list[3]) {

                        // Untuk Supervisor
                        case "1":
                            session.createLoginSession(list[0], list[1], list[2], list[3],
                                    list[4], list[5], list[6], list[7], list[8], list[9],
                                    list[10], null);

                            startActivity(new Intent(LoginActivity.this, vay.enterwind.auto2000samarinda.module.supervisor.HomeActivity.class));
                            finish();
                            break;

                        // Untuk Sales
                        case "2":

                            session.createLoginSession(list[0], list[1], list[2], list[3],
                                    list[4], list[5], list[6], list[7], list[8], list[9],
                                    list[10], list[11]);

                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                            break;

                        // Untuk GR & BP
                        case "3":
                        case "4":
                            session.createLoginSession(list[0], list[1], list[2], list[3],
                                    list[4], list[5], list[6], list[7], list[8], list[9],
                                    list[10], null);

                            startActivity(new Intent(LoginActivity.this, vay.enterwind.auto2000samarinda.module.service.HomeActivity.class));
                            finish();
                            break;

                        // Untuk Others
                        case "5":
                            session.createLoginSession(list[0], list[1], list[2], list[3],
                                    list[4], list[5], list[6], list[7], list[8], list[9],
                                    list[10], null);

                            startActivity(new Intent(LoginActivity.this, vay.enterwind.auto2000samarinda.module.others.HomeActivity.class));
                            finish();
                            break;

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Toast.makeText(mContext, "Koneksi tidak stabil. Coba lagi.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onErrorResponse: "+error);
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {return 50000;}
            @Override
            public int getCurrentRetryCount() {return 50000;}
            @Override
            public void retry(VolleyError error) throws VolleyError {}
        });
        requestQueue.add(stringRequest);
    }

}
