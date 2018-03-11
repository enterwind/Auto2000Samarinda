package vay.enterwind.auto2000samarinda.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cocosw.bottomsheet.BottomSheet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vay.enterwind.auto2000samarinda.LoginActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.models.Plan;
import vay.enterwind.auto2000samarinda.module.sales.HomeActivity;
import vay.enterwind.auto2000samarinda.module.sales.PlanningActivity;
import vay.enterwind.auto2000samarinda.utils.Config;

/**
 * Created by novay on 07/03/18.
 */

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.MyViewHolder> {

    private List<Plan> planList;
    private Activity mContext;
    private PlanAdapter adapter;

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtNama) TextView txtNama;
        @BindView(R.id.txtAlamat) TextView txtAlamat;
        @BindView(R.id.imgMore) ImageView imgMore;
        @BindView(R.id.imgCheck) TextView imgCheck;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public PlanAdapter(Activity mContext, List<Plan> planList) {
        this.planList = planList;
        this.mContext = mContext;
        this.adapter = this;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_plan, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Plan plan = planList.get(position);
        holder.txtNama.setText(plan.getNama());
        holder.txtAlamat.setText(plan.getAlamat());

        if(plan.getStatus().equals("2")) {
            holder.imgCheck.setVisibility(View.VISIBLE);
            holder.imgMore.setVisibility(View.GONE);
        } else {
            holder.imgMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new BottomSheet.Builder(mContext).title("Pilih Salah Satu").sheet(R.menu.menu_plan)
                            .listener(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case R.id.hapus:
                                            hapusPlan(plan.getUuid());
                                            mContext.startActivityForResult(new Intent(mContext, PlanningActivity.class), 1);
                                            mContext.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            break;
                                    }
                                }
                            }).show();
                }
            });
        }
    }

    private void hapusPlan(String uuid) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        String urlHapus = Config.URL_PLAN + uuid + "/delete";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlHapus, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("sukses")) {
                    Toast.makeText(mContext, "Data berhasil dihapus!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Gagal!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Koneksi tidak stabil. Coba lagi.", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }
}
