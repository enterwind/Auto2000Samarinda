package vay.enterwind.auto2000samarinda.module.others;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.module.sales.profil.TentangActivity;
import vay.enterwind.auto2000samarinda.module.sales.profil.UbahPasswordActivity;
import vay.enterwind.auto2000samarinda.module.sales.profil.UbahProfilActivity;
import vay.enterwind.auto2000samarinda.session.AuthManagement;

public class ProfilActivity extends BaseActivity {
    private static final String TAG = "ProfilActivity";
    private static final int ACTIVITY_NUM = 3;
    private Context mContext = ProfilActivity.this;

    @BindView(R.id.btnLogout) RelativeLayout btnLogout;
    @BindView(R.id.listPassword) LinearLayout btnPassword;
    @BindView(R.id.listProfil) LinearLayout listProfil;
    @BindView(R.id.listTentang) LinearLayout listTentang;

    @BindView(R.id.namaLengkap) TextView namaLengkap;
    @BindView(R.id.jabatan) TextView jabatan;
    @BindView(R.id.telepon) TextView telepon;
    @BindView(R.id.foto) ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profil);
        ButterKnife.bind(this);

        init();

        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

    private void init() {
        namaLengkap.setText(sessionNama);
        jabatan.setText(sessionJabatan);
        telepon.setText(sessionTelepon);
        Picasso.with(mContext).load(sessionFoto)
                .error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher)
                .into(foto);

    }

    @OnClick(R.id.btnLogout) void onLogout() {
        new MaterialStyledDialog.Builder(this)
                .setTitle("Apa Anda Yakin?")
                .setDescription("Apa benar Anda ingin mengeluarkan akun Anda?")
                .setNegativeText("Batalkan")
                .setPositiveText("Logout")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        session.logoutUser();
                    }
                }).show();
    }

    @OnClick(R.id.listPassword) void onPassword() {
        startActivity(new Intent(mContext, UbahPasswordActivity.class));
    }

    @OnClick(R.id.listProfil) void onProfil() {
        startActivity(new Intent(mContext, UbahProfilActivity.class));
    }

    @OnClick(R.id.listTentang) void onTentang() {
        startActivity(new Intent(mContext, TentangActivity.class));
    }
}
