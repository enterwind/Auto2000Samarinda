package vay.enterwind.auto2000samarinda.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
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
import vay.enterwind.auto2000samarinda.models.Plan;
import vay.enterwind.auto2000samarinda.module.sales.PlanningActivity;
import vay.enterwind.auto2000samarinda.module.sales.checkpoint.DetailActivity;
import vay.enterwind.auto2000samarinda.utils.Config;

/**
 * Created by novay on 07/03/18.
 */

public class CheckpointAdapter extends RecyclerView.Adapter<CheckpointAdapter.MyViewHolder> {

    private List<Plan> planList;
    private Activity mContext;
    private CheckpointAdapter adapter;

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtNama) TextView txtNama;
        @BindView(R.id.txtAlamat) TextView txtAlamat;
        @BindView(R.id.imgMore) ImageView imgMore;
        @BindView(R.id.item) CardView item;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public CheckpointAdapter(Activity mContext, List<Plan> planList) {
        this.planList = planList;
        this.mContext = mContext;
        this.adapter = this;
    }

    @Override
    public CheckpointAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_plan, parent, false);

        return new CheckpointAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CheckpointAdapter.MyViewHolder holder, int position) {
        final Plan plan = planList.get(position);
        holder.txtNama.setText(plan.getNama());
        holder.txtAlamat.setText(plan.getAlamat());
        holder.imgMore.setVisibility(View.GONE);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DetailActivity.class);
                i.putExtra("UUID", plan.getUuid());
                i.putExtra("LONGITUDE", plan.getLongitude());
                i.putExtra("LATITUDE", plan.getLatitude());
                i.putExtra("NAMA", plan.getNama());
                i.putExtra("TELEPON", plan.getTelepon());
                i.putExtra("ALAMAT", plan.getAlamat());
                mContext.startActivityForResult(i, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

}
