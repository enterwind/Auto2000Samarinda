package vay.enterwind.auto2000samarinda.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.models.Reference;
import vay.enterwind.auto2000samarinda.module.others.fragments.SalesFragment;
import vay.enterwind.auto2000samarinda.utils.Config;

/**
 * Created by novay on 09/03/18.
 */

public class ReferenceAdapter extends RecyclerView.Adapter<ReferenceAdapter.MyViewHolder> {

    private List<Reference> planList;
    private Activity mContext;
    private ReferenceAdapter adapter;

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtNama) TextView txtNama;
        @BindView(R.id.txtAlamat) TextView txtAlamat;
        @BindView(R.id.txtTelepon) TextView txtTelepon;
        @BindView(R.id.txtDiambilOleh) TextView txtDiambilOleh;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ReferenceAdapter(Activity mContext, List<Reference> planList) {
        this.planList = planList;
        this.mContext = mContext;
        this.adapter = this;
    }

    @Override
    public ReferenceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reference, parent, false);

        return new ReferenceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReferenceAdapter.MyViewHolder holder, int position) {
        final Reference plan = planList.get(position);
        holder.txtNama.setText(plan.getNama());
        holder.txtAlamat.setText(plan.getAlamat());
        holder.txtTelepon.setText(plan.getTelepon());
        if(!plan.getKepada().equals("null")) {
            holder.txtDiambilOleh.setText(plan.getKepada());
        } else {
            holder.txtDiambilOleh.setText("-");
        }

    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

}
