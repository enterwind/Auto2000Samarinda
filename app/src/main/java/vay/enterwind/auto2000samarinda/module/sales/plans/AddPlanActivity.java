package vay.enterwind.auto2000samarinda.module.sales.plans;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.muddzdev.styleabletoastlibrary.ToastLengthTracker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.module.sales.ProfilActivity;
import vay.enterwind.auto2000samarinda.module.sales.profil.UbahPasswordActivity;

public class AddPlanActivity extends AppCompatActivity {

    private Context mContext = AddPlanActivity.this;

    @BindView(R.id.btnBack) ImageView btnBack;

    @BindView(R.id.btnSimpan) Button btnSimpan;
    @BindView(R.id.alamat) TextView alamat;
    @BindView(R.id.npk) EditText npk;
    @BindView(R.id.namaLengkap) EditText namaLengkap;

    @BindView(R.id.longitude) EditText longitude;
    @BindView(R.id.latitude) EditText latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_add_plan);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @OnClick(R.id.btnBack) void onBack() {
        finish();
    }

    @OnClick(R.id.alamat) void onMap() {
        startActivityForResult(new Intent(mContext, MapsActivity.class), 1);
    }

    @OnClick(R.id.btnSimpan) void onSimpan() {
        StyleableToast.makeText(this, "Simpan Clicked!", R.style.ToastInfo).show();
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
